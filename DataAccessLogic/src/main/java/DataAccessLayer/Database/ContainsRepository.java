/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer.Database;

import org.async.jdbc.AsyncConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
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

//        try (PreparedStatement preparedStatement
//                = connection.prepareStatement(sqlInsert)) {
//
//            preparedStatement.setInt(1, termId);
//            preparedStatement.setInt(2, documentId);
//
//            preparedStatement.executeUpdate();
        
    }
}
