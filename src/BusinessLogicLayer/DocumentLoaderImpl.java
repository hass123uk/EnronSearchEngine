package BusinessLogicLayer;

import BusinessEntities.Document;
import BusinessEntities.Term;
import DataAccessLayer.FileSystem.FileLoader;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentLoaderImpl implements DocumentLoader {

    private final FileLoader fileLoader;
    private final TermSplitter termSplitter;

    public DocumentLoaderImpl(FileLoader fileLoader, TermSplitter termSplitter) {
        this.fileLoader = fileLoader;
        this.termSplitter = termSplitter;
    }

    @Override
    public List<Document> loadDocuments(Path basePath) {
        List<Document> documents = new ArrayList();
        List<File> files = fileLoader.loadFiles(basePath);

        files.stream().forEach((file) -> {
            List<String> lines = fileLoader.loadLines(file.toPath());
            long indexedTime = System.currentTimeMillis();

            List<Term> terms = termSplitter.splitLines(lines);

            documents.add(new Document(
                    file.getAbsolutePath(),
                    new Date(indexedTime),
                    terms));
        });
        return documents;
    }

}
