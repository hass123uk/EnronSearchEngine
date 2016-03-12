package DataAccessLayer.Database;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Class setting up the connecting properties for the documentTerms database.
 * The properties are located in a configuration file under the configuration
 * directory;
 */
public final class Database {

    private static final PropertiesConfiguration config = new PropertiesConfiguration();
    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        try {
            config.load(new File("config.properties"));
            String dbType = config.getString("DB_TYPE");
            String dbTypePrefix;
            switch (dbType) {
                case "mysql":
                    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                    dbTypePrefix="jdbc:mysql://";
                    break;
                default:
                    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                    dbTypePrefix="jdbc:mysql://";
                    break;
            }
            dataSource.setUrl(dbTypePrefix+config.getString("DB_URL"));
            dataSource.setUsername(config.getString("DB_USER"));
            dataSource.setPassword(config.getString("DB_PASS"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private Database() {}
}
