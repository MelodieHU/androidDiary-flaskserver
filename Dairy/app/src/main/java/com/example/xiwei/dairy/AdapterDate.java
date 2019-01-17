package com.example.xiwei.dairy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiwei.dairy.wdairy.DDbase;

import java.util.ArrayList;
import java.util.List;


public class AdapterDate extends BaseAdapter {

    private Context context;
    private List<Integer> days = new ArrayList<>();
    //日历数据
    private List<Boolean> status = new ArrayList<>();
    //签到状态，实际应用中初始化签到状态可通过该字段传递
    private OnSignedSuccess onSignedSuccess;
    //签到成功的回调方法，相应的可自行添加签到失败时的回调方法

    private DDbase mHelper;
    private static int cc = 0;

    public AdapterDate(Context context) {
        this.context = context;
        int maxDay = DateUtil.getCurrentMonthLastDay();//获取当月天数
        String yearMonth = DateUtil.getCurrentYearAndMonth();


        mHelper = new DDbase(context, "Diary2.db", null, 1);
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null, null);


        for (int i = 0; i < DateUtil.getFirstDayOfMonth() - 1; i++) {
            //DateUtil.getFirstDayOfMonth()获取当月第一天是星期几，星期日是第一天，依次类推
            days.add(0);
            //0代表需要隐藏的item
            status.add(false);
            //false代表为签到状态
        }

        for (int i = 0; i < maxDay; i++) {
            days.add(i+1);
            //初始化日历数据
            int flag = 0;
            if (cursor.moveToFirst()) {
                // Toast.makeText(getApplicationContext(),"moveToFirst",Toast.LENGTH_SHORT).show();
                do {
                    String dateInDb = cursor.getString(cursor.getColumnIndex("date"));
                    int j = i + 1;
                    String this_date = yearMonth + j + "日";
                    if (dateInDb.equals(this_date)) {// date.equals(dateSystem)
                        status.add(true);
                        flag = 1;
                        break;
                    }
                } while (cursor.moveToNext());
            }
            if(flag == 0){
                // cc = 0;
                status.add(false);
                //初始化日历签到状态
            }
            else{
                cc++;
            }
        }
        System.out.println("cc="+cc);
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int i) {
        return days.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_gv,null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv = view.findViewById(R.id.tvWeek);
        viewHolder.rlItem = view.findViewById(R.id.rlItem);
        viewHolder.ivStatus = view.findViewById(R.id.ivStatus);
        viewHolder.tv.setText(days.get(i)+"");
        if(days.get(i)==0){
            viewHolder.rlItem.setVisibility(View.GONE);
        }
        if(status.get(i)){
            viewHolder.tv.setTextColor(Color.parseColor("#FD0000"));
            viewHolder.ivStatus.setVisibility(View.VISIBLE);
        }else{
            viewHolder.tv.setTextColor(Color.parseColor("#666666"));
            viewHolder.ivStatus.setVisibility(View.GONE);
        }
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.get(i)){
                    Toast.makeText(context,"Already sign in!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"Sign in success!",Toast.LENGTH_SHORT).show();
                    status.set(i,true);
                    notifyDataSetChanged();
                    if(onSignedSuccess!=null){
                        onSignedSuccess.OnSignedSuccess();
                    }
                }
            }
        });*/
        return view;
    }

    class ViewHolder{
        RelativeLayout rlItem;
        TextView tv;
        ImageView ivStatus;
    }

    public void setOnSignedSuccess(OnSignedSuccess onSignedSuccess){
        this.onSignedSuccess = onSignedSuccess;
    }
}