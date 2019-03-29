package notifi.serialization.test;
import notifi.serialization.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/***********************************************
 *
 * Author: Brenden Detels and Andrew Bury
 * Assignment: Project 4
 * Class: CSI 4321
 *
 **********************************************/
public class NoTiFiDeregisterTest {
    @Nested
    @DisplayName("Testing Setters/Constructor")
    public class TestSetterandConstructor{
        @Test
        @DisplayName("Set Address when null")
        public void ipaddressisNull(){
            assertThrows(IllegalArgumentException.class, () ->{
                NoTiFiMessage nm = new NoTiFiDeregister(4, null, 50);
            });
        }

        @Test
        @DisplayName("Check Correct IP")
        public void ipcorrect() throws UnknownHostException {
            Inet4Address address = (Inet4Address) InetAddress.getByName("google.com");
            NoTiFiDeregister nm = new NoTiFiDeregister(4, address, 50);
            nm.setAddress(address);
            assertEquals( nm.getAddress().toString(), address.toString());
        }

        @Test
        @DisplayName("Check Port Min Range")
        public void checkPortMin()throws UnknownHostException {
            Inet4Address address = (Inet4Address)InetAddress.getByName("google.com");
            assertThrows(IllegalArgumentException.class, () ->{
                NoTiFiMessage nm = new NoTiFiDeregister(4, address, NTFProtocols.portrange[0]-1);
            });
        }

        @Test
        @DisplayName("Check Port Max Range")
        public void checkPortMax()throws UnknownHostException {
            Inet4Address address = (Inet4Address)InetAddress.getByName("google.com");
            assertThrows(IllegalArgumentException.class, () ->{
                NoTiFiMessage nm = new NoTiFiDeregister(4, address, NTFProtocols.portrange[1]+1);
            });
        }


    }



    @Test
    @DisplayName("DeRegister Test")
    public void basicDecode()throws IOException {
        byte[] in = new byte[] { 0x33, 0x5, 0x4, 0x3, 0x2, 0x1, 0x0, 0x2};
        NoTiFiMessage msg = NoTiFiMessage.decode(in);
        assertEquals(5, msg.getMsgId());
        assertEquals(3, msg.getCode());
        NoTiFiMessage newMsg = (NoTiFiDeregister) msg;
        byte[] ipAddr = new byte[]{1,2,3,4};
        Inet4Address addr = (Inet4Address)InetAddress.getByAddress(ipAddr);
        assertEquals(addr,((NoTiFiDeregister) msg).getAddress() );
        assertEquals(2,((NoTiFiDeregister) msg).getPort());
    }

    @Test
    @DisplayName("Deregister Encode")
    public void basicEncode() throws IOException{
        byte[] in = new byte[]{0x33, 0x5, 0x4, 0x3, 0x2, 0x1, 0x0, 0x7 };


        byte[] ipAddr = new byte[]{1,2,3,4};
        Inet4Address addr = (Inet4Address)InetAddress.getByAddress(ipAddr);
        NoTiFiMessage msg = new NoTiFiDeregister(5,addr, 7);
        byte[] encoded = msg.encode();
        assertArrayEquals(encoded, in);


        NoTiFiMessage msg2 = NoTiFiMessage.decode(encoded);
        //System.out.println(msg2.toString());
    }
}
