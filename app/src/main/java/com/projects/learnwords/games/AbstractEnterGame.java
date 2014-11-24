package com.projects.learnwords.games;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.projects.learnwords.activities.StartGameActivity;
import com.projects.learnwords.app.DbControl;
import com.projects.learnwords.app.Dictionary;
import com.projects.learnwords.app.IDictionaryRow;

import java.sql.SQLException;

/**
 * Created by Александр on 29.10.2014.
 */
public abstract class AbstractEnterGame extends Activity implements IGame {
    private Dictionary dictionary;
    private DbControl dict;
    private Context context;
    private String dictName;
    protected String language = null;
    protected IDictionaryRow currentDictionaryRow;
    private int studyingRowIndex = 0;
    private int repeat= 0;

    @Override
    public void initialize(Context context, String dictName, int repeat){
        this.context = context;
        this.dictName = dictName;
        this.repeat = repeat;

        getLanguage();
        currentDictionaryRow = getNextWord();
        if(currentDictionaryRow == null) {
            dictionary = readWordsFromBD();
            currentDictionaryRow = getNextWord();
        }
        if(currentDictionaryRow == null)
            return; //end game u win!
        setUI(currentDictionaryRow);
    }

    @Override
    public String getLanguage() {
        return null;
        //need to override
    }

    @Override
    public boolean check(String userWord) {
        boolean rightTranslate = checkWord(userWord);

        rightToBD();
        //make some changes in currentDictionaryRow
        setUI(currentDictionaryRow);
        return rightTranslate;
    }

    @Override
    public Dictionary readWordsFromBD() {
        dict = DbControl.createDbControl(context, dictName, 1);
        try {
            dict.open();
            dictionary = new Dictionary();
            dictionary.setRows(dict.readNextTenWords(language));
            if(dictionary.getRows() == null || dictionary.getRows().size()==0) {
                //U LEARNED ALL WORDS. SUCCESS!
            }
        } catch (SQLException e) {
            Toast.makeText(context, "Ошибка чтения словаря", Toast.LENGTH_LONG).show();
            Log.e("Error", e.toString());
        } catch (Exception e){
            Log.e("Error in", e.toString());
        }
        dict.close();

        return dictionary;
    }

    @Override
    public IDictionaryRow getNextWord(){
        if(dictionary == null || dictionary.getRows().isEmpty())
            return null;

        if(studyingRowIndex == dictionary.getRows().size())
            studyingRowIndex = 0;

        IDictionaryRow row = dictionary.getRows().get(studyingRowIndex);
        if(language.equals(DbControl.CORRECT_FIRST_WORDS)){
            if(row.getCorrectFirstWords()<repeat){
                studyingRowIndex++;
                return row;
            } else {
                dictionary.getRows().remove(studyingRowIndex);
            }
        } else if(language.equals(DbControl.CORRECT_SECOND_WORDS)){
            if(row.getCorrectSecondWords()<repeat){
                studyingRowIndex++;
                return row;
            } else {
                dictionary.getRows().remove(studyingRowIndex);
            }
        }

        return null;
    }

    @Override
    public void setUI(IDictionaryRow row) {
        //override in child
    }

    @Override
    public boolean checkWord(String text) {
        if(language.equals(DbControl.CORRECT_FIRST_WORDS)){
            if(currentDictionaryRow.getSecondWord().toLowerCase().trim().equals(text.toLowerCase().trim())){
                currentDictionaryRow.setCorrectSecondWords(currentDictionaryRow.getCorrectSecondWords()+1);
                return true;
            } else {
                currentDictionaryRow.setCorrectSecondWords(currentDictionaryRow.getCorrectSecondWords()-1);
                return false;
            }
        } else if(language.equals(DbControl.CORRECT_SECOND_WORDS)){
            if(currentDictionaryRow.getFirstWord().toLowerCase().trim().equals(text.toLowerCase().trim())){
                currentDictionaryRow.setCorrectFirstWords(currentDictionaryRow.getCorrectFirstWords() + 1);
                return true;
            } else {
                currentDictionaryRow.setCorrectFirstWords(currentDictionaryRow.getCorrectFirstWords() - 1);
                return false;
            }
        }

        return false;
    }

    @Override
    public void rightToBD() {
        dict = DbControl.createDbControl(context, dictName, 1);
        try {
            dict.open();
            dict.update(currentDictionaryRow);
        } catch (SQLException e) {
            Toast.makeText(context, "Ошибка чтения словаря", Toast.LENGTH_LONG).show();
            Log.e("Error", e.toString());
        } catch (Exception e){
            Log.e("Error in", e.toString());
        }
        dict.close();
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }
}
