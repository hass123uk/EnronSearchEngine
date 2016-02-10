package EnronSearchEngine;

import BE.Document;
import BE.Term;
import BLL.DocumentLoader;
import Crawlers.DocumentLoaderImpl;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class EnronSearchEngine {

    private static final String HOME_DIR = "/Users/HassanMahmud";
    private static final String FILE_NAME = "/EnronDataSet";

    private static final String ALL_DOCS = "/MailDir_FullSet";
    private static final String FEW_DOCS = "/MailDir_SubSet";

    private static final String ENRON_DATASET_DIR
            = HOME_DIR
            + FILE_NAME
            + FEW_DOCS;

    public static void main(String[] args) {
        DocumentLoader loader = new DocumentLoaderImpl();

        try {
            List<Document> allDocuments
                    = loader.loadAllDocumentsWithTerms(ENRON_DATASET_DIR);

            System.out.println(allDocuments.size());
            System.out.println(allDocuments.get(0).getDocument_URL());
            allDocuments.get(0).getDocument_Terms().stream()
                    .forEach((Term t) -> System.out.println(t.getTerm_Value()));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
