package hihats.electricity.database;

/**
 * Created by fredrikkindstrom on 29/10/15.
 */
public interface DBClient {

    void startListen(DBListener listener);
    void stopListen(DBListener listener);
    void publish(DBEvent event, Object object);
}