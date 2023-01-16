package com.example.shakeshake;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class ShakeService extends Service
        implements Shaker.Callback {

    private Shaker shaker=null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int a, int b) {
        shaker=new Shaker(this, 1.25d, 500, this);
        Log.d("ShakeService", "ShakeService started!");

//            if(!Settings.System.canWrite(this.getApplicationContext())){
//                startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
//            }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("ShakerDemo", "Service dead :( !");
        super.onDestroy();

        shaker.close();
    }

    public void shaked() {
        Log.d("ShakeService", "shaked");

        if(!Settings.System.canWrite(this)){
            startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
        }

        boolean isEnabled = Settings.System.getInt(
                getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;

        Log.d("ShakerDemo", new Boolean(isEnabled).toString());
        // toggle airplane mode
        Settings.System.putInt(
                getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, isEnabled ? 0 : 1);
    }
//
//    public void shakingStarted() {
//        Log.d("ShakerDemo", "Shaking started!");
//    }
//
//    public void shakingStopped() {
//        Log.d("ShakerDemo", "Shaking stopped!");
//    }
}
