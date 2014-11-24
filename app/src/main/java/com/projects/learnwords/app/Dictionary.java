package com.projects.learnwords.app;

import java.util.*;

/**
 * Created by Александр on 14.10.2014.
 */
public class Dictionary implements IDictionary {
    private List<IDictionaryRow> rows = new ArrayList<IDictionaryRow>();

    public Dictionary(){

    }

    private List copyRows(List<IDictionaryRow> dictRows){
        //rows = new ArrayList<IDictionaryRow>(rows.size());

        /*for(int i = 0; i<rows.size(); i++) {
            if(!this.rows.get(i).equals(dictRows.get(i))) {
                IDictionaryRowStateDecorator dictRow = new DictionaryRowState(new DictionaryRow(dictRows.get(i).getId(), dictRows.get(i).getFirstWord(), dictRows.get(i).getSecondWord(), dictRows.get(i).getCorrectFirstWords(),
                        dictRows.get(i).getCorrectSecondWords(), dictRows.get(i).getFirstComment(), dictRows.get(i).getSecondComment()));
                rows.add(dictRow);
                dictRow.setChangedState(DictionaryRowState.DictionaryRowStates.UPDATE);
            } else {
                IDictionaryRow dictRow = new DictionaryRowState(dictRows.get(i));
                DictionaryRowState.DictionaryRowStates state;
                try{
                    state = ((DictionaryRowState)dictRow).getChangedState();
                } catch (ClassCastException e){
                    state = DictionaryRowState.DictionaryRowStates.NONE;
                }
                rows.add(dictRow);
                ((DictionaryRowState) dictRow).setChangedState(state);
            }
        }*/
        List rows = new ArrayList<IDictionaryRow>();
        for(int i = 0; i<this.rows.size(); i++){
            if(((IDictionaryRowStateDecorator)this.rows.get(i)).getChangedState() == DictionaryRowState.DictionaryRowStates.NONE)
                rows.add(this.rows.get(i));
            else {
                IDictionaryRow dr = this.rows.get(i);
                DictionaryRowState r = new DictionaryRowState(new DictionaryRow(dr.getId(), new String(dr.getFirstWord()), new String(dr.getSecondWord()), dr.getCorrectFirstWords(), dr.getCorrectSecondWords(), dr.getFirstComment(), dr.getSecondComment()));
                rows.add(r);
            }
        }

        return rows;
    }

    public void getAllRowsAsDecorator(){
        for(int i = 0; i<rows.size(); i++){
            rows.set(i, new DictionaryRowState(rows.get(i)));
        }
    }

    public void setRow(IDictionaryRow row){
        rows.add(row);
    }

    public void setRows(List<IDictionaryRow> rows){
        this.rows = rows;
    }

    public List<IDictionaryRow> getRows(){
        return rows;
    }

    @Override
    public DictionaryState getMemento() {
        return new DictionaryState(copyRows(rows));
    }

    @Override
    public void setMemento(DictionaryState dictionaryState) {
        this.rows = dictionaryState.getState();
    }
}