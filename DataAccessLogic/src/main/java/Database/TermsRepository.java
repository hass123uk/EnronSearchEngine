package Database;
import DomainModels.Term;
import org.apache.commons.configuration2.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TermsRepository {

    private Configuration mConfig;

    public TermsRepository(Configuration config) {
        this.mConfig = config;
    }

    public void batchInsertTerm(List<Term> terms) {
        String sqlInsert = "INSERT INTO terms_tbl(terms_id, terms_value) VALUES(?, ?)";

        try (Connection connection = Database.getConnection(mConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            connection.setAutoCommit(false);

            int i = 0;
            for (Term term : terms) {
                preparedStatement.setInt(1, term.getTerm_ID());
                preparedStatement.setString(2, term.getTerm_Value());

                preparedStatement.addBatch();

                i++;
                if (i % 1000 == 0 || i == terms.size()) {
                    preparedStatement.executeBatch();
                }
            }

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Term> selectAllTerms() {
        List<Term> allTerms = new ArrayList<>();
        String sqlSelect = "Select * FROM terms_tbl";

        try (Connection connection = Database.getConnection(mConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int terms_id = resultSet.getInt("terms_id");
                String terms_value = resultSet.getString("terms_value");
                allTerms.add(new Term(terms_id, terms_value));
            }
            return allTerms;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
