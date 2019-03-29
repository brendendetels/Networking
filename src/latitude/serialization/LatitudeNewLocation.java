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
 * Latitude New Location - Creates a New Location Latitude Protocol
 * Overrides Latitude Message
 * Protocols can be found in LatitudeProtocols
 */
public final class LatitudeNewLocation extends LatitudeMessage {
    private LocationRecord location;


    /**
     * Constructs new location using set values
     * @param mapId ID for message map
     * @param location new location
     * @throws ValidationException if validation fails
     */
    public LatitudeNewLocation(long mapId, LocationRecord location) throws ValidationException{
        super.setMapId(mapId);
        setLocationRecord(location);
    }

    /**
     * Sets location
     * @param location new location
     * @throws ValidationException if null error message
     */
    public final void setLocationRecord(LocationRecord location) throws ValidationException{
        if(location == null){
            throw new ValidationException("LatitudeNewLocation", " setLocationRecord(location) is null");
        }
        this.location = new LocationRecord(location.getUserId(), location.getLongitude(), location.getLatitude(), location.getLocationName(), location.getLocationDescription());
    }

    /**
     * Returns location
     * @return location record
     */
    public final LocationRecord getLocationRecord(){
        return location;
    }


    /**
     * Overrides Encode from LatitudeMessage
     * @param out serialization output destination
     * @throws IOException if IO Exception
     * @throws NullPointerException if out is null
     */
    @Override
    public void encode(MessageOutput out) throws IOException, NullPointerException {
        if(out == null){
            throw new NullPointerException("LatitudeNewLocation.encode: out is null");
        }

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(buf);
        //String operation = getOperation();

        dOut.writeBytes(LatitudeProtocols.LMVersion + " " + getMapId() + " " + LatitudeProtocols.LMProtocols[0] +" ");

        LocationRecord lr = getLocationRecord();
        byte[] msg = buf.toByteArray();
        out.write(msg);
        lr.encode(out);

        ByteArrayOutputStream buf2 = new ByteArrayOutputStream();
        dOut = new DataOutputStream(buf2);
        dOut.writeBytes(LatitudeProtocols.LMdelimeter);
        dOut.flush();
        byte[] msg2 = buf2.toByteArray();
        out.write(msg2);
    }

    /**
     * Overrides Object equals
     * @param o Object
     * @return boolean if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatitudeMessage response = (LatitudeNewLocation) o;
        return Objects.equals(getMapId(), response.getMapId())
                && Objects.equals(location, ((LatitudeNewLocation) response).location);
    }

    /**
     * Overrides Object hashCode
     * @return hash code of errorMessage
     */
    @Override
    public int hashCode() {
        return Objects.hash(getMapId(), location);
    }

    /**
     * Overrides getOperation from LatitudeMessage
     * @return string representation of NEW
     */
    @Override
    public String getOperation(){
        return LatitudeProtocols.LMProtocols[0];
    }


    /**
     * Overrides to String in LatitudeMessage
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "LatitudeNewLocation{" +
                "location=" + location +
                '}';
    }
}
