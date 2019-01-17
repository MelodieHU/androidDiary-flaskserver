package com.example.xiwei.dairy;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.example.xiwei.dairy.ui.login.ServerIP.POSTDIARYURL;
import com.example.xiwei.dairy.schedule.db.DiaryDatabaseHelper;
import static com.example.xiwei.dairy.ui.login.SharedPreferenceHelper.getTableNameBySP;
import com.example.xiwei.dairy.ui.login.LoginActivity;

import java.io.IOException;
import java.util.HashMap;

public class WriteDiary<OnClick> extends AppCompatActivity {

    private DiaryDatabaseHelper mHelper;

    public int sTheme = WriteCalender.sTheme;
    //private DiaryDatabaseHelper mHelper;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WriteDiary.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String title, String content) {
        Intent intent = new Intent(context, WriteDiary.class);
        intent.putExtra("title", title);//putExtra实现activity之间的参数传递
        intent.putExtra("content", content);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (sTheme!=0) {
            //设置主题
            setTheme(sTheme);
        }

        setContentView(R.layout.write_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppManager.getAppManager().addActivity(this);

        Button save_diary = (Button) findViewById(R.id.save_diary);

        mHelper = new DiaryDatabaseHelper(this, "Diary2.db", null, 1);
        final EditText mMood = (EditText) findViewById(R.id.add_diary_et_title1);
        final EditText mContent = (EditText) findViewById(R.id.add_diary_et_title2);
        final EditText mReview = (EditText) findViewById(R.id.add_diary_et_title3);


        final HashMap<String,String> paramsMap = new HashMap<>();

        save_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toast.makeText(getApplicationContext(),"Signed Up?",Toast.LENGTH_SHORT).show();
                String date = com.example.xiwei.dairy.schedule.GetDate.getDate().toString();
                // String tag = String.valueOf(System.currentTimeMillis());
                String title = mMood.getText().toString() + "";//mAddDiaryEtTitle.getText().toString() + "";
                String content = mContent.getText().toString() + "";
                String review = mReview.getText().toString() + "";

                // int hour = timePicker.getMinute();

                if (!content.equals("")) {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("date", date);
                    values.put("title", title);
                    values.put("content", content);
                    values.put("tag", "0");
                    values.put("review", review);
                    db.insert("Diary", null, values);
                    values.clear();
                    // Toast.makeText(getApplicationContext(),title + content,Toast.LENGTH_SHORT).show();、


                    /*// 存入哈希表
                    paramsMap.put("username", "silv");
                    paramsMap.put("date", date);
                    paramsMap.put("title", title);
                    paramsMap.put("content", content);
                    paramsMap.put("tag", "0");
                    paramsMap.put("review", review);
                    FormBody.Builder builder = new FormBody.Builder();

                    for (String key : paramsMap.keySet()) {
                        //追加表单信息
                        builder.add(key, paramsMap.get(key));
                    }

                    OkHttpClient okHttpClient=new OkHttpClient();
                    RequestBody formBody=builder.build();
                    Request request = new Request.Builder().url(POSTDIARYURL).post(formBody).build();
                    Call call=okHttpClient.newCall(request);

                    call.enqueue(new Callback()
                    {
                        @Override
                        public void onFailure(Call call, IOException e)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    // showWarnSweetDialog("服务器错误");
                                    System.out.println("服务器错误");
                                }
                            });

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException
                        {
                            //请求成功返回结果
                            //如果希望返回的是字符串
                            final String res = response.body().string();
                            //注意，此时的线程不是ui线程，
                            //如果此时我们要用返回的数据进行ui更新，操作控件，就要使用相关方法
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (res.equals("0"))//从服务器得到的response
                                    {
                                        // showWarnSweetDialog("无此账号,请先注册");
                                        System.out.println("上传过这篇日记了");
                                    }
                                    else if(res.equals("1"))
                                    {
                                        // showWarnSweetDialog("密码不正确");
                                        System.out.println("上传日记成功");
                                        // finish();
                                    }
                                    else
                                    {
                                        // finish();
                                    }

                                }
                            });
                        }
                    });*/

                    showWarnSweetDialog("Diary Successfully Saved!");
                }

                /*Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);*/
            }
        });
    }
    private void showWarnSweetDialog(String info)
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(info);
        pDialog.setCancelable(true);
        pDialog.show();
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
        {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog)
            {
                pDialog.dismiss();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


}
