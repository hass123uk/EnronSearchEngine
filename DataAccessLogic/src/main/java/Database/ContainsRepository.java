package Database;


import org.apache.commons.configuration2.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ContainsRepository {

    private final Configuration mConfig;

    public ContainsRepository(Configuration config) {
        this.mConfig = config;
    }

    public void batchInsertContains(int documentId, List<Integer> termIds) {
        String sqlInsert = "INSERT INTO contain_tbl(terms_id, documents_id, position_index)"
                + "VALUES(?, ?, ?)";

        try (Connection connection = Database.getConnection(mConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            connection.setAutoCommit(false);

            int positionId = 1;

            int i = 0;
            for (int termId : termIds) {
                preparedStatement.setInt(1, termId);
                preparedStatement.setInt(2, documentId);
                preparedStatement.setInt(3, positionId);
                preparedStatement.addBatch();
                positionId++;
                i++;
                if (i % 1000 == 0 || i == termIds.size()) {
                    preparedStatement.executeBatch();
                }
            }

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
