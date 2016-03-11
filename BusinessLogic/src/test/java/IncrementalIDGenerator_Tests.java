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
        int generatedId = incrementalIDGenerator.termIdGenerator();
        assertNotNull(generatedId);
    }

    @Test
    public void termPKGenerator_GenerateTwoIDs_AssertDifferent() {
        int generatedId1 = incrementalIDGenerator.termIdGenerator();
        int generatedId2 = incrementalIDGenerator.termIdGenerator();

        assertNotEquals(generatedId1, generatedId2);
    }

    @Test
    public void documentPKGenerator_GenerateOneID_AssertNotNull() {
        int generatedId = incrementalIDGenerator.documentPKGenerator();
        assertNotNull(generatedId);
    }

    @Test
    public void documentPKGenerator_GenerateTwoIDs_AssertDifferent() {
        int generatedId1 = incrementalIDGenerator.documentPKGenerator();
        int generatedId2 = incrementalIDGenerator.documentPKGenerator();

        assertNotEquals(generatedId1, generatedId2);
    }
}