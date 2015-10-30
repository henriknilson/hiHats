package hihats.electricity.net;

/**
 * This exception is thrown when there is a problem with the
 * actual http connection to a remote server.
 */
public class AccessErrorException extends Exception {

    // No parameter constructor
    public AccessErrorException() {}

    // Constructor that accepts a message
    public AccessErrorException(String message) {
        super(message);
    }
}
