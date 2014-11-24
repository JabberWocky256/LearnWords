package com.projects.learnwords.app;

/**
 * Created by Александр on 04.11.2014.
 */
public interface IDictionaryRowStateDecorator extends IDictionaryRow {
    public void setChangedState(DictionaryRowState.DictionaryRowStates state);
    public DictionaryRowState.DictionaryRowStates getChangedState();
}
