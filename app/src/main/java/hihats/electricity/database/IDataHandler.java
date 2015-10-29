package hihats.electricity.database;
import java.util.List;

/**
 * Created by filip on 29/10/15.
 */
public interface IDataHandler {
    public interface Callback<T extends Object> {
        public void callback(List<T> data);
    }
    public void getRides(Callback callback);
    public void getBusStops(Callback callback);
    public void getDeals(Callback callback);
}
