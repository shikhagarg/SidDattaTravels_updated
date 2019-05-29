package com.siddhathatravels.siddhathatravels.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.siddhathatravels.siddhathatravels.Adapter.StudentListAdapter;
import com.siddhathatravels.siddhathatravels.DBHelper.DataBaseHelper;
import com.siddhathatravels.siddhathatravels.Model.UserProfile;
import com.siddhathatravels.siddhathatravels.Model.UserTable;
import com.siddhathatravels.siddhathatravels.R;
import com.siddhathatravels.siddhathatravels.Utility.NotificationID;
import com.siddhathatravels.siddhathatravels.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    RecyclerView mListView;
    Context mContext;
    EditText mInputSearch;
    StudentListAdapter adapter;
    List<UserProfile> mStudentList =new ArrayList<>();
    static HashMap<String,UserProfile> phoneNumbers = new HashMap<>();
    //private static boolean isInitialLoad = true, isChildIntitialLoad = true;
    private NotificationManager notifManager;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    DataBaseHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragemnt,container,false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = view.findViewById(R.id.userlist);
        mInputSearch = view.findViewById(R.id.inputSearch);
        prefs = mContext.getSharedPreferences("MyPref",0);
        dbHelper = new DataBaseHelper(mContext);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference usersdRef = rootRef.child("UserProfile/");

        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        UserProfile userProfile = ds.getValue(UserProfile.class);
                        assert userProfile != null;
                        Log.d("TAG", userProfile.toString());
                        //userProfile.createdAt = getDateTime();
                        dbHelper.insertUser(userProfile);

                        if (userProfile.studentName != null)
                            phoneNumbers.put(userProfile.registeredPhone,userProfile);


                    }
                    adapter = new StudentListAdapter(phoneNumbers,mContext);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    mListView.setLayoutManager(mLayoutManager);
                    mListView.setItemAnimator(new DefaultItemAnimator());
                    mListView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
                    mListView.setAdapter(adapter);

                addChildEventListener(usersdRef);
             Log.d("OnDataChange","Data Changed called");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        usersdRef.addListenerForSingleValueEvent(eventListener);
        setTextChangedListener();

    }


    public void initliaseData(List<UserTable> userTables)
    {
        if(userTables != null)
        {
            for (UserTable user: userTables
                 ) {
                phoneNumbers.put(user.REGISTEREDPHONE,user.getUserProfile());
            }
        }
    }


    public void addChildEventListener(DatabaseReference mRootRef)
    {
        mRootRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                assert userProfile != null;
                Log.d("TAG", userProfile.toString());


                if(phoneNumbers != null && !phoneNumbers.containsKey(userProfile.registeredPhone)) {

                    userProfile.createdAt = getDateTime();
                    dbHelper.insertUser(userProfile);
                        if (userProfile.studentName != null)
                            phoneNumbers.put(userProfile.registeredPhone,userProfile);

                    adapter.updateList(userProfile);
                    adapter.notifyDataSetChanged();
                    createNotification(userProfile);
                }

           }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void createNotification(UserProfile userProfile)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext,"channelID");
        final int NOTIFY_ID = 0;
        String id = mContext.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = mContext.getString(R.string.default_notification_channel_title);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (notifManager == null) {
            notifManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                //mChannel.enableVibration(true);
                //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(mContext, id);
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

            builder.setContentTitle("Datta Travels")                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(userProfile.studentName+ " just added") // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(userProfile.studentName+ " just added")
                    .setSound(alarmSound)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOnlyAlertOnce(true);
        }
        else
        {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setSmallIcon(R.drawable.app_icon);
            builder.setContentTitle("Datta Travels");
            builder.setContentText(userProfile.studentName+ " just added");
            builder.setContentIntent(pendingIntent);
            builder.setWhen(System.currentTimeMillis());
            builder.setAutoCancel(true);
            builder.setSound(alarmSound);
            builder.setOnlyAlertOnce(true);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notifManager.notify(NotificationID.getID(), builder.build());
    }


    private void setTextChangedListener()
    {
        mInputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void filter(String text){
        List<UserProfile> temp = new ArrayList();
        List<UserProfile> list = new ArrayList<>(phoneNumbers.values());

        for(UserProfile d: list){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches

            if((d.studentName!=null && d.studentName.toLowerCase().contains(text.toLowerCase())) ||
                    (d.studentPh!=null && d.studentPh.contains(text) ) || ( d.registeredPhone!=null && d.registeredPhone.contains(text) )){

                temp.add(d);

            }
        }
        //update recyclerview
        adapter.updateSearchList(temp);
        //adapter.notifyDataSetChanged();
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    private boolean containsUserProfile(List<UserProfile> userList,String phone)
    {

        for (UserProfile user: userList
             ) {
            if(user.registeredPhone != null)
            phoneNumbers.put(user.registeredPhone,null);
        }

        if(phoneNumbers!=null && phoneNumbers.size() > 0 && phoneNumbers.containsKey(phone))
            return true;
        else
            return false;
    }

}
