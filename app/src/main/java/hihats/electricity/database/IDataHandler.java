package hihats.electricity.database;
import java.util.List;

/**
 * This interface represents how a DataHandler should act and what it should be capable of.
 * A DataHandler handles all interaction with a remote database in the application,.
 */
public interface IDataHandler {
    interface Callback<T extends Object> {
        void callback(List<T> data);
    }

    /**
     * Gets all rides from the database and runs the defined callback with the data.
     * @param callback What should happen when the data is obtained.
     */
    void getRides(Callback callback);

    /**
     * Gets all bus stops from the database and runs the defined callback with the data.
     * @param callback What should happen when the data is obtained.
     */
    void getBusStops(Callback callback);

    /**
     * Gets all deals from the database and runs the defined callback with the data.
     * @param callback What should happen when the data is obtained.
     */
    void getDeals(Callback callback);

    /**
     * Tries to store an object in the database.
     */
    void save(Object o);
}
