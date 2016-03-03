package DataAccessLayer.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TermsRepository {

    public int saveTerm(String term) throws SQLException {
        String sqlInsert = "INSERT INTO terms_tbl(terms_value) VALUES(?)";

        String sqlSelect = "SELECT LAST_INSERT_ID()";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {

            preparedStatement.setString(1, term);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery(sqlSelect);
            if (resultSet.next()) {
                return resultSet.getInt("LAST_INSERT_ID()");
            }
            return -1;
        }
    }
}
