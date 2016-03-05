/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HassanMahmud
 */
public class ContainsRepository {

    public void saveIndexInContainTbl(int termId, int documentId, int positionId) {
        String sqlInsert = "INSERT INTO contain_tbl(terms_id, documents_id, position_index)"
                + "VALUES(?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {

            preparedStatement.setInt(1, termId);
            preparedStatement.setInt(2, documentId);
            preparedStatement.setInt(3, positionId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void bulkSaveIndexInContainTbl(List<Integer> termIds, int documentId) {
        String sqlInsert = "INSERT INTO contain_tbl(terms_id, documents_id, position_index)"
                + "VALUES(?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            int positionId = 0;
            for(Integer termId : termIds) {
                preparedStatement.setInt(1, termId);
                preparedStatement.setInt(2, documentId);
                preparedStatement.setInt(3, positionId);
                preparedStatement.addBatch();
                positionId++;
            }
            preparedStatement.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
