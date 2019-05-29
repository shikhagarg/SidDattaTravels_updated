package com.siddhathatravels.siddhathatravels.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
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
import com.siddhathatravels.siddhathatravels.Adapter.CustomFragmentPagerAdapter;
import com.siddhathatravels.siddhathatravels.Model.UserProfile;
import com.siddhathatravels.siddhathatravels.R;
import com.siddhathatravels.siddhathatravels.login.SessionManager;


public class TabBarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseUser firebaseUser;
    private UserProfile user;
    private ProgressDialog progressBar;
    private SessionManager session;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        session = new SessionManager(this);
        mAuth = FirebaseAuth.getInstance();


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
//
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("UserProfile/");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser.getPhoneNumber() != null) {
            String phone = firebaseUser.getPhoneNumber();

            myRef.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   // progressBar.dismiss();

                    user = dataSnapshot.getValue(UserProfile.class);
                        if(user!=null && user.isAdmin)
                        {
                            Bundle bundle =new Bundle();
                            bundle.putParcelable("UserProfile",user);
                            CustomFragmentPagerAdapter viewPagerAdapetr = new CustomFragmentPagerAdapter(getSupportFragmentManager(),TabBarActivity.this,true,bundle);
//
//                            viewPagerAdapetr.AddFragment(new SearchFragment(),getString(R.string.search_tab_title));
//                            RegisterFragment registerFragment=new RegisterFragment();
//                            registerFragment.setArguments(bundle);
//                            viewPagerAdapetr.AddFragment(registerFragment,getString(R.string.profile_tab_title));
//                            viewPagerAdapetr.AddFragment(new NotificationFragment(),getString(R.string.profile_tab_title));
                            //tabLayout.setupWithViewPager(viewPager);
                            viewPager.setAdapter(viewPagerAdapetr);
                            tabLayout.setupWithViewPager(viewPager);
                            tabLayout.getTabAt(0).setText("Search");
                            tabLayout.getTabAt(1).setText("Profile");
                            //tabLayout.getTabAt(2).setCustomView(viewPagerAdapetr.getTabView(2));

                        }
                        else
                        {
                            final CustomFragmentPagerAdapter viewPagerAdapetr = new CustomFragmentPagerAdapter(getSupportFragmentManager(),TabBarActivity.this,false,null);
//                            Bundle bundle =new Bundle();
//                            bundle.putParcelable("UserProfile",user);
//                            RegisterFragment registerFragment=new RegisterFragment();
//                            registerFragment.setArguments(bundle);
//                            viewPagerAdapetr.AddFragment(registerFragment,getString(R.string.profile_tab_title));
                            viewPager.setAdapter(viewPagerAdapetr);
                            tabLayout.getTabAt(0).setText("Profile");
                        }
                        //viewPager.setAdapter(viewPagerAdapetr);

                    }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });
        }

        if(tabLayout.getTabAt(2) != null)
        {
            tabLayout.getTabAt(2).setCustomView(R.layout.badgeview_layout);
        }

    }


//    class ViewPagerAdapter extends FragmentPagerAdapter{
//
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            return mFragmentList.get(i);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void AddFragment(Fragment fragment,String title)
//        {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position)
//        {
//            return mFragmentTitleList.get(position);
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
