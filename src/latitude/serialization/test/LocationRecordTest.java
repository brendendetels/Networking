package latitude.serialization.test;
import latitude.serialization.MessageInput;
import latitude.serialization.MessageOutput;
import org.junit.jupiter.api.*;
import latitude.serialization.LocationRecord;
import latitude.serialization.ValidationException;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 0
 * Class: CSI 4321
 *
 **********************************************/


public class LocationRecordTest {

    private static final String CHARENC = "ASCII";

    /*****************************************************
     * Testing for Constructor(arguments)
     ****************************************************/
    @Nested
    @DisplayName("Constructor(with arguments) Testing")
    class Constructor1Test{

        @Test
        @DisplayName("Check Constructor not null and correct values")
        public void testConstructor1() throws ValidationException, IOException {
            LocationRecord a = new LocationRecord(0,"80.0", "80.0", "drewsiph","drew's house");
            assertNotNull(a);
            assertEquals(0, a.getUserId());
            assertEquals("80.0", a.getLatitude());
            assertEquals("80.0", a.getLongitude());
            assertEquals("drewsiph", a.getLocationName());
            assertEquals("drew's house", a.getLocationDescription());
            LocationRecord b = new LocationRecord(0,"80.0", "80.0", "drewsiph","drew's house");
            assertEquals(a, b);
        }

        //OUR Constructor calls the Setter Functions and those functions test the values
    }



    /*****************************************************
     * Testing for Constructor(MessageInput)
     ****************************************************/
    @Nested
    @DisplayName("Constructor(MessageInput) Testing AKA Decode")
    class Constructor2Test{

        /*@Test
        @DisplayName("Check Constructor not null")
        public void testConstructor2NotNull() throws ValidationException, IOException {
            byte[] buff = {1,2};
            InputStream in = new ByteArrayInputStream(buff);
            MessageInput messageInput1 = new MessageInput(in);
            LocationRecord b = new LocationRecord(messageInput1);
            assertNotNull(b);
        }
*/
        @Test
        @DisplayName("Constructor 2 Testing bad IOStream")
        public void testBadIOStreamConstructor2() throws ValidationException, IOException {
            assertThrows(NullPointerException.class, () -> {
                LocationRecord a = new LocationRecord(null);
            });
        }

        /*@Test
        @DisplayName("Constructor2 Equals")
        public void testConstructor2Equals() throws ValidationException, IOException {
            byte[] buff = {1};
            InputStream in = new ByteArrayInputStream(buff);
            MessageInput messageInput1 = new MessageInput(in);
            MessageInput messageInput2 = new MessageInput(in);

            LocationRecord a = new LocationRecord(messageInput1);
            LocationRecord b = new LocationRecord(messageInput2);

            assertEquals(a, b);
        }*/

            /**********************************************************
             * Tests Constructor 2 Specifics on the Decode
             **********************************************************/
            @Nested
            @DisplayName("Testing out the Information received on Decode")
            class Constructor2DecodeTests{
                @Test
                @DisplayName("EOF Exceptions")
                public void eofexception(){
                    assertThrows(EOFException.class, () -> {
                        var in = new MessageInput(new ByteArrayInputStream(
                                "5".getBytes(CHARENC)));
                        var msg = new LocationRecord(in);
                    });
                }

                @Test
                @DisplayName("Basic Decode")
                public void testDecode() throws IOException, ValidationException {
                    var in = new MessageInput(new ByteArrayInputStream(
                            "1 1.2 3.4 2 BU6 Baylor".getBytes(CHARENC)));
                    var msg = new LocationRecord(in);
                    Assertions.assertEquals(1, msg.getUserId());
                    Assertions.assertEquals("1.2", msg.getLongitude());
                    Assertions.assertEquals("3.4", msg.getLatitude());
                    Assertions.assertEquals("BU", msg.getLocationName());
                    Assertions.assertEquals("Baylor", msg.getLocationDescription());
                }

