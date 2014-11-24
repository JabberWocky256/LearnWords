package com.projects.learnwords.app;

/**
 * Created by Александр on 28.10.2014.
 */
public class DictionaryUtil {
    private DictionaryState dictionaryState;

    public void saveState(IDictionary originator)
    {
        if(originator == null)
            throw new IllegalArgumentException("originator is null");

        dictionaryState = originator.getMemento();
    }

    public void loadState(IDictionary originator){
        if(originator == null)
            throw new IllegalArgumentException("originator is null");
        if(dictionaryState == null)
            throw new IllegalArgumentException("memento is null");

        originator.setMemento(dictionaryState);
    }
}
