package latitude.serialization.test;
import latitude.serialization.MessageOutput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageOutputTest {


    /*Tests for Message Input / Output*/
    @Test
    @DisplayName("Test if an MessageOutput works")
    public void testMessageOutputWorks() {
        ByteArrayOutputStream a = new ByteArrayOutputStream();
        MessageOutput msgOut = new MessageOutput(a);
        assertNotNull(msgOut);
    }


    @Test
    @DisplayName("Test if a MessageOutput Throws IOException when Null")
    public void testMessageOutputWithNull() {
        assertThrows(NullPointerException.class, () -> {
            ByteArrayOutputStream a = null;
            MessageOutput msgOut = new MessageOutput(a);
        });
    }
}
