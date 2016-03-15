package Database;

import DomainModels.Document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DocumentsRepository {
    public void insertDocument(Document document){
        String sqlInsert = "INSERT INTO documents_tbl(documents_id, documents_url, "
                + "documents_indexTime) VALUES(?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)
        ) {
            preparedStatement.setInt(1, document.getDocument_ID());
            preparedStatement.setString(2, document.getDocument_Path());
            preparedStatement.setDate(3, new java.sql.Date(
                    document.getDocument_IndexTime().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
