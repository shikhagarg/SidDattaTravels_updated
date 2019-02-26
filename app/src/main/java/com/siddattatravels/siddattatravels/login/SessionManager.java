package com.siddattatravels.siddattatravels.login;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.firebase.auth.FirebaseAuth;
import com.siddattatravels.siddattatravels.activity.LoginActivity;
import com.siddattatravels.siddattatravels.activity.TabBarActivity;

public class SessionManager {

    private SharedPreferences prefs;
    private static final String PREF_NAME = "SidDattaPrefs";
    private  static final String IS_LOGIN = "IsLoggedIn";
   // private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private Editor editor;
    private Context mContext;
    private int PRIVATE_MODE = 0;

    public SessionManager(Context context)
    {
        mContext = context;
        prefs = mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = prefs.edit();
        editor.apply();
    }


    public void createLoginSession(String phoneNumber)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_PHONE,phoneNumber);
        editor.commit();
    }


    public String getUserDetails()
    {
        return prefs.getString(KEY_PHONE,null);
    }


    public void logoutUser()
    {
        editor.clear();
        editor.commit();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(mContext,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }

    public void checkLogin()
    {
        if(!isLoggedIn())
        {
            Intent intent = new Intent(mContext,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(intent);
        }
        else
            {
                Intent intent = new Intent(mContext,TabBarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
        }

    }

    private boolean isLoggedIn()
    {
        return prefs.getBoolean(IS_LOGIN,false);
    }

}
