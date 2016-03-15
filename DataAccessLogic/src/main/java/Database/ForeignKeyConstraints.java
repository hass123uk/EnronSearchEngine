package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by HassanMahmud on 14/03/2016.
 */
public class ForeignKeyConstraints {

    private static final String ADD_FK_CONSTRAINT
            = "ALTER TABLE contain_tbl "
            + "ADD CONSTRAINT contain_term_fk1 "
            + "FOREIGN KEY(terms_id) "
            + "REFERENCES terms_tbl(terms_id) "
            + "ON DELETE CASCADE";

    private static final String DROP_FK_CONSTRAINT
            = "ALTER TABLE contain_tbl DROP FOREIGN KEY contain_term_fk1";


    public void createTermIDFKConstraint() {
        executeStatement(ADD_FK_CONSTRAINT);
    }

    public void dropTermIDFKConstraint() {
        executeStatement(DROP_FK_CONSTRAINT);
    }

    private void executeStatement(String SQL) {
        try (Connection connection = Database.getConnection();
             Statement statment = connection.createStatement()
        ) {
            statment.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
