package com.siddattatravels.siddattatravels.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.siddattatravels.siddattatravels.Fragment.RegisterFragment;
import com.siddattatravels.siddattatravels.Fragment.SearchFragment;
import com.siddattatravels.siddattatravels.Model.UserProfile;
import com.siddattatravels.siddattatravels.R;
import com.siddattatravels.siddattatravels.login.SessionManager;

import java.util.ArrayList;
import java.util.List;


public class TabBarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseUser firebaseUser;
    private UserProfile user;
    private ProgressDialog progressBar;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        setUpViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        session = new SessionManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                session.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void setUpViewPager(final ViewPager viewPager)
    {
//        progressBar.setMessage("loading");
//        progressBar.setCancelable(false);
//        progressBar.show();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("UserProfile/");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser.getPhoneNumber() != null) {
            String phone = firebaseUser.getPhoneNumber();

            myRef.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   // progressBar.dismiss();
                    final ViewPagerAdapter viewPagerAdapetr = new ViewPagerAdapter(getSupportFragmentManager());
                    user = dataSnapshot.getValue(UserProfile.class);
                        if(user!=null && user.isAdmin)
                        {
                            Bundle bundle =new Bundle();
                            bundle.putParcelable("UserProfile",user);
                            viewPagerAdapetr.AddFragment(new SearchFragment(),getString(R.string.search_tab_title));
                            RegisterFragment registerFragment=new RegisterFragment();
                            registerFragment.setArguments(bundle);
                            viewPagerAdapetr.AddFragment(registerFragment,getString(R.string.profile_tab_title));
                        }
                        else
                        {

                            Bundle bundle =new Bundle();
                            bundle.putParcelable("UserProfile",user);
                            RegisterFragment registerFragment=new RegisterFragment();
                            registerFragment.setArguments(bundle);
                            viewPagerAdapetr.AddFragment(registerFragment,getString(R.string.profile_tab_title));
                        }
                        viewPager.setAdapter(viewPagerAdapetr);

                    }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });
        }



    }


    class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void AddFragment(Fragment fragment,String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitleList.get(position);
        }
    }


}
