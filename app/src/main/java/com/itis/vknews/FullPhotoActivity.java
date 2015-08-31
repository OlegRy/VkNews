package com.itis.vknews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.itis.vknews.fragments.FullPhotoFragment;
import com.itis.vknews.utils.Constants;

import java.util.ArrayList;

public class FullPhotoActivity extends AppCompatActivity {

    private ViewPager mPager;
    private TextView tv_photo_number;

    private PagerAdapter mAdapter;

    private ArrayList<String> mUrls;
    private int mCurrentPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);

        Intent photoIntent = getIntent();

        if (photoIntent != null) {
            mUrls = (ArrayList<String>) photoIntent.getSerializableExtra(Constants.INTENT_PHOTO_LIST);
            mCurrentPhoto = photoIntent.getIntExtra(Constants.INTENT_CURRENT_PHOTO, 0);
        }

        tv_photo_number = (TextView) findViewById(R.id.tv_photo_number);
        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new FullPhotoPagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setFormattedPageInfo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setCurrentItem(mCurrentPhoto);
        if (mCurrentPhoto == 0) {
            setFormattedPageInfo(mCurrentPhoto);
        }
    }

    private class FullPhotoPagerAdapter extends FragmentPagerAdapter {

        public FullPhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FullPhotoFragment.newInstance(position, mUrls.get(position), mUrls.size());
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }
    }

    private void setFormattedPageInfo(int position) {
        String pageInfo = String.format(getResources().getString(R.string.photo_number),
                position + 1, mUrls.size());
        tv_photo_number.setText(pageInfo);
    }
}
