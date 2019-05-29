package com.siddhathatravels.siddhathatravels.activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.siddhathatravels.siddhathatravels.R;
import com.siddhathatravels.siddhathatravels.login.SessionManager;


public class MainActivity extends AppCompatActivity {

   SessionManager session;
    private SharedPreferences prefs;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
       prefs = getSharedPreferences("MyPref",0);
       SharedPreferences.Editor editor = prefs.edit();
       editor.putBoolean("initial_load",true);
       editor.commit();

   }

    @Override
    protected void onResume() {
        super.onResume();
        session = new SessionManager(this);
        session.checkLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }
}
