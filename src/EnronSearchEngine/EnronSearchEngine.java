package EnronSearchEngine;

import BE.Document;
import DAL.DocumentsRepositoryImpl;
import DAL.IDocumentsRepository;
import java.util.List;

public class EnronSearchEngine {

    public static void main(String[] args) {
        IDocumentsRepository docRepo = new DocumentsRepositoryImpl();

        List<Document> docList = docRepo.readAllDocuments();
        docList.stream().forEach(x -> System.out.println(x.getDocument_URL()));
    }
}
