package com.itis.vknews.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> mImageViewWeakReference;
    private WeakReference<LinearLayout> mLinearLayoutWeakReference;

    public DownloadImageTask(ImageView imageView) {
        mImageViewWeakReference = new WeakReference<>(imageView);
    }

    public DownloadImageTask(ImageView imageView, LinearLayout linearLayout) {
        this(imageView);
        mLinearLayoutWeakReference = new WeakReference<>(linearLayout);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params.length > 0 && params[0] != null) {
            try {
                URL url = new URL(params[0]);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            ImageView imageView = mImageViewWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                if (mLinearLayoutWeakReference != null) {
                    LinearLayout linearLayout = mLinearLayoutWeakReference.get();
                    linearLayout.addView(imageView);
                }
            }

        }

    }
}
