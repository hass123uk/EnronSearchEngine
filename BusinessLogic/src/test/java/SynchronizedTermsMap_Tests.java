import BusinessLogicLayer.SynchronizedTermsMap;
import BusinessLogicLayer.UUIDGenerator;
import com.enron.search.domainmodels.Term;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by HassanMahmud on 09/03/2016.
 */
public class SynchronizedTermsMap_Tests {
    private static final int STARTING_NUM_OF_TERMS = 800;
    private List<Term> startingTerms;
    private SynchronizedTermsMap synchronizedTermsMap;

    @Before
    public void setup() {
        startingTerms = new ArrayList<>();
        for (int i = 0; i < STARTING_NUM_OF_TERMS; i++) {
            startingTerms.add(new Term("" + i, "test" + i));
        }

        synchronizedTermsMap = new SynchronizedTermsMap(startingTerms);
    }

    @Test
    public void synchronizedTermsMap_CheckTermsAreAddedToMap_AssertSizeEqualToTermList() {
        int size = synchronizedTermsMap.getMapSize();
        assertEquals(size, STARTING_NUM_OF_TERMS);
    }

    @Test
    public void checkDuplicateTerms_ProvideWithNewTerm_AssertIdEqualToGeneratedID() {
        //Arrange
        String generatedID = "GenereatedID";
        String term_value = "I AM A NEW Term";
        //Act
        String term_Id = synchronizedTermsMap.checkDuplicateTerms(term_value, generatedID);
        //Assert
        assertEquals(term_Id, generatedID);
    }

    @Test
    public void checkDuplicateTerms_ProvideWithTermAlreadyInMap_AssertIdNotEqualToGeneratedID() {
        //Arrange
        String generatedID = "GenereatedID";
        String term_value = startingTerms.get(0).getTerm_Value();
        //Act
        String term_Id = synchronizedTermsMap.checkDuplicateTerms(term_value, generatedID);
        //Assert
        assertNotEquals(term_Id, generatedID);
    }
}
