package Database;
import DomainModels.Document;
import org.apache.commons.configuration2.Configuration;

import java.sql.*;

public class DocumentsRepository {

    private final Configuration mConfig;

    public DocumentsRepository(Configuration config) {
        this.mConfig = config;
    }

    public void insertDocument(Document document){
        String sqlInsert = "INSERT INTO documents_tbl(documents_id, documents_url, "
                + "documents_indexTime) VALUES(?, ?, ?)";

        try (Connection connection = Database.getConnection(mConfig);
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
