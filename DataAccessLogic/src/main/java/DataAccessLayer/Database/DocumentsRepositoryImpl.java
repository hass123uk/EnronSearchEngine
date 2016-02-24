package DataAccessLayer.Database;

import com.enron.search.domainmodels.Document;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentsRepositoryImpl implements ICRUD<Integer, Document> {

    private final DatabaseConnection connectionManager;

    public DocumentsRepositoryImpl() {
        connectionManager = DatabaseConnection.getInstance();
    }

    @Override
    public boolean create(Document d) {
        try (Connection con = connectionManager.getConnection()) {
            String sql = "INSERT INTO documents_tbl(documents_path, "
                    + "documents_indexTime) VALUES(?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, d.getDocument_Path());
            ps.setDate(2, new java.sql.Date(
                    d.getDocument_IndexTime().getTime()));

            ps.executeUpdate();

            return true;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean createAll(List<Document> d_list) {
        try (Connection con = connectionManager.getConnection()) {
            for (Document doc : d_list) {
                String sql = "INSERT INTO documents_tbl(documents_path, "
                        + "documents_indexTime) VALUES(?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, doc.getDocument_Path());
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
    public List<Document> readAll() {
        try (Connection con = connectionManager.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM documents_tbl");
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

    @Override
    public Document read(Integer id) {
        try (Connection con = connectionManager.getConnection()) {
            String sql = "SELECT * FROM documents_tbl WHERE documents_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return resultSetToObject(rs);
            } else {
                System.err.println("Unable to retrieve document with ID = "
                        + id);
                return null;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(Document d) {
        try (Connection con = connectionManager.getConnection()) {
            String sql = "UPDATE documents_tbl SET documents_path = ?, "
                    + "documents_indexTime = ? WHERE Id = "
                    + d.getDocument_ID();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, d.getDocument_Path());
            ps.setDate(2,
                    new java.sql.Date(d.getDocument_IndexTime()
                            .getTime()));

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("Unable to update document with ID = "
                        + d.getDocument_ID());
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection con = connectionManager.getConnection()) {
            String sql = "DELETE documents_tbl WHERE documents_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("Unable to delete document with ID = "
                        + id);
                return false;
            }

            return true;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public Document resultSetToObject(ResultSet rs) {
        try {
            int ID = rs.getInt("documents_id");
            String path = rs.getString("documents_path");
            Date indexTime = new java.sql.Date(rs.getDate("documents_indexTime").getTime());

            return new Document(ID, path, indexTime);

        } catch (SQLException ex) {
            System.out.println(ex.getSQLState());
            return null;
        }
    }

    @Override
    public ArrayList<Document> search(String search) {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }
}
