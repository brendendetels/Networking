package notifi.serialization.test;
import notifi.serialization.NTFProtocols;
import notifi.serialization.NoTiFiACK;
import notifi.serialization.NoTiFiMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/***********************************************
 *
 * Author: Brenden Detels and Andrew Bury
 * Assignment: Project 4
 * Class: CSI 4321
 *
 **********************************************/
public class NoTiFiMessageTest {


    @Nested
    @DisplayName("Set Message ID")
    public class setMessageId{
        @Test
        @DisplayName("Message ID out of range")
        public void MaxRangeCheck(){
            assertThrows(IllegalArgumentException.class, () ->{
                NoTiFiMessage nm = new NoTiFiACK(NTFProtocols.mapIdrange[1]+1);
            });



        }

        @Test
        @DisplayName("Message ID out of range2")
        public void MinRangeCheck(){
            assertThrows(IllegalArgumentException.class, () ->{
                NoTiFiMessage nm = new NoTiFiACK(NTFProtocols.mapIdrange[0]-1);
            });
        }

        @Test
        @DisplayName("Check Correct Message ID")
        public void checkCorrect(){
            NoTiFiMessage nm = new NoTiFiACK(NTFProtocols.mapIdrange[1]);
            nm.setMsgId(47);
            assertEquals(nm.getMsgId(), 47);
        }
    }






}
