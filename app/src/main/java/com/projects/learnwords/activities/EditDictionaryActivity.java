package com.projects.learnwords.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.projects.learnwords.adapters.EditDictionaryAdapter;
import com.projects.learnwords.app.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Александр on 14.10.2014.
 */
public class EditDictionaryActivity extends Activity {
    private TextView txtDictName;
    private ListView lvWords;
    private Button btnSave;
    private Button btnAddNewData;
    private DbControl dict;
    private Dictionary dictionary;
    private DictionaryUtil dictionaryUtil;
    private boolean isDictionaryChanged = false;

    public static String dictName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_dictionary);

        txtDictName = (TextView) findViewById(R.id.txtDictName);
        lvWords = (ListView) findViewById(R.id.lvWords);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnAddNewData = (Button) findViewById(R.id.btnAddNewWords);

        txtDictName.setText(dictName);

        dictionary = createDictionary();
        dictionary.getAllRowsAsDecorator();
        dictionaryUtil = new DictionaryUtil();
        dictionaryUtil.saveState(dictionary);

        EditDictionaryAdapter adapter = new EditDictionaryAdapter(this, dictionary.getRows());
        lvWords.setAdapter(adapter);
        btnSave.setOnClickListener(btnSaveListener());
        btnAddNewData.setOnClickListener(btnAddNewDataListener());
    }

    public Dictionary createDictionary() {
        Dictionary dictionary = new Dictionary();
        List<IDictionaryRow> dictionaryRows = readWordsFromDB();

        for(IDictionaryRow row: dictionaryRows){
            dictionary.setRow(row);
        }
        return dictionary;
    }

    private List<IDictionaryRow> readWordsFromDB(){
        List<IDictionaryRow> rows = null;
        dict = DbControl.createDbControl(getApplicationContext(), dictName, 1);
        try {
            dict.open();
            rows = dict.readAll();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Ошибка чтения словаря", Toast.LENGTH_LONG).show();
            Log.e("Error", e.toString());
        } catch (Exception e){
            Log.e("Error in", e.toString());
        } finally {
            dict.close();
        }

        return rows;
    }

    private View.OnClickListener btnSaveListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDictionaryChanged = false;
                writeChangesToDB(dictionary);
                finish();
            }
        };
    }

    private void writeChangesToDB(Dictionary dictionary){
        try {
            dict.open();
            for(IDictionaryRow dr: dictionary.getRows()) {
                DictionaryRowState.DictionaryRowStates state;
                try {
                    state = ((IDictionaryRowStateDecorator) dr).getChangedState();
                } catch (ClassCastException ex){
                    state = DictionaryRowState.DictionaryRowStates.NONE;
                }

                if(state == DictionaryRowState.DictionaryRowStates.ADD)
                    dict.insert(dr.getFirstWord(), dr.getSecondWord(), dr.getCorrectFirstWords(),
                            dr.getCorrectSecondWords(), dr.getFirstComment(), dr.getSecondComment());
                if(state == DictionaryRowState.DictionaryRowStates.UPDATE)
                    dict.update(new DictionaryRow(dr.getId(), dr.getFirstWord(), dr.getSecondWord(), dr.getCorrectFirstWords(),
                            dr.getCorrectSecondWords(), dr.getFirstComment(), dr.getSecondComment())) ;
                if(state == DictionaryRowState.DictionaryRowStates.REMOVE)
                    dict.delete(dr.getId() + "");

            }
        } catch (SQLException e)
        {
            Log.e("CREATE_OR_OPEN_ERROR", e.toString());
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
        } finally {
            dict.close();
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        if(isDictionaryChanged) {
            showWarning();
        } else {
            finish();
        }
    }

    private void showWarning(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                writeChangesToDB(dictionary);
                finish();
            }
        };

        DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dictionaryUtil.loadState(dictionary);
                finish();
            }
        };

        alertBuilder
                .setTitle("Данные могут быть утеряны")
                .setMessage("Сохранить изменения перед выходом?")
                .setPositiveButton("Сохранить", okButtonListener)
                .setNegativeButton("Выйти", cancelButtonListener)
                .show();
    }

    private View.OnClickListener btnAddNewDataListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDictionaryChanged = true;
                DictionaryRowState drs = new DictionaryRowState(new DictionaryRow(dictionary.getRows().get(dictionary.getRows().size() - 1).getId() + 1,
                        "Новое слово", "Новый перевод", 0, 0, "", ""));
                dictionary.getRows().add(drs);
                drs.setChangedState(DictionaryRowState.DictionaryRowStates.ADD);
                recreate();
            }
        };
    }

    @Override
    public void recreate() {
        EditDictionaryAdapter adapter = new EditDictionaryAdapter(this, dictionary.getRows());
        lvWords.setAdapter(adapter);
    }

    private void deleteOldDB(){
        try {
            dict.open();
            dict.delete();
            dict =  DbControl.createDbControl(getApplicationContext(), dictName, 1);
        } catch (SQLException e)
        {
            Log.e("CREATE_OR_OPEN_ERROR", e.toString());
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
        } finally {
            dict.close();
        }
    }

    public void setDictionaryChanged(){
        isDictionaryChanged = true;
    }
}
