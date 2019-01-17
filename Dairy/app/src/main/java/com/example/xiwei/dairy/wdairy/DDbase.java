package com.example.xiwei.dairy.wdairy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DDbase extends SQLiteOpenHelper {

    public static final String CREATE_DIARY = "create table Diary("
            + "id integer primary key autoincrement, "
            + "date text, "
            + "title text, "
            + "tag text, "
            + "content text, "
            + "review text)";


    private Context mContext;
    public DDbase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists Diary");
        onCreate(db);
    }
}
