import DataAccessLayer.FileSystem.FileUtil;
import Database.Database;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import static Database.Database.checkForTables;
import static Database.SQLHelper.executeSql;
import static junit.framework.Assert.assertFalse;
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
                Connection connection = Database.getConnection(mConfig);
                InputStream sql = FileUtil.getInputStreamFrom(PATH_TO_CREATE_SQL);
                executeSql(connection, sql);
            } catch (SQLException | FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Simple test to see if database connection if working
     */
    @Test
    public void testDbConnection() throws SQLException {
        Database database = mock(Database.class);

        verify(database).getConnection(mConfig);
    }


    @After
    public void tearDown() {
        try {
            Connection connection = Database.getConnection(mConfig);
            InputStream sql = FileUtil.getInputStreamFrom(PATH_TO_DELETE_SQL);
            executeSql(connection, sql);
        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
