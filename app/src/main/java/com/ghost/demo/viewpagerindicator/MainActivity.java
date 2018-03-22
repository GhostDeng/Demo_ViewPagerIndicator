package com.ghost.demo.viewpagerindicator;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;

    private ViewPagerIndicator mIndicator;

    private List<String> mTitles = Arrays.asList("条目1", "条目2", "条目3",
            "条目4", "条目5", "条目6",
            "条目7", "条目8", "条目9");

    private List<VpFragment> mContents = new ArrayList<>();

    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initDatas();

        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //tabWidth*positionOffset + position * tabWidth
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViews() {
        mViewPager = findViewById(R.id.viewPager);
        mIndicator = findViewById(R.id.indicator);
    }

    private void initDatas() {
        for (String title : mTitles) {
            VpFragment fragment = VpFragment.newInstance(title);
            mContents.add(fragment);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }

            @Override
            public int getCount() {
                return mContents.size();
            }
        };
    }

}
