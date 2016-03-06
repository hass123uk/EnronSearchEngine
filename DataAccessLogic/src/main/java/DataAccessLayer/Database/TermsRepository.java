package DataAccessLayer.Database;

import com.enron.search.domainmodels.Term;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TermsRepository {

    public int saveTerm(String term) throws SQLException {
        String sqlInsert = "INSERT INTO terms_tbl(terms_value) VALUES(?)";

        String sqlSelect = "SELECT LAST_INSERT_ID()";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)
        ) {

            preparedStatement.setString(1, term);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery(sqlSelect);
            if (resultSet.next()) {
                return resultSet.getInt("LAST_INSERT_ID()");
            }
            return -1;
        }
    }

    public List<Term> readAll() {
        List<Term> allTerms = new ArrayList<>();
        String sqlSelect = "Select * FROM terms_tbl";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int terms_id = resultSet.getInt("terms_id");
                String terms_value = resultSet.getString("terms_value");
                allTerms.add(new Term(terms_id, terms_value));
            }
            return allTerms;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
