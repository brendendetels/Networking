package latitude.serialization;

import java.io.*;
import java.util.*;
import latitude.serialization.LocationProtocols;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/


/**
 * Location Record- is a record of a location
 * Protocols can be found in LocationProtocols
 */
public class LocationRecord {
    private long userid;
    private String longitude, latitude, locationName, locationDescription;



    /**
     * Constructs location record using deserialization
     * @param in deserialization input source
     * @throws ValidationException if validation failure
     * @throws IOException if I/O problem
     */
    public LocationRecord(MessageInput in) throws ValidationException, IOException {
            if(in == null){
                throw new NullPointerException("Message Input in was null");
            }

            String word = in.ReadTillSpace();

            if(!word.matches(LocationProtocols.asciiregex)){
                throw new ValidationException("Decode()" ,"UserID does not follow ASCII");
            }
            if(!LocationProtocols.isNumeric(word)){
                throw new ValidationException("Not numeric", "Location Record Decode");
            }
            setUserId(Long.parseLong(word));

            word = in.ReadTillSpace();
            setLongitude(word);
            word = in.ReadTillSpace();
            setLatitude(word);
            word = in.ReadTillSpace();
            //check if word contains characters to see if length incorrect

            if(!LocationProtocols.isNumeric(word)){
                throw new ValidationException("Not numeric", "Location Record Decode");
            }
            int size = Integer.parseInt(word);
            word = in.ReadTillN(size);
            setLocationName(word);
            word = in.ReadTillSpace();
            //same as above
            if(!LocationProtocols.isNumeric(word)){
                throw new ValidationException("Not numeric", "Location Record Decode");
            }
            int size2 = Integer.parseInt(word);
            word = in.ReadTillN(size2);
            setLocationDescription(word);

    }

    /**
     * Method is not required. Constructs location record using user input
     * @param in user input source
     * @param out user output(prompt) destination
     * @throws ValidationException if validation fails
     */
    public LocationRecord(Scanner in, PrintStream out) throws ValidationException{


        out.println("User ID>");
        String tmp = "";

        tmp = in.nextLine();
        if(!LocationProtocols.isNumeric(tmp)){
           // throw new ValidationException("LocationRecord(Scanner, PrintStream)", "UserID is not numeric");
            System.err.println("Invalid user input: userID is not numeric");
        }
        setUserId(Long.parseLong(tmp));

        out.println("Longitude>");
        tmp = in.nextLine();
        if(!LocationProtocols.isNumeric(tmp)){
            System.err.println("Invalid user input: Longitude is not numeric");
        }
        setLongitude(tmp);

        out.println("Latitude>");
        tmp = in.nextLine();
        if(!LatitudeProtocols.isNumeric(tmp)){
            System.err.println("Invalid user input: Latitude is not numeric");
        }
        setLatitude(tmp);

        out.println("Location Name>");
        String ln = in.nextLine();

        setLocationName(ln);

        out.println("Location Description>");
        String ld = in.nextLine();
        setLocationDescription(ld);



    }

    /**
     * Constructs location record with set values
     * @param userId ID for user
     * @param longitude position of location
     * @param latitude position of location
     * @param locationName name of location
     * @param locationDescription description of location
     * @throws ValidationException if validation fails
     */
    public LocationRecord(long userId, String longitude, String latitude, String locationName, String locationDescription) throws ValidationException{
        setUserId(userId);
        setLongitude(longitude);
        setLatitude(latitude);
        setLocationName(locationName);
        setLocationDescription(locationDescription);
    }


    /**
     * Serializes Location Record
     * @param out serialization output destination
     * @throws IOException if I/O problem
     */
    public void encode(MessageOutput out) throws NullPointerException, IOException{
        //Page 45 TCP/IP Book for Help
        if(out == null){
            throw new NullPointerException("LocationRecord.encode: MessageOut out was null");
        }

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(buf);

        //writing UserID Longitude Latitude
        dOut.writeBytes(String.valueOf(getUserId()) + " " + getLongitude() + " " + getLatitude() + " ");

        // writing the Location Name Size and Location Name
        int size = getLocationName().length();
        dOut.writeBytes(String.valueOf(size));
        dOut.writeBytes(" " + getLocationName());


        //Writing the location description size and location description
        int dsize = getLocationDescription().length();
        dOut.writeBytes(String.valueOf(dsize));
        dOut.writeBytes(" " + getLocationDescription());


        dOut.flush();
        byte[] msg = buf.toByteArray();
        out.write(msg);
    }

    /**
     * Returns latitude
     * @return latitude
     */
    public final String getLatitude(){
        return latitude;
    }

    /**
     * Returns location description
     * @return location description
     */
    public final String getLocationDescription(){
        return locationDescription;
    }

    /**
     * Returns location name
     * @return location name
     */
    public final String getLocationName() {
        return locationName;
    }


