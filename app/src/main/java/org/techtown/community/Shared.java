package org.techtown.community;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Shared {
    public final String APP_SHARED_PREFS = "thisApp.SharedPreference";
    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public Shared(Context context){
        this.sharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }
    public void setSharedMainlist(String[] emaillist, String[] nicknamelist, String[] passwordlist){
        for(int i = 0; i<100; i++) {
            if(emaillist[i] == null){
                i = 100;
            }else {
                editor.putString("emaillist" + i, emaillist[i]);
                editor.putString("nicknamelist"+i,nicknamelist[i]);
                editor.putString("passwordlist"+i,passwordlist[i]);
            }
        }
        editor.commit();
    }
    public void setSharedMainitem(String[] itemnickname, String[] itememail, String[] itemhead, String[] itembody, int[] itemnumber){
        for(int i = 0; i<100; i++) {
            if(itemnickname[i] == null){
                i = 100;
            }else {
                editor.putString("itemnickname" + i, itemnickname[i]);
                editor.putString("itememail"+i,itememail[i]);
                editor.putString("itemhead"+i,itemhead[i]);
                editor.putString("itembody"+i,itembody[i]);
                editor.putInt("itemnumber"+i,itemnumber[i]);
            }
        }
        editor.commit();
    }
    public void setSharedMainstate(int login, String nowemail, String nownickname){
        editor.putInt("login",login);
        editor.putString("nowemail",nowemail);
        editor.putString("nownickname",nownickname);
    }
    public void setSharedinform(String[] nicknamelist, String[] itemnickname, String nownickname){
        for(int i = 0; i<100; i++) {
            if(nicknamelist[i] == null){
                i = 100;
            }else {
                editor.putString("nicknamelist"+i,nicknamelist[i]);
            }
        }
        for(int i = 0; i<100; i++) {
            if(itemnickname[i] == null){
                i = 100;
            }else {
                editor.putString("itemnickname" + i, itemnickname[i]);
            }
        }
        editor.putString("nownickname",nownickname);
        editor.commit();
    }
    public void setSharedpost(String[] itemhead, String[] itembody){
        for(int i = 0; i<100; i++) {
            if(itemhead[i] == null){
                i = 100;
            }else {
                editor.putString("itemhead"+i,itemhead[i]);
                editor.putString("itembody"+i,itembody[i]);
            }
        }
        editor.commit();
    }
    public void setSharedsign(){

    }
    public String[] getSharedemaillist(){
        String[] emaillist = new String[100];
        for(int i = 0; i<100; i++) {
            if (sharedPreferences.getString("emaillist" + i, null) == null) {
                i = 100;
            }else {
                emaillist[i] = sharedPreferences.getString("emaillist" + i, null); // "test"는 키, "defValue"는 키에 대한 값이 없을 경우 리턴해줄 값 }
            }
        }
            return emaillist;
    }
    public String[] getSharednicknamelist(){
        String[] nicknamelist = new String[100];
        for(int i = 0; i<100; i++) {
            if (sharedPreferences.getString("nicknamelist" + i, null) == null) {
                i = 100;
            }else {
                nicknamelist[i] = sharedPreferences.getString("nicknamelist" + i, null); // "test"는 키, "defValue"는 키에 대한 값이 없을 경우 리턴해줄 값 }
            }
        }
        return nicknamelist;
    }
    public String[] getSharedpasswordlist(){
        String[] passwordlist = new String[100];
        for(int i = 0; i<100; i++) {
            if (sharedPreferences.getString("passwordlist" + i, null) == null) {
                i = 100;
            }else {
                passwordlist[i] = sharedPreferences.getString("passwordlist" + i, null); // "test"는 키, "defValue"는 키에 대한 값이 없을 경우 리턴해줄 값 }
            }
        }
        return passwordlist;
    }
    public String[] getShareditemnickname(){
        String[] itemnickname = new String[100];
        for(int i = 0; i<100; i++) {
            if (sharedPreferences.getString("itemnickname" + i, null) == null) {
                i = 100;
            }else {
                itemnickname[i] = sharedPreferences.getString("itemnickname" + i, null); // "test"는 키, "defValue"는 키에 대한 값이 없을 경우 리턴해줄 값 }
            }
        }
        return itemnickname;
    }
    public String[] getShareditememail(){
        String[] itememail = new String[100];
        for(int i = 0; i<100; i++) {
            if (sharedPreferences.getString("itememail" + i, null) == null) {
                i = 100;
            }else {
                itememail[i] = sharedPreferences.getString("itememail" + i, null); // "test"는 키, "defValue"는 키에 대한 값이 없을 경우 리턴해줄 값 }
            }
        }
        return itememail;
    }
    public String[] getShareditemhead(){
        String[] itemhead = new String[100];
        for(int i = 0; i<100; i++) {
            if (sharedPreferences.getString("itemhead" + i, null) == null) {
                i = 100;
            }else {
                itemhead[i] = sharedPreferences.getString("itemhead" + i, null); // "test"는 키, "defValue"는 키에 대한 값이 없을 경우 리턴해줄 값 }
            }
        }
        return itemhead;
    }
    public String[] getShareditembody(){
        String[] itembody = new String[100];
        for(int i = 0; i<100; i++) {
            if (sharedPreferences.getString("itembody" + i, null) == null) {
                i = 100;
            }else {
                itembody[i] = sharedPreferences.getString("itembody" + i, null); // "test"는 키, "defValue"는 키에 대한 값이 없을 경우 리턴해줄 값 }
            }
        }
        return itembody;
    }
    public int[] getShareditemnumber(){
        int[] itemnumber = new int[100];
        for(int i = 0; i<100; i++) {
                itemnumber[i] = sharedPreferences.getInt("itemnumber" + i, 0); // "test"는 키, "defValue"는 키에 대한 값이 없을 경우 리턴해줄 값 }
        }
        return itemnumber;
    }
    public int getSharedlogin(){
        return sharedPreferences.getInt("login",0);
    }
    public String getSharednownickname(){
        return sharedPreferences.getString("nownickname",null);
    }
    public String getSharednowemail(){
        return sharedPreferences.getString("nowemail",null);
    }
}
