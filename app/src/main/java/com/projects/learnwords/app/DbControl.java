package com.projects.learnwords.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbControl {
    private static final String DB_NAME = "LearnWordsDB.db";
    private final String TABLE_NAME;
    private final int DB_VERSION;

    static final String FIRST_WORD = "FIRST_WORD";
    static final String SECOND_WORD = "SECOND_WORD";
    static final String CORRECT_FIRST_WORDS = "CORRECT_ENGLISH_WORDS";
    static final String CORRECT_SECOND_WORDS = "CORRECT_RUSSIANS_WORDS";

    private Cursor cursor;
    private SQLiteDatabase database;
    private DbOpenHelper dbOpenHelper;
    private Context context;

    public DbControl(Context context, final String TABLE_NAME, int DB_VERSION){
        super();
        this.context = context;
        this.TABLE_NAME = TABLE_NAME;
        this.DB_VERSION =DB_VERSION;
        dbOpenHelper = new DbOpenHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void open() throws SQLException {
        database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.onCreate(database);
    }

    public void close(){
        if(database!=null)
            database.close();
    }

    public void insert(final String FIRST_WORD, final String SECOND_WORD,
                       final int CORRECT_FIRST_WORDS, final int CORRECT_SECOND_WORDS) {

        ContentValues values = new ContentValues();

        values.put(DbControl.FIRST_WORD, FIRST_WORD);
        values.put(DbControl.SECOND_WORD, SECOND_WORD);
        values.put(DbControl.CORRECT_FIRST_WORDS, CORRECT_FIRST_WORDS);
        values.put(DbControl.CORRECT_SECOND_WORDS, CORRECT_SECOND_WORDS);

        database.insert(TABLE_NAME, null, values);
    }

    public void changeFirstWord(final DictionaryRow firstWord) {
        update(firstWord);
    }

    public void changeSecondWord(final DictionaryRow secondWord){
        update(secondWord);
    }

    public void changeCorrectFirstWords(final DictionaryRow correctFirstWords) {
        update(correctFirstWords);
    }

    public void changeCorrectSecondWords(final DictionaryRow correctSecondWords) {
        update(correctSecondWords);
    }

    public int delete(final String where) throws SQLException{
        int delete = database.delete(TABLE_NAME, where, null);
        return delete;
    }

    public List<DictionaryRow> readAll() throws SQLException{
        return read(null);
    }

    public List<DictionaryRow> readNextTenWords(String correctWordsColumnName) throws SQLException{
        return read(correctWordsColumnName);
    }

    private int update(final DictionaryRow dictObject) {
        ContentValues values = new ContentValues();
        values.put(DbControl.FIRST_WORD, dictObject.getFirstWord());
        values.put(DbControl.SECOND_WORD, dictObject.getSecondWord());
        values.put(DbControl.CORRECT_FIRST_WORDS, dictObject.getCorrectFirstWords());
        values.put(DbControl.CORRECT_SECOND_WORDS, dictObject.getCorrectSecondWords());

        String id = String.valueOf(dictObject.getId());

        int update = database.update(TABLE_NAME, values, "_id = ?", new String[] {id});
        return update;
    }

    private List<DictionaryRow> read(String columnName) throws SQLException {
        List<DictionaryRow> rows = new ArrayList<DictionaryRow>();

        Cursor cursor;
        int index = 0;
        cursor = database.query(TABLE_NAME,null,null,null,null,null,null);

        cursor.moveToFirst();
        do {
            int _id = cursor.getInt(0);
            String firstWord = cursor.getString(1);
            String secondWord = cursor.getString(2);
            int correctFirstWords = cursor.getInt(3);
            int correctSecondWords = cursor.getInt(4);

            if(columnName == null)
                rows.add(new DictionaryRow(_id, firstWord, secondWord, correctFirstWords, correctSecondWords));
            else if((columnName.equals(DbControl.CORRECT_FIRST_WORDS) && correctFirstWords <4)||
                    (columnName.equals(DbControl.CORRECT_SECOND_WORDS) && correctSecondWords<4)) {
                rows.add(new DictionaryRow(_id, firstWord, secondWord, correctFirstWords, correctSecondWords));
                index++;
            }

        } while(cursor.moveToNext() && index<10);

        cursor.close();

        return rows;
    }

    private class DbOpenHelper extends SQLiteOpenHelper{

        public DbOpenHelper(Context context, final String DB_NAME, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DB_NAME, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + FIRST_WORD + " TEXT, " +
                    SECOND_WORD + " TEXT, " + CORRECT_FIRST_WORDS + " INTEGER, " + CORRECT_SECOND_WORDS + " INTEGER )";
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }
}
