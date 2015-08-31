package com.itis.vknews.async;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.itis.vknews.FullPhotoActivity;
import com.itis.vknews.utils.Constants;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadSomeImagesTask extends AsyncTask<String, Void, List<Bitmap>> {

    private WeakReference<Activity> mActivityWeakReference;
    private WeakReference<GridView> mGridViewWeakReference;
    private WeakReference<LinearLayout> mLinearLayoutWeakReference;
    private List<String> mSmallPhotoUrls;
    private List<String> mBigPhotoUrls;

    public DownloadSomeImagesTask(Activity activity, LinearLayout linearLayout, List<String> bigPhotoUrls) {
        mActivityWeakReference = new WeakReference<>(activity);
        mLinearLayoutWeakReference = new WeakReference<>(linearLayout);
        mSmallPhotoUrls = new ArrayList<>();
        mBigPhotoUrls = bigPhotoUrls;
    }

    @Override
    protected List<Bitmap> doInBackground(String... params) {
        if (params != null && params.length > 0) {
            List<Bitmap> bitmaps = new ArrayList<>();
            for (String param : params) {
                mSmallPhotoUrls.add(param);
                try {
                    URL url = new URL(param);
                    bitmaps.add(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmaps;
        }
        return null;
    }

    @Override
    protected void onPostExecute(final List<Bitmap> bitmaps) {
        super.onPostExecute(bitmaps);
        if (bitmaps != null && bitmaps.size() > 0) {
            final Activity currentActivity = mActivityWeakReference.get();
            LinearLayout linearLayout = mLinearLayoutWeakReference.get();
            for (final Bitmap bitmap : bitmaps) {
                ImageView image = new ImageView(currentActivity);
                image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                image.setImageBitmap(bitmap);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent photoIntent = new Intent(currentActivity, FullPhotoActivity.class);
                        photoIntent.putExtra(Constants.INTENT_PHOTO_LIST, (ArrayList<String>) mBigPhotoUrls);
                        photoIntent.putExtra(Constants.INTENT_CURRENT_PHOTO, bitmaps.indexOf(bitmap));

                        currentActivity.startActivity(photoIntent);
                    }
                });
                linearLayout.addView(image);
            }
        }
    }
}
