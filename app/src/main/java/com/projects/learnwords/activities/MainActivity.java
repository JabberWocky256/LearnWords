package com.projects.learnwords.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.projects.learnwords.app.DbControl;
import com.projects.learnwords.app.DictionaryRow;
import com.projects.learnwords.app.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;


public class MainActivity extends Activity {

    private SharedPreferences savedNamesLearned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnStart = (Button) findViewById(R.id.btnStart);
        final Button btnDownloadDictionary = (Button) findViewById(R.id.btnDownloadDictionary);
        final Button btnCreateDictionary = (Button) findViewById(R.id.btnCreateDictionary);

        savedNamesLearned = getSharedPreferences("namesLearned", MODE_PRIVATE);
        readStaticDictionary();

        btnStart.setOnClickListener(btnStartListener);
        btnDownloadDictionary.setOnClickListener(btnDownloadDictionaryListener);
        btnCreateDictionary.setOnClickListener(btnCreateNewDictionaryListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_preferences:
                callPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callPreferences(){
        Intent preferenceActivity = new Intent(MainActivity.this, PreferencesActivity.class);
        startActivity(preferenceActivity);
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
        DbControl dict = DbControl.createDbControl(getApplicationContext(), "BasicDictionary", 1);
        String[] strLines = contents.split("\n");

        DictionaryRow[] dictRows = new DictionaryRow[strLines.length];
        for(int i = 0; i<strLines.length; i++) {
            dictRows[i] = new DictionaryRow(strLines[i]);
        }

        try {
            dict.open();
            for(DictionaryRow dr: dictRows) {
                dict.insert(dr.getFirstWord(), dr.getSecondWord(), dr.getCorrectFirstWords(),
                        dr.getCorrectSecondWords(), dr.getFirstComment(), dr.getSecondComment());
            }
        } catch (SQLException e)
        {
            Log.e("CREATE_OR_OPEN_ERROR", e.toString());
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
        }finally {
            dict.close();
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void rememberDictionaryName(String newDictionaryName){
        SharedPreferences.Editor preferencesEditor = savedNamesLearned.edit();
        preferencesEditor.putBoolean(newDictionaryName, false);
        preferencesEditor.apply();
    }

    View.OnClickListener btnStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent startGameActivity = new Intent(MainActivity.this, StartGameActivity.class);
            startActivity(startGameActivity);
        }
    };

    View.OnClickListener btnDownloadDictionaryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent fileManagerActivity = new Intent(MainActivity.this,FileManagerActivity.class);
            startActivity(fileManagerActivity);
        }
    };

    View.OnClickListener btnCreateNewDictionaryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent createDictionaryActivity = new Intent(MainActivity.this, CreateDictionaryActivity.class);
            startActivity(createDictionaryActivity);
        }
    };
}
