package DAL;

import BE.Document;
import java.util.List;

public interface IDocumentsRepository {

    public boolean createDocument(Document new_Document);

    public List<Document> readAllDocuments();

    public Document readDocument(int document_ID);

    public boolean updateDocument(Document updated_Document);

    public boolean deleteDocument(int document_ID);

}
