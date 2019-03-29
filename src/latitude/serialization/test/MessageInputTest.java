package latitude.serialization.test;
import latitude.serialization.MessageInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 0
 * Class: CSI 4321
 *
 **********************************************/

public class MessageInputTest {

    @Test
    @DisplayName("Test if a MessageInput works")
    public void testMessageInputWorks() throws IOException{
        byte[] buff = {1};
        InputStream in = new ByteArrayInputStream(buff);
        MessageInput msgIn = new MessageInput(in);
        assertNotNull(msgIn);
    }



    @Test
    @DisplayName("Test if MessageInput Throws IOException when given null input")
    public void testMessageInputWithNull() {
        assertThrows(NullPointerException.class, () -> { //assertSame(IOException, IOException);
            InputStream in = null;
            MessageInput msgIn = new MessageInput(in);
        });
    }
}
