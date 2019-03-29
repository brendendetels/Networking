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
public class NoTiFiDeregister extends NoTiFiMessage{

    private static final int CODE = NTFProtocols.codes.get(NTFProtocols.messageTypes[3]);
    private Inet4Address address;
    private int port;

    /**
     * Constructs deregister message using set values
     * @param msgId message id
     * @param address address to deregister
     * @param port port to deregister
     * @throws IllegalArgumentException if any value fails validation
     */
    public NoTiFiDeregister(int msgId, Inet4Address address, int port) throws IllegalArgumentException{
        super.setMsgId(msgId);
        setAddress(address);
        setPort(port);
    }

    /**
     * Get deregister address
     * @return deregister address
     */
    public Inet4Address getAddress(){
        return address;
    }

    /**
     * Set Deregister address
     * @param address deregister address
     * @throws IllegalArgumentException if address is null
     */
    public void setAddress(Inet4Address address) throws IllegalArgumentException{
        if(address == null){
            throw new IllegalArgumentException("Address is null");
        }
        this.address = address;
    }

    /**
     * Get Deregister Port
     * @return deregister port
     */
    public int getPort(){
        return port;
    }

    /**
     * Set Deregister Port
     * @param port deregister port
     * @throws IllegalArgumentException if port is out of range(0... 65535)
     */
    public void setPort(int port) throws IllegalArgumentException{
        if(port < NTFProtocols.portrange[0]){
            throw new IllegalArgumentException("Port is out of range: " + port + " is less than 0");
        }
        if(port > NTFProtocols.portrange[1]){
            throw new IllegalArgumentException("Port is out of range: " + port + " is greater than 65535");
        }
        this.port = port;
    }

    /**
     * Serializes message
     * @return serialized message bytes
     */
    public byte[] encode(){

        byte codeandversion = NTFProtocols.bDeregister;
        byte b = (byte)getMsgId();

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(bOut);
        short port = (short)getPort();

        try {
            dOut.writeShort(port);
            dOut.flush();

            return new byte[] {codeandversion, b, address.getAddress()[3], address.getAddress()[2], address.getAddress()[1], address.getAddress()[0], bOut.toByteArray()[1], bOut.toByteArray()[0]};
        }catch(IOException io){
            return null;
        }

    }

    /**
     * Deserializes message
     * @param pkt byte array of message
     * @return represented class
     * @throws IllegalArgumentException if bad version or code
     * @throws IOException if I/O problem including packet too long/short(EOFException)
     */
    public static NoTiFiMessage decode(byte[] pkt) throws IllegalArgumentException, IOException{
        int msgId = pkt[1] & 0xFF;
        byte[] ip = new byte[4];
        int ipstart = 5;
        for(int i = 0; i < ip.length;i++) {
            ip[i] = pkt[ipstart];
            ipstart--;
        }
        Inet4Address addr = (Inet4Address) InetAddress.getByAddress(ip);

        int portn = pkt[6] & 0xFF | (pkt[7] & 0xFF) << 8;


        NoTiFiDeregister reg = new NoTiFiDeregister(msgId,addr,portn);

        return reg;
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
        if (!super.equals(o)) return false;
        NoTiFiDeregister that = (NoTiFiDeregister) o;
        return port == that.port && getMsgId() == that.getMsgId() &&
                Objects.equals(address, that.address);
    }

    /**
     * Overriden Hash code
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address, port);
    }

    /**
     * Overriden to String
     * @return string rep of class
     */
    @Override
    public String toString() {
        return "NoTiFiDeregister{" + "messageID=" + getMsgId() +
                ", address=" + address +
                ", port=" + port +
                '}';
    }

}
