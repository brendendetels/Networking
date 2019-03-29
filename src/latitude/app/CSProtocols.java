package latitude.app;


/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/

import java.net.Inet4Address;

/**
 * Client and Server Protocol - used for constants found in LatitudeClient and LatitudeServer
 */
public final class CSProtocols {
    public static final String asciiregex = "\\A\\p{ASCII}*\\z";
    public static final String numericcheck = "-?\\d+(\\.\\d+)?";


    //Files used in Server
    public static final String loggerInfo = "Server Logs";
    public static final String logFile = "src/connections.log";
    public static final boolean appendLogs = true;
    public static final String markLocationFile = "markers.js";

    //Currently Used MapID
    public static final Long currentlyUsedMapID = 345L;

    //User input n or N to stop continuing
    public static final String noprotocol[] = {"n", "N"};


    //Error Message Protocols
    public static final String badmap = "No such map: ";
    public static final String baduser = "No such user: ";

    //Map Name
    public static final String mapName = "CSI 4321 Map";


    //Network I/O Block Restriction(in ms)
    public static final int networkiorestriction = 5000;

    /**
     * Constructs log message
     * @param message is message sent
     * @param ip ip of client
     * @param port of client
     * @return string rep
     */
    public static final String constructLogMessage(String message, Inet4Address ip, int port){
        return "IP: " + ip + " Port: " + port + " " + message + "\n";
    }


    /**
     * Checks if String is numeric
     * @param strNum string that is getting checked
     * @return boolean if numeric
     */
    public static final boolean isNumeric(String strNum) {
        return strNum.matches(numericcheck);
    }
}
