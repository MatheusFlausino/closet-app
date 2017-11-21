package com.matheusflausino.closetapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.matheusflausino.closetapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by matheusflausino on 13/11/17.
 */

public class UtilString {
    public static final String PREFERENCES_MODELS = "MODELS";
    public static final String PREFERENCES_COLORS = "COLORS";

    public static final String MODO    = "MODO";
    public static final String ID      = "ID";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    public static boolean stringVazia(String texto){

        if (texto == null || texto.trim().length() == 0){
            return true;
        }else{
            return false;
        }
    }

    public static String getPreference(Context context, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference),
                Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

    public static void setPreference(Context context, String key, String value){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void userPreference(Context context, String key, String value){
        boolean flag = false;
        String p = getPreference(context, key);
        String[] adapter = new Gson().fromJson(p,String[].class);
        List<String> stringList = new ArrayList<String>(Arrays.asList(adapter));

        String[] string = value.split("#");

        for(String str : string) {
            str = str.replaceAll("\\s+$", "");
            if(! stringList.contains(str)){
                stringList.add(str);
                flag = true;
            }
        }

        if(flag) {
            String[] newPreference = new String[stringList.size()];
            newPreference = stringList.toArray(newPreference);
            setPreference(context, key,  new Gson().toJson(newPreference));
        }

    }
}
