package latitude.serialization;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/

/**
 * Latitude Location Response - gives a response to the mapId and mapName
 * Overrides LatitudeMessage
 * Protocols can be found in LatitudeProtocols
 */
public final class LatitudeLocationResponse extends LatitudeMessage{
    private List<LocationRecord> locations;
    private String mapName;


    /**
     * Constructs location response using set values
     * @param mapId ID for message map
     * @param mapName name of location
     * @throws ValidationException if validation fails
     */
    public LatitudeLocationResponse(long mapId, String mapName) throws ValidationException{
        setMapName(mapName);
        super.setMapId(mapId);
        locations = new ArrayList<>();
    }

    /**
     * Deserializes message from byte source
     * @param in deserialization input source
     * @param mapId is the mapID
     * @return a specific message resulting from deserialization
     * @throws ValidationException if validation fails
     * @throws IOException if I/O problem
     */
    public static LatitudeMessage decode(MessageInput in, long mapId) throws IOException, ValidationException{
        String check = in.ReadTillSpace();
        if(!LatitudeProtocols.isNumeric(check)){
            throw new ValidationException("LatitudeMessage Decode", "Size entered was not Numeric");
        }
        int sizemapname = Integer.parseInt(check);
        String mapname = in.ReadTillN(sizemapname);

        String check1 = in.ReadTillSpace();
        if(!LatitudeProtocols.isNumeric(check1)){
            throw new ValidationException("LatitudeMessage Decode", "Size entered was not Numeric");
        }
        int sizer = Integer.parseInt(check1);
        LatitudeLocationResponse llr = new LatitudeLocationResponse(mapId, mapname);
        for(int i = 0; i < sizer; i++){
            LocationRecord r = new LocationRecord(in);
            llr.addLocationRecord(r);
        }
        in.checkEOS();
        return llr;
    }


    /**
     * Returns map name
     * @return map name
     */
    public String getMapName(){
        return mapName;
    }

    /**
     * Sets the map name
     * @param mapName map name
     * @throws ValidationException if validation fails(including MapName is null)
     */
    public void setMapName(String mapName) throws ValidationException{
        if(mapName == null){
            throw new ValidationException("setMapName", "Threw Null");
        }
        else if(!mapName.matches(LatitudeProtocols.asciiregex)){
            throw new ValidationException("setMapName", "Does not follow ASCII");
        }
        this.mapName = mapName;
    }

    /**
     * Returns list of map locations
     * @return map locations
     */
    public List<LocationRecord> getLocationRecordList() {
        List<LocationRecord> lr = Collections.unmodifiableList(locations);
        return lr;
    }

    //public Object clone(List<LocationRecord> lr) throws CloneNotSupportedException{
      //  List<LocationRecord> llr = (List<LocationRecord>)super.clone();
    //}

    /**
     * Adds new location
     * @param location new location to add
     * @throws ValidationException if validation error
     */
    public void addLocationRecord(LocationRecord location) throws ValidationException{
        if(location == null){
            throw new ValidationException("Location", "is null");
        }
        locations.add(location);
    }


    /**
     * Overrides getOperation in Latitude Message
     * @return string representation of RESPONSE class
     */
    @Override
    public String getOperation(){
        return LatitudeProtocols.LMProtocols[2];
    }


    /**
     * Overrides the encode in LatitudeMessage
     * @param out serialization output destination
     * @throws IOException if IO Exception Failure
     * @throws NullPointerException if out is null
     */
    @Override
    public void encode(MessageOutput out) throws IOException, NullPointerException{
            if(out == null){
                throw new NullPointerException("Response encode out- null");
            }

            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            DataOutputStream dOut = new DataOutputStream(buf);
            String operation = getOperation();

            dOut.writeBytes(LatitudeProtocols.LMVersion + " " + getMapId() + " " + operation + " ");


            int size = getMapName().length();
            dOut.writeBytes(size + " " + getMapName());

            List<LocationRecord> list = getLocationRecordList();
            if(list == null){
                throw new IOException("if list is null");
            }
            size = list.size();
            dOut.writeBytes("" + size + " ");



            byte[] msg = buf.toByteArray();
            out.write(msg);
            for (int i = 0; i < size; i++) {
                LocationRecord lr = list.get(i);
                lr.encode(out);
            }

            //dOut.flush();
            ByteArrayOutputStream buf2 = new ByteArrayOutputStream();
            dOut = new DataOutputStream(buf2);
            dOut.writeBytes(LatitudeProtocols.LMdelimeter);
            dOut.flush();
            byte[] msg2 = buf2.toByteArray();
            out.write(msg2);

    }

    /**
     * Returns Equals of Object, it is overriden
     * @param o is the object
     * @return if objects are equal or not in boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatitudeLocationResponse response = (LatitudeLocationResponse) o;
        return Objects.equals(locations, response.locations) &&
                Objects.equals(mapName, response.mapName) &&
                Objects.equals(getMapId(), response.getMapId());
    }

    /**
     * Overrides HashCode from Object
     * @return hash of class
     */
    @Override
    public int hashCode() {
        return Objects.hash(locations, mapName, getMapId());
    }

    /**
     * Overrides toString in class LatitudeMessage
     * @return string rep
     */
    @Override
    public String toString() {
        String x = "mapID="+getMapId()+ " - LocationResponse\n";
        for(int i = 0; i < getLocationRecordList().size(); i++){

            LocationRecord lr = getLocationRecordList().get(i);
            //String mapn = getMapName().substring(4);

            x += "User " + lr.getUserId() + ":" + lr.getLocationName() + " - " + lr.getLocationDescription() + " at (" + lr.getLongitude() + ", " + lr.getLatitude() + ")\n";
        }
        return x;
    }
}
