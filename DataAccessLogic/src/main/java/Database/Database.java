package Database;

import DataAccessLayer.FileSystem.FileUtil;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;

public class Database {

    private static final BasicDataSource dataSource = new BasicDataSource();

    public static Connection getConnection(Configuration config) throws SQLException {
        String dbtype=config.getString("DB_TYPE");
        switch (dbtype) {
            case "mysql":
                dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        }
        dataSource.setUrl("jdbc:"+dbtype+"://"+config.getString("DB_HOSTNAME")+"/"+config.getString("DB_NAME")+"?autoReconnect=true&useSSL=false&rewriteBatchedStatements=true");
        dataSource.setUsername(config.getString("DB_USER"));
        dataSource.setPassword(config.getString("DB_PASS"));

        return dataSource.getConnection();
    }

    private Database() {}

    public static boolean checkForTables(Configuration config) {
        String sqlCheck = "SHOW TABLES";
        try (Connection connection = Database.getConnection(config);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlCheck);
             ResultSet res = preparedStatement.executeQuery()) {
                res.last();
                if (res.getRow() != 3) {
                    return false;
                }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void runSqlScript(Configuration config, String sqlScriptPath) throws SQLException, FileNotFoundException {
        Connection connection = Database.getConnection(config);
        InputStream sql = FileUtil.getInputStreamFrom(sqlScriptPath);
        SQLHelper.executeSql(connection,sql);
    }

}

