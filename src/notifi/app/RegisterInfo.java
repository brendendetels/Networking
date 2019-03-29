package notifi.app;

import java.net.Inet4Address;
import java.util.ArrayList;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 6
 * Class: CSI 4321
 *
 **********************************************/
public class RegisterInfo {

    private ArrayList<Registered> registeredUsers = new ArrayList<>();

    /**
     * Checks if Source IP/Port are registered already
     *
     * @param srcip   ip of source
     * @param srcPort port of source
     * @return true if already registered
     */
    public boolean checkRegisteredAlready(Inet4Address srcip, int srcPort) {
        for (int i = 0; i < registeredUsers.size(); i++) {
            if (registeredUsers.get(i).checkEqual(srcip, srcPort)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes the requested user
     * @param srcip ip to delete
     * @param srcPort port to delete
     * @return if deleted or not
     */
    public boolean deleteUserRegistered(Inet4Address srcip, int srcPort) {
        for (int i = 0; i < registeredUsers.size(); i++) {
            if (registeredUsers.get(i).checkEqual(srcip, srcPort)) {
                registeredUsers.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a client IP and port to class
     * @param ip added ip
     * @param port added port
     */
    public void addClient(Inet4Address ip, int port){
        registeredUsers.add(new Registered(ip,port));
    }


    /**
     * Gets size of ArrayList
     * @return size of registered Clients
     */
    public int registeredSize(){
        return registeredUsers.size();
    }

    /**
     * Gets IP at index i
     * @param i index to get
     * @return ip at i
     */
    public Inet4Address getIPatI(int i ){
        return registeredUsers.get(i).sourceIP;
    }


    /**
     * Gets port at index i
     * @param i index to get
     * @return port at i
     */
    public int getPortatI(int i){
        return registeredUsers.get(i).sourcePort;
    }


    /**
     * Registered class, keeps registered IP and port
     */
    public class Registered{
        private Inet4Address sourceIP;
        private int sourcePort;

        /**
         * Constructor for Registered Users
         * @param ip ip of registered user
         * @param port port of registered user
         */
        public Registered(Inet4Address ip, int port){
            sourceIP = ip;
            sourcePort = port;
        }

        /**
         * Checks if ip and port equal to current Register Info
         * @param srcip is that srcip
         * @param srcp is that srcport
         * @return true if they are equal
         */
        public boolean checkEqual(Inet4Address srcip, int srcp){
            return srcip.equals(sourceIP) && srcp == sourcePort;
        }
    }

}
