package com.itis.vknews;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.itis.vknews.fragments.AuthorizationFragment;
import com.itis.vknews.utils.Constants;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;


public class VkNewsActivity extends AppCompatActivity {

    private VKSdkListener mVKSdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError vkError) {
            new VKCaptchaDialog(vkError).show(VkNewsActivity.this);
        }

        @Override
        public void onTokenExpired(VKAccessToken vkAccessToken) {
            VKSdk.authorize(Constants.SCOPES);
        }

        @Override
        public void onAccessDenied(VKError vkError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(vkError.toString()).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_news);
        VKSdk.initialize(mVKSdkListener, Constants.APP_ID);
        VKUIHelper.onCreate(this);
        if (VKSdk.wakeUpSession()) {
            showNewsActivity();
        } else {
            showAuthorizationFragment();
        }

    }

    private void showAuthorizationFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new AuthorizationFragment())
                .commit();
    }

    private void showNewsActivity() {
        Intent i = new Intent(this, NewsActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            showNewsActivity();
        }
    }
}
