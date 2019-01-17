package com.example.xiwei.dairy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.xiwei.dairy.schedule.GetDate;
import com.example.xiwei.dairy.schedule.db.DiaryDatabaseHelper;

import java.util.Calendar;

public class WriteSchedule extends AppCompatActivity {
    private DiaryDatabaseHelper mHelper;
    private DatePicker datePicker;
    private TimePicker timePicker;

    public static final String INTENT_ALARM_LOG = "intent_alarm_log";

    private int hour1;
    private int minute1;

    public int sTheme = WriteCalender.sTheme;

    AlarmManager am;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sTheme!=0) {
            //设置主题
            setTheme(sTheme);
        }

        setContentView(R.layout.addschedule);
        //////
        //Button picktime = (Button) findViewById(R.id.finish_add_schedule);
        Button add_schedule = (Button) findViewById(R.id.finish_add_schedule);
        mHelper = new DiaryDatabaseHelper(this, "Schedule1.db", null, 1);
        final EditText mAddSchedule = (EditText) findViewById(R.id.add_schedule_et_title);
        datePicker = (DatePicker) findViewById(R.id.dpPicker);
        timePicker = (TimePicker) findViewById(R.id.tpPicker);

        add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toast.makeText(getApplicationContext(),"Signed Up?",Toast.LENGTH_SHORT).show();
                // String date = com.example.xiwei.dairy.schedule.GetDate.getDate().toString();
                // String tag = String.valueOf(System.currentTimeMillis());
                String title = "todo";//mAddDiaryEtTitle.getText().toString() + "";
                String content = mAddSchedule.getText().toString() + "";
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+ 1;
                int day = datePicker.getDayOfMonth();
                String date = String.valueOf(year) + "年" + String.valueOf(month) + "月" + String.valueOf(day) + "日";

                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                String time = String.valueOf(hour) + "时" + String.valueOf(minute) + "分";

                if (!content.equals("")) {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("date", date);
                    values.put("time", time);
                    values.put("content", content);
                    values.put("tag", "0");
                    db.insert("Schedule", null, values);
                    values.clear();
                    Toast.makeText(getApplicationContext(),"Schedule Added !",Toast.LENGTH_SHORT).show();
                }

                am = (AlarmManager) getSystemService(ALARM_SERVICE);

                if (Build.VERSION.SDK_INT >= 23) { //to check the version of Android Studio
                    hour1 = timePicker.getHour();
                    minute1 = timePicker.getMinute();
                } else {
                    hour1 = timePicker.getCurrentHour();
                    minute1 = timePicker.getCurrentMinute();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour1); //to set the time for the alarm
                calendar.set(Calendar.MINUTE, minute1);
                calendar.set(Calendar.SECOND, 0);

                Intent intent = new Intent(INTENT_ALARM_LOG);
                PendingIntent pi = PendingIntent.getBroadcast(WriteSchedule.this, 0, intent, 0);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

                Intent i = new Intent(WriteSchedule.this, Schedule.class);
                startActivity(i);
                finish();
            }
        });


        datePicker.init(2019, 0, 8, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                Calendar calendar = Calendar.getInstance();
                //calendar.set(year, monthOfYear, dayOfMonth);
                //SimpleDateFormat format = new SimpleDateFormat(
                //        "yyyy年MM月dd日  HH:mm");
                monthOfYear = monthOfYear+1;
                /*Toast.makeText(WriteSchedule.this,
                        year + "年" +monthOfYear + "月" + dayOfMonth + "日",
                        Toast.LENGTH_SHORT).show();*/
            }
        });


        timePicker.setIs24HourView(true);
        timePicker
                .setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay,
                                              int minute) {
                        /*Toast.makeText(WriteSchedule.this,
                                hourOfDay + "小时" + minute + "分钟",
                                Toast.LENGTH_SHORT).show();*/
                    }
                });

    }
}