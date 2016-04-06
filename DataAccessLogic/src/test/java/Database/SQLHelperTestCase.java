package Database;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by anthonymonori on 31/03/16.
 */
public class SQLHelperTestCase {

    public static final String PATH_TO_VALID_SQL = System.getProperty("user.dir") + "/../TestData/valid.sql";
    public static final String PATH_TO_INVALID_SQL= System.getProperty("user.dir") + "/../TestData/invalid.sql";
    Connection mConnection = null;
    File mValidSql;
    File mInvalidSql;

    // Load in two files, one with valid sql, and one with didn't
    @Before
    public void setup() {
        Database database = mock(Database.class);
        try {
            mConnection = database.getConnection(SharedTestConfiguration.getInstance().getConfiguration());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // load files
        mValidSql = new File(PATH_TO_VALID_SQL);
        mInvalidSql = new File(PATH_TO_INVALID_SQL);
    }

    @Test
    public void sqlHelperTestCase_executeValidSqlFile_ReturnNoError() throws FileNotFoundException, SQLException {
        SQLHelper.executeSqlFile(mConnection, mValidSql);
    }

    @Test(expected = SQLException.class)
    public void sqlHelperTestCase_executeInvalidSqlFile_SqlException() throws FileNotFoundException, SQLException {
        SQLHelper.executeSqlFile(mConnection, mInvalidSql);
    }

    @Test(expected = NullPointerException.class)
    public void sqlHelperTestCase_executeNull_FileNotFoundException() throws FileNotFoundException, SQLException {
        SQLHelper.executeSqlFile(mConnection, null);
    }
}
