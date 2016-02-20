package BusinessLogicLayer;

import BusinessEntities.Document;
import BusinessEntities.Term;
import DataAccessLayer.FileSystem.FileLoader;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentsFactory {

    private final FileLoader fileLoader;
    private final TermSplitter termSplitter;

    public DocumentsFactory(FileLoader fileLoader, TermSplitter termSplitter) {
        this.fileLoader = fileLoader;
        this.termSplitter = termSplitter;
    }

    public List<Document> loadFilesAndPopulateNewDocuments(String baseDir) {
        Path basePath = Paths.get(baseDir);
        List<Document> documents = new ArrayList();

        List<File> files = fileLoader.loadFiles(basePath);
        for (File file : files) {
            List<String> lines = fileLoader.loadLines(file.toPath());
            long indexedTime = System.currentTimeMillis();

            List<Term> terms
                    = termSplitter.splitLines(
                            fileLoader.loadLines(file.toPath()));

            Document document
                    = new Document(
                            file.getAbsolutePath(),
                            new Date(indexedTime),
                            terms);
            documents.add(document);
        }
        return documents;
    }

}
