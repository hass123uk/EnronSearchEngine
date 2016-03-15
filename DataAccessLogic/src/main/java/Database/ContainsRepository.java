package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ContainsRepository {

    public void batchInsertContains(int documentId, List<Integer> termIds) {
        String sqlInsert = "INSERT INTO contain_tbl(terms_id, documents_id, position_index)"
                + "VALUES(?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)
        ) {
            connection.setAutoCommit(false);
            int positionId = 1;

            int i = 0;
            for (int termId : termIds) {
                insertStatement.setInt(1, termId);
                insertStatement.setInt(2, documentId);
                insertStatement.setInt(3, positionId);
                insertStatement.addBatch();
                positionId++;
                i++;
                if (i % 1000 == 0 || i == termIds.size()) {
                    insertStatement.executeBatch();
                }
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
