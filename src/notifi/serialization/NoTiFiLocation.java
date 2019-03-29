package notifi.serialization;

import latitude.serialization.LocationProtocols;
import latitude.serialization.ValidationException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Objects;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 4
 * Class: CSI 4321
 *
 **********************************************/
public class NoTiFiLocation {
    private long userId;
    private double longitude;
    private double latitude;
    private String locationName;
    private String locationDescription;


    /**
     * Constructs Location Record with Set Values
     * @param userId ID for user
     * @param longitude position for location
     * @param latitude position for location
     * @param locationName name of location
     * @param locationDescription description of location
     * @throws IllegalArgumentException if parameter validation fails
     */
    public NoTiFiLocation(long userId, double longitude, double latitude, String locationName, String locationDescription) throws IllegalArgumentException{
        setUserId(userId);
        setLongitude(longitude);
        setLatitude(latitude);
        setLocationName(locationName);
        setLocationDescription(locationDescription);
    }

    /**
     * Overrides toString in java.lang.Object
     * @return string representation of Location
     */
    @Override
    public String toString() {
        return "NoTiFiLocation{" +
                "userId=" + userId +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", locationName='" + locationName + '\'' +
                ", locationDescription='" + locationDescription + '\'' +
                '}';
    }

    /**
     * Get User ID
     * @return user ID
     */
    public long getUserId(){
        return userId;
    }

    /**
     * Get Longitude
     * @return longitude of location
     */
    public double getLongitude(){
        return longitude;
    }

    /**
     * Get Latitude of Location
     * @return latitude of location
     */
    public double getLatitude(){
        return latitude;
    }

    /**
     * Get Name of Location
     * @return name of location
     */
    public String getLocationName(){
        return locationName;
    }

    /**
     * Get Description of Location
     * @return description of location
     */
    public String getLocationDescription(){
        return locationDescription;
    }


    /**
     * Sets user ID
     * @param userId new user ID
     * @throws IllegalArgumentException if user ID out of range
     */
    public void setUserId(long userId) throws IllegalArgumentException{
        if(userId < NTFProtocols.userIdProtocols[0] || userId > NTFProtocols.userIdProtocols[1]){
            throw new IllegalArgumentException("userID is out of range");
        }
        this.userId = userId;
    }

    /**
     * Set Longitude
     * @param longitude new longitude
     * @throws IllegalArgumentException if longitude out of bounds
     */
    public void setLongitude(double longitude)throws IllegalArgumentException{
        if(longitude < LocationProtocols.longprotocol[0]){
            throw new IllegalArgumentException( "Longitude is not > -180.0");
        }
        else if(longitude > LocationProtocols.longprotocol[1]){
            throw new IllegalArgumentException( "Longitude is not < 180.0");
        }
        this.longitude = longitude;
    }

    /**
     * Set Latitude
     * @param latitude new latitude
     * @throws IllegalArgumentException if latitude out of bounds
     */
    public void setLatitude(double latitude) throws IllegalArgumentException{
        if(latitude < LocationProtocols.latprotocol[0]){
            throw new IllegalArgumentException( "Latitude is not > -90.0");
        }
        else if(latitude > LocationProtocols.latprotocol[1]){
            throw new IllegalArgumentException( "Latitude is not < 90.0");
        }
        this.latitude = latitude;
    }

    /**
     * Sets Location Name
     * @param locationName new location name
     * @throws IllegalArgumentException if name null or too long
     */
    public void setLocationName(String locationName) throws IllegalArgumentException{
        if(locationName == null){
            throw new IllegalArgumentException("LocationName is null");
        }
        if(!locationName.matches(LocationProtocols.asciiregex)){
            throw new IllegalArgumentException("LocationName does not follow regex");
        }
        if(locationName.length() > NTFProtocols.stringmaxsize || locationName.length() < 0){
            throw new IllegalArgumentException("LocationName is too big");
        }
        this.locationName = locationName;
    }

    /**
     * Sets Location Description
     * @param locationDescription new location description
     * @throws IllegalArgumentException if description null or too long
     */
    public void setLocationDescription(String locationDescription) throws IllegalArgumentException{
        if(locationDescription == null){
            throw new IllegalArgumentException("LocationDescription is null");
        }
        if(!locationDescription.matches(LocationProtocols.asciiregex)){
            throw new IllegalArgumentException("LocationName does not follow regex");
        }
        if(locationDescription.length() > NTFProtocols.stringmaxsize || locationDescription.length() < 0){
            throw new IllegalArgumentException("LocationDescription is too big");
        }
        this.locationDescription = locationDescription;
    }

