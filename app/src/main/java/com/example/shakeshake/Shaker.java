package com.example.shakeshake;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Shaker {
    private SensorManager mgr=null;
    private long lastShakeTimestamp=0;
    private long startShakingTimestamp=0;
    private double howLongToShake=1d;
    private double threshold=0.1d;
    private long gap=0;
    private Shaker.Callback cb=null;

    public Shaker(Context ctxt, double threshold, long gap,
                  Shaker.Callback cb) {
        this.threshold=threshold*threshold;
        this.threshold=this.threshold
                *SensorManager.GRAVITY_EARTH
                *SensorManager.GRAVITY_EARTH;
        this.gap=gap;
        this.cb=cb;

        mgr=(SensorManager)ctxt.getSystemService(Context.SENSOR_SERVICE);
        mgr.registerListener(listener,
                mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    public void close() {
        mgr.unregisterListener(listener);
    }

    private void isShaking() {
        long now=SystemClock.uptimeMillis();

        Log.d("shaking","is shaking now!");
        if (lastShakeTimestamp==0) {
            lastShakeTimestamp=now;

            if (cb!=null) {
                //cb.shakingStarted();
                startShakingTimestamp = now;
            }
        }
        else {
            lastShakeTimestamp=now;
        }
    }

    private void isNotShaking() {
        long now=SystemClock.uptimeMillis();

        if (lastShakeTimestamp>0) {
            if (now-lastShakeTimestamp>gap) {
                lastShakeTimestamp=0;

                if (cb!=null) {
                    //cb.shakingStopped();
                    double length = now - startShakingTimestamp;
                    if(length > howLongToShake) {
                        Log.d("shaker", String.format("%s", length));
                        cb.shaked();
                    }
                }
            }
        }
    }

    public interface Callback {
        //void shakingStarted();
        //void shakingStopped();
        void shaked();
    }

    private SensorEventListener listener=new SensorEventListener() {
        public void onSensorChanged(SensorEvent e) {
            if (e.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
                double netForce=e.values[0]*e.values[0];

                netForce+=e.values[1]*e.values[1];
                netForce+=e.values[2]*e.values[2];


                if (threshold<netForce) {
                    //Log.d("Force", new Double(netForce).toString());
                    isShaking();
                }
                else {
                    isNotShaking();
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // unused
        }
    };
}