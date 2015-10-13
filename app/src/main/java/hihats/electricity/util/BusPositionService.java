package hihats.electricity.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BusPositionService extends Service {

    private static String TAG = BusPositionService.class.getSimpleName();
    private ServiceThread serviceThread;
    private BusDataHelper busDataHelper = new BusDataHelper();
    public boolean isRunning = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        serviceThread = new ServiceThread();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if(!isRunning){
            serviceThread.interrupt();
            serviceThread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
        if (!isRunning){
            serviceThread.start();
            isRunning = true;
        }
    }

    public void readBusPosition() {

    }

    class ServiceThread extends Thread {
        static final long DELAY = 3000;
        @Override
        public void run(){
            while(isRunning){
                Log.d(TAG,"Running");
                try {
                    readBusPosition();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }

}