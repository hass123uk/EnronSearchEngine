package BusinessLogicLayer;

import DataAccessLayer.Database.PopulateDB;
import DataAccessLayer.FileSystem.FileLoader;
import DataAccessLayer.FileSystem.FileLoaderImpl;
import java.nio.file.Paths;

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
            + HALF_ALL_DOCS;

    private static FileLoader loader;
    private static TermSplitter splitter;
    private static DocumentLoaderImpl documentsLoader;
    private static PopulateDB populateDB;

    public static void main(String[] args) {
        createDependencies();
        documentsLoader.saveDocuments(Paths.get(ENRON_DATASET_DIR));

//        List<Document> documents = loadDocuments();
//        saveDocuments(documents);
    }

    public static void createDependencies() {
        loader = new FileLoaderImpl();
        splitter = new TermSplitterImpl("\\s+");
        populateDB = new PopulateDB();

        documentsLoader = new DocumentLoaderImpl(loader, splitter, populateDB);
    }

//    public static List<Document> loadDocuments() {
//        long startTime = System.nanoTime();
//
//
//        double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;
//
//        printResult("Load from File System", executionTimeInSeconds,
//                documentsFromFileSystem.size());
//
//        return documentsFromFileSystem;
//    }
//    public static void saveDocuments(List<Document> documents) {
//        long startTime = System.nanoTime();
//        double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;
//
//        printResult("Save to DB", executionTimeInSeconds, documents.size());
//
//    }
    private static void printResult(
            String methodName, double executionTimeInSeconds, int filesSize) {
        System.out.println(
                methodName + "!  "
                + "MethodExecitionTime: " + executionTimeInSeconds
                + ", Files No: " + filesSize
        );
    }
}
