package com.siddattatravels.siddattatravels.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.net.InetAddress;

public class Utility {

    private static String TAG = "Util Class";

    public static boolean editTextIsEmpty(EditText edittext) {
        if (edittext.getText().toString().trim().length() < 1)
            return true;
        else
            return false;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

//    public void editTextListener(final EditText editText,final String errorText) {
//        editText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (editTextIsEmpty(editText) && editText.isEnabled())
//                    editText.setError(errorText);
//                else
//                    editText.setError(null);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (editTextIsEmpty(editText) && editText.isEnabled())
//                    editText.setError("Nodig");
//                else
//                    editText.setError(null);
//            }
//        });
  //  }



}
