package hihats.electricity.database;

/**
 * Created by fredrikkindstrom on 29/10/15.
 */
public interface DBListener {

    void onDBAction(DBEvent event, Object object);
}