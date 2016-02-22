package BusinessLogicLayer;

import com.enron.search.domainmodels.Document;
import java.nio.file.Path;
import java.util.List;

public interface DocumentLoader {

    public List<Document> loadDocuments(Path basePath);
}
