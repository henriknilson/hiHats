package hihats.electricity.util;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import hihats.electricity.model.Bus;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.NoDataException;

public class BusPositionService extends Service {

    private static final String TAG = BusPositionService.class.getSimpleName();
    private static final String BROADCAST_ACTION = "hihats.electricity.util.buspositionservice";
    private static final Handler handler = new Handler();
    private static final BusDataHelper busDataHelper = new BusDataHelper();
    private Intent intent;
    private Bus bus;
    public boolean isRunning = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        String busId = intent.getStringExtra("busID");
        bus = new Bus(busId);
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000);
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        @Override
        public void run() {
            getNewData();
            handler.postDelayed(this, 5000);
        }
    };

    private void getNewData() {
        try {
            busDataHelper.updateDataForBus(bus);
        } catch (AccessErrorException | NoDataException e) {
            e.printStackTrace();
        }
        intent.putExtra("newLat", bus.getDatedPosition().getLatitude());
        intent.putExtra("newLong", bus.getDatedPosition().getLongitude());
        intent.putExtra("newBearing", bus.getBearing());
        sendBroadcast(intent);
    }
}