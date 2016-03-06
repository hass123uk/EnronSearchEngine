import DataAccessLayer.Database.Database;
import DataAccessLayer.Database.DocumentsRepository;
import com.enron.search.domainmodels.Document;
import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.Date;

public class DatabaseUnitTest {

    @Test
    public void AddDocumentToDB() throws SQLException {
        Document document = new Document(-1, "/Users/", new Date());

        DocumentsRepository documentsRepository = new DocumentsRepository();
        int i = documentsRepository.saveDocument(document);
        System.out.println(i);
    }

    @Test
    public void getConnection_Assert_Datbase_Connection() {
        try {
            Connection connection = Database.getConnection();
            Assert.assertNotNull(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
