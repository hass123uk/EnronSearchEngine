package DataAccessLayer.Database;

import org.async.jdbc.AsyncConnection;
import org.async.jdbc.PreparedStatement;
import org.async.jdbc.ResultSetCallback;
import org.async.jdbc.Statement;

import java.sql.SQLException;

public class TermsRepository {

    private final AsyncConnection connection;

    public TermsRepository() {
        connection = new DatabaseConnection().getConnection();
    }

    public int saveTerm(String term, int documentId) {
        String sqlInsert = "INSERT INTO terms_tbl(terms_value) VALUES(?)";

        String sqlSelect = "SELECT LAST_INSERT_ID()";

        final int[] lastID = new int[1];

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

            preparedStatement.executeUpdate(psmt -> {
                psmt.setString(1, term);
            }, DatabaseConnection.returnSuccessCallback());

            Statement st = connection.createStatement();

            st.executeCall(sqlSelect,
                    new ResultSetCallback() {
                        @Override
                        public void onError(SQLException e) {

                        }

                        @Override
                        public void onResultSet(org.async.jdbc.ResultSet resultSet) {
                            while (resultSet.hasNext()) {
                                lastID[0] = resultSet.getInteger("LAST_INSERT_ID()");
                            }
                        }
            }, DatabaseConnection.returnSuccessCallback());

        } catch (SQLException e) {
            e.printStackTrace();
            lastID[0] = -1;
        }
        return lastID[0];
    }
}
