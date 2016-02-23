package DataAccessLayer.Database;

import com.enron.search.domainmodels.Document;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PopulateDB {

    private final Connection connection;

    public PopulateDB() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public int saveDocument(Document document) {
        String sqlInsert = "INSERT INTO documents_tbl(documents_url, "
                + "documents_indexTime) VALUES(?, ?)";

        String sqlSelect = "SELECT LAST_INSERT_ID()";
        try (PreparedStatement preparedStatement
                = connection.prepareStatement(sqlInsert)) {

            preparedStatement.setString(1, document.getDocument_URL());
            preparedStatement.setDate(2, new java.sql.Date(
                    document.getDocument_IndexTime().getTime()));

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery(sqlSelect);

            if (resultSet.next()) {
                return resultSet.getInt("LAST_INSERT_ID()");
            }
            return -1;

        } catch (SQLException ex) {
            Logger.getLogger(PopulateDB.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int saveTerm(String term, int documentId) {
        String sqlInsert = "INSERT INTO terms_tbl(terms_value) VALUES(?)";
        String sqlSelect = "SELECT LAST_INSERT_ID()";
        try (PreparedStatement preparedStatement
                = connection.prepareStatement(sqlInsert)) {

            preparedStatement.setString(1, term);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery(sqlSelect);
            if (resultSet.next()) {
                return resultSet.getInt("LAST_INSERT_ID()");
            }
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(PopulateDB.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public void saveIndexInContainTbl(int termId, int documentId) {
        String sqlInsert = "INSERT INTO contain_tbl(terms_id, documents_id)"
                + "VALUES(?, ?)";

        try (PreparedStatement preparedStatement
                = connection.prepareStatement(sqlInsert)) {

            preparedStatement.setInt(1, termId);
            preparedStatement.setInt(2, documentId);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PopulateDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
