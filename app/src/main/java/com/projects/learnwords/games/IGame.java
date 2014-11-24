package com.projects.learnwords.games;

import android.content.Context;
import com.projects.learnwords.app.Dictionary;
import com.projects.learnwords.app.IDictionaryRow;

/**
 * Created by Александр on 29.10.2014.
 */
public interface IGame {
    public void initialize(Context context, String dictName, int repeat);
    public String getLanguage();
    public boolean check(String userWord);
    public Dictionary readWordsFromBD();
    public IDictionaryRow getNextWord();
    public void setUI(IDictionaryRow row);
    public boolean checkWord(String text);
    public void rightToBD();
    public void setLanguage(final String LANGUAGE);
}
