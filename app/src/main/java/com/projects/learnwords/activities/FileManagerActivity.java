package com.projects.learnwords.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import java.sql.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.projects.learnwords.app.DbControl;
import com.projects.learnwords.app.DictionaryRow;
import com.projects.learnwords.adapters.FileManagerArrayAdapter;
import com.projects.learnwords.exception.FileFormatException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class FileManagerActivity extends ListActivity {
    //сохораненные названия словарей, чтобы избежать повторений
    private SharedPreferences savedNamesLearned;
    //текущий список файлов и директорий
    private List<String> directoryEntries = new ArrayList<String>();
    private File currentDirectory = Environment.getExternalStorageDirectory();
    private final String HOME_DIRECTORY = currentDirectory.getPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedNamesLearned = getSharedPreferences("namesLearned", MODE_PRIVATE);
        note();
        try {
            browseTo(currentDirectory);
        } catch (FileFormatException e){
            Toast.makeText(getApplicationContext(), "Неверный формат файла. Необходим .txt", Toast.LENGTH_LONG).show();
        }
    }

    //выводит сообщение на экран
    private void note(){
        final TextView newDict = new TextView(this);
        final String text = "dog | собака \narrow | стрела \ncat |кот \nleft | лево \nhouse | дом \nmouse | мыш" ;

        newDict.setText(text);
        newDict.setBackgroundColor(-1);
        newDict.setTextSize(25);

        DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
            Set<String> tags;
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        new AlertDialog.Builder(this)
                    .setTitle("Напоминание")
                    .setMessage("Словарь должен быть в формате txt и иметь вид \"слово | перевод\", например:")
                    .setView(newDict)
                    .setPositiveButton("Запомнил", okButtonListener)
                    .show();
    }

    private void upOneLevel(){
        if(currentDirectory.getParent() != null){
            try {
                browseTo(currentDirectory.getParentFile());
            } catch (FileFormatException e){
                Toast.makeText(getApplicationContext(), "Неверный формат файла. Необходим .txt", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void browseTo(final File aDirectory) throws FileFormatException {
        final EditText newDictionaryName = new EditText(this);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        String appStaticName = aDirectory.getName();
        newDictionaryName.setText(appStaticName.substring(0, aDirectory.getName().length() - 4));

        if(aDirectory.isDirectory()){
            currentDirectory = aDirectory;
            fill(aDirectory.listFiles());
        } else {

            if(!appStaticName.endsWith(".txt"))
                throw new FileFormatException();

            DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
                Set<String> tags;
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tags = savedNamesLearned.getAll().keySet();
                    String name = newDictionaryName.getText().toString();
                    if(!tags.contains(name)) {
                        readFile(aDirectory, name);
                        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        Toast.makeText(getApplicationContext(), "Словарь \"" + name + "\" создан", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(mainIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Данное имя занято! \nСловарь НЕ создан.", Toast.LENGTH_LONG).show();
                    }
                }
            };

            DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            };

            alertBuilder
                    .setTitle("Загрузить данный словарь?")
                    .setMessage("Словарь будет загружен в приложение. Укажите имя нового словаря")
                    .setView(newDictionaryName)
                    .setPositiveButton("Да", okButtonListener)
                    .setNegativeButton("Нет", cancelButtonListener)
                    .show();
        }
    }

    private void fill(File[] files){
        directoryEntries.clear();

        if(currentDirectory.getParent() != null && !HOME_DIRECTORY.equals(currentDirectory.getAbsolutePath()))
            directoryEntries.add("..");

        for(File file : files){
            if(file.canRead() && !file.isHidden()){
                if(file.isDirectory())
                    directoryEntries.add("/" + file.getName());
                else
                    directoryEntries.add(file.getName());
            }
        }

        FileManagerArrayAdapter directoryList = new FileManagerArrayAdapter (this, (ArrayList)directoryEntries);
        setListAdapter(directoryList);
    }

    private void readFile(File file, String newDictionaryName) {
        int length = (int) file.length();
        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(file);
            try {
                in.read(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Ошибка чтения", Toast.LENGTH_LONG).show();
            } finally {
                in.close();
            }
            String contents = new String(bytes, "cp1251");

            if (contents.length() > 0) {
                writeToDictionary(contents, newDictionaryName);
            } else {
                Toast.makeText(getApplicationContext(), "Файл пустой", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Файл отсутствует", Toast.LENGTH_LONG).show();
        }
    }

    private void writeToDictionary(String contents, String dictName) {
        rememberDictionaryName(dictName);
        DbControl dict = DbControl.createDbControl(getApplicationContext(), dictName, 1);
        DictionaryRow[] dictRows = parseWords(contents);
        try {
            dict.open();
            for(DictionaryRow dr: dictRows) {
                dict.insert(dr.getFirstWord(), dr.getSecondWord(), dr.getCorrectFirstWords(),
                        dr.getCorrectSecondWords(), dr.getFirstComment(), dr.getSecondComment());
            }
        } catch (SQLException e)
        {
            Log.e("CREATE_OR_OPEN_ERROR", e.toString());
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
        } finally {
            dict.close();
        }
    }


    private DictionaryRow[] parseWords(String context){
        String[] strLines = context.split("\n");
        DictionaryRow[] dictionaryRows = new DictionaryRow[strLines.length];
        for(int i = 0; i<strLines.length; i++) {
            dictionaryRows[i] = new DictionaryRow(strLines[i]);
        }
        return dictionaryRows;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void rememberDictionaryName(String newDictionaryName){
        SharedPreferences.Editor preferencesEditor = savedNamesLearned.edit();
        preferencesEditor.putBoolean(newDictionaryName, false);
        preferencesEditor.apply();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        int selectionRowID = position;
        String selectedFileString = directoryEntries.get(selectionRowID);
        if(selectedFileString.startsWith("/"))
            selectedFileString = currentDirectory + selectedFileString;
        else
            selectedFileString = currentDirectory + "/" + selectedFileString;

        if(selectedFileString.equals(currentDirectory + "/..")){
            upOneLevel();
        } else {
            File clickedFile = null;
            clickedFile = new File(selectedFileString);
            if(clickedFile != null) {
                try {
                    browseTo(clickedFile);
                } catch (FileFormatException e){
                    Toast.makeText(getApplicationContext(), "Неверный формат файла. Необходим .txt", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(HOME_DIRECTORY.equals(currentDirectory.getAbsolutePath())) {
            finish();
        } else {
            upOneLevel();
        }
    }
}
