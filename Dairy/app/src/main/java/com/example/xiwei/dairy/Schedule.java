package com.example.xiwei.dairy;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiwei.dairy.schedule.bean.DiaryBean;
import com.example.xiwei.dairy.schedule.db.DiaryDatabaseHelper;
import com.example.xiwei.dairy.schedule.event.StartUpdateDiaryEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Schedule<OnClick> extends AppCompatActivity {

    // final RecyclerView mMainRvShowDiary = (RecyclerView) findViewById(R.id.main_rv_show_schedule);

    private Boolean b_sub_square = false;
    private List<DiaryBean> mDiaryBeanList;
    private DiaryDatabaseHelper mHelper;
    private int mEditPosition = -1;
    // private static TextView mTvTest;

    public int sTheme = WriteCalender.sTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sTheme!=0) {
            //设置主题
            setTheme(sTheme);
        }

        setContentView(R.layout.schedule);
        mHelper = new DiaryDatabaseHelper(this, "Schedule1.db", null, 1);

        RecyclerView mMainRvShowDiary = (RecyclerView) findViewById(R.id.main_rv_show_schedule);

        getDiaryBeanList();
        // String i = String.valueOf(mDiaryBeanList.size());
        // Toast.makeText(getApplicationContext(),i,Toast.LENGTH_SHORT).show();

        mMainRvShowDiary.setLayoutManager(new LinearLayoutManager(this));
        mMainRvShowDiary.setAdapter(new com.example.xiwei.dairy.schedule.DiaryAdapter(this, mDiaryBeanList));

        /*
        //获取Button
        final Button sub_square = (Button) findViewById(R.id.sub_square);
        //监听事件
        sub_square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!b_sub_square) {
                    b_sub_square = true;
                    //设置是否被激活状态，true表示被激活
                    sub_square.setActivated(b_sub_square);
                }
                else {
                    b_sub_square = false;
                    //设置是否被激活状态，false表示未激活
                    sub_square.setActivated(b_sub_square);
                }
            }
        });
        */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cc.trity.floatingactionbutton.FloatingActionButton fab = (cc.trity.floatingactionbutton.FloatingActionButton) findViewById(R.id.scheduleadd);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(),"Signed Up?",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Schedule.this, WriteSchedule.class);
                startActivity(i);
                finish();
            }
        });
    }

    private List<DiaryBean> getDiaryBeanList() {

        LinearLayout mMainLlMain = (LinearLayout) findViewById(R.id.main_ll_main);
        LinearLayout mItemFirst = (LinearLayout) findViewById(R.id.item_first);

        mDiaryBeanList = new ArrayList<>();
        List<DiaryBean> diaryList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();

        /*ContentValues values = new ContentValues();
        values.put("date", "11");
        values.put("time", "22");
        values.put("content", "33");
        values.put("tag", "0");
        sqLiteDatabase.insert("Schedule", null, values);
        values.clear();*/

        Cursor cursor = sqLiteDatabase.query("Schedule", null, null, null, null, null, null, null);

        String c = String.valueOf(cursor.getCount());
        // Toast.makeText(getApplicationContext(),"getDiaryBeanList "+ c,Toast.LENGTH_SHORT).show();

        if (cursor.moveToFirst()) {
            // Toast.makeText(getApplicationContext(),"moveToFirst",Toast.LENGTH_SHORT).show();
            do {
                //String date = cursor.getString(cursor.getColumnIndex("date"));
                //String dateSystem = com.example.xiwei.dairy.schedule.GetDate.getDate().toString();
                if (cursor.getCount() > 0) {// date.equals(dateSystem)
                    // Toast.makeText(getApplicationContext(),"want to remove ",Toast.LENGTH_SHORT).show();
                    //mMainLlMain.removeView(mItemFirst);
                    break;
                }
            } while (cursor.moveToNext());
        }

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("time"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                mDiaryBeanList.add(new DiaryBean(date, title, content, tag));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }

        Collections.sort(diaryList);

        mDiaryBeanList = diaryList;
        return mDiaryBeanList;
    }

    public void startUpdateDiaryActivity(StartUpdateDiaryEvent event) {
        String title = mDiaryBeanList.get(event.getPosition()).getTitle();
        String content = mDiaryBeanList.get(event.getPosition()).getContent();
        String tag = mDiaryBeanList.get(event.getPosition()).getTag();
        // UpdateDiaryActivity.startActivity(this, title, content, tag);

    }
}
