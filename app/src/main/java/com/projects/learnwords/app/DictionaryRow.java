package com.projects.learnwords.app;


public class DictionaryRow {
    private int _id;
    private String firstWord;
    private String secondWord;
    private int correctFirstWords;
    private int correctSecondWords;

    public DictionaryRow(int _id, String firstWord, String secondWord, int correctFirstWords, int correctSecondWords) {
        this._id = _id;
        this.firstWord = firstWord;
        this.secondWord = secondWord;
        this.correctFirstWords = correctFirstWords;
        this.correctSecondWords = correctSecondWords;
    }

    public DictionaryRow() {

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
}
