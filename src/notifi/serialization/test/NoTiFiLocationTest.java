package notifi.serialization.test;
import notifi.serialization.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
/***********************************************
 *
 * Author: Brenden Detels and Andrew Bury
 * Assignment: Project 4
 * Class: CSI 4321
 *
 **********************************************/
public class NoTiFiLocationTest {

    @Nested
    @DisplayName("Testing the Constructor/Setters")
    public class TestSetters{

        @Nested
        @DisplayName("Test setUserID")
        public class setUserId{
            @Test
            @DisplayName("Check Max Range")
            public void checkMaxRange(){
                assertThrows(IllegalArgumentException.class, () ->{
                    NoTiFiLocation nl = new NoTiFiLocation(5,40.0, 40.0, "Location Name", "Location Description");
                    nl.setUserId(NTFProtocols.userIdProtocols[1]+1);
                });
            }
            @Test
            @DisplayName("Check Min Range")
            public void checkMinRange(){
                assertThrows(IllegalArgumentException.class, () ->{
                    NoTiFiLocation nl = new NoTiFiLocation(5,40.0, 40.0, "Location Name", "Location Description");
                    nl.setUserId(NTFProtocols.userIdProtocols[0]-1);
                });
            }

            @Test
            @DisplayName("Check Inside Range")
            public void checkCorrect(){
                    NoTiFiLocation nl = new NoTiFiLocation(5,40.0, 40.0, "Location Name", "Location Description");
                    nl.setUserId(4);
                    assertEquals(nl.getUserId(), 4);
            }
        }

        @Nested
        @DisplayName("Longitude and Latitude Test")
        public class positionTests{
            @Test
            @DisplayName("Check Latitude Correct")
            public void checkLatitudeCorrect(){
                NoTiFiLocation nl = new NoTiFiLocation(5,40.0, 40.0, "Location Name", "Location Description");
                nl.setLatitude(30.0);
                assertEquals(nl.getLatitude(), 30.0);
            }

            @Test
            @DisplayName("Check Longitude Correct")
            public void checkLongitudeCorrect(){
                NoTiFiLocation nl = new NoTiFiLocation(5,40.0, 40.0, "Location Name", "Location Description");
                nl.setLongitude(30.0);
                assertEquals(nl.getLongitude(), 30.0);
            }
        }

        @Nested
        @DisplayName("Location Name and Description Tests")
        public class namedescTests{
            @Test
            @DisplayName("Check Location Name is null")
            public void checkLocationNameNull(){
                assertThrows(IllegalArgumentException.class, () ->{
                    NoTiFiLocation nl = new NoTiFiLocation(5,40.0, 40.0, null, "Location Description");
                });
            }

            @Test
            @DisplayName("Check Location Description is null")
            public void checkLocationDescriptionNull(){
                assertThrows(IllegalArgumentException.class, () ->{
                    NoTiFiLocation nl = new NoTiFiLocation(5,40.0, 40.0, "name", null);
                });
            }

            @Test
            @DisplayName("Check Location Description is null")
            public void checkLocationNameSizeTooLarge(){
                assertThrows(IllegalArgumentException.class, () ->{
                    NoTiFiLocation nl = new NoTiFiLocation(5,40.0, 40.0, "name", "hie");
                    String x = "";
                    for(int i = 0; i < NTFProtocols.stringmaxsize+1; i++){
                        x += (char)i;
                    }
                    nl.setLocationName(x);
                });
            }


            @Test
            @DisplayName("Check Location Description is null")
            public void checkLocationDescriptionSizeTooLarge(){
                assertThrows(IllegalArgumentException.class, () ->{
                    NoTiFiLocation nl = new NoTiFiLocation(5,40.0, 40.0, "name", "hi");
                    String x = "";
                    for(int i = 0; i < NTFProtocols.stringmaxsize+1; i++){
                        x += 'c';
                    }
                    nl.setLocationDescription(x);
                });
            }

