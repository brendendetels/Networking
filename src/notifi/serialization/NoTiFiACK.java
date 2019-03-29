package notifi.serialization;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
public class NoTiFiACK extends NoTiFiMessage{

    private static final int CODE = NTFProtocols.codes.get(NTFProtocols.messageTypes[5]);

    /**
     * Constructs NoTiFi ACK from values
     * @param msgId message ID
     * @throws IllegalArgumentException if message ID invalid
     */
    public NoTiFiACK(int msgId) throws IllegalArgumentException{
        super.setMsgId(msgId);
    }

    /**
     * Serializes message
     * @return serialized message bytes
     */
    public byte[] encode(){

        byte vers = NTFProtocols.bACK;
        byte msgId = (byte)getMsgId();

        return new byte[]{vers,msgId};
        /*ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(bOut);
        try {


            ByteBuffer protocol = ByteBuffer.allocate(1);
            protocol.order(ByteOrder.LITTLE_ENDIAN);
            protocol.put(NTFProtocols.bACK);
            dOut.write(protocol.array());

            ByteBuffer msg = ByteBuffer.allocate(4);
            msg.putInt(getMsgId());
            dOut.write(msg.array()[3]);

            dOut.flush();

        }catch(IOException ie){

        }
        return bOut.toByteArray();*/
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
        NoTiFiACK that = (NoTiFiACK) o;
        return getMsgId() == that.getMsgId() &&
                getCode() == that.getCode();
    }

    /**
     * Overriden to String
     * @return string rep of class
     */
    @Override
    public String toString() {
        return "NoTiFiACK{" +
                "msgId=" + getMsgId() +
                ", code=" + getCode() +
                '}';
    }

    /**
     * Overriden Hash code
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getMsgId(), getCode());
    }
}
