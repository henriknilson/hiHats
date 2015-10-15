package hihats.electricity.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import hihats.electricity.model.Bus;
import hihats.electricity.net.AccessErrorException;
import hihats.electricity.net.NoDataException;
import hihats.electricity.util.BusDataHelper;

public class RideDataService extends Service {

    public static final String TAG = RideDataService.class.getSimpleName();
    public static final String BROADCAST_ACTION = "hihats.electricity.service.RideDataService";
    private static final BusDataHelper busDataHelper = new BusDataHelper();
    private MyThread thread;
    private boolean isRunning = false;
    private Intent intent;
    private Bus bus;

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
        bus = new Bus(busDgw);
        if (!isRunning) {
            thread.start();
            isRunning = true;
        }
    }

    private void getNewData() {
        try {
            boolean isBusAtStop = busDataHelper.isBusAtStop(bus);
            String nextStop = busDataHelper.getNextStopForBus(bus);
            int totalDistance = busDataHelper.getTotalDistanceForBus(bus);
            intent.putExtra("isBusAtStop", isBusAtStop);
            intent.putExtra("nextStop", nextStop);
            intent.putExtra("totalDistance", totalDistance);
            sendBroadcast(intent);
        } catch (AccessErrorException | NoDataException e) {
            Log.d(TAG, "Network error");
        }
    }

    class MyThread extends Thread {
        static final long DELAY = 10000;
        @Override
        public void run(){
            while (isRunning){
                Log.d(TAG,"Updating ride data...");
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