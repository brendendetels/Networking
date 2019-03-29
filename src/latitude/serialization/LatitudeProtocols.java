package latitude.serialization;
/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/

/**
 * Latitude Message Protocols- Constants for Latitude Messages
 */
public final class LatitudeProtocols {
    //ASCII and Numeric Check
    public static final String asciiregex = "\\A\\p{ASCII}*\\z";
    public static final String numericcheck = "-?\\d+(\\.\\d+)?";


    //Latitude Message Protocols
    public static final String LMVersion = "LATITUDEv1";
    public static final String LMProtocols[] = {"NEW", "ALL", "RESPONSE", "ERROR"};
    public static final String LMdelimeter = "\r\n";

    public static final Long mapIdProtocols[] = {0L, 4294967296L};



    /**
     * Checks if String is numeric
     * @param strNum string that is getting checked
     * @return boolean if numeric
     */
    public static final boolean isNumeric(String strNum) {
        return strNum.matches(numericcheck);
    }

}
