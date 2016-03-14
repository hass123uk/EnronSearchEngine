package Database;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class Database {

    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/temDocs?autoReconnect=true&useSSL=false&rewriteBatchedStatements=true");
        dataSource.setUsername("sqluser");
        dataSource.setPassword("sqluserpw");
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private Database() {}
}
