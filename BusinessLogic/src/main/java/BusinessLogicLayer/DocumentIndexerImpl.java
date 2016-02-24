package BusinessLogicLayer;

import DataAccessLayer.Database.ContainsRepository;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
import DataAccessLayer.FileSystem.FileLoader;
import com.enron.search.domainmodels.Document;
import com.enron.search.domainmodels.Term;
import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

public class DocumentIndexerImpl implements DocumentIndexer {

    private final FileLoader fileLoader;
    private final TermSplitter termSplitter;
    private final DocumentsRepository documentsRepository;
    private final TermsRepository termsRepository;
    private final ContainsRepository containsRepositroy;

    public DocumentIndexerImpl(FileLoader fileLoader,
            TermSplitter termSplitter,
            DocumentsRepository documentsRepository,
            TermsRepository termsRepository,
            ContainsRepository containsRepositroy) {

        this.fileLoader = fileLoader;
        this.termSplitter = termSplitter;
        this.documentsRepository = documentsRepository;
        this.termsRepository = termsRepository;
        this.containsRepositroy = containsRepositroy;

    }

    @Override
    public void saveDocuments(Path basePath) {
        long startTime = System.nanoTime();
        List<File> files = fileLoader.loadFiles(basePath);
        double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;

        System.out.println(
                "Load Files: NumberOfFiles: " + files.size() + ".\n"
                + "ExecutionTime: " + executionTimeInSeconds);

        files.stream().forEach((File file) -> {
            long startLoadLines = System.nanoTime();
            List<String> lines = fileLoader.loadLines(file.toPath());
            List<Term> terms = termSplitter.splitLines(lines);
            double loadLinesExecutionTime = (System.nanoTime() - startLoadLines) / 1E9;

            Document document = new Document(
                    file.getAbsolutePath(),
                    new Date(System.currentTimeMillis()));

            long startSaveToDB = System.nanoTime();
            int docId = documentsRepository.saveDocument(document);
            terms.stream()
                    .forEach((Term term) -> {
                        int termId = termsRepository.saveTerm(term.getTerm_Value(), docId);
                        containsRepositroy.saveIndexInContainTbl(termId, docId);
                    });
            double saveToDBExecutionTime = (System.nanoTime() - startSaveToDB) / 1E9;

            System.out.println(
                    "Document: " + document.getDocument_Path()
                    + "\nLoad and Split Terms -"
                    + " ExecutionTime: " + loadLinesExecutionTime
                    + " NumberOfLines:" + terms.size()
                    + "\nSaveDocument - ExectionTime:" + saveToDBExecutionTime
                    + "\n"
            );
        });
    }
}
