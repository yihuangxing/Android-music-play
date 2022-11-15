package com.music.play.service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

/**
 * desc   :
 */
public class MusicService extends BaseSerVice {
    private static final String TAG = "=======service=======";
    private final IBinder binder = new MyMusicBinder();
    private MediaPlayer mMediaPlayer;

    private boolean isFirstPlay = true;
    private int currentPosition = 0;

    public boolean isFirstPlay() {
        return isFirstPlay;
    }

    public void setFirstPlay(boolean firstPlay) {
        isFirstPlay = firstPlay;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void stopMediaPlayer() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
            if (!isFirstPlay()) {
                mMediaPlayer.seekTo(0);
                currentPosition = 0;
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
        } else Log.i(TAG, "stopMediaPlayer:  mMediaPlayer is null ");
    }

    private void setMediaPlayerResource(String path, boolean isNetMusic) {
        stopMediaPlayer();
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.setDataSource(path);
                playMediaPlayer(isNetMusic);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else Log.i(TAG, "setMediaPlayerResource: mMediaPlayer is null ");

    }

    private void playMediaPlayer(boolean isNetMusic) {
        if (mMediaPlayer != null) {
            if (currentPosition == 0) {
                if (isNetMusic) mMediaPlayer.prepareAsync();
                else {
                    try {
                        mMediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //1.接着上次播放
                //2.暂停后播放
                if (isFirstPlay()) {

                }
            }
            if (isFirstPlay()) { //只执行一次
                if (mMediaPlayer != null) {
                    mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            return false;
                        }
                    });
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            Log.d(TAG, "onPrepared: ");
                            if (isFirstPlay()) {
                                isFirstPlay = false;
                            }
                            if (currentPosition >= 0) mediaPlayer.seekTo(currentPosition);

                            //播放
                            mediaPlayer.start();
                        }
                    });

                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {

                        }
                    });
                }
            }
        }
    }

    public void onPause() {

    }

    public void onContinuePlay() {

    }

    public void seekTo(int progress) {
        Log.d(TAG, "seekTo: " + progress);
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(progress);
        }
    }


    public void onPlay(String path) {
        setMediaPlayerResource(path, true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        releaseMediaPlayer();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    public class MyMusicBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }


}