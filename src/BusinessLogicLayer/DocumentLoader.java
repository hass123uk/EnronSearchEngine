package BusinessLogicLayer;

import BusinessEntities.Document;
import java.nio.file.Path;
import java.util.List;

public interface DocumentLoader {

    public List<Document> loadDocuments(Path basePath);
}