    /**
     * Deserializes message
     * @param pkt byte array of message
     * @return represented class
     * @throws IllegalArgumentException if bad version or code
     * @throws IOException if I/O problem including packet too long/short(EOFException)
     */
    public static NoTiFiLocation decode(byte[] pkt) throws IllegalArgumentException, IOException{
        try{
            int bytendx = 2;

            ByteBuffer buff = ByteBuffer.wrap(pkt,bytendx,bytendx+4);
            buff.order(ByteOrder.BIG_ENDIAN);
            long b2 = buff.getInt(bytendx);
            bytendx+=4;
            long userId = b2;


            ByteBuffer buff2 = ByteBuffer.wrap(pkt,bytendx, bytendx+8);
            buff2.order(ByteOrder.LITTLE_ENDIAN);
            double longitude = buff2.getDouble(bytendx);
            bytendx+=8;

            ByteBuffer buff3 = ByteBuffer.wrap(pkt, bytendx, bytendx+8);
            buff3.order(ByteOrder.LITTLE_ENDIAN);
            double latitude = buff3.getDouble(bytendx);
            bytendx+=8;


            int lengthsize = pkt[bytendx] & 0xFF;
            bytendx++;

            ByteBuffer buff4 = ByteBuffer.wrap(pkt, bytendx,bytendx+lengthsize);
            String locname = "";
            for(int i = 0; i < lengthsize; i++){
                    locname += (char)buff4.get(i+bytendx);
            }
            bytendx+=lengthsize;


            int descsize = pkt[bytendx] & 0xFF;
            bytendx++;

            ByteBuffer buff5 = ByteBuffer.wrap(pkt, bytendx, bytendx+descsize);
            String locdesc = "";
            for(int i = 0; i < descsize; i++){
                locdesc+= (char)buff5.get(i+bytendx);
            }
            bytendx += descsize;


            NoTiFiLocation loc = new NoTiFiLocation(userId,longitude, latitude, locname, locdesc);


            return loc;
        }catch (ArrayIndexOutOfBoundsException aioe){
            throw new IOException("Index Out of Bounds IO");
        }
    }


    /**
     * Serializes Location Record
     * @return serialized byte array
     */
    public byte[] encode(){
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(bOut);
        try {

            ByteBuffer userId = ByteBuffer.allocate(4);
            userId.order(ByteOrder.BIG_ENDIAN);
            userId.putInt((int)getUserId());
            dOut.write(userId.array());

            ByteBuffer longitude = ByteBuffer.allocate(8);
            longitude.order(ByteOrder.LITTLE_ENDIAN);
            longitude.putDouble(getLongitude());
            dOut.write(longitude.array());

            ByteBuffer latitude = ByteBuffer.allocate(8);
            latitude.order(ByteOrder.LITTLE_ENDIAN);
            latitude.putDouble(getLatitude());
            dOut.write(latitude.array());

            ByteBuffer namesize = ByteBuffer.allocate(4);
            namesize.putInt(getLocationName().length());
            dOut.write(namesize.array()[3]);

            dOut.write(getLocationName().getBytes(NTFProtocols.charenc));

            ByteBuffer descsize = ByteBuffer.allocate(4);
            descsize.putInt(getLocationDescription().length());
            dOut.write(descsize.array()[3]);

            dOut.write(getLocationDescription().getBytes(NTFProtocols.charenc));



            dOut.flush();

        }catch(IOException ie){

        }
        return bOut.toByteArray();

    }

    /**
     * Overrides equals
     * @param o Object
     * @return equals boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoTiFiLocation that = (NoTiFiLocation) o;
        return userId == that.userId &&
                Double.compare(that.longitude, longitude) == 0 &&
                Double.compare(that.latitude, latitude) == 0 &&
                Objects.equals(locationName, that.locationName) &&
                Objects.equals(locationDescription, that.locationDescription);
    }

    /**
     * Hash code
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, longitude, latitude, locationName, locationDescription);
    }


}
