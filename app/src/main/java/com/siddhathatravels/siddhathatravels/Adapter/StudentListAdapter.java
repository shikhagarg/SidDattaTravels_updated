package com.siddhathatravels.siddhathatravels.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siddhathatravels.siddhathatravels.Model.UserProfile;
import com.siddhathatravels.siddhathatravels.R;
import com.siddhathatravels.siddhathatravels.activity.StudentProfileActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private  HashMap<String,UserProfile> phoneNumbers;
    private List<UserProfile> userProfiles;
    Context mContext;

    public StudentListAdapter(HashMap<String,UserProfile> phoneNumbers, Context context)
    {
        this.phoneNumbers = phoneNumbers;
        mContext =context;
        userProfiles = new ArrayList<UserProfile>(phoneNumbers.values());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_student,viewGroup,false);

        return  new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.name.setText(userProfiles.get(i).studentName);
        viewHolder.registeredPhoneNo.setText(userProfiles.get(i).registeredPhone);

        if(userProfiles.get(i).studentPh!=null)
            viewHolder.phoneNumber.setText(" , "+userProfiles.get(i).studentPh);

    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView name, phoneNumber,registeredPhoneNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_student_name);
            phoneNumber = itemView.findViewById(R.id.tv_phone_number);
            registeredPhoneNo = itemView.findViewById(R.id.tv_registered_phone_number);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            UserProfile user = userProfiles.get(getAdapterPosition());
            Intent intent = new Intent(mContext,StudentProfileActivity.class);
            intent.putExtra("UserProfiles",user);
            mContext.startActivity(intent);

        }
    }

    public void updateList(UserProfile userProfile){
        //phoneNumbers = list;
        userProfiles.add(userProfile);
        //userProfiles = new ArrayList<UserProfile>(phoneNumbers.values());
        //notifyDataSetChanged();
    }
    public void updateSearchList(List<UserProfile> list){
        userProfiles = list;
        notifyDataSetChanged();
    }
}
