package hihats.electricity.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import hihats.electricity.model.ParseBusFactory;
import hihats.electricity.model.IBus;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.NoDataException;
import hihats.electricity.util.BusDataHelper;

public class BusPositionService extends Service {

    public static final String TAG = BusPositionService.class.getSimpleName();
    public static final String BROADCAST_ACTION = "hihats.electricity.service.BusPositionService";

    private static final BusDataHelper busDataHelper = new BusDataHelper();

    private MyThread thread;
    private boolean isRunning = false;
    private Intent intent;
    private IBus bus;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        thread = new MyThread();
    }

    @Override
    public synchronized void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        String busDgw = intent.getStringExtra("busDgw");
        bus = ParseBusFactory.getInstance().getBus(busDgw);
        if (!isRunning) {
            thread.start();
            isRunning = true;
        }
    }

    private void getNewData() {
        try {
            busDataHelper.updateDataForBus(bus);
            intent.putExtra("newTime", bus.getDatedPosition().getDate().getTime());
            intent.putExtra("newLat", bus.getDatedPosition().getLatitude());
            intent.putExtra("newLong", bus.getDatedPosition().getLongitude());
            intent.putExtra("newBearing", bus.getBearing());
            sendBroadcast(intent);
        } catch (AccessErrorException | NoDataException e) {
            Log.d(TAG, "Network error");
        }
    }

    class MyThread extends Thread {
        static final long DELAY = 5000;
        @Override
        public void run(){
            while (isRunning){
                Log.d(TAG,"Updating bus position...");
                try {
                    getNewData();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }
}