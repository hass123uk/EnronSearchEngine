package BusinessLogicLayer;

import DataAccessLayer.Database.ContainsRepository;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
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

    public static void main(String[] args) {
        DocumentIndexer documentsLoader = createDocumentIndexer();
        documentsLoader.saveDocuments(Paths.get(ENRON_DATASET_DIR));
    }

    public static DocumentIndexer createDocumentIndexer() {
        FileLoader loader = new FileLoaderImpl();
        TermSplitter splitter = new TermSplitterImpl("\\s+");
        DocumentsRepository documentsRepository = new DocumentsRepository();
        TermsRepository termsRepository = new TermsRepository();
        ContainsRepository containsRepository = new ContainsRepository();

        return new DocumentIndexerImpl(
                loader, splitter,
                documentsRepository, termsRepository, containsRepository
        );
    }
}
