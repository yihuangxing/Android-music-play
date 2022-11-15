package com.music.play.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.gyf.immersionbar.ImmersionBar;
import com.music.play.R;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityPlayMusicBinding;
import com.music.play.entity.MusicInfo;
import com.music.play.service.AudioPlayer;
import com.music.play.service.OnPlayerEventListener;

import java.util.Locale;

/**
 * 音乐播放界面
 */
public class PlayMusicActivity extends BaseActivity<ActivityPlayMusicBinding> implements OnPlayerEventListener {
    private static final String TAG = "============";
    private MusicInfo musicInfo;
    private int mLastProgress;

    @Override
    protected ActivityPlayMusicBinding getViewBinding() {
        return ActivityPlayMusicBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {

        mBinding.sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
                    mBinding.tvCurrentTime.setText(formatTime("mm:ss", progress));
                    mLastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (AudioPlayer.get().isPlaying() || AudioPlayer.get().isPausing()) {
                    int progress = seekBar.getProgress();
                    AudioPlayer.get().seekTo(progress);
                } else {
                    seekBar.setProgress(0);
                }
            }
        });

        mBinding.ivMusicPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioPlayer.get().playPause();
            }
        });

        mBinding.ivMusicPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioPlayer.get().prev();
            }
        });

        mBinding.ivMusicNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioPlayer.get().next();
            }
        });

    }

    @Override
    protected void initData() {
        initSystemBar();
        musicInfo = (MusicInfo) getIntent().getSerializableExtra("musicInfo");
        //监听
        AudioPlayer.get().addOnPlayEventListener(this);
        if (null != musicInfo) {
            AudioPlayer.get().addAndPlay(musicInfo);
        }
    }

    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        ImmersionBar.with(this).init();
    }

    public String formatTime(String pattern, long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return pattern.replace("mm", mm).replace("ss", ss);
    }

    @SuppressLint("SetTextI18n")
    private void onChangeImpl(MusicInfo music) {
        if (music == null) {
            return;
        }
        mBinding.sbProgress.setProgress((int) AudioPlayer.get().getAudioPosition());
        mBinding.sbProgress.setSecondaryProgress(0);
        mLastProgress = 0;
        mBinding.tvCurrentTime.setText("00:00");
        if (AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing()) {
            mBinding.ivMusicPlay.setSelected(true);
        } else {
            mBinding.ivMusicPlay.setSelected(false);
        }
        mBinding.toolbar.setTitle(music.getMusic_title());
        mBinding.tvMusicTitle.setText(music.getMusic_title());
        mBinding.tvMusicSongType.setText(music.getMusic_type());

        startAnim();
    }

    @Override
    public void onChange(MusicInfo music) {
        onChangeImpl(music);
    }

    @Override
    public void onPlayerStart(long duration) {
        Log.d(TAG, "onPlayerStart: " + duration);
        //一定要设置最大值
        mBinding.sbProgress.setMax((int) duration);
        mBinding.tvTotalTime.setText(formatTime("mm:ss", duration));
        mBinding.ivMusicPlay.setSelected(true);
        startAnim();
    }

    @Override
    public void onPlayerPause() {
        Log.d(TAG, "onPlayerPause: ");
        mBinding.ivMusicPlay.setSelected(false);
        stopAnim();
    }

    @Override
    public void onPublish(int progress) {
        Log.d(TAG, "onPublish: " + progress);
        mBinding.sbProgress.setProgress(progress);
    }

    @Override
    public void onBufferingUpdate(int percent) {
        Log.d(TAG, "onBufferingUpdate: ");
        mBinding.sbProgress.setSecondaryProgress(mBinding.sbProgress.getMax() * 100 / percent);
    }


    private Animation animation;

    private void startAnim() {
        animation = AnimationUtils.loadAnimation(this, R.anim.rotation_animation);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        mBinding.imgCd.startAnimation(animation);
    }

    private void stopAnim() {
       if (mBinding.imgCd.getAnimation()!=null){
           mBinding.imgCd.clearAnimation();
       }
    }

}