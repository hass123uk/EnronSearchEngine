package BusinessLogicLayer;

import DataAccessLayer.Database.ContainsRepository;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
import DataAccessLayer.FileSystem.FileLoader;
import com.enron.search.domainmodels.Document;
import com.enron.search.domainmodels.Term;

import java.nio.file.Path;
import java.sql.SQLException;
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
            int mapTermId = syncTermsMap.putTermInMap(term);
            if (mapTermId != syncTermsMap.TEMP_TERM_ID) {
                term.setTerm_ID(mapTermId);
            } else try {
                int termId = termsRepository.saveTerm(term.getTerm_Value());
                if (termId != ERROR_CODE) {
                    term.setTerm_ID(termId);
                    syncTermsMap.updateTermID(term);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return terms.stream().map(Term::getTerm_ID).collect(Collectors.toList());
    }

    private void saveRelation(int documentId, List<Integer> termIDs) {
        containsRepository.bulkSaveIndexInContainTbl(termIDs, documentId);
    }
}
