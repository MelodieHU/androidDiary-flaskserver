package com.example.xiwei.dairy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xiwei.dairy.ui.login.LoginActivity;
import com.example.xiwei.dairy.AdapterDate;
import com.example.xiwei.dairy.wdairy.DDbase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WriteCalender extends AppCompatActivity {
    private SignDate signDate;
    public static int sTheme;

    private int continuous = 0;
    private DDbase mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sTheme!=0) {
            //设置主题
            setTheme(sTheme);
        }

        setContentView(R.layout.calender);
        signDate = findViewById(R.id.signDate);
        Button changeTheme = (Button) findViewById(R.id.change_theme);

        int Day = DateUtil.getCurrentDay();//获取当天数
        String yearMonth = DateUtil.getCurrentYearAndMonth();

        mHelper = new DDbase(WriteCalender.this, "Diary2.db", null, 1);
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null, null);

        for (int i = 0; i < Day; i++) {
            int flag = 0;
            if (cursor.moveToFirst()) {
                // Toast.makeText(getApplicationContext(),"moveToFirst",Toast.LENGTH_SHORT).show();
                do {
                    String dateInDb = cursor.getString(cursor.getColumnIndex("date"));
                    int j = i + 1;
                    String this_date = yearMonth + j + "日";
                    if (dateInDb.equals(this_date)) {// date.equals(dateSystem)
                        flag = 1;
                        break;
                    }
                } while (cursor.moveToNext());
            }
            if(flag == 0){
                continuous = 0;
                //初始化日历签到状态
            }
            else{
                continuous++;
            }
        }

        // System.out.println("con"+continuous);

        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = 4;
                if (continuous < max) {
                    showNOTSweetDialog("At least "+max+" DAYs needed !");
                } else {
                    // showWarnSweetDialog("You can change theme now !");
                    String[] dessert = new String[]{"抹茶千层", "芒果拿破仑", "草莓雪媚娘", "蓝莓慕斯", "原味黑加仑"};
                    final boolean[] begin = new boolean[]{false, false, false, false, false};
                    android.support.v7.app.AlertDialog.Builder builder3 = new android.support.v7.app.AlertDialog.Builder(WriteCalender.this);
                    builder3.setTitle("Which theme do you prefer?");
                    builder3.setMultiChoiceItems(dessert, begin, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            // Toast.makeText(getApplicationContext(),which+"",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showWarnSweetDialog("You've changed your theme!");
                            for (int j = 0; j < 5; j++) {
                                if (begin[j] == true && j == 0) {
                                    sTheme = R.style.AppThemeDeepBlue;
                                    recreate();
                                    break;
                                }
                                if (begin[j] == true && j == 1) {
                                    sTheme = R.style.AppThemeGreen;
                                    recreate();
                                    break;
                                }
                                if (begin[j] == true && j == 2) {
                                    sTheme = R.style.AppThemeStraw;
                                    recreate();
                                    break;
                                }
                                if (begin[j] == true && j == 3) {
                                    sTheme = R.style.AppThemeBerry;
                                    recreate();
                                    break;
                                }
                                if (begin[j] == true && j == 4) {
                                    sTheme = R.style.AppThemeBlack;
                                    Toast.makeText(getApplicationContext(), "black", Toast.LENGTH_SHORT).show();
                                    recreate();
                                    break;
                                }
                            }

                        }
                    });
                    builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "You have not changed anything!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder3.show();
                }
            }
        });

        /*signDate.setOnSignedSuccess(new OnSignedSuccess() {
            @Override
            public void OnSignedSuccess() {
                Log.e("hxw","Success");
            }
        });*/
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

    private void showNOTSweetDialog(String info)
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(info);
        // pDialog.setCancelable(true);
        pDialog.show();
        /*pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
        {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog)
            {
                pDialog.dismiss();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });*/
    }
}