                @Test
                @DisplayName("Testing Longitude Not ASCII")
                public void LongitudenotASCII() throws IOException, ValidationException {
                    assertThrows(ValidationException.class, () -> {
                        var in = new MessageInput(new ByteArrayInputStream(
                                "1 ££§¡ 3.4 2 BU6 Baylor".getBytes(CHARENC)));
                        var msg = new LocationRecord(in);
                    });
                }



                @Test
                @DisplayName("Testing Longitude Not ASCII")
                public void LatitudenotASCII() throws IOException, ValidationException {
                    assertThrows(ValidationException.class, () -> {
                        var in = new MessageInput(new ByteArrayInputStream(
                                "1 3.4 ££§¡ 2 BU6 Baylor".getBytes(CHARENC)));
                        var msg = new LocationRecord(in);
                    });
                }

                @Test
                @DisplayName("Testing Longitude Not ASCII")
                public void EOFExc() throws IOException, ValidationException {
                    assertThrows(EOFException.class, () -> {
                        var in = new MessageInput(new ByteArrayInputStream(
                                "1 3.4 4.3".getBytes(CHARENC)));
                        var msg = new LocationRecord(in);
                    });
                }

                //Encode uses the Setters so It does not need to check for much else


                /*
                @Test
                @DisplayName("Testing LocationName Not ASCII")
                public void LocationNamenotASCII() throws IOException, ValidationException {
                    assertThrows(ValidationException.class, () -> {
                        var in = new MessageInput(new ByteArrayInputStream(
                                "1 3.4 2.4 4 ££§¡6 Baylor".getBytes(CHARENC)));
                        var msg = new LocationRecord(in);
                    });
                }
                */



            }
    }


    /**********************************************************
     * Tests Constructor 3 with Scanner (NOT NEEDED)
     **********************************************************/
    /*@Nested
    @DisplayName("Constructor(Scanner) Testing")
    class Constructor3Test {

        @Test
        @DisplayName("Constructor3 Equals")
        public void testContructor3Equals() throws ValidationException {
            Scanner in = new Scanner(System.in);
            OutputStream out = new ByteArrayOutputStream(10);
            PrintStream printer = new PrintStream(out);
            LocationRecord a = new LocationRecord(in, printer);
            LocationRecord b = new LocationRecord(in, printer);
            assertEquals(a, b);
        }

        @Test
        @DisplayName("Constructor3 NotEquals")
        public void testConstructor3NotEquals() throws ValidationException {
            Scanner in = new Scanner(System.in);
            OutputStream out = new ByteArrayOutputStream(10);
            PrintStream printer = new PrintStream(out);
            LocationRecord a = new LocationRecord(in, printer);
            assertNotEquals(null, a);
        }
    }
    */



    /********************************************************
     * Tests for Encode
    /*********************************************************/
    @Nested
    @DisplayName("Testing the Encode Function")
    class EncodeTesting{
        @Test
        @DisplayName("Checking to make sure not sending null for encode")
        public void checknotNullEncode() throws IOException, ValidationException{
            var bOut = new ByteArrayOutputStream();
            var out = new MessageOutput(bOut);
            LocationRecord lr = new LocationRecord(1, "1.2", "3.4", "BU", "Baylor");
            lr.encode(out);
            assertNotNull(out);
        }

        @Test
        @DisplayName("Basic Encode")
        public void testEncode() throws IOException, ValidationException {
            var bOut = new ByteArrayOutputStream();
            var out = new MessageOutput(bOut);
            new LocationRecord(1, "1.2", "3.4", "BU", "Baylor").encode(out);
            Assertions.assertArrayEquals("1 1.2 3.4 2 BU6 Baylor".getBytes(CHARENC), bOut.toByteArray());
        }


        @Test
        @DisplayName("Testing userId throws when too large")
        public void testUserIdUpperBound() throws IOException, ValidationException{
            assertThrows(ValidationException.class , () -> {
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                new LocationRecord(10000000000000000L, "1.2", "3.4", "name", "locationDescription").encode(out);
            });
        }

        @Test
        @DisplayName("Testing UserId throws when below 0")
        public void testUserIdLowerBound() throws IOException, ValidationException{
            assertThrows(ValidationException.class , () -> {
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                new LocationRecord(-1L, "1.2", "3.4", "name", "locationDescription").encode(out);
            });
        }


