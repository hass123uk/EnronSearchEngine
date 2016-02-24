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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        List<File> files = fileLoader.loadFiles(basePath);

        ExecutorService executor = Executors.newWorkStealingPool();

        files.stream().forEach((File file) -> {
            executor.submit(() -> {
                processFile(file);
            });
        });

        try {
            while (executor.awaitTermination(1, TimeUnit.DAYS)) {

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(DocumentIndexerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processFile(File file) {
        List<String> lines = fileLoader.loadLines(file.toPath());
        List<Term> terms = termSplitter.splitLines(lines);
        Document document = new Document(
                file.getAbsolutePath(),
                new Date(System.currentTimeMillis()));

        int docId = documentsRepository.saveDocument(document);
        terms.stream()
                .forEach((Term term) -> {
                    int termId = termsRepository.saveTerm(term.getTerm_Value(), docId);
                    containsRepositroy.saveIndexInContainTbl(termId, docId);
                });
    }
}
