package EnronSearchEngine;

import BusinessEntities.Document;
import BusinessLogicLayer.DocumentLoader;
import BusinessLogicLayer.DocumentLoaderImpl;
import BusinessLogicLayer.TermSplitter;
import BusinessLogicLayer.TermSplitterImpl;
import DataAccessLayer.FileSystem.FileLoader;
import DataAccessLayer.FileSystem.FileLoaderImpl;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 */
public class EnronSearchEngine {

    private static final String HOME_DIR = "/Users/HassanMahmud";
    private static final String FILE_NAME = "/EnronDataSet";

    private static final String ALL_DOCS = "/MailDir_FullSet";
    private static final String HALF_ALL_DOCS = "/MailDir_HalfSet";
    private static final String FEW_DOCS = "/MailDir_SubSet";

    private static final String ENRON_DATASET_DIR
            = HOME_DIR
            + FILE_NAME
            + ALL_DOCS;

    public static void main(String[] args) {
        FileLoader loader = new FileLoaderImpl();
        TermSplitter splitter = new TermSplitterImpl("//s");
        DocumentLoader documentsFactory
                = new DocumentLoaderImpl(loader, splitter);
        Path basePath = Paths.get(ENRON_DATASET_DIR);

        loadFilesIntoDoc(documentsFactory, basePath);
    }

    private static void loadFilesIntoDoc(
            DocumentLoader documentsFactory, Path basePath) {
        long startTime = System.nanoTime();
        List<Document> allDocumentsWithinDir
                = documentsFactory.loadDocuments(basePath);
        double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;

        System.out.println(
                "loadFilesAndPopulateDocuments!  "
                + "MethodExecitionTime: " + executionTimeInSeconds
                + ", Files No: " + allDocumentsWithinDir.size()
        );
    }

}
