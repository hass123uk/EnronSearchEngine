package BusinessLogicLayer;

import DataAccessLayer.Database.ContainsRepository;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
import DataAccessLayer.FileSystem.FileLoader;
import com.enron.search.domainmodels.Document;
import com.enron.search.domainmodels.Term;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class IndexTaskCallable implements Callable {

    private static final int ERROR_CODE = -1;

    private final SynchronizedTermsMap syncTermsMap;
    private final Path filePath;
    private final FileLoader fileLoader;
    private final TermSplitter termSplitter;
    private final DocumentsRepository documentsRepository;
    private final TermsRepository termsRepository;
    private final ContainsRepository containsRepository;

    public IndexTaskCallable(
            Path filePath,
            SynchronizedTermsMap synchronizedTermsMap,
            FileLoader fileLoader,
            TermSplitter termSplitter,
            DocumentsRepository documentsRepository,
            TermsRepository termsRepository,
            ContainsRepository containsRepository) {

        this.filePath = filePath;
        this.syncTermsMap = synchronizedTermsMap;
        this.fileLoader = fileLoader;
        this.termSplitter = termSplitter;
        this.documentsRepository = documentsRepository;
        this.termsRepository = termsRepository;
        this.containsRepository = containsRepository;
    }

    @Override
    public Object call() throws Exception {
        List<String> lines = fileLoader.loadLines(filePath);
        Date indexTime = new Date();
        String documentId = UUIDGenerator.generateID();
        saveDocument(documentId, filePath.toAbsolutePath().toString(), indexTime);

        List<Term> terms = termSplitter.splitLines(lines);
        List<String> termIDs = saveTerms(terms);

        saveRelation(documentId, termIDs);

        return null;
    }

    private void saveDocument(String documentId, String absoluteFilePath, Date indexTime) {
        Document document = new Document(documentId, absoluteFilePath, indexTime);
        documentsRepository.saveDocument(document);
    }

    public List<String> saveTerms(List<Term> terms) {
        for (Term term : terms) {
            String generatedTermId = UUIDGenerator.generateID();
            String termId = syncTermsMap.checkDuplicateTerms(term.getTerm_Value(), generatedTermId);
            term.setTerm_ID(termId);
            termsRepository.saveTerm(term);
        }
        return terms.stream()
                .map(Term::getTerm_ID)
                .collect(Collectors.toList());
    }

    private void saveRelation(String documentId, List<String> termIDs) {
        containsRepository.bulkSaveIndexInContainTbl(termIDs, documentId);
    }
}
