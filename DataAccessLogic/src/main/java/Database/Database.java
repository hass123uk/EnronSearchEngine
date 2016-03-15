package Database;

import DataAccessLayer.FileSystem.FileUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public final class Database {

    private static final String PATH_TO_SQL_SCRIPT = System.getProperty("user.dir") + "/DocumentTermsStructureDump.sql";

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

    public static boolean checkForTables() {
        String sqlCheck = "SHOW TABLES";
        try (Connection connection = Database.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sqlCheck);
            ResultSet res = ps.executeQuery();
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

    public static boolean initDatabase() {
        Statement st = null;
        try (Connection connection = Database.getConnection())
        {
            Scanner s = new Scanner(FileUtil.getInputStreamFrom(PATH_TO_SQL_SCRIPT));
            s.useDelimiter("(;(\r)?\n)|(--\n)");
            st = connection.createStatement();
            while (s.hasNext())
            {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/"))
                {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }

                if (line.trim().length() > 0)
                {
                    st.execute(line);
                }
            }
            st.close();
        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
