package DataAccessLayer.Database;

import com.enron.search.domainmodels.Document;

import java.sql.*;

public class DocumentsRepository {

    public void saveDocument(Document document){
        String sqlInsert = "INSERT IGNORE INTO documents_tbl(documents_id, documents_url, "
                + "documents_indexTime) VALUES(?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)
        ) {
            preparedStatement.setString(1, document.getDocument_ID());
            preparedStatement.setString(2, document.getDocument_Path());
            preparedStatement.setDate(3, new java.sql.Date(
                    document.getDocument_IndexTime().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//
//    public boolean saveDocuments(List<Document> documents_list) {
//        String sql = "INSERT INTO documents_tbl(documents_url, "
//                + "documents_indexTime) VALUES(?, ?)";
//
//        try (PreparedStatement preparedStatement
//                = connection.prepareStatement(sql)) {
//            for (Document doc : documents_list) {
//                preparedStatement.setString(1, doc.getDocument_Path());
//                preparedStatement.setDate(2, new java.sql.Date(
//                        doc.getDocument_IndexTime().getTime()));
//
//                preparedStatement.executeUpdate();
//            }
//            return true;
//
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            return false;
//        }
//    }
//
//    public List<Document> readAllDocuments() {
//        try (Statement st = connection.createStatement();) {
//
//            ResultSet rs = st.executeQuery(
//                    "SELECT * FROM DocumentTerms.documents_tbl");
//            ArrayList<Document> documents = new ArrayList<>();
//            while (rs.next()) {
//                documents.add(resultSetToObject(rs));
//            }
//            return documents;
//        } catch (SQLException ex) {
//            System.out.println(ex.getSQLState());
//            return null;
//        }
//    }
//
//    private Document resultSetToObject(ResultSet rs) throws SQLException {
//        int ID = rs.getInt("documents_id");
//        String URL = rs.getString("documents_url");
//
//        return new Document(ID, URL);
//
//    }
}
