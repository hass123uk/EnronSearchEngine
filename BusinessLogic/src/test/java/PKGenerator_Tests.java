import BusinessLogicLayer.UUIDGenerator;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by HassanMahmud on 09/03/2016.
 */
public class PKGenerator_Tests {

    @Test
    public void generate64BitID_GenerateOneID_AssertNotNull() {
        String generatedId = UUIDGenerator.generateID();
        assertNotNull(generatedId);
    }

    @Test
    public void generate64BitID_GenerateTwoIDs_AssertDifferent() {
        String generatedId1 = UUIDGenerator.generateID();
        String generatedId2 = UUIDGenerator.generateID();

        assertNotEquals(generatedId1, generatedId2);
    }
}