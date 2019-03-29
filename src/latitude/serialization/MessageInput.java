package latitude.serialization;
import java.io.*;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/

/**
 * MessageInput - streams input using inputstream
 * Protocols can be found in LocationProtocols and LatitudeProtocols
 */
public class MessageInput{

    private InputStream input;


    /**
     * Constructs a new input source from an Input Stream
     * @param in byte input source
     */
    public MessageInput(InputStream in) throws NullPointerException
    {
        if(in == null){
            throw new NullPointerException("MessageInput(): in is NULL");
        }
        input = in;
    }

    /**
     * ReadTillSpace - Reads stream char by char until there is a space
     * @return the word in the stream till space
     * @throws IOException if EOFException or read error
     */
    public String ReadTillSpace() throws IOException{
        boolean test = false;
        String word = "";
        int i = 0;
        while(!test){

            int x;
            char c;

            byte b[] = new byte[1];
            if(( (x = input.read(b, i, 1)) == -1)){
                throw new EOFException("EOF- end of stream exception: ReadTillSpace");
            }
            c = (char)b[0];
            if(c == '\0'){
                throw new EOFException("EOF- end of stream exception: ReadTillSpace");
            }
            if(c == ' '){
                test = true;
            }
            else{
                word += c;
            }
        }
        return word;
    }
    /**
     * ReadTillN - Reads stream char by char until reach N
     * @param n is number that will be read too
     * @return the word in the stream till reach N
     * @throws IOException if EOFException or read error
     */
    public String ReadTillN(int n) throws IOException{
        String word = "";
        int j = 0;
        for(int i = 0; i < n; i++){
            char c;
            int x;
            byte b[] = new byte[1];
            if(( (x = input.read(b, j, 1)) == -1)){
                throw new EOFException("EOF- end of stream exception: ReadTillN");
            }
            c = (char)b[0];
            if(c == '\0'){
                throw new EOFException("EOF- end of stream exception: ReadTillN");
            }
            word += c;
        }
        return word;
    }

    /**
     * checkEOS - Checks to make sure the delimeter is there for LM protocols
     * @throws IOException - if ReadTillN throws an IOException
     * @throws ValidationException - if LM does not contain the delimeter
     */
    public void checkEOS() throws IOException, ValidationException{
        String r = ReadTillN(LatitudeProtocols.LMdelimeter.length());
        if(r.toCharArray()[0] != LatitudeProtocols.LMdelimeter.charAt(0)){
            throw new ValidationException("Incorrect", "delimiter protocol");
        }
        if(r.toCharArray()[1] != LatitudeProtocols.LMdelimeter.charAt(1)){
            throw new ValidationException("Incorrect", "delimeter protocol");
        }


    }
}
