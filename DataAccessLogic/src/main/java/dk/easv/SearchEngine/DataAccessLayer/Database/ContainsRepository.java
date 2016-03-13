/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.easv.SearchEngine.DataAccessLayer.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
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
            connection.setAutoCommit(false);

            int positionId = 1;
            int i = 0;

            for (Integer termId : termIds) {
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
