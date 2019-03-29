package latitude.serialization.test;
import latitude.serialization.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



/***********************************************
 *
 * Author: Brenden Detels and Andrew Bury
 * Assignment: Project 0
 * Class: CSI 4321
 *
 **********************************************/

public class LatitudeMessageTest {

    private static final String CHARENC = "ASCII";



    /**********************************************************
     * Tests All the Encode Overriden Functions
     **********************************************************/
    @Nested
    @DisplayName("All Encode Tests")
    class AllEncodeTests{

        @Nested
        @DisplayName("LatitudeNEWLocation Tests")
        class NEWEncodeTest{
            @Test
            @DisplayName("LatitudeNEWMessage Tests")
            void NEWtest()throws ValidationException, IOException{
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                LocationRecord rec = new LocationRecord(1, "5.0", "6.0", "Name", "desc");
                LatitudeNewLocation msg = new LatitudeNewLocation(4, rec);
                assertNotNull(msg);
                assertTrue(msg.getOperation() == "NEW");
                msg.encode(out);
                assertArrayEquals("LATITUDEv1 4 NEW 1 5.0 6.0 4 Name4 desc\r\n".getBytes(CHARENC), bOut.toByteArray() );
            }
        }



        @Nested
        @DisplayName("Encode RESPONSE Tests")
        class RESPONSEEncodeTest{
            @Test
            @DisplayName("LatitudeRESPONSE Tests")
            void RESPONSETest() throws ValidationException, IOException{
                LocationRecord rec1 = new LocationRecord(1, "5.0", "6.0", "Name", "desc");
                LocationRecord rec2 = new LocationRecord(4, "9.0", "5.0", "Nhge", "dffc");
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                LatitudeLocationResponse msg = new LatitudeLocationResponse(5, "Honduras");
                assertNotNull(msg);
                msg.addLocationRecord(rec1);
                msg.addLocationRecord(rec2);
                msg.encode(out);
                assertArrayEquals("LATITUDEv1 5 RESPONSE 8 Honduras2 1 5.0 6.0 4 Name4 desc4 9.0 5.0 4 Nhge4 dffc\r\n".getBytes(CHARENC), bOut.toByteArray());
            }
        }


        @Nested
        @DisplayName("Encode ERROR Tests")
        class ERRORTest{
            @Test
            @DisplayName("LatitudeErrorMessage Tests")
            void ErrorTest() throws IOException , ValidationException{
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                LatitudeError err = new LatitudeError(5, "Not a valid location");
                assertNotNull(err);
                err.encode(out);
                assertArrayEquals("LATITUDEv1 5 ERROR 20 Not a valid location\r\n".getBytes(CHARENC), bOut.toByteArray());
            }
        }


        @Nested
        @DisplayName("Encode ALL Tests")
        class ALLTest{
            @Test
            @DisplayName("ALL Tests")
            void AllTest() throws IOException, ValidationException{
                var bOut = new ByteArrayOutputStream();
                var out = new MessageOutput(bOut);
                LatitudeLocationRequest llr = new LatitudeLocationRequest(5);
                llr.encode(out);
                assertArrayEquals("LATITUDEv1 5 ALL \r\n".getBytes(CHARENC), bOut.toByteArray());
            }
        }


    }




    /**********************************************************
     * Tests All the Decode Overriden Tests
     **********************************************************/

    @Nested
    @DisplayName("All Decode Tests")
    class AllDecodeTests{
        @Nested
        @DisplayName("Generic Decode Tests")
        class GenericTests{
            @Test
            @DisplayName("Bad Input(= null) on Decode")
            void GenericDecode1() throws IOException, ValidationException{
                assertThrows(NullPointerException.class, () ->{
                    var in = new MessageInput(null);
                    LatitudeError le = (LatitudeError)LatitudeMessage.decode(in);
                });
            }

            @Test
            @DisplayName("Not Correctly Encoded LatitudeV1")
            void GenericDecode2() throws IOException, ValidationException{
                assertThrows(ValidationException.class, () ->{
                    var in = new MessageInput(new ByteArrayInputStream("LATIEv1 5 NEW 1 5.0 6.0 4 Name4 desc\r\n".getBytes(CHARENC)));
                    LatitudeError le = (LatitudeError)LatitudeMessage.decode(in);
                });
            }

            @Test
            @DisplayName("Not correctly inputted Map ID")
            void GenericDecode3() throws IOException, ValidationException{
                assertThrows(ValidationException.class, () ->{
                    var in = new MessageInput(new ByteArrayInputStream("LATITUDEv1 -1 NEW 1 5.0 6.0 4 Name4 desc\r\n".getBytes(CHARENC)));
                    LatitudeError le = (LatitudeError)LatitudeMessage.decode(in);
                });
            }

            @Test
            @DisplayName("Not Correctly Inputted Operation")
            void GenericDecode4() throws IOException, ValidationException{
                assertThrows(ValidationException.class, () ->{
                    var in = new MessageInput(new ByteArrayInputStream("LATITUDEv1 5 NEWSPONSE 1 5.0 6.0 4 Name4 desc\r\n".getBytes(CHARENC)));
                    LatitudeError le = (LatitudeError)LatitudeMessage.decode(in);
                });
            }
        }

