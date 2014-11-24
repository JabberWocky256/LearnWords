package com.projects.learnwords.app;

/**
 * Created by Александр on 28.10.2014.
 */
public interface IDictionary {
    public DictionaryState getMemento();
    public void setMemento(DictionaryState dictionaryState);
}
