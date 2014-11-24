package com.projects.learnwords.app;

import java.util.ArrayList;
import java.util.List;


public class DictionaryState {
    private List state;

    public DictionaryState(List state) {
        this.state = new ArrayList(state);
    }

    public List getState() {
        return state;
    }
}