        @Nested
        @DisplayName("Decode ALL Tests")
        class ALLTest{
            @Test
            @DisplayName("Decode ALL Test")
            void ALLTestDecode1() throws IOException, ValidationException{
                var in = new MessageInput(new ByteArrayInputStream("LATITUDEv1 5 ALL \r\n".getBytes(CHARENC)));
                LatitudeLocationRequest llr = (LatitudeLocationRequest)LatitudeMessage.decode(in);
                assertEquals(llr.getOperation(), "ALL");
                assertEquals(5, llr.getMapId());
            }
        }

        @Nested
        @DisplayName("Decode NEW Tests")
        class NEWDecodeTests{
            @Test
            @DisplayName("Checking Correctness")
            void NEWTestDecode1() throws IOException, ValidationException{
                var in = new MessageInput(new ByteArrayInputStream("LATITUDEv1 5 NEW 1 5.0 6.0 4 Name4 desc\r\n".getBytes(CHARENC)));
                LatitudeNewLocation lnew = (LatitudeNewLocation)LatitudeMessage.decode(in);
                assertEquals(lnew.getMapId(), 5);
                assertEquals(lnew.getOperation(), "NEW");
                LocationRecord lr = new LocationRecord(1, "5.0", "6.0", "Name", "desc");
                assertEquals(lnew.getLocationRecord(), lr);
            }
        }

        @Nested
        @DisplayName("Decode ERROR Tests")
        class ERRORDecodeTests{
            @Test
            @DisplayName("Error Test")
            void ERRORTestDecode1() throws IOException, ValidationException{
                var in = new MessageInput(new ByteArrayInputStream("LATITUDEv1 5 ERROR 20 Not a valid location\r\n".getBytes(CHARENC)));
                LatitudeError le = (LatitudeError)LatitudeMessage.decode(in);
                assertEquals(le.getOperation(), "ERROR");
                assertEquals(le.getErrorMessage().length(), 20);
                assertEquals(le.getErrorMessage(), "Not a valid location");
            }
        }


        @Nested
        @DisplayName("Decode RESPONSE Tests")
        class RESPONSEDecodeTests{
            @Test
            @DisplayName("Response Test Correctness")
            void REPONSETest1() throws IOException, ValidationException{
                var in = new MessageInput(new ByteArrayInputStream("LATITUDEv1 8 RESPONSE 8 Honduras2 1 1.2 3.4 2 BU6 Baylor1 1.2 3.4 2 BU6 Baylor\r\n".getBytes(CHARENC)));
                LatitudeLocationResponse response = (LatitudeLocationResponse)LatitudeMessage.decode(in);
                assertEquals(response.getOperation(), "RESPONSE");
                assertEquals(response.getMapName().length(), 8);
                assertEquals(response.getLocationRecordList().size(), 2);
                    LocationRecord lr = new LocationRecord(1, "1.2", "3.4", "BU", "Baylor");
                    List<LocationRecord> listlr = new ArrayList<LocationRecord>();
                    listlr.add(lr);
                assertEquals(lr, response.getLocationRecordList().get(0));
                    assertEquals(lr, response.getLocationRecordList().get(1));
                    listlr.add(lr);
                assertEquals(response.getLocationRecordList(), listlr);

            }

            /*@Test
            @DisplayName("Making Sure Throws on Bad Input")
            void RESPONSETest2() throws IOException, ValidationException{
                assertThrows(IOException.class, () ->{
                    var in = new MessageInput(new ByteArrayInputStream("LATITUDEv1 8 RESPONSE 8 Hondas2 1 1.2 3.4 2 BU6 Baylor1 1.2 3.4 2 BU6 Baylor".getBytes(CHARENC)));
                    LatitudeLocationResponse response = (LatitudeLocationResponse)LatitudeMessage.decode(in);
                });
            }*/

        }
    }


    /**********************************************************
     * Tests setMapId
     **********************************************************/

    @Nested
    @DisplayName("Test Set Map ID using other classes that call it")
    class testMapIdSetter{

        @Test
        @DisplayName("Testing working value for mapId")
        void testWorkingValueForMapID() throws ValidationException{
            LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "Honduras");
            llr.setMapId(8);
            assertEquals(8, llr.getMapId());
        }


        @Test
        @DisplayName("Test Bad MapId value")
        void testBadMapIdValue1() throws ValidationException{
            assertThrows(ValidationException.class, () ->{
                LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "Honduras");
                llr.setMapId(-1);
            });

        }
        @Test
        @DisplayName("Test Bad MapId value")
        void testBadMapIdValue2() throws ValidationException{
            assertThrows(ValidationException.class, () -> {
                LatitudeLocationResponse llr = new LatitudeLocationResponse(5, "Honduras");
                llr.setMapId(4294967296L);
            });
        }
    }


}
