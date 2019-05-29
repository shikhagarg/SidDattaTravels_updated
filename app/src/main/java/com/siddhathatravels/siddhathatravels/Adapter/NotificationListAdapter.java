package com.siddhathatravels.siddhathatravels.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siddhathatravels.siddhathatravels.R;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private List<String> userProfiles;
    Context mContext;

    public NotificationListAdapter(List<String> users, Context context)
    {
        mContext =context;
        userProfiles = users;
    }

    @NonNull
    @Override
    public NotificationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item,viewGroup,false);

        return  new NotificationListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.ViewHolder viewHolder, int i) {

        viewHolder.name.setText(userProfiles.get(i) + "just added");

    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_notification);


        }
    }

    public void updateList(String user){
        //phoneNumbers = list;
        userProfiles.add(user);
        //userProfiles = new ArrayList<UserProfile>(phoneNumbers.values());
        //notifyDataSetChanged();
    }

}
