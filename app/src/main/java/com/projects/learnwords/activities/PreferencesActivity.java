package com.projects.learnwords.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import com.projects.learnwords.app.R;

/**
 * Created by Александр on 15.09.2014.
 */
public class PreferencesActivity extends Activity {
    private SeekBar seekBar;
    private TextView txtMessage;
    private SharedPreferences savedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_menu);

        savedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        int value;

        if(!savedPreferences.getAll().isEmpty())
            value = (Integer)savedPreferences.getAll().get("number_of_words");
        else
            value = 4;

        txtMessage = (TextView) findViewById(R.id.txtNumberOfWords);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        txtMessage.setText("Изучать слова (раз) " + value + ":");
        seekBar.setProgress(value);
        seekBar.setOnSeekBarChangeListener(changeNumbersOfWordsListener());
    }

    private SeekBar.OnSeekBarChangeListener changeNumbersOfWordsListener(){
        return new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtMessage.setText("Изучать слова (раз) " + progress + ":");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor preferencesEditor = savedPreferences.edit();
                preferencesEditor.remove("number_of_words");
                preferencesEditor.putInt("number_of_words", seekBar.getProgress());
                preferencesEditor.apply();
            }
        };
    }
}
