package dk.easv.SearchIndexer.DataAccessLayer.Database;


import dk.easv.SearchIndexer.DomainModels.Document;

import java.sql.*;

public class DocumentsRepository {

    public int saveDocument(Document document) throws SQLException {
        String sqlInsert = "INSERT INTO documents_tbl(documents_url, "
                + "documents_indexTime) VALUES(?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, document.getDocument_Path());
            preparedStatement.setDate(2, new java.sql.Date(
                    document.getDocument_IndexTime().getTime()));

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return -1;
        }
    }


    private Document resultSetToObject(ResultSet rs) throws SQLException {
        int ID = rs.getInt("documents_id");
        String URL = rs.getString("documents_url");

        return new Document(ID, URL);

    }
}
