package com.siddhathatravels.siddhathatravels.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.siddhathatravels.siddhathatravels.Fragment.NotificationFragment;
import com.siddhathatravels.siddhathatravels.Fragment.RegisterFragment;
import com.siddhathatravels.siddhathatravels.Fragment.SearchFragment;
import com.siddhathatravels.siddhathatravels.R;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Search", "Profile" ,"Count"};
    private Context mContext;
    private boolean isAdmin;
    private Bundle bundle;

    public CustomFragmentPagerAdapter(FragmentManager fm, Context context, boolean isAdmin,Bundle bundle) {
        super(fm);
        mContext = context;
        this.isAdmin = isAdmin;
        this.bundle = bundle;
    }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(mContext).inflate(R.layout.badgeview_layout, null);
            TextView tv_title = v.findViewById(R.id.textview);
            TextView tv_badge =  v.findViewById(R.id.tab_badge);
            if (position == 2)
                tv_badge.setVisibility(View.VISIBLE);

            if(!isAdmin)
            {
                tv_title.setText("Profile");
            }
            else {
                tv_title.setText(tabTitles[position]);
            }

            return v;
        }

    public String[] getTabTitles() {
        return tabTitles;
    }

    @Override
    public Fragment getItem(int i) {
      if(isAdmin)
      {
          switch(i)
          {
              case 0:
                  return new SearchFragment();

              case 1:
                  RegisterFragment registerFragment=new RegisterFragment();
                  registerFragment.setArguments(bundle);
                  return  registerFragment;

              case 2:
                  return new NotificationFragment();

                  default:return  null;
          }
      }
    else
      {
          RegisterFragment registerFragment=new RegisterFragment();
          registerFragment.setArguments(bundle);
          return  registerFragment;
      }

    }

    @Override
    public int getCount() {
        if(isAdmin)
            return 2;
        return 1;
    }
}
