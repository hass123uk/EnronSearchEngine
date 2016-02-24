package DataAccessLayer.Database;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchDB {

    private final Connection connection;

    public SearchDB() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public Multimap<String, String> getSimilarTermsWithDocumentPath(String searchTerm) {
        Multimap<String, String> termMultimap = ArrayListMultimap.create();
        String sqlSelect
                = "SELECT terms_value, documents_url\n"
                + "FROM terms_tbl\n"
                + "inner join contain_tbl\n"
                + "on contain_tbl.terms_id = terms_tbl.terms_id\n"
                + "inner join documents_tbl\n"
                + "on contain_tbl.documents_id = documents_tbl.documents_id\n"
                + "\n"
                + "where terms_value like ?;";

        try (PreparedStatement preparedStatement
                = connection.prepareStatement(sqlSelect)) {

            preparedStatement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String term = resultSet.getString("terms_value");
                String doc_Path = resultSet.getString("documents_url");
                termMultimap.put(term, doc_Path);
            }
            return termMultimap;

        } catch (SQLException ex) {
            Logger.getLogger(SearchDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
