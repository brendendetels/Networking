package latitude.app;

import latitude.serialization.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/

/**
 * LatitudeClient - client that communicates with server with specific protocol
 * Protocols can be found in CSProtocols, LatitudeProtocols, and LocationProtocols
 */
public class LatitudeClient {


    /**
     * main for LatitudeClient
     * @param args Server and Port
     * @throws IOException - if I/O error occurs
     * @throws ValidationException - if protocol is not followed
     */
    public static void main(String[] args) throws IOException, ValidationException {
        if((args.length != 2)){
            throw new IllegalArgumentException("Parameters: <Server> <Port>");
        }

        try {


        String server = args[0];
        int servPort = Integer.parseInt(args[1]);


//        String server = "192.168.56.1";
//        int servPort = 12345;


            Socket socket = new Socket(server, servPort);
            if (!socket.isConnected()) {
                System.err.println("Unable to communicate: socket is not connected");
            }
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            MessageOutput mOut = new MessageOutput(out);
            MessageInput mIn = new MessageInput(in);
            PrintStream pstream = new PrintStream(System.out);
            Scanner scan = new Scanner(System.in);
            boolean cont = true;

            while (cont) {
                if (socket.isInputShutdown()) {
                    System.err.println("Unable to communicate: input/read-half of the socket connection is closed");
                }
                if (socket.isOutputShutdown()) {
                    System.err.println("Unable to communicate: output/write-half of the socket connection is closed");
                }

                String temp = "";
                pstream.println("Operation(NEW or ALL)>");
                temp = scan.nextLine();
                if (temp.equals(LatitudeProtocols.LMProtocols[0])) {
                    pstream.println("Map ID>");
                    temp = scan.nextLine();
                    if (!CSProtocols.isNumeric(temp)) {
                        System.err.println("Invalid Message: MapID contains non numeric characters");
                    }
                    LocationRecord lr = new LocationRecord(scan, pstream);
                    long num = Long.parseLong(temp);

                    LatitudeNewLocation lnl = new LatitudeNewLocation(num, lr);
                    lnl.encode(mOut);
                    LatitudeMessage lm = LatitudeMessage.decode(mIn);
                    if (!lm.getOperation().equals(LatitudeProtocols.LMProtocols[2]) && !lm.getOperation().equals(LatitudeProtocols.LMProtocols[3])) {
                        System.err.println("Unexpected Message: " + lm.getOperation());
                    }
                    System.out.println(lm.toString());
                } else if (temp.equals(LatitudeProtocols.LMProtocols[1])) {
                    pstream.println("Map ID>");
                    temp = scan.nextLine();
                    if (!CSProtocols.isNumeric(temp)) {
                        System.err.println("Invalid Message: MapID contains non numeric characters");
                    }
                    LatitudeLocationRequest llr = new LatitudeLocationRequest(Long.parseLong(temp));
                    llr.encode(mOut);
                    LatitudeMessage lm = LatitudeMessage.decode(mIn);
                    if (!lm.getOperation().equals(LatitudeProtocols.LMProtocols[2]) && !lm.getOperation().equals(LatitudeProtocols.LMProtocols[3])) {
                        System.err.println("Unexpected Message: " + lm.getOperation());
                    }
                    System.out.println(lm.toString());
                } else {
                    // if operation does not equal
                    System.err.println("Invalid user input: Did not enter valid operation.");
                }
                pstream.print("Continue(Yes/No)> ");
                temp = scan.nextLine();
                if (temp.startsWith(CSProtocols.noprotocol[0]) || temp.startsWith(CSProtocols.noprotocol[1])) {
                    cont = false;
                }
            }
            socket.close();
        }catch(IOException ioe){
            System.err.println("Unable to communicate: Server terminated the socket due network I/O block restrictions");
        }catch(IllegalArgumentException iae){
            System.err.println("Unable to communicate: Exception in Socket()- Port parameter is outside the specified range of valid port values: 0-65535");
        }

    }

}
