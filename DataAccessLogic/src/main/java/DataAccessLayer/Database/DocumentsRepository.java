package DataAccessLayer.Database;

import com.enron.search.domainmodels.Document;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentsRepository {

    private final Connection connection;

    public DocumentsRepository() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public int saveDocument(Document document) throws SQLException{
        String sqlInsert = "INSERT INTO documents_tbl(documents_url, "
                + "documents_indexTime) VALUES(?, ?)";

        String sqlSelect = "SELECT LAST_INSERT_ID()";
        try (PreparedStatement preparedStatement
                = connection.prepareStatement(sqlInsert)) {

            preparedStatement.setString(1, document.getDocument_Path());
            preparedStatement.setDate(2, new java.sql.Date(
                    document.getDocument_IndexTime().getTime()));

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery(sqlSelect);

            if (resultSet.next()) {
                return resultSet.getInt("LAST_INSERT_ID()");
            }
            return -1;
        }
    }

    public boolean saveDocuments(List<Document> documents_list) {
        String sql = "INSERT INTO documents_tbl(documents_url, "
                + "documents_indexTime) VALUES(?, ?)";

        try (PreparedStatement preparedStatement
                = connection.prepareStatement(sql)) {
            for (Document doc : documents_list) {
                preparedStatement.setString(1, doc.getDocument_Path());
                preparedStatement.setDate(2, new java.sql.Date(
                        doc.getDocument_IndexTime().getTime()));

                preparedStatement.executeUpdate();
            }
            return true;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public List<Document> readAllDocuments() {
        try (Statement st = connection.createStatement();) {

            ResultSet rs = st.executeQuery(
                    "SELECT * FROM DocumentTerms.documents_tbl");
            ArrayList<Document> documents = new ArrayList<>();
            while (rs.next()) {
                documents.add(resultSetToObject(rs));
            }
            return documents;
        } catch (SQLException ex) {
            System.out.println(ex.getSQLState());
            return null;
        }
    }

    private Document resultSetToObject(ResultSet rs) throws SQLException {
        int ID = rs.getInt("documents_id");
        String URL = rs.getString("documents_url");

        return new Document(ID, URL);

    }
}
