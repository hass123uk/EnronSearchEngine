package BusinessLogicLayer;

import com.enron.search.domainmodels.Document;
import DataAccessLayer.Database.PopulateDB;
import DataAccessLayer.FileSystem.FileLoader;
import DataAccessLayer.FileSystem.FileLoaderImpl;
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
            + FEW_DOCS;

    private static FileLoader loader;
    private static TermSplitter splitter;
    private static DocumentLoader documentsLoader;
    private static PopulateDB populateDB;

    public static void main(String[] args) {
        createDependencies();
        List<Document> documentsFromFileSystem
                = documentsLoader.loadDocuments(Paths.get(ENRON_DATASET_DIR));

        populateDB.populateDBWithDocumentsAndTerms(documentsFromFileSystem);
    }

    public static void createDependencies() {
        loader = new FileLoaderImpl();
        splitter = new TermSplitterImpl("//s");
        documentsLoader = new DocumentLoaderImpl(loader, splitter);
        populateDB = new PopulateDB();
    }

}
