package com.example.admin.snapchatclone.Start;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.admin.snapchatclone.Fragment.CameraFragment;
import com.example.admin.snapchatclone.Fragment.ChatFragment;
import com.example.admin.snapchatclone.Fragment.StoryFragment;
import com.example.admin.snapchatclone.R;
import com.example.admin.snapchatclone.UserInformation;

public class MainActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserInformation userInformationlistener=new UserInformation();
        userInformationlistener.startFetching();
        ViewPager viewPager= findViewById(R.id.viewPager);
        adapterViewPager=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(1);

    }

    public static class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return ChatFragment.newInstance();
                case 1:
                    return CameraFragment.newInstance();
                case 2:
                    return StoryFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
