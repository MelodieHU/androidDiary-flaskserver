package com.example.xiwei.dairy.ui.login;


public  class ServerIP
{
    //120.25.232.120:8070
    //192.168.253.1:8070
    private static final String HOST = "192.168.1.127:8000";
    public static final String LOGURL="http://"+HOST+"/user";
    public static final String SIGNURL="http://"+HOST+"/register";
    public static final String POSTURL = "http://"+HOST+"/postdata";
    public static final String GETBILLSNAMEURL = "http://"+HOST+"/getbillsname";
    public static final String POSTBILLNAMEURL = "http://"+HOST+"/postBillList";
    public static final String GETDATAURL = "http://"+HOST+"/getdata";
    public static final String POSTDIARYURL = "http://"+HOST+"/postdiary";
    public static final String GETDIARYURL = "http://"+HOST+"/getdiary";
    public static final String POST_TO_FRIEND_URL=POSTURL;
    public static final String CHECK_ID_EXISTED="http://"+HOST+"/checkidexisted";
    public static final String HAS_NEW_MESSAGE_URL="http://"+HOST+"/hasnewmessage";
    public static final String DELETE_SERVER_BILLNAME_URL="http://"+HOST+"/deletebillname";
    public static final String DELETE_ALL_BILL_URL="http://"+HOST+"/deleteallbill";
    public static final String TESTURL = "http://"+HOST;

}
