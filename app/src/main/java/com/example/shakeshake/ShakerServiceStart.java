package com.example.shakeshake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShakerServiceStart extends Activity {
    private TextView transcript=null;
    private ScrollView scroll=null;

    private Button button=null;

    private ShakerServiceStart self=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        transcript=(TextView)findViewById(R.id.transcript);
        scroll=(ScrollView)findViewById(R.id.scroll);
        button=(Button) findViewById(R.id.button);
        self=this;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean isEnabled = Settings.System.getInt(
                        getContentResolver(),
                        Settings.System.ACCELEROMETER_ROTATION, 0) == 1;

                transcript.setText(transcript.getText().toString()+"Button clicked\n");
                // toggle airplane mode
                Settings.System.putInt(
                        getContentResolver(),
                        Settings.System.ACCELEROMETER_ROTATION, isEnabled ? 0 : 1);
                if(isEnabled)
                    transcript.setText(transcript.getText().toString()+"Disabled rotation\n");
                else
                    transcript.setText(transcript.getText().toString()+"Enabled rotation\n");

                // Post an intent to reload
//                Intent intent = new Intent(Intent.Action_);
//                intent.putExtra("state", !isEnabled);
//                sendBroadcast(intent);

                Intent intent2 = new Intent(self, ShakeService.class);
                self.startService(intent2);
            }

        });

        if(!Settings.System.canWrite(self.getApplicationContext())){
            startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
        }

    }

}