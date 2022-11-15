package com.music.play.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityPlayMusicBinding;
import com.music.play.entity.MusicInfo;
import com.music.play.service.MusicService;

/**
 * 音乐播放界面
 */
public class PlayMusicActivity extends BaseActivity<ActivityPlayMusicBinding> {
    private static final String TAG = "============";
    private MusicInfo musicInfo;

    private MyComm mMyComm;
    private Intent mServiceIntent;
    private MusicService mMusicService;

    @Override
    protected ActivityPlayMusicBinding getViewBinding() {
        return ActivityPlayMusicBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {

        mBinding.seekbarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (null!=mMusicService){
                    mMusicService.seekTo(progress);
                }
            }
        });

    }

    @Override
    protected void initData() {
        musicInfo = (MusicInfo) getIntent().getSerializableExtra("musicInfo");
        if (null != musicInfo) {
            mBinding.toolbar.setTitle(musicInfo.getMusic_title());
            mBinding.tvMusicTitle.setText(musicInfo.getMusic_title());
            mBinding.tvMusicSongType.setText(musicInfo.getMusic_type());
        }

        //初始化音乐播放器
        mServiceIntent = new Intent(this, MusicService.class);
        mMyComm = new MyComm();



    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(mServiceIntent);
        bindService(mServiceIntent,mMyComm, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mMyComm);
        stopService(mServiceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MyComm implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            MusicService.MyMusicBinder binder = (MusicService.MyMusicBinder) service;
            Log.i(TAG, "onServiceConnected: ");
            mMusicService = binder.getMusicService();
            //播放音乐
            if (null!=mMusicService && null!=musicInfo){
                mMusicService.onPlay(musicInfo.getMusic_url());
            }else Log.i(TAG, "onStart:   mMusicService  is  null");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (null != mMusicService) {
                mMusicService = null;

            }
        }
    }



}