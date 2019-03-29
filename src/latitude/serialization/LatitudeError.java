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
 * Latitude Error Class - Creates an Error Latitude Message
 * Overrides LatitudeMessage
 * Protocols can be found in LatitudeProtocols
 */
public class LatitudeError extends LatitudeMessage{

    private String errorMessage;


    /**
     * Constructs error message using set values
     * @param mapId ID for Message Map
     * @param errorMessage Error Message
     * @throws ValidationException if validation fails
     */
    public LatitudeError(long mapId, String errorMessage) throws ValidationException{
        if(errorMessage == null){
            throw new ValidationException("LatitudeError()", "Error Message is null");
        }
        super.setMapId(mapId);
        setErrorMessage(errorMessage);
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
        String checkerr = in.ReadTillSpace();
        if(!LatitudeProtocols.isNumeric(checkerr)){
            throw new ValidationException("LatitudeMessage Decode", "Size entered was not Numeric");
        }
        int size = Integer.parseInt(checkerr);
        String errmsg = in.ReadTillN(size);
        if(errmsg.length() != size){
            throw new ValidationException("Size incorrect", "Size entered was not same size");
        }
        LatitudeError lerr = new LatitudeError(mapId, errmsg);
        in.checkEOS();
        return lerr;
    }

    /**
     * Return error message
     * @return error message
     */
    public final String getErrorMessage(){
        return errorMessage;
    }

    /**
     * Set Error message
     * @param errorMessage new error message
     * @throws ValidationException if validation fails
     */
    public final void setErrorMessage(String errorMessage) throws ValidationException{
        if(errorMessage == null){
            throw new ValidationException("LM.ERROR(): ", "setErrorMessage() is null");
        }
        if(!errorMessage.matches(LatitudeProtocols.asciiregex) ){
            throw new ValidationException("LM.ERROR(): setErrorMessage()", " did not follow ASCII");
        }

        this.errorMessage = errorMessage;
    }


    /**
     * Overrides the Encode in Latitude Message
     * @param out serialization output destination
     * @throws IOException if bad I/O
     * @throws NullPointerException if out is null
     */
    @Override
    public void encode(MessageOutput out) throws IOException, NullPointerException{
        if( out == null){
            throw new NullPointerException("LM.ERROR(): out is null");
        }

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(buf);
        String operation = getOperation();
        int size = getErrorMessage().length();
        dOut.writeBytes(LatitudeProtocols.LMVersion+ " " + String.valueOf(getMapId()) + " " + operation + " " + size + " " + getErrorMessage() + LatitudeProtocols.LMdelimeter);

        dOut.flush();
        byte[] r = buf.toByteArray();
        out.write(r);

    }

    /**
     * Overrides Latitude Message
     * @return Strng representation of LatitudeError
     */
    @Override
    public String getOperation(){
        return LatitudeProtocols.LMProtocols[3];
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
        LatitudeError that = (LatitudeError) o;
        return Objects.equals(errorMessage, that.errorMessage) &&
                Objects.equals(getMapId(), that.getMapId());
    }

    /**
     * Overrides Object hashCode
     * @return hash code of errorMessage
     */
    @Override
    public int hashCode() {
        return Objects.hash(errorMessage, getMapId());
    }

    /**
     * Overrides toString in Latitude Message
     * @return string rep of class
     */
    @Override
    public String toString() {
        return "ERROR: " + errorMessage;
    }
}
