package com.projects.learnwords.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.*;
import java.sql.SQLException;


public class MainActivity extends Activity {

    private SharedPreferences savedNamesLearned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnStart = (Button) findViewById(R.id.btnStart);
        final Button btnDownloadDictionary = (Button) findViewById(R.id.btnDownload);

        savedNamesLearned = getSharedPreferences("namesLearned", MODE_PRIVATE);
        readStaticDictionary();

        btnStart.setOnClickListener(btnStartListener);
        btnDownloadDictionary.setOnClickListener(btnDownloadDictionaryListener);
    }

    private void readStaticDictionary(){
        if(savedNamesLearned.getAll().isEmpty()){
            String data = readFromAssets();
            writeToDictionary(data);
        }
    }

    private String readFromAssets(){
        String text = "BasicDictionary.txt";
        byte[] buffer = null;
        InputStream is;
        try {
            is = getAssets().open(text);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str_data = null;
        try {
            str_data = new String(buffer, "cp1251");
        } catch (UnsupportedEncodingException e) {
            Log.e("Encoding Error", e.toString());
        }
        return str_data;
    }

    private void writeToDictionary(String contents){
        rememberDictionaryName("BasicDictionary");
        DbControl dict = new DbControl(getApplicationContext(), "BasicDictionary", 1);
        DictionaryRow[] dictRows = FileManager.parseWords(contents);
        try {
            dict.open();
            for(DictionaryRow dr: dictRows) {
                dict.insert(dr.getFirstWord(), dr.getSecondWord(), dr.getCorrectFirstWords(), dr.getCorrectSecondWords());
            }
        } catch (SQLException e)
        {
            Log.e("CREATE_OR_OPEN_ERROR", e.toString());
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
        }finally {
            dict.close();
        }
    }

    private void rememberDictionaryName(String newDictionaryName){
        SharedPreferences.Editor preferencesEditor = savedNamesLearned.edit();
        preferencesEditor.putBoolean(newDictionaryName, false);
        preferencesEditor.apply();
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
