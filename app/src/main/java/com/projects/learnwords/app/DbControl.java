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
    private static DbControl dbControl;

    private static final String DB_NAME = "LearnWordsDB.db";
    private String TABLE_NAME;
    private final int DB_VERSION;

    public static final String FIRST_WORD = "FIRST_WORD";
    public static final String SECOND_WORD = "SECOND_WORD";
    public static final String CORRECT_FIRST_WORDS = "CORRECT_ENGLISH_WORDS";
    public static final String CORRECT_SECOND_WORDS = "CORRECT_RUSSIANS_WORDS";
    public static final String FIRST_COMMENT = "FIRST_COMMENT";
    public static final String SECOND_COMMENT = "SECOND_COMMENT";

    private SQLiteDatabase database;
    private DbOpenHelper dbOpenHelper;
    private Context context;

    private DbControl(Context context, final String TABLE_NAME, int DB_VERSION){
        super();
        this.context = context;
        this.TABLE_NAME = TABLE_NAME;
        this.DB_VERSION =DB_VERSION;
        dbOpenHelper = new DbOpenHelper(context, DB_NAME, null, DB_VERSION);
    }

    //Singleton
    public static DbControl createDbControl(Context context, final String TABLE_NAME, int DB_VERSION){
        if(dbControl == null){
            dbControl = new DbControl(context, TABLE_NAME, DB_VERSION);
        }
        dbControl.TABLE_NAME = TABLE_NAME;
        return dbControl;
    }

    public void open() throws SQLException {
        database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.onCreate(database);
    }

    public void close(){
        if(database!=null)
            database.close();
    }

    public void delete(){
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        dbControl = null;
    }

    public void insert(final String FIRST_WORD, final String SECOND_WORD, final int CORRECT_FIRST_WORDS,
                       final int CORRECT_SECOND_WORDS, String FIRST_COMMENT, String SECOND_COMMENT) {

        ContentValues values = new ContentValues();

        values.put(DbControl.FIRST_WORD, FIRST_WORD);
        values.put(DbControl.SECOND_WORD, SECOND_WORD);
        values.put(DbControl.CORRECT_FIRST_WORDS, CORRECT_FIRST_WORDS);
        values.put(DbControl.CORRECT_SECOND_WORDS, CORRECT_SECOND_WORDS);
        values.put(DbControl.FIRST_COMMENT, FIRST_COMMENT);
        values.put(DbControl.SECOND_COMMENT, SECOND_COMMENT);

        database.insert(TABLE_NAME, null, values);
    }

    public int delete(final String id) throws SQLException{
        int delete = database.delete(TABLE_NAME, "_id = ?", new String[] {id});
        return delete;
    }

    public List<IDictionaryRow> readAll() throws SQLException{
        return read(null);
    }

    public List<IDictionaryRow> readNextTenWords(String correctWordsColumnName) throws SQLException{
        return read(correctWordsColumnName);
    }

    public int update(final IDictionaryRow dictObject) {
        ContentValues values = new ContentValues();
        values.put(DbControl.FIRST_WORD, dictObject.getFirstWord());
        values.put(DbControl.SECOND_WORD, dictObject.getSecondWord());
        values.put(DbControl.CORRECT_FIRST_WORDS, dictObject.getCorrectFirstWords());
        values.put(DbControl.CORRECT_SECOND_WORDS, dictObject.getCorrectSecondWords());
        values.put(DbControl.FIRST_COMMENT, dictObject.getFirstComment());
        values.put(DbControl.SECOND_COMMENT, dictObject.getSecondComment());

        String id = String.valueOf(dictObject.getId());

        int update = database.update(TABLE_NAME, values, "_id = ?", new String[] {id});
        return update;
    }

    private List<IDictionaryRow> read(String columnName) throws SQLException {
        List<IDictionaryRow> rows = new ArrayList<IDictionaryRow>();

        Cursor cursor;
        cursor = database.query(TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();

        cursorRead(cursor, rows, columnName);

        cursor.close();

        return rows;
    }

    private void cursorRead(Cursor cursor, List<IDictionaryRow> rows, String columnName){
        int index = 0;
        do {
            int _id = cursor.getInt(0);
            String firstWord = cursor.getString(1);
            String secondWord = cursor.getString(2);
            int correctFirstWords = cursor.getInt(3);
            int correctSecondWords = cursor.getInt(4);
            String firstComment = cursor.getString(5);
            String secondComment = cursor.getString(6);

            if(columnName == null) {

                rowAdd(rows, _id, firstWord, secondWord, correctFirstWords,
                        correctSecondWords, firstComment, secondComment);

            }
            else if((columnName.equals(DbControl.CORRECT_FIRST_WORDS) && correctFirstWords <4)||
                    (columnName.equals(DbControl.CORRECT_SECOND_WORDS) && correctSecondWords<4)) {

                rowAdd(rows, _id, firstWord, secondWord, correctFirstWords,
                        correctSecondWords, firstComment, secondComment);
                index++;

            }

        } while(cursor.moveToNext() && index<10);
    }

    private void rowAdd(List<IDictionaryRow> rows, int _id, String firstWord, String secondWord, int correctFirstWords,
                        int correctSecondWords, String firstComment, String secondComment){
        rows.add(new DictionaryRow(_id, firstWord, secondWord, correctFirstWords,
                correctSecondWords, firstComment, secondComment));
    }


    private class DbOpenHelper extends SQLiteOpenHelper{

        public DbOpenHelper(Context context, final String DB_NAME, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DB_NAME, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + FIRST_WORD + " TEXT, " +
                    SECOND_WORD + " TEXT, " + CORRECT_FIRST_WORDS + " INTEGER, " + CORRECT_SECOND_WORDS + " INTEGER, " + FIRST_COMMENT + " TEXT, " + SECOND_COMMENT + " TEXT )";
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
