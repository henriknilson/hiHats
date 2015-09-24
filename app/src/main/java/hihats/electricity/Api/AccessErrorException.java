package hihats.electricity.api;

/**
 * Created by fredrikkindstrom on 24/09/15.
 */
public class AccessErrorException extends Exception
{
    //Parameterless Constructor
    public AccessErrorException() {}

    //Constructor that accepts a message
    public AccessErrorException(String message)
    {
        super(message);
    }
}
