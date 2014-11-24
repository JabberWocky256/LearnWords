package com.projects.learnwords.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.projects.learnwords.app.DbControl;
import com.projects.learnwords.app.R;

import java.sql.SQLException;


/**
 * Created by Александр on 14.10.2014.
 */
public class CreateDictionaryActivity extends Activity {
    private String[] names;
    private ListView lvAllDictionaries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_dictionary);

        final Button btnAddDictionary = (Button) findViewById(R.id.btnAddDictionary);
        lvAllDictionaries = (ListView) findViewById(R.id.LVDictionaries);

        btnAddDictionary.setOnClickListener(btnAddDictionaryListener());

        names = getAllNames();
        ArrayAdapter<String> allDictArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        lvAllDictionaries.setAdapter(allDictArrayAdapter);

        lvAllDictionaries.setOnItemClickListener(lvAllDictionariesOnItemClickListener());
    }


    private View.OnClickListener btnAddDictionaryListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDictionary();
            }
        };
    }

    private void addNewDictionary(){
        final EditText newDictionaryName = new EditText(this);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = newDictionaryName.getText().toString();
                //вся магия просиходит здесь
                writeDictionaryToDB(name);
                rememberDictionaryName(name);
                recreate();
            }
        };

        DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        };

        alertBuilder
                .setTitle("Создание словаря")
                .setMessage("Укажите имя нового словаря")
                .setView(newDictionaryName)
                .setPositiveButton("Создать", okButtonListener)
                .setNegativeButton("Отмена", cancelButtonListener)
                .show();
    }

    private void writeDictionaryToDB(String name){
        if(isExist(name)){
            DbControl dict = DbControl.createDbControl(getApplicationContext(), name, 1);
            try {
                dict.open();
                Toast.makeText(getApplicationContext(), "Словарь создан!", Toast.LENGTH_LONG).show();
            } catch (SQLException e)
            {
                Log.e("CREATE_OR_OPEN_ERROR", e.toString());
                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
            } finally {
                dict.close();
            }
        }
    }

    private boolean isExist(String name){

        if(name.length()<2){
            Toast.makeText(getApplicationContext(), "Введите имя длиннее 2-х символов!", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(int i = 0; i<names.length; i++){
            if(names[i].equals(name)){
                Toast.makeText(getApplicationContext(), "Данное имя занято!", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void rememberDictionaryName(String newDictionaryName){
        SharedPreferences.Editor preferencesEditor = getSharedPreferences("namesLearned", MODE_PRIVATE).edit();
        preferencesEditor.putBoolean(newDictionaryName, false);
        preferencesEditor.apply();
    }

    @Override
    public void recreate() {
        names = getAllNames();
        ArrayAdapter<String> allDictArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        lvAllDictionaries.setAdapter(allDictArrayAdapter);
    }

    private String[] getAllNames(){
        SharedPreferences savedNamesLearned = getSharedPreferences("namesLearned", MODE_PRIVATE);
        String[] names = savedNamesLearned.getAll().keySet().toArray(new String[0]);

        return names;
    }

    private AdapterView.OnItemClickListener lvAllDictionariesOnItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CreateDictionaryActivity.this, EditDictionaryActivity.class);
                EditDictionaryActivity.dictName = names[position];
                startActivity(intent);
            }
        };
    }
}
