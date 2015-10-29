package hihats.electricity.database;
import java.util.List;

/**
 * Created by filip on 29/10/15.
 */
public interface IDataHandler {
    interface Callback<T extends Object> {
        void callback(List<T> data);
    }
    void getRides(Callback callback);
    void getBusStops(Callback callback);
    void getDeals(Callback callback);
    void save(Object o);
}
