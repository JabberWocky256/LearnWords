package com.projects.learnwords.games;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.projects.learnwords.app.DbControl;
import com.projects.learnwords.app.IDictionaryRow;
import com.projects.learnwords.app.R;

/**
 * Created by Александр on 11.11.2014.
 */
public class TranslateWordGameActivity extends AbstractEnterGame {
    private TextView txtWord;
    private EditText txtTranslateWord;
    private Button btnAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_word_game);

        txtWord = (TextView)findViewById(R.id.word);
        txtTranslateWord = (EditText) findViewById(R.id.translateWord);
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
        });

        initialize(getApplicationContext(), getDictionaryName(), 4);

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

    @Override
    public String getLanguage() {
        Intent thisIntent = getIntent();
        language = thisIntent.getStringExtra("lang");

        return language;
    }

}
