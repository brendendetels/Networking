package notifi.serialization;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 4
 * Class: CSI 4321
 *
 **********************************************/
public class NoTiFiError extends NoTiFiMessage{
    private static final int CODE = NTFProtocols.codes.get(NTFProtocols.messageTypes[4]);
    private String errorMessage;

    /**
     * Constructs NoTiFi error from values
     * @param msgId message ID
     * @param errorMessage error Message
     * @throws IllegalArgumentException if values invalid
     */
    public NoTiFiError(int msgId, String errorMessage) throws IllegalArgumentException{
        super.setMsgId(msgId);
        setErrorMessage(errorMessage);
    }


    /**
     * Set Error Message
     * @param errorMessage error message
     * @throws IllegalArgumentException if error message is null
     */
    public void setErrorMessage(String errorMessage) throws IllegalArgumentException{
        if(errorMessage == null){
            throw new IllegalArgumentException("error message is null");
        }
        if(!errorMessage.matches(NTFProtocols.asciiregex)){
            throw new IllegalArgumentException("LocationName does not follow regex");
        }
        this.errorMessage = errorMessage;
    }

    /**
     * Get Error Message
     * @return error message
     */
    public String getErrorMessage(){
        return errorMessage;
    }

    /**
     * Serializes message
     * @return serialized message bytes
     */
    public byte[] encode(){
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(bOut);
        try {


            ByteBuffer protocol = ByteBuffer.allocate(1);
            protocol.put(NTFProtocols.bError);
            dOut.write(protocol.array());

            ByteBuffer msg = ByteBuffer.allocate(4);
            msg.putInt(getMsgId());
            dOut.write(msg.array()[3]);

            dOut.write(errorMessage.getBytes(NTFProtocols.charenc));

            dOut.flush();

        }catch(IOException ie){

        }
        return bOut.toByteArray();
    }

    /**
     * Deserializes message
     * @param pkt byte array of message
     * @return represented class
     * @throws IllegalArgumentException if bad version or code
     * @throws IOException if I/O problem including packet too long/short(EOFException)
     */
    public static NoTiFiMessage decode(byte[] pkt) throws IllegalArgumentException, IOException{
        try {
            int msgId = pkt[1] & 0xFF;
            String errormsg = "";
            for (int i = 2; i < pkt.length; i++) {
                errormsg += (char) pkt[i];
            }

            NoTiFiError err = new NoTiFiError(msgId, errormsg);
            return err;
        }catch (ArrayIndexOutOfBoundsException aioe){
            throw new IOException("Index Out of Bounds IO");
        }

    }

    /**
     * Get code
     * @return code for protocol
     */
    @Override
    public int getCode() {
        return CODE;
    }


    /**
     * Overriden Equals
     * @param o Object
     * @return boolean if equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoTiFiError that = (NoTiFiError) o;
        return getMsgId() == that.getMsgId() &&
                Objects.equals(getErrorMessage(), that.getErrorMessage()) &&
                getCode() == that.getCode();
    }

    /**
     * Overriden to String
     * @return string rep of class
     */
    @Override
    public String toString() {
        return "NoTiFiError{" +
                "msgId=" + getMsgId() +
                ", errorMessage=" + errorMessage +
                ", code=" + getCode() +
                '}';
    }

    /**
     * Overriden Hash code
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getMsgId(), getErrorMessage() ,getCode());
    }
}
