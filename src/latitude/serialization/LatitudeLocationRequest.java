package latitude.serialization;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
 * LatitudeLocationRequest - creates a Location Request for Latitude Protocol
 * Overrides LatitudeMessage
 * Protocols can be found in LatitudeProtocols
 */
public class LatitudeLocationRequest extends LatitudeMessage{


    /**
     * Constructs location request using set values
     * @param mapId is the id of the map
     * @throws ValidationException if validation fails
     */
    public LatitudeLocationRequest(long mapId) throws ValidationException{
        super.setMapId(mapId);
    }

    /**
     * Overrides LatitudeMessage
     * @return String representation of decode class
     */
    @Override
    public String getOperation(){
        return LatitudeProtocols.LMProtocols[1];
    }

    /**
     * Overrides Encode in Latitude Message
     * @param out serialization output destination
     * @throws IOException if bad I/O call
     * @throws NullPointerException if out is null
     */
    @Override
    public void encode(MessageOutput out) throws IOException, NullPointerException{
        if(out == null){
            throw new NullPointerException("Response out = Null");
        }

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(buf);
        String operation = getOperation();

        dOut.writeBytes(LatitudeProtocols.LMVersion+ " " + String.valueOf(getMapId()) + " " + operation + " " + LatitudeProtocols.LMdelimeter);

        dOut.flush();
        byte[] r = buf.toByteArray();
        out.write(r);

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
        LatitudeLocationRequest response = (LatitudeLocationRequest) o;
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


    /**
     * Overrides toString in class LatitudeMessage
     * @return String representation of class
     */
    @Override
    public String toString() {
        return "LatitudeLocationRequest{}";
    }
}
