package latitude.serialization.test;
import latitude.serialization.LatitudeLocationRequest;
import latitude.serialization.LatitudeLocationResponse;
import latitude.serialization.LocationRecord;
import latitude.serialization.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/***********************************************
 *
 * Author: Brenden Detels and Andrew Bury
 * Assignment: Project 1
 * Class: CSI 4321
 *
 **********************************************/

public class LatitudeLocationResponseTest {


    /**********************************************************
     * Tests Reponse Constructor
     **********************************************************/
    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
            @Test
            @DisplayName("Test Constructor Correctness")
            void testConstructor1() throws ValidationException {
                LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "South America");
                assertEquals(llr.getMapName(), "South America");
                assertTrue(llr.getMapName().matches("\\A\\p{ASCII}*\\z"));
            }

            @Test
            @DisplayName("Test Constructor Not Null")
            void testConstructor2() throws ValidationException {
                LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "South America");
                assertNotNull(llr);
            }


        @Test
        @DisplayName("Test Constructor mapId is greater than 0")
        void testConstructor3() throws ValidationException {
            assertThrows(ValidationException.class, () -> {
                LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "South America");
                llr.setMapId(-3);
            });
        }

        @Test
        @DisplayName("Test Constructor mapId is less than limit")
        void testConstructor4() throws ValidationException {
            assertThrows(ValidationException.class, () -> {
                LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "South America");
                llr.setMapId(4294967296L);
            });
        }

    }



    @Nested
    @DisplayName("Testing setMapName")
    class testSetMapName {

        @Test
        @DisplayName("Test Setter")
        public void testSetter() throws ValidationException {
            LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "South America");
            llr.setMapName("USA");
            assertEquals(llr.getMapName(), "USA");
            assertTrue(llr.getMapName().matches("\\A\\p{ASCII}*\\z"));
        }

        @Test
        @DisplayName("Test Setter Not Null")
        public void testSetter2() throws ValidationException {
            LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "South America");
            llr.setMapName("USA");
            assertNotNull(llr.getMapName());
        }

        @Test
        @DisplayName("Throwing SetMapName if Null")
        public void testSetter3() throws ValidationException{
            assertThrows(ValidationException.class, () -> {
                LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "South America");
                llr.setMapName(null);
            });
        }

        @Test
        @DisplayName("Throwing when Map Name is Not ASCII")
        public void testSetter4() throws ValidationException{
            assertThrows(ValidationException.class, () -> {
                LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "South America");
                llr.setMapName("¡¢µ¶");
            });
        }

    }


    @Nested
    @DisplayName("addLocationRecord and getLocationRecord Tests")
    class addLocationRecordTests{
        @Test
        @DisplayName("Test Adder")
        public void testAdder() throws ValidationException{
            LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "USA");
            LocationRecord a = new LocationRecord(0,"80.0", "80.0", "bdeets","crib");
            llr.addLocationRecord(a);
            LocationRecord b = new LocationRecord(0,"80.0", "80.0", "drewsiph","drew's house");
            llr.addLocationRecord(b);

            List<LocationRecord> check = new ArrayList<LocationRecord>();
            check.add(a);
            check.add(b);
            //List<LocationRecord> list = llr.getLocationRecordList();

            //make sure 2 values
            assertEquals(2, llr.getLocationRecordList().size() );

            //checking list is not null and two values inserted
            assertNotNull(llr.getLocationRecordList());

            assertEquals(llr.getLocationRecordList().get(0), check.get(0));
            assertEquals(llr.getLocationRecordList().get(1), check.get(1));
            assertEquals(llr.getLocationRecordList(), check);
        }

    }

    // NOT NEEDED
    @Test
    @DisplayName("Testing to String Not Null")
    public void testToString() throws ValidationException{
        LatitudeLocationResponse llr = new LatitudeLocationResponse(5,"CA");
        assertNotNull(llr.toString());
        assertTrue(llr.toString().matches("\\A\\p{ASCII}*\\z"));
    }
}
