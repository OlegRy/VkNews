package com.itis.vknews.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.itis.vknews.FullPhotoActivity;
import com.itis.vknews.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<Bitmap> mBitmaps;
    private List<String> mBigPhotoUrls;

    public ImageAdapter(Context context, List<Bitmap> bitmaps, List<String> bigPhotoUrls) {
        mContext = context;
        mBitmaps = bitmaps;
        mBigPhotoUrls = bigPhotoUrls;
    }

    @Override
    public int getCount() {
        return mBitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView image;
        if (convertView == null) {
            image = new ImageView(mContext);
            int imageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, mContext
                    .getResources().getDisplayMetrics());
            image.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
        } else {
            image = (ImageView) convertView;
        }
        image.setImageBitmap(mBitmaps.get(position));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(mContext, FullPhotoActivity.class);
                photoIntent.putExtra(Constants.INTENT_PHOTO_LIST, (ArrayList<String>) mBigPhotoUrls);
                photoIntent.putExtra(Constants.INTENT_CURRENT_PHOTO, position);

                mContext.startActivity(photoIntent);
            }
        });

        return image;
    }
}
