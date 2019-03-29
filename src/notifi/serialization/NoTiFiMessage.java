package notifi.serialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 4
 * Class: CSI 4321
 *
 **********************************************/
public abstract class NoTiFiMessage {

    private int msgId;
    private int code;



    /**
     * Deserializes from byte array
     * @param pkt byte array to deserialize
     * @return a specific NoTiFi message resulting from deserialization
     * @throws IllegalArgumentException if bad version or code
     * @throws IOException if I/O problem including packet too long/short(EOFException)
     */
    public static NoTiFiMessage decode(byte[] pkt) throws IllegalArgumentException, IOException{
        try {

            if (pkt.length < 2) {
                throw new IOException("Packet too short");
            }

            int msgId = pkt[1] & 0xFF;
            if (pkt[0] == NTFProtocols.bACK) {

                /*if(pkt.length > 2){
                    throw new IOException("Packet contains data when should not since it is ACK");
                }*/

                NoTiFiACK ack = new NoTiFiACK(msgId);
                return ack;
            } else if (pkt[0] == NTFProtocols.bRegister) {

                NoTiFiRegister reg = (NoTiFiRegister) NoTiFiRegister.decode(pkt);
                return reg;

            } else if (pkt[0] == NTFProtocols.bDeregister) {
                NoTiFiDeregister dereg = (NoTiFiDeregister) NoTiFiDeregister.decode(pkt);
                return dereg;


            } else if (pkt[0] == NTFProtocols.bError) {
                NoTiFiError err = (NoTiFiError) NoTiFiError.decode(pkt);
                return err;

            } else if (pkt[0] == NTFProtocols.bLocationAdd) {
                NoTiFiLocationAddition add = (NoTiFiLocationAddition) NoTiFiLocationAddition.decode(pkt);
                return add;
            } else if (pkt[0] == NTFProtocols.bLocationDel) {
                NoTiFiLocationDeletion del = (NoTiFiLocationDeletion) NoTiFiLocationDeletion.decode(pkt);
                return del;

            } else {
                throw new IllegalArgumentException("Bad version or code");
            }
        }catch(ArrayIndexOutOfBoundsException aie){
            throw new IOException("io problem");
        }
    }

    /**
     * Serializes message
     * @return serialized message bytes
     */
    public abstract byte[] encode();

    /**
     * Set Message UD
     * @param msgId message ID
     * @throws IllegalArgumentException if message IS id out of range
     */
    public void setMsgId(int msgId) throws IllegalArgumentException{
        if(msgId < NTFProtocols.msgIdrange[0]){
            throw new IllegalArgumentException("Map ID out of Range 0 > " + msgId);
        }
        else if(msgId > NTFProtocols.msgIdrange[1]){
            throw new IllegalArgumentException("Map ID out of Range: " + msgId);
        }
        this.msgId = msgId;
    }

    /**
     * Get Message ID
     * @return the message ID
     */
    public int getMsgId(){
        return msgId;
    }

    /**
     * Get code
     * @return code for protocol
     */
    public abstract int getCode();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoTiFiMessage that = (NoTiFiMessage) o;
        return msgId == that.msgId &&
                code == that.code;
    }

    @Override
    public String toString() {
        return "NoTiFiMessage{" +
                "msgId=" + msgId +
                ", code=" + code +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(msgId, code);
    }
}
