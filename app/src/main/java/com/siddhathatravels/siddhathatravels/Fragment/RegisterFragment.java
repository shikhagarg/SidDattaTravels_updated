package com.siddhathatravels.siddhathatravels.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.siddhathatravels.siddhathatravels.Model.UserProfile;
import com.siddhathatravels.siddhathatravels.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterFragment extends Fragment implements View.OnClickListener {


    FirebaseUser firebaseUser;
    private TextView datetime;
    final Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private TextView tvTerms;
    private CheckBox chkTerms,chkTransport;
    private UserProfile user;
    private EditText edtStudentName, edtFatherName, edtAddress, edtBatch, edtStream, edtRollNo, edtStudentPh, edtFatherPh, edtMotherPh,edtMonthlyCharges;
    private Context mContext;
    private ProgressDialog progressBar;
    private static boolean isEdit = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_registration,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        customizeUI(view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if( getArguments() != null)
                user = getArguments().getParcelable("UserProfile");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SetValues();
                    }
                });

            }
        }).start();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void SetValues()
    {
        if(user != null)
        {
            System.out.print(user.toString());
            edtStudentName.setText(user.studentName);
            edtFatherName.setText(user.fatherName);
            edtAddress.setText(user.address);
            edtStream.setText(user.stream);
            edtBatch.setText(user.batch);
            edtStudentPh.setText(user.studentPh);
            edtFatherPh.setText(user.fatherPh);
            edtMotherPh.setText(user.motherPh);
            edtRollNo.setText(user.rollNo);
            datetime.setText(user.availTransportDate);
            chkTransport.setChecked(user.isAvailingAC);
            chkTransport.jumpDrawablesToCurrentState();
            edtMonthlyCharges.setText(user.monthlyCharges);
        }
    }


    private void customizeUI(View view)
    {

        datetime = view.findViewById(R.id.datetime);
        Button btnSubmit = view.findViewById(R.id.btn_register);
        tvTerms = view.findViewById(R.id.tvTerms);
        edtStudentName = view.findViewById(R.id.edt_name);
        edtFatherName = view.findViewById(R.id.edt_fathers_name);
        edtAddress = view.findViewById(R.id.edt_address);
        edtBatch = view.findViewById(R.id.edt_batch);
        edtStream = view.findViewById(R.id.edt_stream);
        edtRollNo = view.findViewById(R.id.edt_roll_no);
        edtStudentPh = view.findViewById(R.id.edt_student_contact);
        edtFatherPh = view.findViewById(R.id.edt_father_contact);
        edtMotherPh = view.findViewById(R.id.edt_mother_contact);
        chkTerms = view.findViewById(R.id.check_terms);
        chkTransport = view.findViewById(R.id.check_transport);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        edtMonthlyCharges = view.findViewById(R.id.edt_monthly_charges);
        setTermsAndConditions();
        btnSubmit.setOnClickListener(this);
        datetime.setOnClickListener(this);

    }


    public void setDate(View view) {

        new DatePickerDialog(mContext,date,myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datetime.setText(sdf.format(myCalendar.getTime()));
        datetime.setError(null);
    }

    private void setTermsAndConditions()
    {
        SpannableString terms = new SpannableString(getResources().getString(R.string.terms_and_conditions));
        terms.setSpan(new myClickableSpan(1),15,35,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        terms.setSpan(new myClickableSpan(2),40,54,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTerms.setText(terms);
        tvTerms.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showDialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Terms and Conditions");
        alertDialog.setMessage(getResources().getString(R.string.terms_data));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void showPolicyDialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Privacy Policy");
        alertDialog.setMessage(getResources().getString(R.string.policy_data));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_register:
                boolean isValid = validateInputs();

                if(isValid)
                {
                    createUserProfile();
                    //getActivity().finish();
                }
                break;
            case R.id.datetime:
                setDate(v);
                break;

        }

    }


    private void createUserProfile()
    {
        try {
            UserProfile userProfile =  new UserProfile();
            userProfile.studentName = edtStudentName.getText().toString();
            userProfile.fatherName = edtFatherName.getText().toString();
            userProfile.address = edtAddress.getText().toString();
            userProfile.stream = edtStream.getText().toString();
            userProfile.rollNo = edtRollNo.getText().toString();
            userProfile.studentPh = edtStudentPh.getText().toString();
            userProfile.fatherPh = edtFatherPh.getText().toString();
            userProfile.motherPh = edtMotherPh.getText().toString();
            userProfile.batch = edtBatch.getText().toString();
            userProfile.availTransportDate = datetime.getText().toString();//new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH).parse(datetime.getText().toString());
            userProfile.isAvailingAC = chkTransport.isChecked();
            userProfile.monthlyCharges = edtMonthlyCharges.getText().toString();
            if(user !=null && user.isAdmin)
                userProfile.isAdmin = true;

            registerUser(userProfile);
            Toast.makeText(mContext, "Registered successfully", Toast.LENGTH_SHORT).show();


        }
        catch (Exception ex)
        {
            System.out.print("Exception : "+ ex);
        }

    }


    private void registerUser(UserProfile user)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("UserProfile");
        if(firebaseUser.getPhoneNumber() != null)
        {
            mDatabase.child("classified").child(firebaseUser.getPhoneNumber());
            user.registeredPhone = firebaseUser.getPhoneNumber();
            //  user.isAdmin = true;
            mDatabase.child(firebaseUser.getPhoneNumber()).setValue(user);

        }
    }

    private boolean validateInputs()
    {
        if(TextUtils.isEmpty(edtStudentName.getText().toString()))
        {
            edtStudentName.requestFocus();
            edtStudentName.setError("Can not be empty");
            return false;
        }
        if(TextUtils.isEmpty(edtFatherName.getText().toString()))
        {
            edtFatherName.requestFocus();
            edtFatherName.setError("Can not be empty");
            return false;
        }
        if(TextUtils.isEmpty(edtAddress.getText().toString()))
        {
            edtAddress.requestFocus();
            edtAddress.setError("Can not be empty");
            return false;
        }
        if(TextUtils.isEmpty(edtBatch.getText()))
        {
            edtBatch.requestFocus();
            edtBatch.setError("Can not be empty");
            return false;
        }
        if(TextUtils.isEmpty(edtStream.getText()))
        {
            edtStream.requestFocus();
            edtStream.setError("Can not be empty");
            return false;
        }
        if(TextUtils.isEmpty(edtRollNo.getText()))
        {
            edtRollNo.requestFocus();
            edtRollNo.setError("Can not be empty");
            return false;
        }
        if(TextUtils.isEmpty(datetime.getText()))
        {
            datetime.requestFocus();
            datetime.setError("Can not be empty");
            return false;
        }
        if(TextUtils.isEmpty(edtStudentPh.getText()) || edtStudentPh.getText().length() !=  10)
        {
            edtStudentPh.requestFocus();
            edtStudentPh.setError("Can not be empty");
            return false;
        }
        if(TextUtils.isEmpty(edtFatherPh.getText()) || edtStudentPh.getText().length() !=  10)
        {
            edtFatherPh.requestFocus();
            edtFatherPh.setError("Can not be empty");
            return false;
        }
        if(TextUtils.isEmpty(edtMotherPh.getText()) || edtStudentPh.getText().length() !=  10)
        {
            edtMotherPh.requestFocus();
            edtMotherPh.setError("Can not be empty");
            return false;
        }
        if(TextUtils.isEmpty(edtMonthlyCharges.getText()))
        {
            edtMonthlyCharges.requestFocus();
            edtMonthlyCharges.setError("Can not be empty");
            return false;
        }
        if(!chkTerms.isChecked())
        {
            Toast.makeText(mContext, "Please read and agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public class myClickableSpan extends ClickableSpan{

        int pos;
        public myClickableSpan(int position){
            this.pos=position;
        }

        @Override
        public void onClick(View widget) {
           if(pos==1)
               showDialog();
           else
               showPolicyDialog();
        }

    }
}
