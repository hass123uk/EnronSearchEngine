import BusinessLogicLayer.IncrementalIDGenerator;
import BusinessLogicLayer.SynchronizedTermsMap;
import com.enron.search.domainmodels.Term;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

/**
 * Created by HassanMahmud on 09/03/2016.
 */
public class SynchronizedTermsMap_Tests {
    private static final int STARTING_NUM_OF_TERMS = 800;
    private List<Term> startingTerms;
    private SynchronizedTermsMap synchronizedTermsMap;
    private IncrementalIDGenerator mockIncrementalIDGenerator;

    @Before
    public void setup() {
        startingTerms = new ArrayList<>();
        for (int i = 0; i < STARTING_NUM_OF_TERMS; i++) {
            startingTerms.add(new Term("" + i, "test" + i));
        }
        mockIncrementalIDGenerator = mock(IncrementalIDGenerator.class);
        synchronizedTermsMap = new SynchronizedTermsMap(startingTerms, mockIncrementalIDGenerator);
    }

    @Test
    public void synchronizedTermsMap_CheckTermsAreAddedToMap_AssertSizeEqualToTermList() {
        int size = synchronizedTermsMap.getMapSize();
        assertEquals(size, STARTING_NUM_OF_TERMS);
    }

    @Test
    public void checkDuplicateTerms_ProvideWithNewTerm_AssertIdEqualToGeneratedID() {
        //Arrange
        String generatedId = "IDGenerated";
        String term_value = "I AM A NEW Term";
        when(mockIncrementalIDGenerator.termIdGenerator()).thenReturn(generatedId);
        //Act
        String term_Id = synchronizedTermsMap.getIdOrGenerateNewId(term_value);
        //Assert
        assertEquals(term_Id, generatedId);
    }

    @Test
    public void checkDuplicateTerms_ProvideWithTermAlreadyInMap_AssertIdNotEqualToGeneratedID() {
        //Arrange
        String generatedId = "IDGenerated";
        when(mockIncrementalIDGenerator.termIdGenerator()).thenReturn(generatedId);

        String term_value = startingTerms.get(0).getTerm_Value();
        //Act
        String term_Id = synchronizedTermsMap.getIdOrGenerateNewId(term_value);
        //Assert
        assertNotEquals(term_Id, generatedId);
    }
}
