package DataAccessLayer.Database;

import com.enron.search.domainmodels.Term;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TermsRepository {

    public int saveTerm(String term) throws SQLException {
        String sqlInsert = "INSERT INTO terms_tbl(terms_value) VALUES(?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, term);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return -1;
        }
    }

    public List<Integer> batchSaveTerm(List<Term> terms) {
        String sqlInsert = "INSERT INTO terms_tbl(terms_value) VALUES(?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            int i = 0;
            for (Term term : terms) {
                preparedStatement.setString(1, term.getTerm_Value());
                preparedStatement.addBatch();

                i++;
                if (i % 1000 == 0 || i == terms.size()) {
                    preparedStatement.executeBatch();
                }
            }

            List<Integer> termIds = new ArrayList<>();
            ResultSet ids = preparedStatement.getGeneratedKeys();
            while (ids.next()) {
                termIds.add(ids.getInt(1));
            }
            connection.commit();
            return termIds;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Term> readAll() {
        List<Term> allTerms = new ArrayList<>();
        String sqlSelect = "Select * FROM terms_tbl";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect, Statement.RETURN_GENERATED_KEYS)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
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

    public Term getTerm(String term_Value) {
        String sqlSelect = "Select * FROM terms_tbl where terms_Value = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect)
        ) {
            preparedStatement.setString(1, term_Value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int terms_id = resultSet.getInt("terms_id");
                String terms_value = resultSet.getString("terms_value");
                Term term = new Term(terms_id, terms_value);
                return term;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
