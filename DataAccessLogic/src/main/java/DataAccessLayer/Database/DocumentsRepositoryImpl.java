package DataAccessLayer.Database;

import com.enron.search.domainmodels.Document;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DocumentsRepositoryImpl implements DocumentsRepository {

    private final DatabaseConnection connectionManager;

    public DocumentsRepositoryImpl() {
        connectionManager = DatabaseConnection.getInstance();
    }

    @Override
    public boolean createDocument(Document new_Document) {
        try (Connection con = connectionManager.getConnection()) {
            String sql = "INSERT INTO documents_tbl(documents_url, "
                    + "documents_indexTime) VALUES(?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, new_Document.getDocument_URL());
            ps.setDate(2, new java.sql.Date(
                    new_Document.getDocument_IndexTime().getTime()));

            ps.executeUpdate();

            return true;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean createDocuments(List<Document> documents_list) {
        try (Connection con = connectionManager.getConnection()) {
            for (Document doc : documents_list) {
                String sql = "INSERT INTO documents_tbl(documents_url, "
                        + "documents_indexTime) VALUES(?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, doc.getDocument_URL());
                ps.setDate(2, new java.sql.Date(
                        doc.getDocument_IndexTime().getTime()));

                ps.executeUpdate();
            }
            return true;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Document> readAllDocuments() {
        try (Connection con = connectionManager.getConnection()) {
            Statement st = con.createStatement();
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

    @Override
    public Document readDocument(int document_ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateDocument(Document updated_Document) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteDocument(int document_ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
