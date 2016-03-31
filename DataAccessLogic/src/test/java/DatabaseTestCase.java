import Database.Database;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import static Database.Database.checkForTables;
import static Database.Database.runSqlScript;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by anthonymonori on 17/03/16.
 */
public class DatabaseTestCase {

    private Configuration mConfig = null;
    private final String PATH_TO_CREATE_SQL = System.getProperty("user.dir") + "/../DocumentTermsDump.sql";
    private final String PATH_TO_DELETE_SQL = System.getProperty("user.dir") + "/../DbCleanup.sql";
    private Database mDatabase = null;

    /**
     * Create a custom configuration object that is needed to be passed onto the database connection class
     */
    @Before
    public void setUp() {
        mConfig = new PropertiesConfiguration();

        mConfig.addProperty("DB_TYPE", "mysql");
        mConfig.addProperty("DB_HOSTNAME", "localhost");
        mConfig.addProperty("DB_NAME", "IndexingTestDb");
        mConfig.addProperty("DB_USER", "sqluser");
        mConfig.addProperty("DB_PASS", "sqluserpw");

        mConfig.addProperty("ID_DEFAULT_FOLDER_TO_MONITOR", "TestData");
        mConfig.addProperty("ID_EXTENSION", ".txt");

        if (!checkForTables(mConfig)) {
            try {
                runSqlScript(mConfig, PATH_TO_CREATE_SQL);
            } catch (SQLException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        mDatabase = mock(Database.class);
    }

    /**
     * Simple test to see if database connection if working.
     */
    @Test
    public void databaseTestCase_verifyConnection_assertNotNull() throws SQLException {
        Connection connection = verify(mDatabase).getConnection(mConfig);

        assertNotNull(connection);

        connection.close();
    }

    /**
     * Verify that there is three table in the database
     */
    @Test
    public void databaseTestCase_checkForTables_assertBoolean() throws SQLException {
        boolean tablesExist = false;                ;
        tablesExist = mDatabase.checkForTables(mConfig);

        assertTrue(tablesExist);
    }

    /**
     * Using a custom sql script, drop the three tables and clean up the database
     */
    @After
    public void tearDown() {
        try {
            runSqlScript(mConfig, PATH_TO_DELETE_SQL);
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
