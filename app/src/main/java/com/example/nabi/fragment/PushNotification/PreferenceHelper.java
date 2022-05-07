package com.example.nabi.fragment.PushNotification;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.share.Share;

public class PreferenceHelper {
    private static final String DEFAULT_SHARED_PREF_FILE_NAME = "sample preference";
    private static final Boolean DEFAULT_BOOLEAN_VALUE = true;  // 스위치는 default가 true
    private static final Boolean DEFAULT_NOTIFICATION_TIME_VALUE = true;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(DEFAULT_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
    }
    public static void setBoolean(Context context, String key, Boolean value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getBoolean(key, DEFAULT_BOOLEAN_VALUE);
    }

    public static void setNotification_TimeBoolean(Context context, Boolean value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("setNotificationTime", value);
        editor.commit();
    }

    public static Boolean getNotification_TimeBoolean(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getBoolean("setNotificationTime", DEFAULT_NOTIFICATION_TIME_VALUE);
    }

    public static void setGloomy(Context context, int value){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("User_Gloomy", value);
        editor.commit();
    }

    public static int getGloomy(Context context){
        SharedPreferences prefs = getPreferences(context);
        //User_Gloomy라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 -1를 반환
        return prefs.getInt("User_Gloomy", -1);
    }

    public static void setTomorrowWeather(Context context, int value){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Tomorrow_weather", value);
        editor.commit();
    }

    public static int getTomorrowWeather(Context context){
        SharedPreferences prefs = getPreferences(context);
        return prefs.getInt("Tomorrow_weather", -1);
    }

    public static void setStep(Context context, int value){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Temp_walk", value);
        editor.commit();
    }

    public static int getStep(Context context){
        SharedPreferences prefs = getPreferences(context);
        return prefs.getInt("Temp_walk", 0);
    }

    public static void setDate(Context context, String date){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Date", date);
        editor.commit();
    }

    public static String getDate(Context context){
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString("Date","");
    }

    public static void setMeditate(Context context, int value){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Temp_meditate", value);
        editor.commit();
    }

    public static int getMeditate(Context context){
        SharedPreferences prefs = getPreferences(context);
        return prefs.getInt("Temp_meditate", 0);
    }





    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }

    public static void clear(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }
}
