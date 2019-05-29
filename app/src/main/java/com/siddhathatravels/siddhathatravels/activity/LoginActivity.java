package com.siddhathatravels.siddhathatravels.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.siddhathatravels.siddhathatravels.R;
import com.siddhathatravels.siddhathatravels.login.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {


    private static final long ONE_HOUR_MILLI = 60*60*1000;
    private static String uniqueIdentifier = null;
    private static final String UNIQUE_ID = "UNIQUE_ID";

    private static final String TAG = "FirebasePhoneNumAuth";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth firebaseAuth;

    private String phoneNumber;
    private Button sendCodeButton;
    private Button verifyCodeButton;

    private EditText phoneNum;
    private EditText verifyCodeET;
    private TextView mLoginText;
    private FirebaseFirestore firestoreDB;
    private FirebaseUser firebaseUser;
    ProgressDialog progress;
    private Toolbar toolbar;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private SessionManager session;

    private  PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialiseViews();
        showSendCodeButton();
        createCallback();
        getInstallationIdentifier();
        // getVerificationDataFromFirestoreAndVerify(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initialiseViews()
    {
        sendCodeButton = findViewById(R.id.send_code_b);
        verifyCodeButton = findViewById(R.id.verify_code_b);
        phoneNum = findViewById(R.id.phone);
        verifyCodeET = findViewById(R.id.phone_auth_code);
        mLoginText = findViewById(R.id.login_text);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addOnClickListeners();
        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();

        session = new SessionManager(this);
    }

    private void addOnClickListeners() {
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(phoneNum);
                verifyPhoneNumberInit();

            }
        });
        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(verifyCodeET);
                progress = new ProgressDialog(LoginActivity.this);
                progress.setTitle("Loading");
                progress.setMessage("Wait while loading...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                verifyPhoneNumberCode();
            }
        });
    }

    private void createCallback() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "verification completed" + credential);
                progress.dismiss();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progress.dismiss();
                Log.w(TAG, "verification failed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phoneNum.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(LoginActivity.this,
                            "Trying too many timeS",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "code sent " + verificationId);
                progress.dismiss();
                findViewById(R.id.phone_auth_items).setVisibility(View.GONE);
                findViewById(R.id.phone_auth_code_items).setVisibility(View.VISIBLE);

                mLoginText.setText(String.format(getString(R.string.login_text),"+91-"+phoneNumber));
                addVerificationDataToFirestore(phoneNumber, verificationId);
                mResendToken = token;
            }
        };

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    System.out.println("User logged in");
                }
                else{
                    System.out.println("User not logged in");
                }
            }
        };
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNum.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

    private void verifyPhoneNumberInit() {
        phoneNumber = phoneNum.getText().toString();
        if (!validatePhoneNumber(phoneNumber)) {
            return;
        }
        verifyPhoneNumber(phoneNumber);

    }

    private void verifyPhoneNumber(String phno){
        String number = "+91"+ phno;
        progress = new ProgressDialog(LoginActivity.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 70,
                TimeUnit.SECONDS, this, callbacks,mResendToken);
    }


    private void verifyPhoneNumberCode() {
        final String phone_code = verifyCodeET.getText().toString();
        getVerificationDataFromFirestoreAndVerify(phone_code);
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.dismiss();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "code verified signIn successful");
                            firebaseUser = task.getResult().getUser();
//                            showSingInButtons();
                            if(firebaseUser!=null)
                            {
                                session.createLoginSession(phoneNumber);
                                Intent intent = new Intent(LoginActivity.this,TabBarActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                        } else {
                            Log.w(TAG, "code verification failed", task.getException());
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                verifyCodeET.setError("Invalid code.");
                            }
                        }
                    }
                });

        firebaseAuth.signInWithCredential(credential)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        progress.dismiss();
                        if (e instanceof FirebaseAuthException)
                        {
                            ((FirebaseAuthException) e).getErrorCode();
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG);
                        }
                    }
                });
    }
    private void createCredentialSignIn(String verificationId, String verifyCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.
                getCredential(verificationId, verifyCode);
        signInWithPhoneAuthCredential(credential);
    }




    private void getVerificationDataFromFirestoreAndVerify(final String code) {
        //initButtons();
        firestoreDB.collection("phoneAuth").document(uniqueIdentifier)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            if(ds.exists()){
                                // disableSendCodeButton(ds.getLong("timestamp"));
                                if(code != null){
                                    createCredentialSignIn(ds.getString("verificationId"),
                                            code);
                                }else{
                                    verifyPhoneNumber(ds.getString("phone"));
                                }
                            }else{
                                showSendCodeButton();
                                Log.d(TAG, "Code hasn't been sent yet");
                            }

                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });
    }

    public synchronized void getInstallationIdentifier() {
        if (uniqueIdentifier == null) {
            SharedPreferences sharedPrefs = this.getSharedPreferences(
                    UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueIdentifier = sharedPrefs.getString(UNIQUE_ID, null);
            if (uniqueIdentifier == null) {
                uniqueIdentifier = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(UNIQUE_ID, uniqueIdentifier);
                editor.apply();
            }
        }
    }


    private void showSendCodeButton(){
        findViewById(R.id.phone_auth_items).setVisibility(View.VISIBLE);
        findViewById(R.id.phone_auth_code_items).setVisibility(View.GONE);
    }
    private void showSignInButtons(){
        findViewById(R.id.phone_auth_items).setVisibility(View.GONE);
        findViewById(R.id.phone_auth_code_items).setVisibility(View.GONE);
    }
    private void initButtons(){
        findViewById(R.id.phone_auth_items).setVisibility(View.GONE);
        findViewById(R.id.phone_auth_code_items).setVisibility(View.GONE);
    }



    public void HideKeyboard(EditText editText)
    {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }


        private void addVerificationDataToFirestore(String phone, String verificationId) {
        Map verifyMap = new HashMap();

        verifyMap.put("phone", phone);
        verifyMap.put("verificationId", verificationId);
        verifyMap.put("timestamp",System.currentTimeMillis());

        firestoreDB.collection("phoneAuth").document(uniqueIdentifier)
                .set(verifyMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "phone auth info added to db ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding phone auth info", e);
                    }
                });
    }

    //    private void disableSendCodeButton(long codeSentTimestamp){
//        long timeElapsed = System.currentTimeMillis()- codeSentTimestamp;
//        if(timeElapsed > ONE_HOUR_MILLI){
//            showSendCodeButton();
//        }else{
//            findViewById(R.id.phone_auth_items).setVisibility(View.GONE);
//            findViewById(R.id.phone_auth_code_items).setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

}
