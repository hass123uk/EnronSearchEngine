import Database.ContainsRepository;
import Database.DocumentsRepository;
import Database.TermsRepository;
import DomainModels.Document;
import DomainModels.Term;
import FileSystem.FileLoader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class IndexTaskCallable implements Callable {

    private final SynchronizedTermsMap synchronizedTermsMap;
    private final Path filePath;
    private final FileLoader fileLoader;
    private final StringSplitter stringSplitter;
    private final DocumentsRepository documentsRepository;
    private final TermsRepository termsRepository;
    private final ContainsRepository containsRepository;
    private IncrementalIDGenerator incrementalIDGenerator;

    public IndexTaskCallable(
            Path filePath,
            IncrementalIDGenerator incrementalIDGenerator, SynchronizedTermsMap synchronizedTermsMap,
            FileLoader fileLoader,
            StringSplitter stringSplitter,
            DocumentsRepository documentsRepository,
            TermsRepository termsRepository,
            ContainsRepository containsRepository) {

        this.filePath = filePath;
        this.incrementalIDGenerator = incrementalIDGenerator;
        this.synchronizedTermsMap = synchronizedTermsMap;
        this.fileLoader = fileLoader;
        this.stringSplitter = stringSplitter;
        this.documentsRepository = documentsRepository;
        this.termsRepository = termsRepository;
        this.containsRepository = containsRepository;
    }

    @Override
    public Void call() throws Exception {
        List<String> lines = fileLoader.loadLines(filePath);

        int documentId = incrementalIDGenerator.getNextDocumentID();
        Document document = new Document(documentId, filePath.toAbsolutePath().toString(), new Date());
        documentsRepository.insertDocument(document);

        List<Integer> termIds = processAndBatchSaveTerms(stringSplitter.split(lines));

        containsRepository.batchInsertContains(documentId, termIds);

        return null;
    }

    private List<Integer> processAndBatchSaveTerms(List<String> termValues) {
        List<Integer> termIds = new ArrayList<>();
        List<Term> newTerms = new ArrayList<>();

        int newTermID = incrementalIDGenerator.getNextTermID();
        for (String termValue : termValues) {
            int termID = getOrSetTermID(termValue, newTermID);
            termIds.add(termID);

            if (termID == newTermID) {
                newTerms.add(new Term(termID, termValue));
                newTermID = incrementalIDGenerator.getNextTermID();
            }
        }
        termsRepository.batchInsertTerm(newTerms);
        return termIds;
    }

    private int getOrSetTermID(String termValue, int newID) {
        int termID;
        synchronizedTermsMap.lock.lock();
        try {
            int idFromMap = synchronizedTermsMap.getTermIDIfPresent(termValue);
            if (idFromMap != SynchronizedTermsMap.TERM_NOT_PRESENT) {
                termID = idFromMap;
            } else {
                synchronizedTermsMap.putTerm(termValue, newID);
                termID = newID;
            }
        } finally {
            synchronizedTermsMap.lock.unlock();
        }
        return termID;
    }
}
