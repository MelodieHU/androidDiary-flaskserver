package com.example.xiwei.dairy.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xiwei.dairy.schedule.bean.DiaryBean;
import com.example.xiwei.dairy.R;
import com.example.xiwei.dairy.schedule.db.DiaryDatabaseHelper;
import com.example.xiwei.dairy.schedule.event.StartUpdateDiaryEvent;

import com.example.xiwei.dairy.schedule.GetDate;

// import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DiaryBean> mDiaryBeanList;
    private int mEditPosition = -1;
    private Boolean b_sub_square = false;
    private DiaryDatabaseHelper mHelper;

    public DiaryAdapter(Context context, List<DiaryBean> mDiaryBeanList){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDiaryBeanList = mDiaryBeanList;
    }
    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiaryViewHolder(mLayoutInflater.inflate(R.layout.item_rv_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiaryViewHolder holder, final int position) {

        String dateSystem = GetDate.getDate().toString();
        int c = Integer.valueOf(mDiaryBeanList.get(position).getTag()).intValue();
        boolean check=(c>0)?true:false;

        if(mDiaryBeanList.get(position).getDate().equals(dateSystem)){
            // holder.mIvCircle.setImageResource(R.drawable.circle_orange);
        }
        holder.mTvDate.setText(mDiaryBeanList.get(position).getDate());
        holder.mTvTitle.setText(mDiaryBeanList.get(position).getTitle());
        holder.mTvContent.setText("       " + mDiaryBeanList.get(position).getContent());
        holder.mButton.setChecked(check);
        holder.mIvEdit.setVisibility(View.INVISIBLE);
        if(mEditPosition == position){
            holder.mIvEdit.setVisibility(View.VISIBLE);
        }else {
            holder.mIvEdit.setVisibility(View.GONE);
        }
        holder.mLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mIvEdit.getVisibility() == View.VISIBLE){
                    holder.mIvEdit.setVisibility(View.GONE);
                }else {
                    holder.mIvEdit.setVisibility(View.VISIBLE);
                }
                if(mEditPosition != position){
                    notifyItemChanged(mEditPosition);
                }
                mEditPosition = position;
            }
        });

        holder.mButton.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(holder.mButton.isChecked()){
                    mHelper = new DiaryDatabaseHelper(mContext, "Schedule1.db", null, 1);
                    SQLiteDatabase dbUpdate = mHelper.getWritableDatabase();
                    ContentValues valuesUpdate = new ContentValues();
                    String content = mDiaryBeanList.get(position).getContent();
                    valuesUpdate.put("tag", "1");
                    dbUpdate.update("Schedule", valuesUpdate, "content = ?", new String[]{content});
                }
                else{
                    mHelper = new DiaryDatabaseHelper(mContext, "Schedule1.db", null, 1);
                    SQLiteDatabase dbUpdate = mHelper.getWritableDatabase();
                    ContentValues valuesUpdate = new ContentValues();
                    String content = mDiaryBeanList.get(position).getContent();
                    valuesUpdate.put("tag", "0");
                    dbUpdate.update("Schedule", valuesUpdate, "content = ?", new String[]{content});
                }
            }
        });

        // holder.mButton.setChecked(true);
        /*holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!b_sub_square) {
                    b_sub_square = true;
                    //设置是否被激活状态，true表示被激活
                    holder.mButton.setActivated(b_sub_square);
                }
                else {
                    b_sub_square = false;
                    //设置是否被激活状态，false表示未激活
                    holder.mButton.setActivated(b_sub_square);
                }
            }
        });*/
/*
        holder.mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartUpdateDiaryEvent(position));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mDiaryBeanList.size();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder{

        TextView mTvDate;
        TextView mTvTitle;
        TextView mTvContent;
        ImageView mIvEdit;
        LinearLayout mLlTitle;
        LinearLayout mLl;
        // ImageView mIvCircle;
        LinearLayout mLlControl;
        RelativeLayout mRlEdit;
        CheckBox mButton;

        DiaryViewHolder(View view){
            super(view);
            // mIvCircle = (ImageView) view.findViewById(R.id.main_iv_circle);
            mTvDate = (TextView) view.findViewById(R.id.main_tv_date);
            mTvTitle = (TextView) view.findViewById(R.id.main_tv_title);
            mTvContent = (TextView) view.findViewById(R.id.main_tv_content);
            mIvEdit = (ImageView) view.findViewById(R.id.main_iv_edit);
            mLlTitle = (LinearLayout) view.findViewById(R.id.main_ll_title);
            mLl = (LinearLayout) view.findViewById(R.id.item_ll);
            mLlControl = (LinearLayout) view.findViewById(R.id.item_ll_control);
            mRlEdit = (RelativeLayout) view.findViewById(R.id.item_rl_edit);
            mButton = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }
}