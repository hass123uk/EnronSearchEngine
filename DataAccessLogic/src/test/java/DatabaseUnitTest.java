import DataAccessLayer.Database.Database;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
import com.enron.search.domainmodels.Document;
import com.enron.search.domainmodels.Term;
import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DatabaseUnitTest {

    @Test
    public void BulkSaveTermsToDB() throws SQLException {
//        List<Term> terms = Arrays.asList(new Term(-1, "UnitTest1"), new Term(-1, "UnitTest2"));
//        TermsRepository termsRepository = new TermsRepository();
//        List<Integer> termIds = termsRepository.batchSaveTerm(terms);
//
//
//        terms.stream().forEach(term -> {
//            term.setTerm_ID(termsRepository.getTerm(term.getTerm_Value()).getTerm_ID());
//            System.out.println(term.getTerm_ID() + " " + term.getTerm_Value());
//        });
//        termIds.stream().forEach(System.out::println);
    }

    @Test
    public void AddDocumentToDB() throws SQLException {
//        Document document = new Document(-1, "/Users/", new Date());
//
//        DocumentsRepository documentsRepository = new DocumentsRepository();
//        int i = documentsRepository.saveDocument(document);
//        System.out.println(i);
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
