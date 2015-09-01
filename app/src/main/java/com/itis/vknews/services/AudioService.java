package com.itis.vknews.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.itis.vknews.utils.Constants;

import java.io.IOException;

public class AudioService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mPlayer;
    private boolean playing = false;

    @Override
    public void onCreate() {
        super.onCreate();

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.AUDIO_SERVICE_ACTION_PLAY)) {
            if (!playing) {
                try {
                    mPlayer.setDataSource(intent.getStringExtra(Constants.AUDIO_SERVICE_DATA_SOURCE));
                    mPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else mPlayer.start();
        } else if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopSelf();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playing = true;
        mp.start();
    }

    private void releaseMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
