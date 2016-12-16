package com.example.user.cram1001.Fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by user on 2016/11/13.
 */

public class GCMUtils {
    public static String getGCMToken(Context context){
        try {
            InstanceID instanceID = InstanceID.getInstance(context);
            String token = instanceID.getToken
                    ("463612255542", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d("GCM token",""+token);
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void saveToken(Context context, String token){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",null);
        editor.commit();
    }
    public static String getSavedToken(Context context){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("token",null);
    }
}
