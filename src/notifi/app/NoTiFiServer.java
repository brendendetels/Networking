package notifi.app;

import latitude.app.CSProtocols;
import latitude.serialization.LocationRecord;
import notifi.serialization.*;

import java.io.IOException;
import java.net.*;
import java.nio.channels.IllegalBlockingModeException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 6
 * Class: CSI 4321
 *
 **********************************************/


public class NoTiFiServer{
    //private ArrayList<RegisterInfo> registeredUsers = new ArrayList<>();

    private static DatagramSocket notifiSocket;
    private static int serverPort;
    private static RegisterInfo registeredClients;
    private static Logger logger;

    /**
     * Handles the Registration and Deregistration of NoTiFiClients
     */
    public static class NoTiFiServerRegister implements Runnable {

        /**
         * Constructor for NoTiFi(De)Register
         * @param servPort is the port of server
         * @param thatreg is the Registration Info that contains all registered users
         */
        public NoTiFiServerRegister(int servPort, RegisterInfo thatreg, Logger logs1) {
            serverPort = servPort;
            registeredClients = thatreg;
            logger = logs1;
            try {
                notifiSocket = new DatagramSocket(servPort);
            } catch (SocketException se) {
                logger.log(Level.SEVERE, "The socket could not be opened, or the socket could not bind to the specified local port." + "\n", se);
            }
        }

        /**
         * Handles NoTiFi Register and Deregister for packets sent by clients
         */
        void initiateNoTiFiMessages() {
            try {

                byte[] recNoTiFiMsg = new byte[1024];
                DatagramPacket packet = new DatagramPacket(recNoTiFiMsg, recNoTiFiMsg.length);
                notifiSocket.receive(packet);

                NoTiFiMessage msg = NoTiFiMessage.decode(recNoTiFiMsg);
                if (msg.getCode() == NTFProtocols.codes.get(NTFProtocols.messageTypes[0])) { //register
                    NoTiFiRegister reg = (NoTiFiRegister) msg;
                    logger.info("Received: " + CSProtocols.constructLogMessage(reg.toString(), (Inet4Address)packet.getAddress(), packet.getPort()));
                    if (reg.getAddress().isMulticastAddress()) { //if address is multicast
                        sendError("Bad Address: multicast", reg.getMsgId(), reg.getAddress(), reg.getPort());
                    } else if (registeredClients.checkRegisteredAlready(reg.getAddress(), reg.getPort())) { // if already registered
                        sendError("Already registered", reg.getMsgId(), reg.getAddress(), reg.getPort());
                    } else if (reg.getPort() != packet.getPort()) {
                        //a port is correct if it matches the port of the source to the received UDP packet
                        sendError("Incorrect Port", reg.getMsgId(), reg.getAddress(), reg.getPort());
                    } else { // if newly registered
                        registeredClients.addClient(reg.getAddress(), reg.getPort());
                        sendACK(reg.getMsgId(), (Inet4Address) packet.getAddress(), reg.getPort());
                    }

                } else if (msg.getCode() == NTFProtocols.codes.get(NTFProtocols.messageTypes[3])) { //if it is deregister
                    NoTiFiDeregister dereg = (NoTiFiDeregister) msg;
                    logger.info("Received: " + CSProtocols.constructLogMessage(dereg.toString(), (Inet4Address)packet.getAddress(), packet.getPort()));
                    if (registeredClients.deleteUserRegistered(dereg.getAddress(), dereg.getPort())) { //if it is signed up
                        sendACK(dereg.getMsgId(), dereg.getAddress(), dereg.getPort());
                    } else {
                        sendError("Unknown Client", dereg.getMsgId(), dereg.getAddress(), dereg.getPort());
                    }
                } else {
                    sendError("Unexpected Message Type", msg.getMsgId(), (Inet4Address) packet.getAddress(), packet.getPort());
                }


            } catch (PortUnreachableException port) {
                //may be thrown if the socket is connected to a currently unreachable destination. Note, there is no guarantee that the exception will be thrown.
                logger.log(Level.SEVERE, "may be thrown if the socket is connected to a currently unreachable destination. Note, there is no guarantee that the exception will be thrown." + "\n", port);
            } catch (IllegalBlockingModeException block) {
                //if this socket has an associated channel, and the channel is in non-blocking mode.
                logger.log(Level.SEVERE, "if this socket has an associated channel, and the channel is in non-blocking mode." + "\n", block);
            } catch (IOException ioe) {
                //if IO occurs
                logger.log(Level.SEVERE, "if IO Occured: Message not valid(possibly Code)" + "\n", ioe);
            }

        }

        /**
         * Sends a NoTiFi Error
         * @param Errormsg message of error
         * @param msgId message id of error
         * @param ip to be sent too
         * @param port to be sent too
         */
        void sendError(String Errormsg, int msgId, Inet4Address ip, int port) {
            try {

                NoTiFiError err = new NoTiFiError(msgId, Errormsg);
                logger.info("Sending: " + CSProtocols.constructLogMessage(err.toString(), ip, port));
                byte[] errencode = err.encode();
                DatagramPacket sendError = new DatagramPacket(errencode, errencode.length, ip, port);
                notifiSocket.send(sendError);
            } catch (IOException io) {
                logger.log(Level.SEVERE, "Error sending a NoTiFi Error message" + "\n", io);
            }
        }

