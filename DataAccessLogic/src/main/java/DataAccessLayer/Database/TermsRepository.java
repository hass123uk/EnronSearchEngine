package DataAccessLayer.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TermsRepository {

    private final Connection connection;

    public TermsRepository() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public int saveTerm(String term) throws SQLException {
        String sqlInsert = "INSERT INTO terms_tbl(terms_value) VALUES(?)";

        String sqlSelect = "SELECT LAST_INSERT_ID()";
        try (PreparedStatement preparedStatement
                = connection.prepareStatement(sqlInsert)) {

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
