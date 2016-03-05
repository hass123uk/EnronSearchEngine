package DataAccessLayer.Database;

import com.enron.search.domainmodels.Term;
import org.async.jdbc.AsyncConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TermsRepositoryImpl implements ICRUD<Integer, Term> {

    private final DatabaseConnection connectionManager;

    public TermsRepositoryImpl() {
        connectionManager = new DatabaseConnection();
    }

    @Override
    public boolean create(Term t) {
        AsyncConnection con = connectionManager.getConnection();
        String sql = "INSERT INTO terms_tbl (terms_value) VALUES (?)";
//        PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
//        ps.setString(1, t.getTerm_Value());;
//
//        ps.executeUpdate();

        return true;
    }

    @Override
    public boolean createAll(List<Term> t_list) {
        AsyncConnection con = connectionManager.getConnection();
        for (Term t : t_list) {
            String sql = "INSERT INTO terms_tbl(terms_value) VALUES(?)";
//
//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setString(1, t.getTerm_Value());
//
//            ps.executeUpdate();
        }
        return true;
    }

    @Override
    public List<Term> readAll() {
        AsyncConnection con = connectionManager.getConnection();
//            Statement st = con.createStatement();
//            ResultSet rs = st.executeQuery(
//                    "SELECT * FROM terms_tbl");
            ArrayList<Term> terms = new ArrayList<>();
//            while (rs.next()) {
//                terms.add(resultSetToObject(rs));
//            }
        return terms;
    }

    @Override
    public Term read(Integer id) {
        AsyncConnection con = connectionManager.getConnection();
            String sql = "SELECT * FROM terms_tbl WHERE terms_id = ?";

//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setInt(1, id);
//
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                return resultSetToObject(rs);
//            } else {
//                System.err.println("Unable to retrieve term with ID = "
//                        + id);
//                return null;
//            }
        return null;
    }

    @Override
    public boolean update(Term t) {
        AsyncConnection con = connectionManager.getConnection();
            String sql = "UPDATE terms_tbl SET terms_value = ? WHERE Id = "
                    + t.getTerm_ID();
//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setString(1, t.getTerm_Value());
//
//            int affectedRows = ps.executeUpdate();
//
//            if (affectedRows == 0) {
//                System.err.println("Unable to update term with ID = "
//                        + t.getTerm_ID());
//                return false;
//            }

        return true;
    }

    @Override
    public boolean delete(Integer id) {
        AsyncConnection con = connectionManager.getConnection();
            String sql = "DELETE terms_tbl WHERE terms_id = ?";

//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setInt(1, id);
//
//            int affectedRows = ps.executeUpdate();
//
//            if (affectedRows == 0) {
//                System.err.println("Unable to delete term with ID = "
//                        + id);
//                return false;
//            }

            return true;
    }

    @Override
    public Term resultSetToObject(ResultSet rs) {
        try {
            int ID = rs.getInt("terms_id");
            String value = rs.getString("terms_value");

            return new Term(ID, value);

        } catch (SQLException ex) {
            System.out.println(ex.getSQLState());
            return null;
        }
    }

    @Override
    public ArrayList<Term> search(String query) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
