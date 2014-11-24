package com.projects.learnwords.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.projects.learnwords.app.DbControl;
import com.projects.learnwords.app.R;
import com.projects.learnwords.adapters.StartGameArrayAdapter;
import com.projects.learnwords.games.EnterWordGameActivity;
import com.projects.learnwords.games.PazzleWordGameActivity;
import com.projects.learnwords.games.TranslateWordGameActivity;

import java.util.Map;

public class StartGameActivity extends Activity {

    private Map<String, ?> dictionaryNames;
    private String dName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);
        dictionaryNames = getSharedPreferences("namesLearned", MODE_PRIVATE).getAll();

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        final Button btnOkEng = (Button) findViewById(R.id.btnOkEng);
        final Button btnOkRus = (Button) findViewById(R.id.btnOkRus);

        final Button btnOkEngSelect = (Button) findViewById(R.id.btnOkEngSelect);
        final Button btnOkRusSelect = (Button) findViewById(R.id.btnOkRusSelect);

        StartGameArrayAdapter gameAdapter = new StartGameArrayAdapter(this, dictionaryNames);
        spinner.setAdapter(gameAdapter);

        spinner.setOnItemSelectedListener(spinnerListener);

        btnOkEng.setOnClickListener(btnOkEngListener);
        btnOkRus.setOnClickListener(btnOkRusListener);
        btnOkEngSelect.setOnClickListener(btnOkEngSelectListener);
        btnOkRusSelect.setOnClickListener(btnOkRusSelectListener);
    }

    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            dName = dictionaryNames.keySet().toArray(new String[0])[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener btnOkEngListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent translateWordGameActivity = new Intent(getApplicationContext(), TranslateWordGameActivity.class);
            translateWordGameActivity.putExtra("dName", dName);
            translateWordGameActivity.putExtra("lang", DbControl.CORRECT_FIRST_WORDS);
            startActivity(translateWordGameActivity);
        }
    };

    private View.OnClickListener btnOkRusListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent translateWordGameActivity = new Intent(getApplicationContext(), TranslateWordGameActivity.class);
            translateWordGameActivity.putExtra("dName", dName);
            translateWordGameActivity.putExtra("lang", DbControl.CORRECT_SECOND_WORDS);
            startActivity(translateWordGameActivity);
        }
    };

    private View.OnClickListener btnOkEngSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent puzzleWordGameActivity = new Intent(getApplicationContext(), PazzleWordGameActivity.class);
            puzzleWordGameActivity.putExtra("dName", dName);
            puzzleWordGameActivity.putExtra("lang", DbControl.CORRECT_FIRST_WORDS);
            startActivity(puzzleWordGameActivity);
        }
    };

    private View.OnClickListener btnOkRusSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent puzzleWordGameActivity = new Intent(getApplicationContext(), PazzleWordGameActivity.class);
            puzzleWordGameActivity.putExtra("dName", dName);
            puzzleWordGameActivity.putExtra("lang", DbControl.CORRECT_SECOND_WORDS);
            startActivity(puzzleWordGameActivity);
        }
    };
}
