package notifi.serialization;

import java.util.HashMap;
import java.util.Map;
/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 4
 * Class: CSI 4321
 *
 **********************************************/
public final class NTFProtocols {
    public static final String charenc = "ASCII";


    public static final byte bRegister = 0x30;
    public static final byte bLocationAdd = 0x31;
    public static final byte bLocationDel = 0x32;
    public static final byte bDeregister = 0x33;
    public static final byte bError = 0x34;
    public static final byte bACK = 0x35;


    public static final String[] messageTypes = {"Register", "Location Addition", "Location Deletion", "Deregister", "Error", "ACK"};
    public static final Map<String, Integer> codes = Map.of(
            messageTypes[0], 0,
            messageTypes[1], 1,
            messageTypes[2], 2,
            messageTypes[3], 3,
            messageTypes[4], 4,
            messageTypes[5], 5
    );

    public static int versionnumber = 3;
    public static final String asciiregex = "\\A\\p{ASCII}*\\z";
    public static final String numericcheck = "-?\\d+(\\.\\d+)?";

    public static final int msgIdrange[] = {0, 255};

    public static final int mapIdrange[] = {0, Integer.MAX_VALUE+1 };
    public static final int portrange[] = {0, 65535 };
    public static final int stringmaxsize = 255;
    //public static final Double longprotocol[] = { -180.0 , 180.0};
    //public static final Double latprotocol[] = { -90.0 , 90.0};


    //UserID Protocols
    public static final Long userIdProtocols[] = {0L, 4294967295L};


    public static String fillBytes(int numBytesNeeded, String current){
        String fill = "";
        for(int i = current.length(); i < numBytesNeeded; i++){
            fill += '0';
        }
        String filledstring = fill + current;
        return filledstring;
    }


}
