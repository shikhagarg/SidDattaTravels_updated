package com.siddattatravels.siddattatravels.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.siddattatravels.siddattatravels.Model.UserProfile;
import com.siddattatravels.siddattatravels.R;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private  List<UserProfile> userProfiles;

    public StudentListAdapter(List<UserProfile> userProfiles)
    {
        this.userProfiles = userProfiles;
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
        viewHolder.phoneNumber.setText(userProfiles.get(i).studentPh);
    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name, phoneNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_student_name);
            phoneNumber = itemView.findViewById(R.id.tv_phone_number);
        }


    }

    public void updateList(List<UserProfile> list){
        userProfiles = list;
        notifyDataSetChanged();
    }
}
