package DataAccessLayer.Database;

import java.io.IOException;
import java.sql.SQLException;

import org.async.jdbc.*;
import org.async.mysql.MysqlConnection;
import org.async.mysql.protocol.packets.OK;
import org.async.net.Multiplexer;
/**
 * Class setting up the connecting properties for the documentTerms database.
 * The properties are located in a configuration file under the configuration
 * directory;
 */
public class DatabaseConnection {

    //DB-Connection Properties
    private static final String DB_URL = "localhost";
    private static final String DB_NAME = "documentterms";

    private static final String DBUSER_USERNAME = "sqluser";
    private static final String DBUSER_PASSWORD = "sqluserpw";

    private Connection connection;
    private static DatabaseConnection instance = null;

    /**
     * Creates an instance of this class based on the properties from the
     * configuration file.
     *
     */
    public DatabaseConnection() {


    }

    public static SuccessCallback returnSuccessCallback() {
        return new SuccessCallback() {

            @Override
            public void onSuccess(OK ok) {
                System.out.println("OK");
            }

            @Override
            public void onError(SQLException e) {
                e.printStackTrace();
            }

        };
    }

    public AsyncConnection getConnection() {
        Multiplexer mpx = null;
        try {
            mpx = new Multiplexer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return (AsyncConnection) (connection = new MysqlConnection(DB_URL, 3306, DBUSER_USERNAME, DBUSER_PASSWORD, DB_NAME, mpx.getSelector(), returnSuccessCallback()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