        @Test
        @DisplayName("Testing that it throws when Longitude is Not ASCII")
        public void testLongitudeASCII() throws ValidationException {
            assertThrows(ValidationException.class , () -> {
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                new LocationRecord(100L, "££§¡", "3.4", "name", "locationDescription").encode(out);
            });
        }

        @Test
        @DisplayName("Testing that it throws when Latitude is Not ASCII")
        public void testLatitudeForASCII() throws ValidationException {
            assertThrows(ValidationException.class, () -> {
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                new LocationRecord(100L, "8.0", "££§¡", "name", "locationDescription").encode(out);
            });
        }

        @Test
        @DisplayName("Testing that it throws Validation Exception when Longitude outside of bounds")
        public void testLongitudeUpperBound() throws ValidationException{
            assertThrows(ValidationException.class, () -> {
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                new LocationRecord(100L, "248.0", "7.0", "name", "locationDescription").encode(out);
            });
        }

        @Test
        @DisplayName("Testing that it throws Validation Exception when Latitude outside of bounds")
        public void testLatitudeLowerBound() throws ValidationException{
            assertThrows(ValidationException.class, () -> {
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                new LocationRecord(100L, "108.0", "-247.0", "name", "locationDescription").encode(out);
            });
        }

        @Test
        @DisplayName("Testing that it throws when Location Name Not ASCII")
        public void testLocationNameForASCII() throws ValidationException {
            assertThrows(ValidationException.class , () -> {
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                new LocationRecord(100L, "8.0", "8.0", "££§¡", "locationDescription").encode(out);
            });
        }

        @Test
        @DisplayName("Testing that it throws when Location Description Not ASCII")
        public void testLocationDescForASCII() throws ValidationException {
            assertThrows(ValidationException.class , () -> {
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                new LocationRecord(100L, "8.0", "8.0", "££§¡", "££§¡").encode(out);
            });
        }
    }




    /*********************************************************
     * Testing for Setter UserID
     *********************************************************/
    @Nested
    @DisplayName("Testing UserID Setter")
    class TestSetUserId{

        @Test
        @DisplayName("Will test a value that is too Big for a UserId")
        public void testTooBigUserID() throws ValidationException {
            assertThrows(ValidationException.class,() -> {
                LocationRecord a = new LocationRecord(100, "80.0", "80.0", "drew", "drew's");
                a.setUserId(4294967296L);
            });
        }

