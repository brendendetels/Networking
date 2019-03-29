package latitude.serialization;

/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 3
 * Class: CSI 4321
 *
 **********************************************/


/**
 * ValidationException is an exception that is thrown when the protocol is not followed
 * Extends Exception
 */
public class ValidationException extends Exception{

    private String invalidToken;

    /**
     * Constructs validation exception with null clause
     * @param invalidToken  token that failed validation
     * @param message  exception message
     */
    public ValidationException(String invalidToken, String message){
        super(message);
        this.invalidToken = invalidToken;
    }

    /**
     * Constructs validation exception
     * @param invalidToken  token that failes validation
     * @param message  exception message
     * @param cause  Throwable exception cause
     */
    public ValidationException(String invalidToken, String message, Throwable cause){
        super(message, cause);
        this.invalidToken = invalidToken;
    }

    /**
     * Gets the token that failed validation
     * @return invalid token of the exception
     */
    public String getInvalidToken(){
        return invalidToken;
    }
}