            @Test
            @DisplayName("Test working constructor")
            public void testWorkingConstructor(){
                long userId = 4l;
                double longitude = 1.1;
                double latitude = 2.2;
                String locationName = "Swagstein";
                String locationDescription = "Swaggin";
                NoTiFiLocation noTiFiLocation = new NoTiFiLocation(userId, longitude, latitude, locationName, locationDescription);
                assertAll(
                        ()-> assertEquals(userId, noTiFiLocation.getUserId()),
                        ()-> assertEquals(longitude, noTiFiLocation.getLongitude()),
                        ()-> assertEquals(latitude, noTiFiLocation.getLatitude()),
                        ()-> assertEquals(locationName, noTiFiLocation.getLocationName()),
                        ()-> assertEquals(locationDescription, noTiFiLocation.getLocationDescription()),
                        ()-> assertNotNull(noTiFiLocation)
                );
            }

            @Test
            @DisplayName("Test bad values constructor")
            public void testBadConstructor(){
                long userId = -1;
                double longitude = 100000.00;
                double latitude = 100000.00;
                String locationName = null;
                String locationDescription = null;
                assertThrows(IllegalArgumentException.class, ()->{
                    NoTiFiLocation noTiFiLocation = new NoTiFiLocation(userId, longitude, latitude, locationName, locationDescription);
                });
            }

