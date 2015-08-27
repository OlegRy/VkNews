package com.itis.vknews;

import android.app.Application;
import android.content.Intent;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

public class VkNewsApp extends Application {

    VKAccessTokenTracker mVKAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken vkAccessToken, VKAccessToken vkAccessToken1) {
            if (vkAccessToken1 == null) {
                Intent intent = new Intent(VkNewsApp.this, VkNewsActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mVKAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }
}
