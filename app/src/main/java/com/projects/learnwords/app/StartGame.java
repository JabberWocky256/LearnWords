package com.projects.learnwords.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Map;

public class StartGame extends Activity {

    private Map<String, ?> dicionaryNames;
    private String dName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);
        dicionaryNames = getSharedPreferences("namesLearned", MODE_PRIVATE).getAll();

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Button btnOkEng = (Button) findViewById(R.id.btnOkEng);
        final Button btnOkRus = (Button) findViewById(R.id.btnOkRus);

        StartGameArrayAdapter gameAdapter = new StartGameArrayAdapter(this, dicionaryNames);
        spinner.setAdapter(gameAdapter);

        spinner.setOnItemSelectedListener(spinnerListener);
        btnOkEng.setOnClickListener(btnOkEngListener);
        btnOkRus.setOnClickListener(btnOkRusListener);
    }

    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            dName = dicionaryNames.keySet().toArray(new String[0])[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener btnOkEngListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent enterWordGameActivity = new Intent(getApplicationContext(), EnterWordGame.class);
            enterWordGameActivity.putExtra("dName", dName);
            enterWordGameActivity.putExtra("lang", DbControl.CORRECT_FIRST_WORDS);
            startActivity(enterWordGameActivity);
        }
    };
    private View.OnClickListener btnOkRusListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent enterWordGameActivity = new Intent(getApplicationContext(), EnterWordGame.class);
            enterWordGameActivity.putExtra("dName", dName);
            enterWordGameActivity.putExtra("lang", DbControl.CORRECT_SECOND_WORDS);
            startActivity(enterWordGameActivity);
        }
    };


}
