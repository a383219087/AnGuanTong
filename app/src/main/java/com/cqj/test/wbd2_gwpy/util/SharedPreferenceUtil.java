package com.cqj.test.wbd2_gwpy.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作用：SharedPreferences的包装类
 * Created by cqj on 2017-09-14.
 */
public class SharedPreferenceUtil {

    public static final String SP_CONFIG_NAME = "config";

    public static Boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static Boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key,value);
        edit.apply();
    }

    public static void putString(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key,value);
        edit.apply();
    }


    public static void putInt(Context context, String key, int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key,value);
        edit.apply();
    }
}
