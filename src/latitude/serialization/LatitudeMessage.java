package latitude.serialization;

import javax.xml.stream.Location;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Objects;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/

/**
 * Latitude Message Class - Abstract class for all the different Latitude Protocols
 * Is Overridden by the Latitude Protocols
 * Protocols can be found in LatitudeProtocols
 */
public abstract class LatitudeMessage {
    private long mapId;


    /**
     * Serializes message to MessageOutput
     * @param out serialization output destination
     * @throws IOException if I/O problem
     */
    public abstract void encode(MessageOutput out) throws IOException;

    /**
     * Deserializes message from byte source
     * @param in deserialization input source
     * @return a specific message resulting from deserialization
     * @throws ValidationException if validation fails
     * @throws IOException if I/O problem
     */
    public static LatitudeMessage decode(MessageInput in) throws ValidationException, IOException{

        String x = in.ReadTillSpace();
        if(!x.equals(LatitudeProtocols.LMVersion)){
            throw new ValidationException("LM.Decode()", "Not LATITUDEv1 Message: Wrong Version");
        }


        String id = in.ReadTillSpace();
        if(!LatitudeProtocols.isNumeric(id)){
            throw new ValidationException("LM.Decode()", "Invalid Non Numeric Map Id");
        }
        long mapId = Long.parseLong(id);
        if(mapId < LatitudeProtocols.mapIdProtocols[0] || mapId >= LatitudeProtocols.mapIdProtocols[1]){
            throw new ValidationException("LM.Decode(): ", "mapId is less than 0 or greater than max for long");
        }


        String operation = in.ReadTillSpace();

        //IF "NEW" LM
        if(operation.equals(LatitudeProtocols.LMProtocols[0])){
            LocationRecord rec = new LocationRecord(in);
            LatitudeNewLocation newL = new LatitudeNewLocation(mapId,rec);
            in.checkEOS();
            return newL;
        }
        // if "ALL" LM
        else if(operation.equals(LatitudeProtocols.LMProtocols[1])){
            LatitudeLocationRequest lr = new LatitudeLocationRequest(mapId);
            in.checkEOS();
            return lr;
        }
        // IF "RESPONSE" LM
        else if(operation.equals(LatitudeProtocols.LMProtocols[2])){
            LatitudeMessage llr = LatitudeLocationResponse.decode(in,mapId);
            return llr;
        }
        // IF "ERROR" LM
        else if(operation.equals(LatitudeProtocols.LMProtocols[3])){
            LatitudeMessage le = LatitudeError.decode(in,mapId);
            return le;
        }
        else{
            throw new ValidationException("Decode", "Did not properly encode the operation");
        }

    }


    /**
     * Returns map Id
     * @return map ID
     */
    public final long getMapId(){
        return mapId;
    }

    /**
     * Sets Map ID
     * @param mapId new Map ID
     * @throws ValidationException if Validation Fails
     */
    public final void setMapId(long mapId) throws ValidationException{
        if(mapId < 0){
            throw new ValidationException("SetMapID()", "Map ID is less than 0");
        }
        else if(mapId >= LatitudeProtocols.mapIdProtocols[1]){
            throw new ValidationException("SetMapID()", "Map ID is greater than max");
        }

        this.mapId = mapId;
    }

    /**
     * Overriden by all other Latitude Classes
     * @return String representation of Decode
     */
    public abstract String getOperation();


    /**
     * Overrides toString in java.lang.Object
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "LatitudeMessage{" +
                "mapId=" + mapId +
                '}';
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
        LatitudeMessage response = (LatitudeMessage) o;
        return Objects.equals(getMapId(), response.getMapId());
    }

    /**
     * Overrides Object hashCode
     * @return hash code of errorMessage
     */
    @Override
    public int hashCode() {
        return Objects.hash(getMapId());
    }


}
