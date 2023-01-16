package com.example.shakeshake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.net.URISyntaxException;

public class ShakerDemo extends Activity
        implements Shaker.Callback {
    private Shaker shaker=null;
    private TextView transcript=null;
    private ScrollView scroll=null;

    private Button button=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        transcript=(TextView)findViewById(R.id.transcript);
        scroll=(ScrollView)findViewById(R.id.scroll);
        button=(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                transcript.setText(transcript.getText().toString()+"Button clicked\n");

                boolean isEnabled = Settings.System.getInt(
                        getContentResolver(),
                        Settings.System.ACCELEROMETER_ROTATION, 0) == 1;

                // toggle airplane mode
                Settings.System.putInt(
                        getContentResolver(),
                        Settings.System.ACCELEROMETER_ROTATION, isEnabled ? 0 : 1);

                // Post an intent to reload
//                Intent intent = new Intent(Intent.Action_);
//                intent.putExtra("state", !isEnabled);
//                sendBroadcast(intent);

            }
        });

        shaker=new Shaker(this, 1.25d, 500, this);

        if(!Settings.System.canWrite(this.getApplicationContext())){
            startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        shaker.close();
    }

//    public void shakingStarted() {
//        Log.d("ShakerDemo", "Shaking started!");
//        transcript.setText(transcript.getText().toString()+"Shaking started\n");
//        scroll.fullScroll(View.FOCUS_DOWN);
//    }

//    public void shakingStopped() {
//        Log.d("ShakerDemo", "Shaking stopped!");
//        transcript.setText(transcript.getText().toString()+"Shaking stopped\n");
//        scroll.fullScroll(View.FOCUS_DOWN);
//    }

    public void shaked() {
        Log.d("ShakerDemo", "Shaked");
    }
}