        @Test
        @DisplayName("Test negative value for a UserId")
        public void testNegativeValueUserId() throws ValidationException {
            assertThrows(ValidationException.class, () -> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setUserId(-1);
            });
        }

    }


    /*********************************************************
     * Testing for Setter Longitude
     *********************************************************/
    @Nested
    @DisplayName("Testing Longitude Setter")
    class TestLongitudeSetter{

        @Test
        @DisplayName("Testing throws if NULL")
        public void testNotNull() throws ValidationException {
            assertThrows(ValidationException.class, () -> {
                LocationRecord a = new LocationRecord(1, "8.0", "80.0", "name", "drew's");
                a.setLongitude(null);
            });
        }

        @Test
        @DisplayName("Test Correct Longitude and Latitude")
        public void testLongitudeBounds() throws ValidationException {
            LocationRecord a = new LocationRecord(1, "-80.0", "-80.0", "Drew", "drew's");
            double val = Double.parseDouble(a.getLatitude());
            assertTrue((val <= 180.0 && val >= -180.0));
        }

        @Test
        @DisplayName("Test Upper Bound on Longitude")
        public void testUpperBoundOfLongitude() throws ValidationException {
            assertThrows(ValidationException.class, () -> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setLongitude("180.1");
            });
        }

        @Test
        @DisplayName("Test Lower Bound of Latitude")
        public void testLowerBoundOfLongitude() throws ValidationException {
            assertThrows(ValidationException.class, ()-> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setLongitude("-271");
            });
        }


        @Test
        @DisplayName("Checking to make sure follows REGEX")
        public void testRegex() throws ValidationException{
            LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
            a.setLongitude("8.4");
            assertTrue(a.getLongitude().matches("^-?[0-9]+\\.[0-9]+$"));
        }

        @Test
        @DisplayName("Testing that it throwing when not following REGEX")
        public void testThrowifNotRegex() throws ValidationException{
            assertThrows(ValidationException.class, ()-> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setLongitude("84");
            });
        }

        @Test
        @DisplayName("Testing that throw if not ASCII")
        public void testThrowIfNotASCII(){
            assertThrows(ValidationException.class, ()-> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setLongitude("££§¡");
            });
        }

    }


    /*********************************************************
     * Testing for Setter Latitude
     *********************************************************/
    @Nested
    @DisplayName("Testing Latitude Setter")
    class TestLatitudeSetter{

        @Test
        @DisplayName("Testing throws if NULL")
        public void testNotNull() throws ValidationException {
            assertThrows(ValidationException.class, () -> {
                LocationRecord a = new LocationRecord(1, "8.0", "80.0", "name", "drew's");
                a.setLatitude(null);
            });
        }

        @Test
        @DisplayName("Test Correct Longitude and Latitude")
        public void testLatitudeBounds() throws ValidationException{
            LocationRecord a = new LocationRecord(1, "-80.0", "-80.0", "Drew", "drew's");
            double val = Double.parseDouble(a.getLongitude());
            assertTrue((val <= 90.0 && val >= -90.0));
        }

        @Test
        @DisplayName("Test Upper Bound of Latitude")
        public void testUpperBoundOfLatitude() throws ValidationException{
            assertThrows(ValidationException.class, () -> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setLatitude("90.1");
            });
        }

        @Test
        @DisplayName("Test Lower Bound of Latitude")
        public void testLowerBoundOfLatitude() throws ValidationException {
            assertThrows(ValidationException.class, ()-> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setLatitude("-90.1");
            });
        }
        @Test
        @DisplayName("Checking to make sure follows REGEX")
        public void testRegex() throws ValidationException{
            LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
            a.setLongitude("8.4");
            assertTrue(a.getLatitude().matches("^-?[0-9]+\\.[0-9]+$"));
        }

        @Test
        @DisplayName("Testing that it throwing when not following REGEX")
        public void testThrowifNotRegex() throws ValidationException{
            assertThrows(ValidationException.class, ()-> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setLatitude("84");
            });
        }

        @Test
        @DisplayName("Testing that throw if not ASCII")
        public void testThrowIfNotASCII(){
            assertThrows(ValidationException.class, ()-> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setLatitude("££§¡");
            });
        }
    }

    /*********************************************************
     * Testing for Setter Location Name
     *********************************************************/

    @Nested
    @DisplayName("Testing the Setter for LocationName")
    class TestLocationNameSetter{

        @Test
        @DisplayName("Testing throws if NULL")
        public void testInvalidLocationName() throws ValidationException {
            assertThrows(ValidationException.class, () -> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "name", "drew's");
                a.setLocationName(null);
            });
        }

        @Test
        @DisplayName("Testing that throw if not ASCII")
        public void testThrowIfNotASCII(){
            assertThrows(ValidationException.class, ()-> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setLocationName("££§¡");
            });
        }


    }






    /*********************************************************
     * Testing for Setter Location Description
     *********************************************************/
    @Nested
    @DisplayName("Testing the Setter for LocationDescription")
    class TestLocationDescriptionSetter{
        @Test
        @DisplayName("Testing throws if null")
        public void testInvalidLocationDescription() throws ValidationException {
            assertThrows(ValidationException.class, () -> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "name", "drew's");
                a.setLocationDescription(null);
            });
        }

        @Test
        @DisplayName("Testing that throw if not ASCII")
        public void testThrowIfNotASCII(){
            assertThrows(ValidationException.class, ()-> {
                LocationRecord a = new LocationRecord(1, "-80.0", "80.0", "drew", "drew's");
                a.setLocationName("££§¡");
            });
        }


    }






}
