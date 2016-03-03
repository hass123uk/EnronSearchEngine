package DataAccessLayer.Database;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Class setting up the connecting properties for the documentTerms database.
 * The properties are located in a configuration file under the configuration
 * directory;
 */
public final class Database {

    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/documentterms");
        dataSource.setUsername("sqluser");
        dataSource.setPassword("sqluserpw");
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private Database() {
        //
    }


}
