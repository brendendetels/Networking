package latitude.serialization.test;
import latitude.serialization.LatitudeLocationRequest;
import latitude.serialization.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/***********************************************
 *
 * Author: Brenden Detels and Andrew Bury
 * Assignment: Project 0
 * Class: CSI 4321
 *
 **********************************************/

public class LatitudeLocationRequestTest {

    @Nested
    @DisplayName("Testing Constructor")
    public class testConstructor{
        @Test
        @DisplayName("Checking Not Null")
        public void testConstructor() throws ValidationException {
            LatitudeLocationRequest llr = new LatitudeLocationRequest(8);
            assertNotNull(llr);
        }

        @Test
        @DisplayName("Checking MapID Correctness")
        public void testConstructor2() throws ValidationException{
            LatitudeLocationRequest llr = new LatitudeLocationRequest(8);
            assertNotNull(llr.getMapId());
            assertEquals(llr.getMapId(), 8);
            assertTrue(llr.getMapId() >= 0);
        }
    }

    @Test
    @DisplayName("Test toString not null")
    public void testToString()throws ValidationException{
        LatitudeLocationRequest llr = new LatitudeLocationRequest(8);
        assertNotNull(llr.toString());
        assertTrue(llr.toString().matches("\\A\\p{ASCII}*\\z"));
    }


}
