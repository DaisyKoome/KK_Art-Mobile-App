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
    public static final String COL_2 = "C_NATIONAL_ID";
    public static final String COL_3 = "C_FIRST_NAME";
    public static final String COL_4 = "C_LAST_NAME";
    public static final String COL_5 = "C_EMAIL";
    public static final String COL_6 = "C_PASSWORD";
    public static final String COL_7 = "C_PHONE_NO";

    public ClientPasswordDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABlE_NAME +
                "(CLIENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, C_NATIONAL_ID TEXT, C_FIRST_NAME TEXT, C_LAST_NAME TEXT, C_EMAIL TEXT UNIQUE, C_PASSWORD TEXT, C_PHONE_NO TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABlE_NAME);
        onCreate(db);
    }

    //Registration handler
    public boolean insertData(String national_id2, String first_name2, String last_name2, String email2, String password2, String phone_no2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, national_id2);
        contentValues.put(COL_3, first_name2);
        contentValues.put(COL_4, last_name2);
        contentValues.put(COL_5, email2);
        contentValues.put(COL_6, password2);
        contentValues.put(COL_7, phone_no2);
        long result = db.insert(TABlE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    //Login handler
    public Cursor login_user(String email2) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABlE_NAME + " WHERE C_EMAIL='" + email2 + "';", null);
        return res;
    }


}

