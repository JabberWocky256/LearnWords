package com.projects.learnwords.app;

/**
 * Created by Александр on 04.11.2014.
 */
public class DictionaryRowState implements IDictionaryRowStateDecorator {
    DictionaryRow dictionaryRow;
    DictionaryRowStates state = DictionaryRowStates.NONE;

    public DictionaryRowState(IDictionaryRow dictRow){
        this.dictionaryRow = new DictionaryRow(dictRow.getId(), new String(dictRow.getFirstWord()),
                new String(dictRow.getSecondWord()), dictRow.getCorrectFirstWords(), dictRow.getCorrectSecondWords(),
                dictRow.getFirstComment(), dictRow.getSecondComment());
    }

    @Override
    public void setChangedState(DictionaryRowStates state) {
        this.state = state;
    }

    @Override
    public DictionaryRowStates getChangedState() {
        return state;
    }

    @Override
    public int getId() {
        return dictionaryRow.getId();
    }

    @Override
    public void setId(int id) {
        dictionaryRow.setId(id);
    }

    @Override
    public String getFirstWord() {
        return dictionaryRow.getFirstWord();
    }

    @Override
    public void setFirstWord(String firstWord) {
        dictionaryRow.setFirstWord(firstWord);
    }

    @Override
    public String getSecondWord() {
        return dictionaryRow.getSecondWord();
    }

    @Override
    public void setSecondWord(String secondWord) {
        dictionaryRow.setSecondWord(secondWord);
    }

    @Override
    public int getCorrectFirstWords() {
        return dictionaryRow.getCorrectFirstWords();
    }

    @Override
    public void setCorrectFirstWords(int correctFirstWords) {
        dictionaryRow.setCorrectFirstWords(correctFirstWords);
    }

    @Override
    public int getCorrectSecondWords() {
        return dictionaryRow.getCorrectSecondWords();
    }

    @Override
    public void setCorrectSecondWords(int correctSecondWords) {
        dictionaryRow.setCorrectSecondWords(correctSecondWords);
    }

    @Override
    public String getSecondComment() {
        return dictionaryRow.getSecondComment();
    }

    @Override
    public void setSecondComment(String secondComment) {
        dictionaryRow.setSecondComment(secondComment);
    }

    @Override
    public String getFirstComment() {
        return dictionaryRow.getFirstComment();
    }

    @Override
    public void setFirstComment(String firstComment) {
        dictionaryRow.setFirstComment(firstComment);
    }

    @Override
    public boolean equals(Object o) {
        DictionaryRow dr = (DictionaryRow) o;
        if(dictionaryRow.getFirstWord().equals(dr.getFirstWord()) && dictionaryRow.getSecondWord().equals(dr.getSecondWord()))
            return true;
        else
            return false;
    }

    public enum DictionaryRowStates{
        NONE,
        REMOVE,
        ADD,
        UPDATE
    }
}
