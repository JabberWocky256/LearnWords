package com.projects.learnwords.games;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.projects.learnwords.app.DbControl;
import com.projects.learnwords.app.IDictionaryRow;
import com.projects.learnwords.app.R;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Александр on 12.11.2014.
 */
public class PazzleWordGameActivity extends AbstractEnterGame{
    private TextView txtWord;
    private EditText txtTranslateWord;
    private LinearLayout buttonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_word_by_symbols);

        txtWord = (TextView)findViewById(R.id.word);
        txtTranslateWord = (EditText) findViewById(R.id.translateWord);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsSymbolLayout);

/*
        btnAnswer = (Button) findViewById(R.id.btnOk);

        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkWord(txtTranslateWord.getText().toString())) {
                    currentDictionaryRow = getNextWord();
                    setUI(currentDictionaryRow);
                    txtTranslateWord.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "WRONG!", Toast.LENGTH_SHORT).show();

                    if(language.equals(DbControl.CORRECT_FIRST_WORDS))
                        txtTranslateWord.setText(currentDictionaryRow.getSecondWord());
                    else
                        txtTranslateWord.setText(currentDictionaryRow.getFirstWord());
                }
            }
        });*/

        initialize(getApplicationContext(), getDictionaryName(), 4);

        char[] answer = getRightWord().toCharArray();

        HashSet<Character> answerHash = new HashSet<Character>();

        for(char ch:answer)
            answerHash.add(ch);
        Iterator<Character> iterator = answerHash.iterator();

        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        while(iterator.hasNext()) {
            final Button btnTag = new Button(this);
            btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btnTag.setText(iterator.next().toString());
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtTranslateWord.setText(txtTranslateWord.getText() + btnTag.getText().toString());
                }
            });
            row.addView(btnTag);
        }

        buttonsLayout.addView(row);
    }

    @Override
    public void setUI(IDictionaryRow row) {

        if(language.equals(DbControl.CORRECT_FIRST_WORDS))
            txtWord.setText(row.getFirstWord());
        else
            txtWord.setText(row.getSecondWord());

        txtTranslateWord.setText("");
    }

    private String getDictionaryName(){
        Intent thisIntent = getIntent();

        return thisIntent.getStringExtra("dName");
    }

    private String getCorrectWordLanguage(){
        Intent thisIntent = getIntent();

        return thisIntent.getStringExtra("lang");
    }

    public String getRightWord(){
        if(language.equals(DbControl.CORRECT_FIRST_WORDS)){
            return currentDictionaryRow.getFirstWord();
        } else if(language.equals(DbControl.CORRECT_SECOND_WORDS)){
            return currentDictionaryRow.getSecondWord();
        }

        return null;
    }

    @Override
    public String getLanguage() {
        Intent thisIntent = getIntent();
        this.language = thisIntent.getStringExtra("lang");

        return language;
    }
}

