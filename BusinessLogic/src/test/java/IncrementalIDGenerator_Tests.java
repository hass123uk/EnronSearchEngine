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
        String generatedId = incrementalIDGenerator.termIdGenerator();
        assertNotNull(generatedId);
    }

    @Test
    public void termPKGenerator_GenerateTwoIDs_AssertDifferent() {
        String generatedId1 = incrementalIDGenerator.termIdGenerator();
        String generatedId2 = incrementalIDGenerator.termIdGenerator();

        assertNotEquals(generatedId1, generatedId2);
    }

    @Test
    public void documentPKGenerator_GenerateOneID_AssertNotNull() {
        String generatedId = incrementalIDGenerator.documentPKGenerator();
        assertNotNull(generatedId);
    }

    @Test
    public void documentPKGenerator_GenerateTwoIDs_AssertDifferent() {
        String generatedId1 = incrementalIDGenerator.documentPKGenerator();
        String generatedId2 = incrementalIDGenerator.documentPKGenerator();

        assertNotEquals(generatedId1, generatedId2);
    }
}