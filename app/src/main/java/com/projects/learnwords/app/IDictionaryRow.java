package com.projects.learnwords.app;

/**
 * Created by Александр on 04.11.2014.
 */
public interface IDictionaryRow {
    public int getId();

    public void setId(int id);

    public String getFirstWord();

    public void setFirstWord(String firstWord);

    public String getSecondWord();

    public void setSecondWord(String secondWord);

    public int getCorrectFirstWords();

    public void setCorrectFirstWords(int correctFirstWords);

    public int getCorrectSecondWords();

    public void setCorrectSecondWords(int correctSecondWords);

    public String getSecondComment();

    public void setSecondComment(String secondComment);

    public String getFirstComment();

    public void setFirstComment(String firstComment);
}
