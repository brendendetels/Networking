package notifi.app;

import latitude.serialization.LatitudeProtocols;
/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 6
 * Class: CSI 4321
 *
 **********************************************/


/**
 *  Represents a User with a userId, username, and password
 */
public class User{
    private String username, password;
    private long userId;

    /**
     * Constructs a class that represents a user with a userId, username, and password
     * @param userId the user ID
     * @param username the username
     * @param password the password
     */
    public User(long userId, String username, String password ){
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the userID
     * @return the userId
     */
    public Long getUserId(){
        return userId;
    }

    /**
     * Gets the Username
     * @return the username
     */
    public String getUsername(){
        return username;
    }
    /**
     * Will send this to output file for each file
     * @return UserId info to file
     */
    public String toString(){
        return userId + ":" + username +":" + password + LatitudeProtocols.LMdelimeter;
    }
}