package EnronSearchEngine;

import BusinessEntities.Document;
import BusinessLogicLayer.DocumentsFactory;
import BusinessLogicLayer.TermSplitter;
import BusinessLogicLayer.TermSplitterImpl;
import DataAccessLayer.FileSystem.FileLoader;
import DataAccessLayer.FileSystem.FileLoaderImpl;
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
        DocumentsFactory documentsFactory
                = new DocumentsFactory(loader, splitter);

        loadFilesIntoDoc(documentsFactory);
    }

    private static void loadFilesIntoDoc(DocumentsFactory documentsFactory) {
        long startTime = System.nanoTime();
        List<Document> allDocumentsWithinDir
                = documentsFactory.loadFilesAndPopulateNewDocuments(
                        ENRON_DATASET_DIR);
        double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;

        printResult("loadFilesAndPopulateDocuments()", executionTimeInSeconds, allDocumentsWithinDir.size());
    }

    private static void printResult(
            String methodName, double executionTimeInSeconds, int filesSize) {
        System.out.println(
                methodName + "!  "
                + "MethodExecitionTime: " + executionTimeInSeconds
                + ", Files No: " + filesSize
        );
    }
}
