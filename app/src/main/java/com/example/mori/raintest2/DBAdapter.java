package com.example.mori.raintest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

/**
 * Created by rikuya on 2016/08/30.
 */

public class DBAdapter {

    static final String DATABASE_NAME = "myDiary.db";
    static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "diary2";
    public static final String COL_ID = "id";
    public static final String COL_DATE = "date";
    public static final String COL_PATH = "path";
    public static final String COL_COMMENT = "comment";
    protected final Context context;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override public void onCreate(SQLiteDatabase db) {
            db.execSQL( "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_PATH + " TEXT NOT NULL," + COL_COMMENT + " TEXT NOT NULL," + COL_DATE + " TEXT NOT NULL);");
        }

        @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void DropTable() {
        String sql = "drop table " + TABLE_NAME + ";";
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
    }

    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public boolean deleteAllDiary(){
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    public boolean deleteDiary(int id){
        return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
    }

    public Cursor getAllDiary(){
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void saveDiary(Diary diary){
        ContentValues values = new ContentValues();
        values.put(COL_COMMENT, diary.getComment());
        values.put(COL_DATE, diary.getDate());
        values.put(COL_PATH, diary.getFileName());
        db.insertOrThrow(TABLE_NAME, null, values);
    }

}
