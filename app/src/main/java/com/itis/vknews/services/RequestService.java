package com.itis.vknews.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.itis.vknews.model.Item;
import com.itis.vknews.utils.Constants;
import com.itis.vknews.utils.ParseUtils;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestService extends Service {

    private final IBinder mBinder = new RequestBinder();
    private List<Item> mItems;
    private String mStartFrom;
    private VKRequest mVKRequest;

    public class RequestBinder extends Binder {
        public RequestService getService() {
            return RequestService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mItems = new ArrayList<>();
        loadNews("");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void loadNews(final String startFrom) {
        if (startFrom.equals("")) {
            mItems.clear();
        }
        mVKRequest = new VKRequest(Constants.METHOD_NAME, VKParameters.from(Constants.FILTERS,
                Constants.FILTER_NAMES, VKApiConst.COUNT, Constants.NEWS_COUNT, Constants.START_FROM,
                startFrom));

        // this performs in another thread
        mVKRequest.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                JSONObject result = response.json;
                try {
                    mItems = ParseUtils.parse(result);
                    mStartFrom = ParseUtils.getNextFrom();
                    sendMessage(startFrom);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void sendMessage(String startFrom) {
        Intent intent = new Intent(Constants.SERVICE_INTENT_BROADCAST);

        intent.putExtra(Constants.INTENT_PULLED, startFrom.equals(""));
        intent.putExtra(Constants.INTENT_LIST, (ArrayList<Item>) mItems);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
