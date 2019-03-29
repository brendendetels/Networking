package notifi.serialization.test;
import notifi.serialization.NoTiFiLocation;
import notifi.serialization.NoTiFiLocationDeletion;
import notifi.serialization.NoTiFiMessage;
import notifi.serialization.NoTiFiRegister;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/***********************************************
 *
 * Author: Brenden Detels and Andrew Bury
 * Assignment: Project 4
 * Class: CSI 4321
 *
 **********************************************/
public class NoTiFiLocationDeletionTest {
    @Nested
    @DisplayName("Basic tests")
    private class testBasics{

        @Test
        @DisplayName("TestBasicValues")
        public void testWorkingConstructor(){
            NoTiFiLocation noTiFiLocation = new NoTiFiLocation(4,1.1,2.2,"Wow", "Town");
            int msg = 2;
            NoTiFiLocationDeletion noTiFiLocationDeletion = new NoTiFiLocationDeletion(msg,noTiFiLocation);
            assertAll(
                    ()-> assertEquals(noTiFiLocation, noTiFiLocationDeletion.getLocation()),
                    ()-> assertEquals(msg, noTiFiLocationDeletion.getMsgId()),
                    ()-> assertNotNull(noTiFiLocationDeletion)
            );
        }


        @Test
        @DisplayName("Test Encode")
        public void testEncode(){
            long userId = 4l;
            double longitude = 1.1;
            double latitude = 2.2;
            String locationName = "Swagstein";
            String locationDescription = "Swaggin";
            NoTiFiLocation noTiFiLocation = new NoTiFiLocation(userId, longitude, latitude, locationName, locationDescription);
            int msg = 2;
            NoTiFiLocationDeletion noTiFiLocationDeletion = new NoTiFiLocationDeletion(msg, noTiFiLocation);
            try{
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

                dataOutputStream.writeByte(48 + 2);
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

                assertEquals(newNoTiFi, noTiFiLocationDeletion.encode());
            }catch(IOException ex){
                ex.printStackTrace();
                assertFalse(true);
            }
        }

        @Test
        @DisplayName("Test decode")
        public void testDecodeLocationDeletion(){
            long userId = 4l;
            double longitude = 1.1;
            double latitude = 2.2;
            String locationName = "Swagstein";
            String locationDescription = "Swaggin";
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

                NoTiFiMessage noTiFiMessage = NoTiFiMessage.decode(newNoTiFi);
                if(noTiFiMessage instanceof NoTiFiLocationDeletion){
                    assertTrue(true);
                }else{
                    assertTrue(false);
                }
            }catch(IOException ex){
                ex.printStackTrace();
                assertFalse(true);
            }
        }


    }
}