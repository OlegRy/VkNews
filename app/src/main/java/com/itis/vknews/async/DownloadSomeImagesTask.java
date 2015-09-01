package com.itis.vknews.async;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.GridView;

import com.itis.vknews.adapters.ImageAdapter;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadSomeImagesTask extends AsyncTask<String, Void, List<Bitmap>> {

    private WeakReference<Activity> mActivityWeakReference;
    private WeakReference<GridView> mGridViewWeakReference;
    private List<String> mSmallPhotoUrls;
    private List<String> mBigPhotoUrls;

    public DownloadSomeImagesTask(Activity activity, GridView gridView, List<String> bigPhotoUrls) {
        mActivityWeakReference = new WeakReference<>(activity);
        mGridViewWeakReference = new WeakReference<>(gridView);
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
            GridView gridView = mGridViewWeakReference.get();
            ImageAdapter adapter = new ImageAdapter(currentActivity, bitmaps, mBigPhotoUrls);
            gridView.setAdapter(adapter);
        }
    }
}
