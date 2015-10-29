package hihats.electricity.database;
import java.util.List;

/**
 * Created by filip on 29/10/15.
 */
public interface IDataHandler {
    interface Callback<T extends Object> {
        void callback(List<T> data);
    }

    /**
     * Gets all rides from the database and runs the defined callback with the data.
     * @param callback
     */
    void getRides(Callback callback);

    /**
     * Gets all bus stops from the database and runs the defined callback with the data.
     * @param callback
     */
    void getBusStops(Callback callback);

    /**
     * Gets all deals from the database and runs the defined callback with the data.
     * @param callback
     */
    void getDeals(Callback callback);

    /**
     * Tries to store an object in the database.
     */
    void save(Object o);
}
