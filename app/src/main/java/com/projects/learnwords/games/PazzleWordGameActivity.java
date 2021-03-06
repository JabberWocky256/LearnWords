package com.projects.learnwords.games;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.projects.learnwords.app.DbControl;
import com.projects.learnwords.app.IDictionaryRow;
import com.projects.learnwords.app.R;
import com.projects.learnwords.elements.ButtonsGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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

        if(!initialize(getApplicationContext(), getDictionaryName(), 4)){
            Toast.makeText(getApplicationContext(), "Все слова изучены", Toast.LENGTH_SHORT).show();
        } else {
            txtTranslateWord.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(txtTranslateWord.getText().toString().equals(getRightWord())){
                        currentDictionaryRow = getNextWord();
                        buttonsLayout = null;
                        setUI(currentDictionaryRow);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });
        }
    }

    private void setButtonGroup(){
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsSymbolLayout);

        char[] answer = getRightWord().toCharArray();

        HashSet<Character> answerHash = new HashSet<Character>();

        for (char ch : answer)
            answerHash.add(ch);
        Iterator<Character> iterator = answerHash.iterator();

        ButtonsGroup btnGroup = new ButtonsGroup(this);

        while (iterator.hasNext()) {
            final Button btnTag = new Button(this);
            btnTag.setText(iterator.next().toString());
            btnGroup.addView(btnTag);
            btnGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtTranslateWord.setText(txtTranslateWord.getText() + btnTag.getText().toString());
                }
            });
        }

        buttonsLayout.removeAllViews();
        buttonsLayout.addView(btnGroup);
        buttonsLayout.refreshDrawableState();
        buttonsLayout.clearFocus();
    }

    @Override
    public void setUI(IDictionaryRow row) {

        if(language.equals(DbControl.CORRECT_FIRST_WORDS))
            txtWord.setText(row.getFirstWord());
        else
            txtWord.setText(row.getSecondWord());

        txtTranslateWord.setText("");
        setButtonGroup();
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

