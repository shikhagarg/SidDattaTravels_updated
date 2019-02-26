package com.siddattatravels.siddattatravels.activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.siddattatravels.siddattatravels.R;
import com.siddattatravels.siddattatravels.login.SessionManager;


public class MainActivity extends AppCompatActivity {

   SessionManager session;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

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