        /**
         * Sends a NoTiFi Acknowledgement
         * @param msgId msgId to send it too
         * @param ip to be sent too
         * @param port to be sent too
         */
        void sendACK(int msgId, Inet4Address ip, int port) {
            try {
                NoTiFiACK ack = new NoTiFiACK(msgId);
                logger.info("Sending: " + CSProtocols.constructLogMessage(ack.toString(), ip, port));

                byte[] ackencode = ack.encode();
                DatagramPacket sendACK = new DatagramPacket(ackencode, ackencode.length, ip, port);
                notifiSocket.send(sendACK);
            } catch (IOException io) {
                //sending error
                logger.log(Level.SEVERE, "Error sending a NoTiFi ACK message" + "\n", io);
            }
        }

        /**
         * Runs the NoTiFi Register and Deregistration of Clients
         */
        public void run() {
            while (true) {
                initiateNoTiFiMessages();
            }
        }
    }

    /**
     * Sends NoTiFi Location Addition updates to all registered clients
     */
    public static class SendLocationAdditionUpdates implements Runnable{
            private static LocationRecord locrecord;
            private static RegisterInfo registered;
            private static Random ran;

        /**
         * Constructor for NoTiFiLocation Addition
         * @param registered1 all registered users
         * @param locrec1 LocationRecord to notify them about
         */
        public SendLocationAdditionUpdates(RegisterInfo registered1, LocationRecord locrec1){
                registered = registered1;
                locrecord = locrec1;
                ran = new Random();
        }

        /**
         * Sending the NoTiFiLocationAddition to registered clients
         */
        void sendNoTiFiAddition(){
                NoTiFiLocation location = new NoTiFiLocation(locrecord.getUserId(), Double.parseDouble(locrecord.getLongitude()), Double.parseDouble(locrecord.getLatitude()), locrecord.getLocationName(), locrecord.getLocationName());
                int ranmsgId = ran.nextInt(NTFProtocols.msgIdrange[1]);
                NoTiFiLocationAddition locAdd = new NoTiFiLocationAddition(ranmsgId, location);
                byte[] locAddit = locAdd.encode();

                for(int i = 0; i < registered.registeredSize(); i++){
                    logger.info("Sending: " + CSProtocols.constructLogMessage(locAdd.toString(), registered.getIPatI(i), registered.getPortatI(i)));

                    DatagramPacket packet = new DatagramPacket(locAddit, locAddit.length, registered.getIPatI(i), registered.getPortatI(i));
                    try {
                        notifiSocket.send(packet);
                    }catch(IOException io){
                        logger.log(Level.SEVERE, "Error sending a NoTiFi Addition message" + "\n", io);
                    }
                }

            }

        /**
         * Runnable used to send NoTiFi Location Additions
         */
        public void run(){
                sendNoTiFiAddition();
            }
        }

    /**
     * Sends NoTiFi Location Deletion Updates to registered clients
     */
    public static class SendLocationDeletionUpdates implements Runnable{
            private static LocationRecord locrecord;
            private static RegisterInfo registered;
            private static Random ran;

        /**
         * Constructor to initialize all registered clients and location record
         * @param registered1 all registered clients
         * @param locrec1 location record to be sent that was deleted
         */
            public SendLocationDeletionUpdates(RegisterInfo registered1, LocationRecord locrec1){
                registered = registered1;
                locrecord = locrec1;
                ran = new Random();
            }

        /**
         * Sends a Notification about deleted location
         */
        void sendNoTiFiDeletion(){
                NoTiFiLocation location = new NoTiFiLocation(locrecord.getUserId(), Double.parseDouble(locrecord.getLongitude()), Double.parseDouble(locrecord.getLatitude()), locrecord.getLocationName(), locrecord.getLocationName());
                int ranmsgId = ran.nextInt(NTFProtocols.msgIdrange[1]);
                NoTiFiLocationDeletion locDel = new NoTiFiLocationDeletion(ranmsgId, location);
                byte[] locDelenc = locDel.encode();
                for(int i = 0; i < registered.registeredSize(); i++){
                    logger.info("Sending: " + CSProtocols.constructLogMessage(locDel.toString(), registered.getIPatI(i), registered.getPortatI(i)));
                    DatagramPacket packet = new DatagramPacket(locDelenc, locDelenc.length, registered.getIPatI(i), registered.getPortatI(i));

                    try {
                        notifiSocket.send(packet);
                    }catch(IOException io){
                        logger.log(Level.SEVERE, "Error sending a NoTiFi Location Deletion message" + "\n", io);
                    }
                }
            }

        /**
         * Runnable that sends the NoTiFi Deletion
         */
        public void run(){
                sendNoTiFiDeletion();
            }
        }

}
