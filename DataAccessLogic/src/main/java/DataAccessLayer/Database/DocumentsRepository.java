package DataAccessLayer.Database;

import com.enron.search.domainmodels.Document;
import org.async.jdbc.*;

import java.sql.SQLException;

public class DocumentsRepository {

    private final AsyncConnection connection;

    public DocumentsRepository() {
        connection = new DatabaseConnection().getConnection();
    }

    public int saveDocument(Document document) {
        String sqlInsert = "INSERT INTO documents_tbl(documents_url, "
                + "documents_indexTime) VALUES(?, ?)";

        String sqlSelect = "SELECT LAST_INSERT_ID()";

        final int[] lastID = new int[1];

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

            preparedStatement.executeUpdate(psmt -> {
                psmt.setString(1, document.getDocument_Path());
                psmt.setDate(2, new java.sql.Date(document.getDocument_IndexTime().getTime()));
            }, DatabaseConnection.returnSuccessCallback());

            Statement st = connection.createStatement();

            st.executeCall(sqlSelect,
                    new ResultSetCallback() {
                        @Override
                        public void onError(SQLException e) {
                            lastID[0] = -1;
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
        }
        return lastID[0];
    }
}
