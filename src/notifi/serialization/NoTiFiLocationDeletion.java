package notifi.serialization;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 4
 * Class: CSI 4321
 *
 **********************************************/
public class NoTiFiLocationDeletion extends NoTiFiMessage{
    private static final int CODE = NTFProtocols.codes.get(NTFProtocols.messageTypes[2]);
    private NoTiFiLocation location;

    /**
     * Constructs NoTiFi location deletion values
     * @param msgId message ID
     * @param location location to delete
     * @throws IllegalArgumentException if message ID invalid
     */
    public NoTiFiLocationDeletion(int msgId, NoTiFiLocation location) throws IllegalArgumentException{
        if(location == null){
            throw new IllegalArgumentException("location is null");
        }
        super.setMsgId(msgId);
        setLocation(location);
    }

    /**
     * Get Location Record
     * @return location record
     */
    public NoTiFiLocation getLocation(){
        return location;
    }

    /**
     * Sets Location Record
     * @param location location to delete
     * @throws IllegalArgumentException if location is null
     */
    public void setLocation(NoTiFiLocation location) throws IllegalArgumentException{
        if(location == null){
            throw new IllegalArgumentException("Location is null");
        }
        this.location = location;
    }

    /**
     * Serializes message
     * @return serialized message bytes
     */
    public byte[] encode(){

        byte[] b = new byte[2];
        b[0] = NTFProtocols.bLocationDel;
        b[1] = (byte)getMsgId();

        byte[] loc = getLocation().encode();


        byte[] concat = Arrays.copyOf(b, b.length + loc.length);
        System.arraycopy(loc, 0, concat, b.length,loc.length);
        //return new byte[]{b[0],b[1],loc[0], loc[1], loc[2], loc[3] };
        return concat;

        /*ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(bOut);
        try {
            ByteBuffer protocol = ByteBuffer.allocate(1);
            protocol.put(NTFProtocols.bLocationDel);
            dOut.write(protocol.array());

            ByteBuffer msg = ByteBuffer.allocate(4);
            msg.putInt(getMsgId());
            dOut.write(msg.array()[3]);

            byte[] location = getLocation().encode();
            dOut.write(location);

            dOut.flush();

        }catch(IOException ie){

        }
        return bOut.toByteArray();*/
    }

    /**
     * Deserializes message
     * @param pkt byte array of message
     * @return represented class
     * @throws IllegalArgumentException if bad version or code
     * @throws IOException if I/O problem including packet too long/short(EOFException)
     */
    public static NoTiFiMessage decode(byte[] pkt) throws IllegalArgumentException, IOException {
        try {
            int msgId = pkt[1] & 0xFF;
            NoTiFiLocation loc = NoTiFiLocation.decode(pkt);
            NoTiFiLocationDeletion noTiFiLocationDeletion = new NoTiFiLocationDeletion(msgId, loc);


            return noTiFiLocationDeletion;
        } catch (ArrayIndexOutOfBoundsException aioe) {
            throw new IOException("Index Out of Bounds IO");
        }
        catch(IndexOutOfBoundsException opb){
            throw new IOException("Index OB");
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
     * Overriden to String
     * @return string rep of class
     */
    @Override
    public String toString() {
        return "NoTiFiLocationDeletion{ msgId=" + getMsgId()
                + " " +location.toString() + '}';
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
        if (!super.equals(o)) return false;
        NoTiFiLocationDeletion that = (NoTiFiLocationDeletion) o;
        return Objects.equals(location, that.location) && getMsgId() == that.getMsgId();
    }
    /**
     * Overriden Hash code
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMsgId(), location);
    }
}
