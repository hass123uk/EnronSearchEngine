import BusinessLogicLayer.IncrementalIDGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by HassanMahmud on 09/03/2016.
 */
public class IncrementalIDGenerator_Tests {

    private IncrementalIDGenerator incrementalIDGenerator;

    @Before
    public void setUp() {
        incrementalIDGenerator = new IncrementalIDGenerator();
    }

    @Test
    public void termPKGenerator_GenerateOneID_AssertNotNull() {
        int generatedId = incrementalIDGenerator.getNextTermID();
        assertNotNull(generatedId);
    }

    @Test
    public void termPKGenerator_GenerateTwoIDs_AssertDifferent() {
        int generatedId1 = incrementalIDGenerator.getNextTermID();
        int generatedId2 = incrementalIDGenerator.getNextTermID();

        assertNotEquals(generatedId1, generatedId2);
    }

    @Test
    public void getNextDocumentID_GenerateOneID_AssertNotNull() {
        int generatedId = incrementalIDGenerator.getNextDocumentID();
        assertNotNull(generatedId);
    }

    @Test
    public void getNextTermID_GenerateTwoIDs_AssertDifferent() {
        int generatedId1 = incrementalIDGenerator.getNextTermID();
        int generatedId2 = incrementalIDGenerator.getNextTermID();

        assertNotEquals(generatedId1, generatedId2);
    }
}