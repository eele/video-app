package edu.zhku.jsj144.lzc.video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import edu.zhku.jsj144.lzc.video.R;
import edu.zhku.jsj144.lzc.video.fragment.DiscoverPageFragment;
import edu.zhku.jsj144.lzc.video.fragment.FollowPageFragment;
import edu.zhku.jsj144.lzc.video.fragment.MinePageFragment;
import edu.zhku.jsj144.lzc.video.util.StatusBar;
import edu.zhku.jsj144.lzc.video.viewpager.NoScrollViewPager;

public class MainActivity extends AppCompatActivity {

    private NoScrollViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    mPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    mPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBar.setStatusBarTransparent(this);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (NoScrollViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // Instantiate a BottomNavigationView
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = new Intent(MainActivity.this, FragmentPageMineActivity.class);
        startActivity(intent);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new DiscoverPageFragment();
                    break;
                case 1:
                    fragment = new FollowPageFragment();
                    break;
                case 2:
                    fragment = new MinePageFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
