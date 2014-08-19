package com.projects.learnwords.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.*;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnStart = (Button) findViewById(R.id.btnStart);
        final Button btnDownloadDictionary = (Button) findViewById(R.id.btnDownload);
        //final Button btnSettings = (Button) findViewById(R.id.btnSettings);

        btnStart.setOnClickListener(btnStartListener);
        btnDownloadDictionary.setOnClickListener(btnDownloadDictionaryListener);
    }

    View.OnClickListener btnStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent startGameAcitvity = new Intent(MainActivity.this, StartGame.class);
            startActivity(startGameAcitvity);
        }
    };

    View.OnClickListener btnDownloadDictionaryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent fileManagerAcivity = new Intent(MainActivity.this,FileManager.class);
            startActivity(fileManagerAcivity);
        }
    };
}
