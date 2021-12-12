package com.example.mysqlimagesuploaderlistview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ClientPasswordDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users";
    public static final String TABlE_NAME = "client";
    public static final String COL_2 = "NATIONAL_ID";
    public static final String COL_3 = "FIRST_NAME";
    public static final String COL_4 = "LAST_NAME";
    public static final String COL_5 = "EMAIL";
    public static final String COL_6 = "PASSWORD";
    public static final String COL_7 = "PHONE_NO";

    public ClientPasswordDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABlE_NAME +
                "(ARTIST_ID INTEGER PRIMARY KEY AUTOINCREMENT, NATIONAL_ID TEXT, FIRST_NAME TEXT, LAST_NAME TEXT, EMAIL TEXT UNIQUE, PASSWORD TEXT, PHONE_NO TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABlE_NAME);
        onCreate(db);
    }

    //Registration handler
    public boolean insertData(String national_id, String first_name, String last_name, String email, String password, String phone_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, national_id);
        contentValues.put(COL_3, first_name);
        contentValues.put(COL_4, last_name);
        contentValues.put(COL_5, email);
        contentValues.put(COL_6, password);
        contentValues.put(COL_7, phone_no);
        long result = db.insert(TABlE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    //Login handler
    public Cursor login_user(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABlE_NAME + " WHERE EMAIL='" + email + "';", null);
        return res;
    }


}

