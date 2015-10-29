package hihats.electricity.util;

import android.test.ActivityInstrumentationTestCase2;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import hihats.electricity.activity.LoginActivity;
import hihats.electricity.model.ParseRide;

/**
 * Created by axel on 2015-10-08.
 */
public class ParseRideHelperTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    public ParseRideHelperTest(){
        super(LoginActivity.class);
    }

    public void testUploadAndGetRides(){

        LoginActivity activity = getActivity();

        ParseObject.registerSubclass(ParseRideHelper.class);
        Parse.initialize(activity, "w5w8u7YeyDApblFiC9XPn509REPNdFIv1SleClrR", "IxqGzyFnJUENwSpNl4vRajZaW9gPPgPdK3cArFse");

        final ParseRide ride = new ParseRide(new Date(), "SvenHultin", "ChalmersPlatsen", 10, 5, "axel");
        final ParseRideHelper helper = new ParseRideHelper();


        helper.uploadRide(
                ride.getDate(),
                ride.getBusStopFrom(),
                ride.getBusStopFrom(),
                ride.getPoints(),
                ride.getDistance(),
                ride.getOwner());

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ArrayList<ParseRide> rides = helper.getRides("axel");
                assertTrue(rides != null);
                ParseRide testRide = rides.get(1);
                assertTrue(ride.equals(testRide));
            }
        }, 2 * 60 * 100);
    }
}