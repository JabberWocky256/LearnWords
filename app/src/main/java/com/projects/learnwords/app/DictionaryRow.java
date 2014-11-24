package com.projects.learnwords.app;


import android.annotation.TargetApi;
import android.os.Build;

import java.util.Arrays;

public class DictionaryRow implements IDictionaryRow {
    private int _id;
    private String firstWord;
    private String secondWord;
    private int correctFirstWords;
    private int correctSecondWords;
    private String firstComment;
    private String secondComment;

    public DictionaryRow(int _id, String firstWord, String secondWord, int correctFirstWords,
                         int correctSecondWords, String firstComment, String secondComment) {
        this._id = _id;
        this.firstWord = firstWord;
        this.secondWord = secondWord;
        this.correctFirstWords = correctFirstWords;
        this.correctSecondWords = correctSecondWords;
        this.firstComment = firstComment;
        this.secondComment = secondComment;
    }

    public DictionaryRow(String row) {
        parseWords(row);
    }


    private void parseWords(String strLine) {

            String words[] = strLine.split("\\|");
            if (!strLine.contains("|") || words.length != 2)
                return;

            words = concat(checkForComment(words[0]), checkForComment(words[1]));

            _id = 0;
            firstWord = words[0].trim();
            firstComment = words[1].trim();
            secondWord = words[2].trim();
            secondComment = words[3].trim();
            correctFirstWords = 0;
            correctSecondWords = 0;

    }

    private String[] checkForComment(String word){
        String[] str = null;
        if(word.contains("(") && word.contains(")")){
            str = new String[2];
            int firstSep = word.indexOf("(");
            int secondSep = word.indexOf(")");
            str[0] = word.substring(0, firstSep);
            str[1] = word.substring(firstSep+1, secondSep);
        } else {
            str = new String[] {word, ""};
        }
        return str;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private String[] concat(String[] first, String[] second) {
        String[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        DictionaryRow dictionaryRow = (DictionaryRow) o;
        if(firstWord.equals(dictionaryRow.firstWord) && secondWord.equals(dictionaryRow.secondWord))
            return true;
        else
            return false;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public void setFirstWord(String firstWord) {
        this.firstWord = firstWord;
    }

    public String getSecondWord() {
        return secondWord;
    }

    public void setSecondWord(String secondWord) {
        this.secondWord = secondWord;
    }

    public int getCorrectFirstWords() {
        return correctFirstWords;
    }

    public void setCorrectFirstWords(int correctFirstWords) {
        this.correctFirstWords = correctFirstWords;
    }

    public int getCorrectSecondWords() {
        return correctSecondWords;
    }

    public void setCorrectSecondWords(int correctSecondWords) {
        this.correctSecondWords = correctSecondWords;
    }

    public String getSecondComment() {
        return secondComment;
    }

    public void setSecondComment(String secondComment) {
        this.secondComment = secondComment;
    }

    public String getFirstComment() {
        return firstComment;
    }

    public void setFirstComment(String firstComment) {
        this.firstComment = firstComment;
    }
}
