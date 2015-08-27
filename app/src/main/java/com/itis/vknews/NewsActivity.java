package com.itis.vknews;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.itis.vknews.fragments.NewsFragment;
import com.itis.vknews.fragments.NewsItemFragment;
import com.itis.vknews.model.Item;
import com.itis.vknews.services.RequestService;
import com.itis.vknews.utils.Constants;
import com.vk.sdk.VKSdk;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements NewsFragment.OnRefreshFragment, NewsFragment.OnNewsItemClickListener {

    private Toolbar app_bar;

    private RequestService mService;
    private boolean mBound = false;

    private BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Item> items = (ArrayList<Item>) intent.getSerializableExtra(Constants.INTENT_LIST);
            showNews(items);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((RequestService.RequestBinder) service).getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mHandleMessageReceiver,
                        new IntentFilter(Constants.SERVICE_INTENT_BROADCAST));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = new Intent(this, RequestService.class);

        bindService(i, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!mBound) return;
        unbindService(mConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.me_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.logout_title)
                    .setMessage(R.string.logout_message)
                    .setPositiveButton(R.string.logout_positive_answer, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logout();
                        }
                    })
                    .setNegativeButton(R.string.logout_negative_answer, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setCancelable(true);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNews(List<Item> items) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        NewsFragment fragment = NewsFragment.newInstance(items);
        if (findViewById(R.id.container) != null) {
            transaction.replace(R.id.container, fragment);
        } else {
            transaction.add(R.id.container, fragment);
        }
        transaction.commit();

    }

    private void logout() {
        Intent intent = new Intent(this, VkNewsActivity.class);
        VKSdk.logout();
        startActivity(intent);
        finish();
    }

    @Override
    public void onRefresh() {
        if (mService != null) {
            mService.loadNews("");
        }
    }

    @Override
    public void onNewsItemClick(Item item) {
        showNewsItem(item);
    }

    private void showNewsItem(Item item) {
        NewsItemFragment newsItemFragment = NewsItemFragment.newInstance(item);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.container) != null) {
            transaction.replace(R.id.container, newsItemFragment);
        } else {
            transaction.add(R.id.container, newsItemFragment);
        }
        transaction.addToBackStack(null).commit();

    }
}
