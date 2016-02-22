package DataAccessLayer.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class setting up the connecting properties for the documentTerms database.
 * The properties are located in a configuration file under the configuration
 * directory;
 */
public class DatabaseConnection {

    //DB-Connection Properties
    private static final String DB_URL = "jdbc:mysql://localhost/documentterms";
    private static final String DB_SETTINGS = "?autoReconnect=true&useSSL=false";

    private static final String DBUSER_USERNAME = "sqluser";
    private static final String DBUSER_PASSWORD = "sqluserpw";

    private Connection connection;
    private static DatabaseConnection instance = null;

    /**
     * Creates an instance of this class based on the properties from the
     * configuration file.
     *
     * @throws IOException if unable to read the configuration file.
     */
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            this.connection = DriverManager
                    .getConnection(DB_URL + DB_SETTINGS,
                            DBUSER_USERNAME,
                            DBUSER_PASSWORD);

        } catch (ClassNotFoundException ex) {
            System.out.println("ERROR: DB JDBC Driver not found.");
        } catch (SQLException ex) {
            System.out.println(ex.getSQLState());
        }
    }

    /**
     * Singleton pattern, gets the instance of the DatabaseConnection. The
 DatabaseConnection is then created only once.
     *
     * @return the instance of the DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Returns a connection for the Administration database.
     *
     * @return a connection to the database.
     */
    public Connection getConnection() {
        return connection;
    }
}