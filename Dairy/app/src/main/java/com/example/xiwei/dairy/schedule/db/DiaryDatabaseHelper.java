package com.example.xiwei.dairy.schedule.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DiaryDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_DIARY = "create table Schedule("
            + "id integer primary key autoincrement, "
            + "date text, "
            + "time text, "
            + "tag text, "
            + "content text)";

    private Context mContext;
    public DiaryDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists Schedule");
        onCreate(db);
    }
}
