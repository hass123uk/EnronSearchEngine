package DataAccessLayer.Database;

import org.async.jdbc.AsyncConnection;
import org.async.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TermsRepository {

    private final AsyncConnection connection;

    public TermsRepository() {
        connection = new DatabaseConnection().getConnection();
    }

    public int saveTerm(String term, int documentId) {
        String sqlInsert = "INSERT INTO terms_tbl(terms_value) VALUES(?)";

        String sqlSelect = "SELECT LAST_INSERT_ID()";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlInsert);
//            preparedStatement.setString(1, term);
//            preparedStatement.executeUpdate();
//            ResultSet resultSet = preparedStatement.executeQuery(sqlSelect);
//            if (resultSet.next()) {
//                return resultSet.getInt("LAST_INSERT_ID()");
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
