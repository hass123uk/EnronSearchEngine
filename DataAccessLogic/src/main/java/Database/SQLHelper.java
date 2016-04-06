package Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SQLHelper {

    public static void executeSqlFile(Connection connection, File sql) throws SQLException, FileNotFoundException {
        InputStream is = new FileInputStream(sql);
        Scanner s = new Scanner(is);
        execute(connection,s);
    }

    private static void execute(Connection connection, Scanner s) throws SQLException {
        try (Statement statement = connection.createStatement()){
            s.useDelimiter("(;(\r)?\n)|(--\n)");
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
                    statement.execute(line);
                }
            }
        }
        finally {
          connection.close();
        }
    }
}
