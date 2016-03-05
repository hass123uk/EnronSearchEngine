package DataAccessLayer.Database;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.async.jdbc.AsyncConnection;
import org.async.jdbc.PreparedStatement;
import org.async.jdbc.ResultSet;
import org.async.jdbc.ResultSetCallback;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class SearchDB {

    private final AsyncConnection connection;

    public SearchDB() {
        connection = new DatabaseConnection().getConnection();
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

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlSelect);

            preparedStatement.executeQuery(psmt -> {
                psmt.setString(1, "%" + searchTerm + "%");
            }, new ResultSetCallback() {
                @Override
                public void onResultSet(ResultSet resultSet) {
                    while (resultSet.hasNext()) {
                        resultSet.next();
                        String term = resultSet.getString("terms_value");
                        String doc_Path = resultSet.getString("documents_url");
                        termMultimap.put(term, doc_Path);
                    }
                }

                @Override
                public void onError(SQLException e) {
                    e.printStackTrace();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return termMultimap;
    }
}
