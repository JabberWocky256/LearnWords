package com.projects.learnwords.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class EnterWordGame extends Activity {

    private String dName;
    private List<DictionaryRow> rows;
    private Iterator<DictionaryRow> enWords;
    private TextView word;
    private EditText translateWord;
    private String correctWordLanguage;
    private DictionaryRow dictRow;
    private DbControl dict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_word_game);

        final Button btnOk = (Button) findViewById(R.id.btnOk);
        word =  (TextView) findViewById(R.id.word);
        translateWord = (EditText) findViewById(R.id.translateWord);

        btnOk.setOnClickListener(btnOkListener);

        dName = getDictionaryName();
        correctWordLanguage = getCorrectWordLanguage();
        readFromDictionary(dName);
    }

    private String getDictionaryName(){
        Intent thisIntent = getIntent();

        return thisIntent.getStringExtra("dName");
    }

    private String getCorrectWordLanguage(){
        Intent thisIntent = getIntent();

        return thisIntent.getStringExtra("lang");
    }

    private void readFromDictionary(String dictName){
        dict = new DbControl(getApplicationContext(), dictName, 1);
        try {
            dict.open();
            rows = dict.readNextTenWords(correctWordLanguage);
            if(rows.size()==0) {
                dictionaryLearned();
                Intent gameIntent = new Intent(getApplicationContext(), StartGame.class);
                startActivity(gameIntent);
                finish();
                Toast.makeText(getApplicationContext(), "Все слова выучены", Toast.LENGTH_LONG).show();
                dict.close();
            } else {
                enWords = rows.iterator();
                nextWord(enWords);
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Ошибка чтения словаря", Toast.LENGTH_LONG).show();
            Log.e("Error", e.toString());
        } catch (Exception e){
            Log.e("Error in", e.toString());
        }
        dict.close();
    }

    private void nextWord(Iterator iterator){
        if(iterator.hasNext())
        {
            dictRow = (DictionaryRow)iterator.next();

            if(correctWordLanguage.equals(DbControl.CORRECT_FIRST_WORDS))
                word.setText(dictRow.getFirstWord());
            else
                word.setText(dictRow.getSecondWord());

            translateWord.setText(" ");
        } else {
            enWords = rows.iterator();
            nextWord(enWords);
        }
    }

    private void dictionaryLearned(){
        SharedPreferences savedNamesLearned = getSharedPreferences("namesLearned", MODE_PRIVATE);

        SharedPreferences.Editor preferencesEditor = savedNamesLearned.edit();
        preferencesEditor.remove(dName);
        preferencesEditor.putBoolean(dName, true);
        preferencesEditor.apply();
    }

    View.OnClickListener btnOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String secondWordUser = translateWord.getText().toString().toLowerCase();
            secondWordUser = FileManager.checkWord(secondWordUser);
            final String firstWord;
            final boolean correctWordLanguageFlag = correctWordLanguage.equals(DbControl.CORRECT_FIRST_WORDS);
            if(correctWordLanguageFlag)
                firstWord = dictRow.getSecondWord();
            else
                firstWord = dictRow.getFirstWord();

            if(firstWord.equals(secondWordUser)){
                Toast correct = Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT);
                correct.setGravity(Gravity.CENTER, 0, 0);
                correct.show();

                int trueAnswers;
                if(correctWordLanguageFlag) {
                    trueAnswers = dictRow.getCorrectFirstWords() + 1;
                    dictRow.setCorrectFirstWords(trueAnswers);
                } else {
                    trueAnswers = dictRow.getCorrectSecondWords() + 1;
                    dictRow.setCorrectSecondWords(trueAnswers);
                }

                try {
                    dict.open();
                    dict.changeCorrectFirstWords(dictRow);
                    dict.close();
                } catch (SQLException e) {
                    Toast.makeText(getApplicationContext(), "Ошибка чтения словаря", Toast.LENGTH_LONG).show();
                    Log.e("Error", e.toString());
                }

                if(trueAnswers == 4) {
                    readFromDictionary(dName);
                } else {
                    nextWord(enWords);
                }

            } else {
                Toast wrong = Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT);
                wrong.setGravity(Gravity.CENTER, 0, 0);
                wrong.show();
            }
        }
    };

}
