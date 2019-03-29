package latitude.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.NullPointerException;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/

/**
 * MessageOutput- directs an output stream
 * Protocols can be found in LocationProtocols and LatitudeProtocols
 */
public class MessageOutput {

    private OutputStream out;

    /**
     * Constructs a new output source from an Output Stream
     * @param out is the output stream
     */
    public MessageOutput(OutputStream out) throws NullPointerException {
        setOut(out);
    }

    /**
     * Sets the output stream
     * @param out is the output stream
     */
    public void setOut(OutputStream out) throws NullPointerException{
        if(out == null){
            throw new NullPointerException("Out Equals Null");
        }
        this.out = out;
    }

    /**
     * write() - will write a byte array to output stream
     * @param b is a byte array
     * @throws IOException if there is an I/O error
     */
    public void write(byte [] b) throws IOException{
        if(b == null){
            throw new IOException("IOException: byte is null");
        }
        out.write(b);
    }
}
