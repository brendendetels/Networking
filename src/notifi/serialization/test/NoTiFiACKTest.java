package notifi.serialization.test;
import notifi.serialization.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
/***********************************************
 *
 * Author: Brenden Detels and Andrew Bury
 * Assignment: Project 4
 * Class: CSI 4321
 *
 **********************************************/
public class NoTiFiACKTest {
    private static final String CHARENC = "ASCII";


    @Test
    @DisplayName("ACK/Header Test")
   public void basicDecode()throws  IOException{
            byte[] in = new byte[]{ 0x35, 0x5};
            NoTiFiMessage msg = NoTiFiMessage.decode(in);
            assertEquals(5, msg.getMsgId());
            assertEquals(5, msg.getCode());
    }

    @Test
    @DisplayName("ACK Encode")
    public void basicEncode() throws IOException{
        byte[] in = new byte[]{0x35, (byte)255};
        NoTiFiMessage msg = new NoTiFiACK(255);
        byte[] encoded = msg.encode();
        assertArrayEquals(encoded, in);
    }
}
