package com.siddattatravels.siddattatravels.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ListAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.siddattatravels.siddattatravels.Adapter.StudentListAdapter;
import com.siddattatravels.siddattatravels.Model.UserProfile;
import com.siddattatravels.siddattatravels.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    RecyclerView mListView;
    Context mContext;
    EditText mInputSearch;
    StudentListAdapter adapter;
    List<UserProfile> mStudentList =new ArrayList<>();

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

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("UserProfile/");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    UserProfile userProfile = ds.getValue(UserProfile.class);
                    assert userProfile != null;
                    Log.d("TAG", userProfile.toString());

                    if(userProfile.studentName != null)
                     mStudentList.add(userProfile);

                }
                adapter = new StudentListAdapter(mStudentList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mListView.setLayoutManager(mLayoutManager);
                mListView.setItemAnimator(new DefaultItemAnimator());
                mListView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);

        setTextChangedListener();
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
        for(UserProfile d: mStudentList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.studentName.toLowerCase().contains(text.toLowerCase()) || d.studentPh.contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }


}
