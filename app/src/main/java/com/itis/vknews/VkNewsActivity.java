package com.itis.vknews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.itis.vknews.fragments.AuthorizationFragment;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;


public class VkNewsActivity extends AppCompatActivity {

    /*private VKSdkListener mVKSdkListener = new VKSdkListener() {
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
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_news);
        if (VKSdk.wakeUpSession(getApplicationContext())) {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken vkAccessToken) {
                showNewsActivity();
            }

            @Override
            public void onError(VKError vkError) {
                showAuthorizationFragment();
                Toast.makeText(VkNewsActivity.this, R.string.authorization_error, Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
