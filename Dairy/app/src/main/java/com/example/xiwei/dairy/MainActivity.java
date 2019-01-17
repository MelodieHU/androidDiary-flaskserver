package com.example.xiwei.dairy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Intent;
// import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.xiwei.dairy.schedule.bean.DiaryBean;
import com.example.xiwei.dairy.schedule.db.DiaryDatabaseHelper;
import com.example.xiwei.dairy.ui.login.LoginActivity;
import com.example.xiwei.dairy.wdairy.DDbase;
import com.example.xiwei.dairy.wdairy.Dbean;
import com.example.xiwei.dairy.wdairy.DAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.example.xiwei.dairy.ui.login.ServerIP.POSTDIARYURL;
import static com.example.xiwei.dairy.ui.login.ServerIP.GETDIARYURL;

import static com.example.xiwei.dairy.ui.login.SharedPreferenceHelper.getTableNameBySP;

public class  MainActivity extends AppCompatActivity {

    private List<Dbean> mDiaryBeanList;
    private List<Dbean> fDiaryBeanList;
    private DDbase mHelper;
    private int mEditPosition = -1;
    private static String SharedPreferenceName;
    public int sTheme = WriteCalender.sTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sTheme!=0) {
            //设置主题
            setTheme(sTheme);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHelper = new DDbase(this, "Diary2.db", null, 1);

        RecyclerView mMainRvShowDiary = (RecyclerView) findViewById(R.id.main_rv_show_diary);

        getDiaryBeanList();
        fetchDiaryBeanList();
        mMainRvShowDiary.setLayoutManager(new LinearLayoutManager(this));
        mMainRvShowDiary.setAdapter(new com.example.xiwei.dairy.wdairy.DAdapter(this, fDiaryBeanList));


        FloatingActionButton diary = (FloatingActionButton) findViewById(R.id.pen);

        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(),"Signed Up?",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, WriteDiary.class);
                startActivity(i);
                // finish();
            }
        });

        FloatingActionButton schedule = (FloatingActionButton) findViewById(R.id.schedule);

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(),"Signed Up?",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, Schedule.class);
                startActivity(i);
                // finish();
            }
        });

        FloatingActionButton signin = (FloatingActionButton) findViewById(R.id.signin);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toast.makeText(getApplicationContext(),"Signed In?",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                // finish();
            }
        });

        FloatingActionButton calender = (FloatingActionButton) findViewById(R.id.calender);

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toast.makeText(getApplicationContext(),"Signed In?",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, WriteCalender.class);
                startActivity(i);
                // finish();
            }
        });


    }

    private List<Dbean> getDiaryBeanList() {

        LinearLayout mMainLlMain = (LinearLayout) findViewById(R.id.main_ll_main);
        LinearLayout mItemFirst = (LinearLayout) findViewById(R.id.item_first);

        mDiaryBeanList = new ArrayList<>();
        List<Dbean> diaryList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();

        String USERNAME = getTableNameBySP(this);
        SharedPreferenceName = USERNAME;
        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null, null);

        String c = String.valueOf(cursor.getCount());
        // Toast.makeText(getApplicationContext(),"getDiaryBeanList "+ c,Toast.LENGTH_SHORT).show();

        if (cursor.moveToFirst()) {
            // Toast.makeText(getApplicationContext(),"moveToFirst",Toast.LENGTH_SHORT).show();
            do {
                //String date = cursor.getString(cursor.getColumnIndex("date"));
                //String dateSystem = com.example.xiwei.dairy.schedule.GetDate.getDate().toString();
                if (cursor.getCount() > 0) {// date.equals(dateSystem)
                    // Toast.makeText(getApplicationContext(),"want to remove ",Toast.LENGTH_SHORT).show();
                    // mMainLlMain.removeView(mItemFirst);
                    break;
                }
            } while (cursor.moveToNext());
        }

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                String review = cursor.getString(cursor.getColumnIndex("review"));
                mDiaryBeanList.add(new Dbean(date, title, content, tag, review));

                if(USERNAME != "") {

                    final HashMap<String, String> paramsMap = new HashMap<>();
                    // 存入哈希表
                    paramsMap.put("username", USERNAME);
                    paramsMap.put("date", date);
                    paramsMap.put("title", title);
                    paramsMap.put("content", content);
                    paramsMap.put("tag", tag);
                    paramsMap.put("review", review);
                    FormBody.Builder builder = new FormBody.Builder();

                    for (String key : paramsMap.keySet()) {
                        //追加表单信息
                        builder.add(key, paramsMap.get(key));
                    }

                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formBody = builder.build();
                    Request request = new Request.Builder().url(POSTDIARYURL).post(formBody).build();
                    Call call = okHttpClient.newCall(request);

                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // showWarnSweetDialog("服务器错误");
                                    System.out.println("服务器错误");
                                }
                            });

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //请求成功返回结果
                            //如果希望返回的是字符串
                            final String res = response.body().string();
                            //注意，此时的线程不是ui线程，
                            //如果此时我们要用返回的数据进行ui更新，操作控件，就要使用相关方法
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (res.equals("0"))//从服务器得到的response
                                    {
                                        // showWarnSweetDialog("无此账号,请先注册");
                                        System.out.println("上传过这篇日记了");
                                    } else if (res.equals("1")) {
                                        // showWarnSweetDialog("密码不正确");
                                        System.out.println("上传日记成功");
                                        // finish();
                                    } else {
                                        // finish();
                                    }

                                }
                            });
                        }
                    });
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }

        mDiaryBeanList = diaryList;
        return mDiaryBeanList;
    }

    private List<Dbean> fetchDiaryBeanList() {

        LinearLayout mMainLlMain = (LinearLayout) findViewById(R.id.main_ll_main);
        LinearLayout mItemFirst = (LinearLayout) findViewById(R.id.item_first);

        fDiaryBeanList = new ArrayList<>();
        List<Dbean> diaryList = new ArrayList<>();
        final SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        String USERNAME = getTableNameBySP(this);
        SharedPreferenceName = USERNAME;

        if(USERNAME != ""){
            final HashMap<String, String> paramsMap = new HashMap<>();
            // 存入哈希表
            paramsMap.put("username", USERNAME);

            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                //追加表单信息
                builder.add(key, paramsMap.get(key));
            }
            RequestBody formBody = builder.build();
            Request request = new Request.Builder().url(GETDIARYURL).post(formBody).build();
            Call call = okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // showWarnSweetDialog("服务器错误");
                            System.out.println("服务器错误");
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //请求成功返回结果
                    //如果希望返回的是字符串
                    final String responseData = response.body().string();
                    System.out.println(responseData);
                    //注意，此时的线程不是ui线程，
                    //如果此时我们要用返回的数据进行ui更新，操作控件，就要使用相关方法
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {// 收到Json文件

                            try{
                                JSONObject jsonObject = new JSONObject(responseData);

                                for(int i = 1; i <= jsonObject.length(); i++)
                                {
                                    JSONObject jo = jsonObject.getJSONObject(String.valueOf(i));
                                    String date = jo.getString("date");
                                    String title = jo.getString("title");
                                    String content = jo.getString("content");
                                    String tag = jo.getString("tag");
                                    String review = jo.getString("review");

                                    Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null, null);

                                    int flag = 0;
                                    if (cursor.moveToFirst()) {
                                        // Toast.makeText(getApplicationContext(),"moveToFirst",Toast.LENGTH_SHORT).show();
                                        do {
                                            String dateInDb = cursor.getString(cursor.getColumnIndex("content"));

                                            if (dateInDb.equals(content)) {// date.equals(dateSystem)
                                                flag = 1;
                                                break;
                                            }
                                        } while (cursor.moveToNext());
                                    }

                                    if(flag == 0) {

                                        ContentValues values = new ContentValues();
                                        values.put("date", date);
                                        values.put("title", title);
                                        values.put("content", content);
                                        values.put("tag", tag);
                                        values.put("review", review);
                                        sqLiteDatabase.insert("Diary", null, values);
                                        values.clear();
                                        System.out.println("云端日记插入成功");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }


        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            // Toast.makeText(getApplicationContext(),"moveToFirst",Toast.LENGTH_SHORT).show();
            do {
                //String date = cursor.getString(cursor.getColumnIndex("date"));
                //String dateSystem = com.example.xiwei.dairy.schedule.GetDate.getDate().toString();
                if (cursor.getCount() > 0) {// date.equals(dateSystem)
                    // Toast.makeText(getApplicationContext(),"want to remove ",Toast.LENGTH_SHORT).show();
                    mMainLlMain.removeView(mItemFirst);
                    break;
                }
            } while (cursor.moveToNext());
        }

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                String review = cursor.getString(cursor.getColumnIndex("review"));
                fDiaryBeanList.add(new Dbean(date, title, content, tag, review));

            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = fDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(fDiaryBeanList.get(i));
        }

        Collections.sort(diaryList);

        fDiaryBeanList = diaryList;
        return fDiaryBeanList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