            @Test
            @DisplayName("Test setters working")
            public void testSettersWorking(){
                long userId = 4l;
                double longitude = 1.1;
                double latitude = 2.2;
                String locationName = "Swagstein";
                String locationDescription = "Swaggin";
                NoTiFiLocation noTiFiLocation = new NoTiFiLocation(userId, longitude, latitude, locationName, locationDescription);
                long newUserId = 50;
                double newLongitude = 1.2;
                double newLatitude = 2.3;
                String newLocationName = "Chill";
                String newLocationDescription = "place";

                noTiFiLocation.setUserId(newUserId);
                noTiFiLocation.setLatitude(newLatitude);
                noTiFiLocation.setLongitude(newLongitude);
                noTiFiLocation.setLocationName(newLocationName);
                noTiFiLocation.setLocationDescription(newLocationDescription);

                assertAll(
                        ()-> assertEquals(newUserId, noTiFiLocation.getUserId()),
                        ()-> assertEquals(newLatitude, noTiFiLocation.getLatitude()),
                        ()-> assertEquals(newLongitude, noTiFiLocation.getLongitude()),
                        ()-> assertEquals(newLocationDescription, noTiFiLocation.getLocationDescription()),
                        ()-> assertEquals(newLocationName, noTiFiLocation.getLocationName()),
                        ()-> assertThrows(IllegalArgumentException.class, ()-> {
                            noTiFiLocation.setUserId(-1);
                        }),
                        ()-> assertThrows(IllegalArgumentException.class, ()-> {
                            noTiFiLocation.setLatitude(100000.00);
                        }),()-> assertThrows(IllegalArgumentException.class, ()-> {
                            noTiFiLocation.setLongitude(1000000.00);
                        }),
                        ()-> assertThrows(IllegalArgumentException.class, ()-> {
                            noTiFiLocation.setLocationName(null);
                        }),
                        ()-> assertThrows(IllegalArgumentException.class, ()-> {
                            noTiFiLocation.setLocationDescription(null);
                        }),
                        ()-> assertThrows(IllegalArgumentException.class, ()-> {
                            noTiFiLocation.setUserId(4294967297L);
                        }),
                        ()-> assertThrows(IllegalArgumentException.class, ()-> {
                            noTiFiLocation.setLongitude(1.000000000000000000000000000000000000000000);
                        }),
                        ()-> assertThrows(IllegalArgumentException.class, ()-> {
                            noTiFiLocation.setLatitude(1.0000000000000000000000000000000000000);
                        }),
                        ()-> assertThrows(IllegalArgumentException.class, ()-> {
                            noTiFiLocation.setLocationName("asdfffffffffffffffffffffffffffffffffffffffffffff"+
                                    "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"+
                                    "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"+
                                    "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"+
                                    "uckasdffffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaSDFWE"+
                                    "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"+
                                    "swagasdffffffffffffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        }),
                        ()-> assertThrows(IllegalArgumentException.class, ()-> {
                            noTiFiLocation.setLocationDescription("asdfffffffffffffffffffffffffffffffffffffffffffff"+
                                    "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"+
                                    "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"+
                                    "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"+
                                    "uckasdffffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaSDFWE"+
                                    "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"+
                                    "swagasdffffffffffffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        })
                );
            }

            @Test
            @DisplayName("test encode")
            public void testEncode(){
                long userId = 4l;
                double longitude = 1.1;
                double latitude = 2.2;
                String locationName = "Swagstein";
                String locationDescription = "Swaggin";
                NoTiFiLocation noTiFiLocation = new NoTiFiLocation(userId, longitude, latitude, locationName, locationDescription);
                try{
                    byte[] noTiFiLoctionByteArray = noTiFiLocation.encode();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

                    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
                    byteBuffer.order(ByteOrder.BIG_ENDIAN);
                    byteBuffer.putInt((int)userId);
                    dataOutputStream.write(byteBuffer.array());

                    ByteBuffer bufferLong = ByteBuffer.allocate(64);
                    bufferLong.order(ByteOrder.LITTLE_ENDIAN);
                    bufferLong.putDouble(longitude);
                    dataOutputStream.write(bufferLong.array());

                    ByteBuffer bufferLat = ByteBuffer.allocate(64);
                    bufferLat.order(ByteOrder.LITTLE_ENDIAN);
                    bufferLat.putDouble(latitude);
                    dataOutputStream.write(bufferLat.array());

                    dataOutputStream.writeInt(locationName.length());
                    dataOutputStream.write(locationName.getBytes());

                    dataOutputStream.writeInt(locationDescription.length());
                    dataOutputStream.write(locationDescription.getBytes());

                    dataOutputStream.flush();
                    byte[] newNoTiFi = byteArrayOutputStream.toByteArray();

                    assertEquals(newNoTiFi, noTiFiLocation.encode());
                }catch(IOException ex){
                    ex.printStackTrace();
                    assertFalse(true);
                }
            }



        }
    }


    @Test
    @DisplayName("NoTiFiLocationDecodetest")
    public void testLocDecode()throws IOException{
        long userId = 4l;
        double longitude = 1.1;
        double latitude = 2.2;
        String locationName = "Swagstein";
        String locationDescription = "Swaggin";
        NoTiFiLocation loc = new NoTiFiLocation(userId, longitude, latitude, locationName, locationDescription);
        byte[]pkt = loc.encode();

        NoTiFiLocation loc2 = NoTiFiLocation.decode(pkt);
        assertEquals(loc,loc2);

    }

    @Test
    @DisplayName("Test decode")
    public void testDecodeLocationAddition(){
        long userId = 4l;
        double longitude = 1.1;
        double latitude = 2.2;
        String locationName = "Swagstein";
        String locationDescription = "Swaggin";
        NoTiFiLocation loc = new NoTiFiLocation(userId, longitude, latitude, locationName, locationDescription);

        int msg = 1;
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            dataOutputStream.writeByte(48 + 1);
            dataOutputStream.writeByte(msg);

            ByteBuffer byteBuffer = ByteBuffer.allocate(32);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.putLong(userId);
            dataOutputStream.write(byteBuffer.array());

            ByteBuffer bufferLong = ByteBuffer.allocate(64);
            bufferLong.order(ByteOrder.LITTLE_ENDIAN);
            bufferLong.putDouble(longitude);
            dataOutputStream.write(bufferLong.array());

            ByteBuffer bufferLat = ByteBuffer.allocate(64);
            bufferLat.order(ByteOrder.LITTLE_ENDIAN);
            bufferLat.putDouble(latitude);
            dataOutputStream.write(bufferLat.array());

            dataOutputStream.writeInt(locationName.length());
            dataOutputStream.write(locationName.getBytes());

            dataOutputStream.writeInt(locationDescription.length());
            dataOutputStream.write(locationDescription.getBytes());

            dataOutputStream.flush();
            byte[] newNoTiFi = byteArrayOutputStream.toByteArray();

            NoTiFiLocationDeletion noTiFiMessage = (NoTiFiLocationDeletion)NoTiFiMessage.decode(newNoTiFi);

            //NoTiFiLocation loc = new NoTiFiLocation(userId, longitude, latitude, locationName, locationDescription);
            assertEquals(loc, noTiFiMessage.getLocation());
            /*if(noTiFiMessage instanceof NoTiFiLocationAddition){
                assertTrue(true);
            }else{
                assertTrue(false);
            }*/
        }catch(IOException ex){
            ex.printStackTrace();
            assertFalse(true);
        }
    }

}
