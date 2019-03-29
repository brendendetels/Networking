package latitude.serialization.test;
import latitude.serialization.LatitudeLocationResponse;
import latitude.serialization.LatitudeNewLocation;
import latitude.serialization.LocationRecord;
import latitude.serialization.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/***********************************************
 *
 * Author: Brenden Detels and Andrew Bury
 * Assignment: Project 0
 * Class: CSI 4321
 *
 **********************************************/
public class LatitudeNewLocationTest {


    /**********************************************************
     * Tests setLocationRecord
     **********************************************************/
    @Nested
    @DisplayName("Test SetLocationRecord")
    public class testSetLocationRecord {
            @Test
            @DisplayName("Test SetLocationRecord")
            public void testSetLocationRecord() throws ValidationException {
                LocationRecord lr = new LocationRecord(1, "1.2", "3.4", "BU", "Baylor");
                LatitudeNewLocation lnl = new LatitudeNewLocation(5, lr);
                lnl.setLocationRecord(lr);
                assertEquals(lr, lnl.getLocationRecord());
            }

            @Test
            @DisplayName("Test setLocationRecord is not null")
            public void testSetLocationRecordNotNull() throws ValidationException {
                LocationRecord lr = new LocationRecord(1, "1.2", "3.4", "BU", "Baylor");
                LatitudeNewLocation lnl = new LatitudeNewLocation(5, lr);
                lnl.setLocationRecord(lr);
                assertNotNull(lnl.getLocationRecord());
            }

            @Test
            @DisplayName("Test SetLocationRecord is throwing since Constructor is using setLocatinoRecord")
            public void testSetLocationRecord2(){
                assertThrows(ValidationException.class, () -> {
                    LocationRecord lr = null;
                    LatitudeNewLocation lnl = new LatitudeNewLocation(5, lr);
                });
            }
    }


    /**********************************************************
     * Tests LatitudeNewLocation Constructor
     **********************************************************/
    @Nested
    @DisplayName("Test Constructor")
    public class testConstructor {

        @Test
        @DisplayName("Test that LocationRecord is not null")
        public void testLatitudeNewLocationConstructor() throws ValidationException {
            LocationRecord lr = new LocationRecord(1, "1.2", "3.4", "BU", "Baylor");
            LatitudeNewLocation lnl = new LatitudeNewLocation(5, lr);
            assertNotEquals(null, lnl.getLocationRecord());
        }

        @Test
        @DisplayName("Test constructor correctness")
        public void testLatitudeNewLocationConstructor2() throws ValidationException {
            LocationRecord lr = new LocationRecord(1, "1.2", "3.4", "BU", "Baylor");
            LatitudeNewLocation lnl = new LatitudeNewLocation(5, lr);
            assertEquals(lr, lnl.getLocationRecord());
        }

    }


    //NOT NEEDED
    @Test
    @DisplayName("Test toString Not Null")
    public void testToString() throws ValidationException{
        LocationRecord lr = new LocationRecord(1, "1.2", "3.4", "BU", "Baylor");
        LatitudeNewLocation lnl = new LatitudeNewLocation(5, lr);
        assertNotNull(lnl.toString());
        assertTrue(lnl.toString().matches("\\A\\p{ASCII}*\\z"));
    }



}
