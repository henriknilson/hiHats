package hihats.electricity.util;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

import hihats.electricity.model.BusStop;

/**
 * Created by axel on 2015-10-07.
 */
@ParseClassName("ParseRideHelper")
public class ParseRideHelper extends ParseObject {

    public ParseRideHelper(){}

    public Date getDate(){
        return getDate("date");
    }

    public String getBusStopFrom(){
        return getString("busStopFrom");
    }

    public String getBusStopToo(){
        return getString("busStopToo");
    }

    public int getPoints(){
        return getInt("points");
    }

    public String getOwner(){
        return getString("owner");}
}
