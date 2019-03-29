package notifi.app;

import notifi.serialization.*;

import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.net.InetAddress.getLocalHost;


/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 5
 * Class: CSI 4321
 *
 **********************************************/
public class NoTiFiClient {
    private static int ms = 3000;

    /**
     * Checks if the ACK follows the correct Message ID
     * @param ack is the NoTiFiACK
     * @param correctMsgID is expected msgID
     * @return if correct msgID
     */
    public static boolean checkACK(NoTiFiACK ack, int correctMsgID){
        if(ack.getMsgId() == correctMsgID){
            return true;
        }
        else{
            System.out.println("Unexpected Message ID");
            return false;
        }
    }

    /**
     * Print out ToString of matched NoTiFiMessage
     * @param msg received NoTiFi Message
     */
    public static void checkOtherMessage(NoTiFiMessage msg){
        if(msg.getCode() == NTFProtocols.codes.get(NTFProtocols.messageTypes[1])) {
            //location addition

            NoTiFiLocationAddition addition = (NoTiFiLocationAddition)msg;
            System.out.println(addition.toString());

        } else if (msg.getCode() == NTFProtocols.codes.get(NTFProtocols.messageTypes[2])) {
            //location deletion
            NoTiFiLocationDeletion deletion = (NoTiFiLocationDeletion)msg;
            System.out.println(deletion.toString());

        } else if (msg.getCode() == NTFProtocols.codes.get(NTFProtocols.messageTypes[4])){
            //location err
            NoTiFiError err = (NoTiFiError)msg;
            System.out.println(err.toString());
        }
        else {
            //unexpected type
            System.out.println(msg.getCode());
            System.out.println("Unexpected Message Type: Was not Error, LocationAddition, or LocationDeletion");
        }

    }

    /**
     * Deregister and Register Core Checking for Correct ACK and Handling Messages
     * @param socket is the socket for communicating with server
     * @param packet is the packet that is being sent
     * @param msgId is the expected msgID to expect from ACK
     * @return if the ACK verified the sent Register/Deregister encoded message with appropriate ACK
     */
    public static boolean DeRegWork(DatagramSocket socket, DatagramPacket packet, int msgId){
        try {
            boolean DeRegistered = false;
            while(!DeRegistered){
                socket.setSoTimeout(ms);
                byte[] byteArrRegACK = new byte[1024];
                DatagramPacket recvACK = new DatagramPacket(byteArrRegACK, byteArrRegACK.length);
                try{

                    socket.receive(recvACK);

                    NoTiFiMessage ntfimsg = NoTiFiMessage.decode(byteArrRegACK);
                    if(ntfimsg.getCode() == NTFProtocols.codes.get(NTFProtocols.messageTypes[5])){//if it is a received ACK
                        NoTiFiACK ack = (NoTiFiACK)ntfimsg;
                        DeRegistered = checkACK(ack, msgId);
                    }
                    else { //if it is another message
                        checkOtherMessage(ntfimsg);
                    }
                }/*************ATTEMPT 2 FOR ACK**********************************/
                catch(SocketTimeoutException ste){//if no ACK received first time, try sending register again
                    socket.send(packet);
                    byte[] byteArrRegACK2 = new byte[1024];
                    DatagramPacket recvRegACK2 = new DatagramPacket(byteArrRegACK, byteArrRegACK.length);
                    try{
                        socket.receive(recvRegACK2);
                        NoTiFiMessage ntfimsg2 = NoTiFiMessage.decode(byteArrRegACK2);
                        if(ntfimsg2.getCode() == NTFProtocols.codes.get(NTFProtocols.messageTypes[5])){//if it is a received ACK
                            NoTiFiACK ack2 = (NoTiFiACK)ntfimsg2;
                            DeRegistered = checkACK(ack2, msgId);
                        }
                        else { //if it is another message
                            checkOtherMessage(ntfimsg2);
                        }
                    }catch(SocketTimeoutException ste2){ //if no ACK for 2nd time, then terminate
                        System.out.println("Did not receive ACK after 6 seconds");
                        return false;
                        //socket.close(); will lead to this
                    }
                }
            }
            return DeRegistered;

        }catch(IOException ie){
            System.out.println("IOException DoRegWork");
            return false;
        }
    }

    /**
     * Class that Handles the single thread that handles everything before Deregistering
     */
    public static class CheckInput implements Runnable{
        private DatagramSocket socket;
        private InetAddress servadd;
        private int servPort;
        private InetAddress localadd;
        private Random ran;
        private int localport;

