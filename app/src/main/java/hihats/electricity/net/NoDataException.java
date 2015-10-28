package hihats.electricity.net;

/**
 * Created by fredrikkindstrom on 24/09/15.
 */
public class NoDataException extends Exception {

    // No parameter Constructor
    public NoDataException() {}

    // Constructor that accepts a message
    public NoDataException(String message) {
        super(message);
    }
}
