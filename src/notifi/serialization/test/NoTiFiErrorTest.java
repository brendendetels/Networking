package notifi.serialization.test;
import notifi.serialization.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
public class NoTiFiErrorTest {

    @Test
    @DisplayName("Test Bad Constructor")
    public void testbadconstructor(){
        assertThrows(IllegalArgumentException.class, () ->{
            NoTiFiMessage nm = new NoTiFiError(-1,"Out of bounds");
        });
    }

    @Test
    @DisplayName("Set Error Message Null")
    public void testNullSetErrorMsg(){
        assertThrows(IllegalArgumentException.class, () ->{
            NoTiFiMessage nm = new NoTiFiError(4,null);
        });
    }

    /*@Test
    @DisplayName("Check Not Ascii")
    public void testASCIISetErrorMsg(){
        assertThrows(IllegalArgumentException.class, () ->{
            NoTiFiMessage nm = new NoTiFiError(4,"££§¡");
        });
    }*/
    @Test
    @DisplayName("Error Decode Test")
    public void basicDecode()throws IOException {
        byte[] in = new byte[]{ 0x34, 0x5, 'Y', 'u', 'r', 'p'};
        NoTiFiMessage msg = NoTiFiMessage.decode(in);
        assertEquals(5, msg.getMsgId());
        assertEquals(4, msg.getCode());
        NoTiFiMessage newMsg = (NoTiFiError) msg;
        assertEquals("Yurp", ((NoTiFiError) newMsg).getErrorMessage());
    }

    @Test
    @DisplayName("Error Encode")
    public void basicEncode() throws IOException{
        byte[] in = new byte[]{ 0x34, 0x5, 'Y', 'u', 'r', 'p'};
        NoTiFiMessage msg = new NoTiFiError(5, "Yurp");
        byte[] encoded = msg.encode();
        assertArrayEquals(encoded, in);
    }



}