    /**
     * Returns longitude
     * @return longitude
     */
    public final String getLongitude() {
        return longitude;
    }


    /**
     * Returns user ID
     * @return userId
     */
    public final long getUserId(){
        return userid;
    }


    /**
     * sets Latitude
     * @param latitude new latitude
     * @throws ValidationException if validation fails
     */
    public final void setLatitude(String latitude) throws ValidationException{

        //System.out.println("hi: " + locProtocols.asciiregex);
        if(latitude == null){
            throw new ValidationException( "setLatitude()" ,"Latitude is null");
        }
        else if(!latitude.matches(LocationProtocols.asciiregex)){
            throw new ValidationException( "setLatitude()" ,"Latitude does not follow ASCII");
        }
        if(latitude.matches(LocationProtocols.latlongprotocol) == false){
            throw new ValidationException( "setLatitude()" ,"Latitude does not follow REGEX");
        }

        else if(Double.parseDouble(latitude) < LocationProtocols.latprotocol[0]){
            throw new ValidationException( "setLatitude()" ,"Latitude is not > -90.0");
        }

        else if(Double.parseDouble(latitude) > LocationProtocols.latprotocol[1]){
            throw new ValidationException( "setLatitude()" ,"Latitude is not < 90.0");
        }
        this.latitude = latitude;
    }


    /**
     * Sets location description
     * @param locationDescription new location description
     * @throws ValidationException if validation fails
     */
    public final void setLocationDescription(String locationDescription)throws ValidationException {
        if(locationDescription == null){
            throw new ValidationException("setLocationDescription", "Location Description is null");
        }
        else if(!locationDescription.matches(LocationProtocols.asciiregex)){
            throw new ValidationException("setLocationDescription", "Location Description does not follow ASCII");
        }
        this.locationDescription = locationDescription;
    }

    /**
     * Sets Location Name
     * @param locationName new location name
     * @throws ValidationException if validation fails
     */
    public final void setLocationName(String locationName) throws ValidationException {
        if(locationName == null){
            throw new ValidationException("setLocationName()", "Location Name is null");
        }
        else if(!locationName.matches(LocationProtocols.asciiregex)){
            throw new ValidationException( "setLocationName()" ,"LocationName does not follow ASCII");
        }
        this.locationName = locationName;
    }

    /**
     * Sets longitude
     * @param longitude new longitude
     * @throws ValidationException if validation fails
     */
    public final void setLongitude(String longitude) throws ValidationException {
        //this will throw Validation Exception if null, empty, or does not follow REGEX

        if(longitude == null){
            throw new ValidationException( "setLongitude()" ,"Longitude is null");
        }
        else if(!longitude.matches(LocationProtocols.asciiregex)){
            throw new ValidationException( "setLongitude()" ,"Longitude does not follow ASCII");
        }
        else if(longitude.matches(LocationProtocols.latlongprotocol) == false){
            throw new ValidationException( "setLongitude()" ,"Longitude does not follow REGEX");
        }
        else if(Double.parseDouble(longitude) < LocationProtocols.longprotocol[0]){
            throw new ValidationException( "setLongitude()" ,"Longitude is not > -180.0");
        }
        else if(Double.parseDouble(longitude) > LocationProtocols.longprotocol[1]){
            throw new ValidationException( "setLongitude()" ,"Longitude is not < 180.0");
        }
        this.longitude = longitude;
    }


    /**
     * Sets user ID
     * @param userId new user ID
     * @throws ValidationException if validation fails
     */
    public final void setUserId(long userId) throws ValidationException {
        if(userId >= LocationProtocols.userIdProtocols[1]){
            throw new ValidationException( "setUserId()" ,"userId too large");
        }
        else if(userId < LocationProtocols.userIdProtocols[0] ){
            throw new ValidationException( "setUserId()" ,"userId is less than 0");
        }
        this.userid = userId;
    }

    /**
     * Overrides equals in class java.lang.Object
     * @param o Object you are comparing
     * @return boolean of true of false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationRecord)) return false;
        LocationRecord that = (LocationRecord) o;
        return userid == that.userid &&
                Objects.equals(getLongitude(), that.getLongitude()) &&
                Objects.equals(getLatitude(), that.getLatitude()) &&
                Objects.equals(getLocationName(), that.getLocationName()) &&
                Objects.equals(getLocationDescription(), that.getLocationDescription());
    }

    /**
     * Overrides hashCode in class java.lang.Object
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(userid, getLongitude(), getLatitude(), getLocationName(), getLocationDescription());
    }

    /**
     * Overrides toString in class java.lang.Object
     * @return string representation of Object
     */
    @Override
    public String toString() {
        return "LocationRecord{" +
                "userid=" + userid +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", locationName='" + locationName + '\'' +
                ", locationDescription='" + locationDescription + '\'' +
                '}';
    }


}
