/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import BE.Document;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DocumentsRepositoryImpl implements IDocumentsRepository {

    private final DBConnectionManager connectionManager;

    public DocumentsRepositoryImpl() {
        connectionManager = DBConnectionManager.getInstance();
    }

    @Override
    public boolean createDocument(Document new_Document) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

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