        /**
         * Constructor to initialize the Executor Thread
         * @param socket1 is the Servers Socket
         * @param localadd is the Local IP from Command Line
         * @param servadd server IP
         * @param port server port
         */
        public CheckInput(DatagramSocket socket1, InetAddress servadd, int port, InetAddress localadd){
            this.socket = socket1;
            this.servadd = servadd;
            this.servPort = port;
            this.localadd = localadd;
            this.localport = socket.getLocalPort();
            ran = new Random();
        }

        /**
         * Registering to the Server
         * @return true if registered or false if did not register
         */
        public boolean startRegUp(){
            try {

                int RegMsgId = ran.nextInt(NTFProtocols.msgIdrange[1]);
                NoTiFiRegister register = new NoTiFiRegister(RegMsgId, (Inet4Address) localadd, localport);
                byte[] data = register.encode();
                DatagramPacket regpacket = new DatagramPacket(data, data.length, servadd, servPort);

                socket.send(regpacket);

                boolean registered = DeRegWork(socket, regpacket, RegMsgId);
                return registered;
            }catch(IOException e){
                // if IO Occurs
                System.out.println("Registration IO ERROR");
                return false;
            }
        }

        /**
         * Handles everything inbetween Register and Deregistering Message
         * Receives Errors, LocationAdditions, and LocationDeletions and outputs
         */
        public void afterReg(){
            try {
                socket.setSoTimeout(0);
                while (!Thread.currentThread().isInterrupted()) { // will run until thread done
                    byte[] recvdata = new byte[1024];
                    DatagramPacket recvData = new DatagramPacket(recvdata, recvdata.length, servadd, servPort);
                    boolean message = true;
                    try{
                        socket.setSoTimeout(ms);
                        socket.receive(recvData);
                    }catch(SocketTimeoutException ste){
                        message = false;
                    }

                    if(message) {
                        try {
                            NoTiFiMessage msg = NoTiFiMessage.decode(recvdata);
                            checkOtherMessage(msg);
                        } catch (IOException badParse) {
                            System.out.println("Unable to Parse Message: Bad Decode Provided");
                        }
                    }
                }
                System.out.println("User is Quitting: Sending Deregistration");
            }catch(IOException ie){
                //System.out.println("Closed Thread");
            }
        }

        /**
         * Runnable for class. Will start the Registration to server and handles all inputs
         */
        public void run(){
            boolean registered = startRegUp();
            if(registered == false){ //if failed to register, then terminate socket
                System.out.println("Unable to Register: Failed to receive ACK with correct Message ID");
                socket.close();
            }
            else{
                System.out.println("Registration Successful with server: Received ACK with same Message ID");
                afterReg();
            }
        }
}


    /**
     * Main Function for NoTiFiClient
     * @param args is command line input
     * @throws IOException if IO Exception
     */
    public static void main(String[] args) throws IOException {
        if((args.length != 3)){
            throw new IllegalArgumentException("Not correct parameters: <Server IP> <Server Port> <Local IP Address");
        }


        InetAddress servaddress = InetAddress.getByName(args[0]);
        int servport = Integer.parseInt(args[1]);
        InetAddress localadd = InetAddress.getByName(args[2]);


//        InetAddress servaddress = InetAddress.getByName("192.168.56.1");
//        int servport = 12345;
//        String x = getLocalHost().getHostAddress().trim();
//
//        InetAddress localadd = InetAddress.getLoopbackAddress();/*InetAddress.getByName("129.62.148.52");*/



        DatagramSocket socket = new DatagramSocket();



        Thread executor = new Thread(new CheckInput(socket, servaddress, servport, localadd));
        executor.start();

        Scanner userInput = new Scanner(System.in);

        boolean userquit = false;
        while(!userquit){
            String input = userInput.nextLine();
            if(input.equals(NTFCSProtocol.inputToQuit)){
                userquit = true;
                executor.interrupt();

                //wait until that thread closes
                while(executor.isAlive()) {        }
            }
        }


        Random ran = new Random();
        int deregmsgId = ran.nextInt(NTFProtocols.msgIdrange[1]);

        NoTiFiDeregister deregister = new NoTiFiDeregister(deregmsgId,(Inet4Address)localadd, socket.getLocalPort() );
        byte[] dereg = deregister.encode();

        DatagramPacket sendDeregister = new DatagramPacket(dereg, dereg.length, servaddress, servport);
        socket.send(sendDeregister);

        boolean deregistered = DeRegWork(socket, sendDeregister, deregmsgId);
        if(!deregistered){ //if not correctly deregistered
            System.out.println("Unable to Deregister");
        }
        else{
            System.out.println("Deregistration successful with server: Received ACK with same message ID");
        }

        socket.close();
    }// end of Main
}
