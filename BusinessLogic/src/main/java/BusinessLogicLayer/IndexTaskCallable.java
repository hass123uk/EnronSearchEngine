package BusinessLogicLayer;

import DataAccessLayer.Database.ContainsRepository;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
import DataAccessLayer.FileSystem.FileLoader;
import com.enron.search.domainmodels.Document;
import com.enron.search.domainmodels.Term;
import com.google.common.collect.BiMap;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class IndexTaskCallable implements Callable {

    private static final int ERROR_CODE = -1;

    private final TermsBiMapLock termsBiMapLock;
    private final Path filePath;
    private final FileLoader fileLoader;
    private final TermSplitter termSplitter;
    private final DocumentsRepository documentsRepository;
    private final TermsRepository termsRepository;
    private final ContainsRepository containsRepository;

    public IndexTaskCallable(
            Path filePath,
            TermsBiMapLock termsBiMapLock,
            FileLoader fileLoader,
            TermSplitter termSplitter,
            DocumentsRepository documentsRepository,
            TermsRepository termsRepository,
            ContainsRepository containsRepository) {

        this.filePath = filePath;
        this.termsBiMapLock = termsBiMapLock;
        this.fileLoader = fileLoader;
        this.termSplitter = termSplitter;
        this.documentsRepository = documentsRepository;
        this.termsRepository = termsRepository;
        this.containsRepository = containsRepository;
    }

    @Override
    public Object call() throws Exception {
        long startTime = System.nanoTime();
        List<String> lines = fileLoader.loadLines(filePath);
        Date indexTime = new Date();
        List<Term> terms = termSplitter.splitLines(lines);

        int documentId = saveDocument(filePath.toAbsolutePath().toString(), indexTime);
        if (documentId != ERROR_CODE) {
            List<Integer> termIDs = saveTerms(terms);
            saveRelation(documentId, termIDs);
            double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;
            return String.format("Success - Document inserted in the database! FilePath: %s.\n" +
                            "TermIds Size: %d - Terms Size: %d.\n" +
                            "Indexing time:%s Seconds  (Loading & Splitting lines/Saving Documents,Terms and Relations)\n"
                    , filePath.toString(), termIDs.size(), terms.size(), executionTimeInSeconds);
        }
        return String.format("Error - Document was not inserted in the database! File Path: %s.\n", filePath.toString());
    }

    private int saveDocument(String absoluteFilePath, Date indexTime) {
        Document document = new Document(absoluteFilePath, indexTime);
        try {
            return documentsRepository.saveDocument(document);
        } catch (SQLException e) {
            e.printStackTrace();
            return ERROR_CODE;
        }
    }

    public List<Integer> saveTerms(List<Term> terms) {
        for (Term term : terms) {
            termsBiMapLock.lock.lock();
            try {
                boolean containsValue = termsBiMapLock.termsBiMap.containsValue(term.getTerm_Value());
                if (containsValue) {
                    BiMap<String, Integer> inverse = termsBiMapLock.termsBiMap.inverse();
                    Integer termId = inverse.get(term.getTerm_Value());
                    term.setTerm_ID(termId);
                } else {
                    try {
                        int termId = termsRepository.saveTerm(term.getTerm_Value());
                        if (termId != ERROR_CODE) {
                            termsBiMapLock.termsBiMap.put(termId, term.getTerm_Value());
                            term.setTerm_ID(termId);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                termsBiMapLock.lock.unlock();
            }
        }
        return terms.stream().map(Term::getTerm_ID).collect(Collectors.toList());
    }

    public void saveRelation(int documentId, List<Integer> termIDs) {
        List<Integer> termIds = termIDs.stream().collect(Collectors.toList());
        containsRepository.bulkSaveIndexInContainTbl(termIds, documentId);
    }
}
