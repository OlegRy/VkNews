package com.itis.vknews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.itis.vknews.R;
import com.itis.vknews.async.DownloadImageTask;
import com.itis.vknews.utils.Constants;

public class FullPhotoFragment extends Fragment {

    private int mPageNumber;
    private int mPageCount;
    private String mUrl;

    public static FullPhotoFragment newInstance(int page, String url, int pageCount) {
        FullPhotoFragment fragment = new FullPhotoFragment();

        Bundle args = new Bundle();

        args.putInt(Constants.BUNDLE_PAGE, page);
        args.putInt(Constants.BUNDLE_PAGE_COUNT, pageCount);
        args.putString(Constants.BUNDLE_BITMAP, url);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            mPageNumber = args.getInt(Constants.BUNDLE_PAGE);
            mPageCount = args.getInt(Constants.BUNDLE_PAGE_COUNT);
            mUrl = args.getString(Constants.BUNDLE_BITMAP);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_photo, container, false);

     //   TextView tv_photo_number = (TextView) view.findViewById(R.id.tv_photo_number);
      //  String pageInfo = String.format(getResources().getString(R.string.photo_number), mPageNumber + 1, mPageCount);
       // tv_photo_number.setText(pageInfo);

        ImageView iv_full_photo = (ImageView) view.findViewById(R.id.iv_full_photo);
        new DownloadImageTask(iv_full_photo).execute(mUrl);
        return view;
    }
}
