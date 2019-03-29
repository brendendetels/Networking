package latitude.app;

import latitude.serialization.*;
import mapservice.Location;
import mapservice.MapBoxObserver;
import mapservice.MapManager;
import mapservice.MemoryMapManager;
import notifi.app.NoTiFiClient;

import java.io.*;
import java.net.*;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import notifi.app.NoTiFiServer;
import notifi.app.RegisterInfo;
import notifi.app.User;


/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/

/**
 * LatitudeServer - Communicates with the client with particular protocol
 * Protocols can be found in CSProtocols, LatitudeProtocols, and LocationProtocols
 */
public class LatitudeServer {

    private static long maxTime=5000L;
    private static BufferedReader br;
    private static BufferedWriter bw;
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<LocationRecord> locations = new ArrayList<>();
    private static String userfile;
    private static RegisterInfo registerInfo = new RegisterInfo();





    /**
     * Checks if a User exists in a ArrayList of Users
     * @param checkuserId the userId of the user
     * @return the index of the user or -1 if does not exist
     */
    public static int checkUserExists(Long checkuserId){
        for(int i = 0; i < users.size(); i++){
            if(checkuserId == users.get(i).getUserId()){
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if a User has already made a LocationRecord. Removes it if they have
     * @param userId the userId of the user
     */
    public static int checkUserHasLocation(long userId){
        for(int i = 0; i < locations.size(); i++){
            if(locations.get(i).getUserId() == userId){

                //locations.remove(i);
                return i;
            }
        }
        return -1;
    }

    /**
     * Inputs all the users from the file in the protocol "usersFile"
     * @return A List of all the current Users
     * @throws NullPointerException - if the file name is null
     * @throws IOException - if I/O error when reading data
     */
    public static ArrayList<User> InputAllUsers() throws NullPointerException, IOException{
        try {
            File file = new File(userfile);
            ArrayList<User> users = new ArrayList<>();

            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(":");
                User tmp = new User(Long.parseLong(split[0]), split[1], split[2]);
                users.add(tmp);
            }
            br.close();
            return users;
        }catch(FileNotFoundException fnfe){
            throw new FileNotFoundException("Could not find the file at: " + userfile);
        }
    }






    /**
     * Is The LatitudeMessageProtocol - will send/receive information with LatitudeClient
     * Protocols can be found in CSProtocols, LatitudeProtocols, and LocationProtocols
     */
    public static class LatitudeMessageProtocol implements Runnable{
        private Socket clntSock;
        private Logger logger;
        private MapManager memMapManager;

        /**
         * Constructs a LatitudeMessage Protocol that will communicate with 1 client
         * @param clntSock is the clients socket that will be communicated with
         * @param logger is the logger that keeps information on the logger
         * @param memMapManager is the MapManager that updates Locations on the HTML
         */
        public LatitudeMessageProtocol(Socket clntSock, Logger logger, MapManager memMapManager){
            this.clntSock = clntSock;
            this.logger = logger;
            this.memMapManager = memMapManager;
        }

        /**
         * HandleLatitudeClient - will do the communicating with the Client
         * @param clntSock is the clients socket that will be communicated with
         * @param logger is the logger that keeps information on the logger
         * @param memMapManager is the MapManager that updates Locations on the HTML
         * @throws ValidationException if there is an error in the protocol
         */
        public void handleLatitudeClient(Socket clntSock, Logger logger, MapManager memMapManager) throws ValidationException{
            try{
                memMapManager.register(new MapBoxObserver(CSProtocols.markLocationFile, memMapManager));


                users = InputAllUsers();

                System.out.print("Handling client <" + clntSock.getInetAddress() + ">-<" + clntSock.getPort() + "> with thread id <" + Thread.currentThread().getId() + ">\n");
                logger.info("Handling client <" + clntSock.getInetAddress() + ">-<" + clntSock.getPort() + "> with thread id <" + Thread.currentThread().getId() + ">\n");

                InputStream in = clntSock.getInputStream();
                OutputStream out = clntSock.getOutputStream();

                MessageInput mIn = new MessageInput(in);
                MessageOutput mOut = new MessageOutput(out);



                while(!clntSock.isClosed()) {
                    LatitudeMessage lm = LatitudeMessage.decode(mIn);
                    //if it is a NEW operation
                    if (lm.getOperation() == LatitudeProtocols.LMProtocols[0]) {
                        LatitudeNewLocation lnl = (LatitudeNewLocation) lm;

                        if (lnl.getMapId() != CSProtocols.currentlyUsedMapID) {
                            LatitudeError le = new LatitudeError(lnl.getMapId(), CSProtocols.badmap + lnl.getMapId());
                            logger.info(clntSock.getInetAddress() + " - " + clntSock.getPort() + ": " + le.toString() + "\n");
                            le.encode(mOut);
                        }
                        else {
                            int ndx = checkUserExists(lnl.getLocationRecord().getUserId());
                            //check if user exists
                            if (ndx == -1) {
                                LatitudeError le = new LatitudeError(lnl.getMapId(), CSProtocols.baduser + lnl.getLocationRecord().getUserId());
                                logger.info(clntSock.getInetAddress() + " - " + clntSock.getPort() + ": " + le.toString() + "\n");
                                le.encode(mOut);
                            } else {
                                String locname = lnl.getLocationRecord().getLocationName();
                                lnl.getLocationRecord().setLocationName(users.get(ndx).getUsername() + ": " + locname);
                                Location temp = new Location(lnl.getLocationRecord().getLocationName(), lnl.getLocationRecord().getLongitude(), lnl.getLocationRecord().getLatitude(), lnl.getLocationRecord().getLocationDescription(), Location.Color.RED);
                                memMapManager.addLocation(temp);



                                int userLocationNdx = checkUserHasLocation(lnl.getLocationRecord().getUserId()); // this deletes it
                                if(userLocationNdx != -1){//then user has location at userLocationNdx
                                    LocationRecord lr = locations.get(userLocationNdx);
                                    locations.remove(userLocationNdx);
                                    Thread deletionexecutor = new Thread(new NoTiFiServer.SendLocationDeletionUpdates(registerInfo, lr));
                                    deletionexecutor.start();
                                }


                                Thread additionexecutor = new Thread(new NoTiFiServer.SendLocationAdditionUpdates(registerInfo, lnl.getLocationRecord()));
                                additionexecutor.start();

                                locations.add(lnl.getLocationRecord());
                                logger.info(clntSock.getInetAddress() + " - " + clntSock.getPort() + ": " + lnl.toString() + "\n");
                                LatitudeLocationResponse llr = new LatitudeLocationResponse(lnl.getMapId(), CSProtocols.mapName );
                                for (int i = 0; i < locations.size(); i++) {
                                    llr.addLocationRecord(locations.get(i));
                                }
                                logger.info(clntSock.getInetAddress() + " - " + clntSock.getPort() + ": " + llr.toString() + "\n");
                                llr.encode(mOut);
                            }
                        }


                    }
                    //if it is a ALL operation
                    else if (lm.getOperation() == LatitudeProtocols.LMProtocols[1]) {
                        LatitudeLocationRequest lnr = (LatitudeLocationRequest) lm;
                        //if incorrect mapId
                        if (lnr.getMapId() != CSProtocols.currentlyUsedMapID) {
                            LatitudeError le = new LatitudeError(lnr.getMapId(), CSProtocols.badmap + lnr.getMapId());
                            logger.info(clntSock.getInetAddress() + " - " + clntSock.getPort() + ": " + le.toString() + "\n");
                            le.encode(mOut);
                        }
                        else {
                            LatitudeLocationResponse llr = new LatitudeLocationResponse(lnr.getMapId(), CSProtocols.mapName);
                            for (int i = 0; i < locations.size(); i++) {
                                llr.addLocationRecord(locations.get(i));
                            }
                            llr.encode(mOut);
                        }
                    }
                    //Receive a Response
                    else if (lm.getOperation() == LatitudeProtocols.LMProtocols[2]) {
                        LatitudeLocationResponse llr = (LatitudeLocationResponse) lm;
                        LatitudeError le = new LatitudeError(llr.getMapId(), "Unexpected Message Type: " + llr.getOperation());
                        logger.info(clntSock.getInetAddress() + " - " + clntSock.getPort() + ": " + le.toString() + "\n");
                        le.encode(mOut);
                    }
                    //receive an error
                    else if (lm.getOperation() == LatitudeProtocols.LMProtocols[3]) {
                        LatitudeError le = (LatitudeError) lm;
                        LatitudeError le2 = new LatitudeError(le.getMapId(), "Unexpected Message Type: " + le.getOperation());
                        logger.info(clntSock.getInetAddress() + " - " + clntSock.getPort() + ": " + le2.toString() + "\n");
                        le2.encode(mOut);
                    } else { //received unknown operation
                        LatitudeError le = new LatitudeError(lm.getMapId(), "Unknown Operation: " + lm.getOperation());
                        logger.info(clntSock.getInetAddress() + " - " + clntSock.getPort() + ": " + le.toString() + "\n");
                        le.encode(mOut);
                    }

                    clntSock.setSoTimeout(CSProtocols.networkiorestriction);

                }

                clntSock.close();

            }catch(SocketTimeoutException ste){
                logger.info("Client Timed Out <" + clntSock.getInetAddress() + ">-<" + clntSock.getPort() + "> with thread id <" + Thread.currentThread().getId() + ">\n");
                try{
                    clntSock.close();
                }catch(IOException e){
                    logger.info("Client Terminated Connection <" + clntSock.getInetAddress() + ">-<" + clntSock.getPort() + "> with thread id <" + Thread.currentThread().getId() + ">\n");
                }

            }
            catch(EOFException eof){

                //System.err.print("Client Closed Connection <" + clntSock.getInetAddress() + ">-<" + clntSock.getPort() + "> with thread id <" + Thread.currentThread().getId() + ">\n");
                logger.info("Client Closed Connection <" + clntSock.getInetAddress() + ">-<" + clntSock.getPort() + "> with thread id <" + Thread.currentThread().getId() + ">\n");
            }
            catch(SocketException se){
                //System.err.print("Client Terminated Connection <" + clntSock.getInetAddress() + ">-<" + clntSock.getPort() + "> with thread id <" + Thread.currentThread().getId() + ">\n");
                logger.info("Client Terminated Connection <" + clntSock.getInetAddress() + ">-<" + clntSock.getPort() + "> with thread id <" + Thread.currentThread().getId() + ">\n");
            }
            catch(IOException ex){
                logger.log(Level.WARNING, clntSock.getInetAddress() + " - " + clntSock.getPort() + ": IOException: I/O error occured" + "\n", ex);
            }
        }

        /**
         * Will run when thread calls it
         */
        public void run() {
            try {
                handleLatitudeClient(clntSock, logger, memMapManager);
            }catch(ValidationException ve){
                System.err.println("Validation Exception Error");
                logger.log(Level.SEVERE, clntSock.getInetAddress() + " - " + clntSock.getPort() + ": ValidationException: Problem with the Protocol, User Input, or Client" + "\n");
            }
        }
    }


    /**
     * Main Function
     * @param args params: port, # threads, name of pasword file
     * @throws NullPointerException
     * @throws IOException
     */
    public static void main(String[] args) throws NullPointerException , IOException{
        if(args.length < 2  || args.length > 3  || !CSProtocols.isNumeric(args[0]) || !CSProtocols.isNumeric(args[1])){
            throw new IllegalArgumentException("Parameter(s): <Port> <# Threads in Thread Pool> <Name of the password file");
        }


        int servPort = Integer.parseInt(args[0]);
        int threadPoolCount = Integer.parseInt(args[1]);
        userfile = args[2];


//        int servPort = 12345;
//        int threadPoolCount = 5;
//        userfile = "password.txt";

        Path path = FileSystems.getDefault().getPath(CSProtocols.logFile);

        Logger logger = Logger.getLogger(CSProtocols.loggerInfo); //throws NullPtrException
        FileHandler fh = new FileHandler(path.toString(), CSProtocols.appendLogs);
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        MapManager memoryMapManager = new MemoryMapManager();


        Thread executor = new Thread(new NoTiFiServer.NoTiFiServerRegister(servPort, registerInfo, logger));
        executor.start();


        try {
            ServerSocket servSocket = new ServerSocket(servPort); //throws IllegalArgumentException, IOException, and BindException
                try{
                    servSocket.setReuseAddress(true); //throws SocketException
                    ThreadPoolExecutor service = new ThreadPoolExecutor(threadPoolCount-1, threadPoolCount, maxTime ,TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()); //Executor Throws: RejectedExecutionException, NullPtrException
                                                                                        //Executors.newFixedThreadPool throws: IllegalArguementException
                    while(true){
                        Socket clntSock = servSocket.accept(); //throws IOException, SocketTimeoutException, IllegalBlockingModeException


                        service.execute(new LatitudeMessageProtocol(clntSock, logger, memoryMapManager)); //RejectedExecutionException
                    }
                }
                catch(SocketTimeoutException ste){
                    logger.log(Level.SEVERE, "ServerSocket.accept(): a timeout was previously set with setSoTimeout and the timeout has been reached." + "\n", ste);
                    //System.err.println("ServerSocket.accept(): a timeout was previously set with setSoTimeout and the timeout has been reached." + "\n");
                }
                catch(SocketException se){
                    logger.log(Level.SEVERE, "Exception in ServerSocket.setReuseAddress(): error occured enabling or disabling the SO_RESUEADDR socket option, or the socket is closed." + "\n", se);
                    //System.err.println("Exception in ServerSocket.setReuseAddress(): error occured enabling or disabling the SO_RESUEADDR socket option, or the socket is closed.");
                }
                catch(IllegalArgumentException iae){
                    logger.log(Level.SEVERE, "Executors.newFixedThreadPool(nThreads): nThreads<=0" + "\n", iae);
                    //System.err.println("Executors.newFixedThreadPool(nThreads): nThreads<=0");
                }
                catch(IOException ex){
                    logger.log(Level.SEVERE, "ServerSocket.accept(): an I/O error occured when waiting for a connection or when starting up" + "\n", ex);
                    //System.err.println( "ServerSocket.accept(): an I/O error occured when waiting for a connection or when starting up" );
                }
                catch(IllegalBlockingModeException ibme){
                    logger.log(Level.SEVERE, "ServerSocket.accept(): this socket has an associated channel, the channel is in non-blocking mode, and there is no connection ready to be accepted" + "\n", ibme);
                    //System.err.println("ServerSocket.accept(): this socket has an associated channel, the channel is in non-blocking mode, and there is no connection ready to be accepted");
                }
                catch(RejectedExecutionException ree){
                    logger.log(Level.SEVERE, "Executor.execute(): The Task cannot be accepted for execution" + "\n", ree);
                    //System.err.println("Executor(): The Task cannot be accepted for execution");
                }
        }
        catch(BindException be){
            logger.log(Level.SEVERE, "Exception in ServerSocket(): Port already in use" + "\n", be);
            //System.err.println("Exception in ServerSocket: Port already in use");
        }
        catch(IllegalArgumentException iae){
            logger.log(Level.SEVERE, "Exception in ServerSocket(): Port parameter is outside the specified range of valid port values: 0-65535" + "\n", iae);
            //System.err.println("Exception in ServerSocket(): Port parameter is outside the specified range of valid port values: 0-65535");
        }
        catch(IOException ex){
            //System.err.println( "if an I/O error occurs when waiting for a connection or when starting up");
            logger.log(Level.SEVERE, "Exception in ServerSocket(): an I/O error occurs when waiting for a connection or when starting up" + "\n");
        }
    }


}
