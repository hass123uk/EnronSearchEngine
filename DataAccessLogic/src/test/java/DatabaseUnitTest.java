import DataAccessLayer.Database.Database;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUnitTest {

    @Test
    public void getConnection_Assert_Datbase_Connection() {
        try {
            Connection connection = Database.getConnection();
            Assert.assertNotNull(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
