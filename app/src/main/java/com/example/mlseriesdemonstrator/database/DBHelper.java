package com.example.mlseriesdemonstrator.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_VECTOR = "vector";
    // Add more columns as needed

    // SQL Create Table statement
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT, " + COLUMN_VECTOR+ " TEXT)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void insertData(String name, String vector) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_VECTOR, vector);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Person> getAllPersons() {
        ArrayList<Person> personArrayList = new ArrayList<Person>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String[] vectors = c.getString((c.getColumnIndex(COLUMN_VECTOR))).split(";");
                float[] vector = new float[vectors.length];
                for (int i = 0; i < vectors.length; i++) {
                    vector[i] = Float.parseFloat(vectors[i]);
                }
                @SuppressLint("Range") Person person = new Person(c.getString(c.getColumnIndex(COLUMN_NAME)), vector);

                // adding to list
                personArrayList.add(person);
            } while (c.moveToNext());
        }
        return personArrayList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

