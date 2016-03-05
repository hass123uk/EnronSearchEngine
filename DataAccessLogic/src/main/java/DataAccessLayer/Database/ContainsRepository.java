/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer.Database;

import org.async.jdbc.AsyncConnection;
import org.async.jdbc.PreparedQuery;
import org.async.jdbc.PreparedStatement;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 *
 * @author HassanMahmud
 */
public class ContainsRepository {

    private final AsyncConnection connection;

    public ContainsRepository() {
        connection = new DatabaseConnection().getConnection();
    }

    public void saveIndexInContainTbl(int termId, int documentId) {
        String sqlInsert = "INSERT INTO contain_tbl(terms_id, documents_id)"
                + "VALUES(?, ?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlInsert);

            preparedStatement.executeUpdate(pstmt -> {
                pstmt.setInteger(1, termId);
                pstmt.setInteger(2, documentId);
            },DatabaseConnection.returnSuccessCallback());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}