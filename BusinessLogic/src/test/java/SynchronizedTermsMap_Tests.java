import BusinessLogicLayer.SynchronizedTermsMap;
import DomainModels.Term;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by HassanMahmud on 09/03/2016.
 */
public class SynchronizedTermsMap_Tests {
    private static final int STARTING_NUM_OF_TERMS = 200;
    private List<Term> startingTerms;
    private SynchronizedTermsMap synchronizedTermsMap;

    @Before
    public void setup() {
        startingTerms = new ArrayList<>();
        for (int i = 0; i < STARTING_NUM_OF_TERMS; i++) {
            startingTerms.add(new Term(i, "test" + i));
        }
        synchronizedTermsMap = new SynchronizedTermsMap(startingTerms);
    }

    @Test
    public void synchronizedTermsMap_CheckTermsAreAddedToMap_AssertSizeEqualToTermList() {
        int size = synchronizedTermsMap.getMapSize();
        assertEquals(size, STARTING_NUM_OF_TERMS);
    }

    @Test
    public void getTermIDIfPresent_ProvideWithNewTerm_AssertTermNotPresent() {
        //Arrange
        String term_value = "I AM A NEW Term";
        //Act
        int term_Id = synchronizedTermsMap.getTermIDIfPresent(term_value);
        //Assert
        assertEquals(term_Id, SynchronizedTermsMap.TERM_NOT_PRESENT);
    }
}
