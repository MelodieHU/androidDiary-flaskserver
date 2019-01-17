package com.example.xiwei.dairy.schedule.bean;


import com.example.xiwei.dairy.wdairy.Dbean;

public class DiaryBean implements Comparable <DiaryBean> {
    private String date;
    private String title;
    private String content;
    private String tag;

    public DiaryBean(String date, String title, String content, String tag) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.tag = tag;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int get_Year(){
        int int_year = 0;
        int pos;
        pos = date.indexOf("年");
        String y = date.substring(0,pos);
        int_year = Integer.parseInt(y);
        System.out.println("y_y = "+y);

        return int_year;
    }

    public int get_Month(){
        int int_month = 0;
        int pos1, pos2;
        pos1 = date.indexOf("年");
        pos2 = date.indexOf("月");
        String y = date.substring(pos1+1,pos2);
        System.out.println("y_m = "+y);
        int_month = Integer.parseInt(y);

        return int_month;
    }

    public int get_Day(){
        int int_day = 0;
        int pos1, pos2;
        pos1 = date.indexOf("月");
        pos2 = date.indexOf("日");
        String y = date.substring(pos1+1,pos2);
        int_day = Integer.parseInt(y);

        return int_day;
    }

    @Override
    public int compareTo(DiaryBean o) {
        DiaryBean p = (DiaryBean)o;
        if(p.get_Year() > this.get_Year()){
            return 1;
        }
        else if(p.get_Year() == this.get_Year()){
            if(p.get_Month() > this.get_Month()){
                return 1;
            }
            else if(p.get_Month() == this.get_Month()){
                if(p.get_Day() > this.get_Day()){
                    return 1;
                }
                else if(p.get_Day() == this.get_Day()){
                    return 0;
                }
                else {
                    return -1;
                }
            }
            else{
                return -1;
            }
        }
        else{
            return -1;
        }
    }

}
