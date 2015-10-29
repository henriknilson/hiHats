package hihats.electricity.net;

/**
 * This exception is thrown when the connection was successful but no
 * data could be obtained from the remote server request.
 */
public class NoDataException extends Exception {

    // No parameter Constructor
    public NoDataException() {}

    // Constructor that accepts a message
    public NoDataException(String message) {
        super(message);
    }
}
