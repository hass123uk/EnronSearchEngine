package DataAccessLayer.Database;

import com.enron.search.domainmodels.Document;
import com.enron.search.domainmodels.Term;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PopulateDB {

    private final DatabaseConnection connectionManager;

    public PopulateDB() {
        connectionManager = DatabaseConnection.getInstance();
    }

    public void populateDBWithDocumentsAndTerms(List<Document> documents) {
        try (Connection con = connectionManager.getConnection()) {

            saveDocuments(con, documents);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PopulateDB.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void saveDocuments(Connection con, List<Document> documents_list)
            throws SQLException, ClassNotFoundException {
        String sqlInsert = "INSERT INTO documents_tbl(documents_url, "
                + "documents_indexTime) VALUES(?, ?)";

        String sqlSelect = "SELECT * FROM DocumentTerms.documents_tbl";

        for (Document doc : documents_list) {
            PreparedStatement preparedStatement
                    = con.prepareStatement(sqlInsert);

            preparedStatement.setString(1, doc.getDocument_URL());
            preparedStatement.setDate(2, new java.sql.Date(
                    doc.getDocument_IndexTime().getTime()));

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery(sqlSelect);

            int documentId = resultSet.getInt("documents_id");

            saveTerms(con, doc.getDocument_Terms(), documentId);
        }
    }

    private void saveTerms(Connection con, List<Term> document_Terms,
            int documentId) throws SQLException {
        String sqlInsert = "INSERT INTO terms_tbl(terms_value)"
                + "VALUES(?)" + "\n"
                + "SELECT * FROM DocumentTerms.documents_tbl";

        for (Term term : document_Terms) {
            PreparedStatement preparedStatment = con.prepareStatement(sqlInsert);
            preparedStatment.setString(1, term.getTerm_Value());
            ResultSet resultSet = preparedStatment.executeQuery();
            int termId = resultSet.getInt("terms_id");

            createContain(con, termId, documentId);
        }
    }

    private void createContain(Connection con, int termId, int documentId)
            throws SQLException {
        String sqlInsert = "INSERT INTO contain_tbl(terms_id, documents_id)"
                + "VALUES(?, ?)";

        PreparedStatement preparedStatement
                = con.prepareStatement(sqlInsert);

        preparedStatement.setInt(1, termId);
        preparedStatement.setInt(2, documentId);

        preparedStatement.executeQuery();
    }
}
