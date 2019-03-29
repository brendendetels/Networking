package latitude.serialization;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/

/**
 * Location Record Protocols- Constants for Location Record
 */
public final class LocationProtocols {

    //ASCII and Numeric Check
    public static final String asciiregex = "\\A\\p{ASCII}*\\z";
    public static final String numericcheck = "-?\\d+(\\.\\d+)?";

    //UserID Protocols
    public static final Long userIdProtocols[] = {0L, 4294967296L};

    //Longitude and Latitude Protocols
    public static final String latlongprotocol = "^-?[0-9]+\\.[0-9]+$";
    public static final Double longprotocol[] = { -180.0 , 180.0};
    public static final Double latprotocol[] = { -90.0 , 90.0};


    /**
     * Checks if String is numeric
     * @param strNum string that is getting checked
     * @return boolean if numeric
     */
    public static final boolean isNumeric(String strNum) {
        return strNum.matches(LocationProtocols.numericcheck);
    }
}
