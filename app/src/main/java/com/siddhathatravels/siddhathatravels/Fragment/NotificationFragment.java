package com.siddhathatravels.siddhathatravels.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siddhathatravels.siddhathatravels.Adapter.NotificationListAdapter;
import com.siddhathatravels.siddhathatravels.DBHelper.DataBaseHelper;
import com.siddhathatravels.siddhathatravels.R;

import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView notificationList;
    DataBaseHelper dbHelper;
    Context mContext;
    private List<String> newUsers ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.notification_fragment,container,false);
        return view;  //super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DataBaseHelper(mContext);
        newUsers  = dbHelper.getAllUsers();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notificationList = view.findViewById(R.id.lv_notification);
        NotificationListAdapter adapter = new NotificationListAdapter(newUsers,mContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        notificationList.setLayoutManager(mLayoutManager);
        notificationList.setItemAnimator(new DefaultItemAnimator());
        notificationList.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        notificationList.setAdapter(adapter);

    }
}
