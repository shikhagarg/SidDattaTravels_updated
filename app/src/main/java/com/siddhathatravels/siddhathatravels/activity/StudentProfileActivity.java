package com.siddhathatravels.siddhathatravels.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.siddhathatravels.siddhathatravels.Model.UserProfile;
import com.siddhathatravels.siddhathatravels.R;

public class StudentProfileActivity extends AppCompatActivity {

    UserProfile user;
    TextView tvStudentName,tvFatherName,tvAddress,tvBatch,tvStream,tvRollNumber,tvDateOfTransport,tvStudentPh,tvMotherPh,tvFatherPh,tvAvailAc,tvMontlhyCharges;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);



        tvStudentName = findViewById(R.id.tv_name);
        tvFatherName = findViewById(R.id.tv_fathers_name);
        tvAddress = findViewById(R.id.tv_address);
        tvBatch = findViewById(R.id.tv_batch);
        tvStream = findViewById(R.id.tv_stream);
        tvRollNumber = findViewById(R.id.tv_roll_no);
        tvDateOfTransport = findViewById(R.id.tvdatetime);
        tvStudentPh = findViewById(R.id.tv_student_contact);
        tvMotherPh = findViewById(R.id.tv_mother_contact);
        tvFatherPh = findViewById(R.id.tv_father_contact);
        tvAvailAc = findViewById(R.id.check_transport);
        tvMontlhyCharges = findViewById(R.id.tv_monthly_charges);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = getIntent().getExtras().getParcelable("UserProfiles");

        if(user.studentName != null)
            tvStudentName.setText(tvStudentName.getText()+ " : "+ user.studentName);

        if(user.fatherName != null)
        tvFatherName.setText(tvFatherName.getText()+ " : "+user.fatherName);


        if(user.address != null)
        tvAddress.setText(tvAddress.getText()+ " : "+user.address);

        if(user.batch != null)
        tvBatch.setText(tvBatch.getText()+ " : "+user.batch);

        if(user.stream != null)
        tvStream.setText(tvStream.getText()+ " : "+user.stream);

        if(user.rollNo != null)
        tvRollNumber.setText(tvRollNumber.getText()+ " : "+user.rollNo);

        if(user.availTransportDate != null)
        tvDateOfTransport.setText(tvDateOfTransport.getText()+ " : "+user.availTransportDate);

        if(user.studentPh != null)
        tvStudentPh.setText(tvStudentPh.getText()+ " : "+user.studentPh);

        if(user.motherPh != null)
        tvMotherPh.setText(tvMotherPh.getText()+ " : "+user.motherPh);

        if(user.fatherPh != null)
        tvFatherPh.setText(tvFatherPh.getText()+ " : "+user.fatherPh);

        if(user.monthlyCharges != null)
            tvMontlhyCharges.setText(tvMontlhyCharges.getText()+ " : "+user.monthlyCharges);

        if(user.isAvailingAC)
            tvAvailAc.setText(tvAvailAc.getText()+ " : Yes");
        else
            tvAvailAc.setText(tvAvailAc.getText()+ " : No");
    }
}
