package com.music.play.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.task.DownloadTask;
import com.arialyy.aria.util.ALog;
import com.arialyy.aria.util.CommonUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lzy.okgo.OkGo;
import com.music.play.R;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityPlayMusicBinding;
import com.music.play.entity.MusicInfo;
import com.music.play.http.HttpStringCallback;
import com.music.play.service.AudioPlayer;
import com.music.play.service.OnPlayerEventListener;
import com.music.play.utils.GsonUtils;

import java.io.File;
import java.util.List;
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

        Aria.download(this).register();

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


        //下载
        mBinding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (XXPermissions.isGranted(mContext, Permission.Group.STORAGE)) {
                    download();
                } else {
                    checkPermission();
                }

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
            //添加到浏览记录
            addRecord(musicInfo);

            //获取单个任务实体
            DownloadEntity entity = Aria.download(this).getFirstDownloadEntity(musicInfo.getMusic_url());
            if (null != entity) {
                mBinding.download.setClickable(false);
                mBinding.download.setImageResource(R.mipmap.ic_download_complete);
            } else {
                mBinding.download.setClickable(true);
                mBinding.download.setImageResource(R.mipmap.iv_download);
            }
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
        //一定要设置最大值
        mBinding.sbProgress.setMax((int) duration);
        mBinding.tvTotalTime.setText(formatTime("mm:ss", duration));
        mBinding.ivMusicPlay.setSelected(true);
        startAnim();
    }

    @Override
    public void onPlayerPause() {
        mBinding.ivMusicPlay.setSelected(false);
        stopAnim();
    }

    @Override
    public void onPublish(int progress) {
        mBinding.sbProgress.setProgress(progress);
    }

    @Override
    public void onBufferingUpdate(int percent) {
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
        if (mBinding.imgCd.getAnimation() != null) {
            mBinding.imgCd.clearAnimation();
        }
    }

    private void addRecord(MusicInfo musicInfo) {
        OkGo.<String>get(ApiConstants.ADD_RECORD_MUSIC_URL)
                .params("username", ApiConstants.getUserInfo().getUsername())
                .params("music_title", musicInfo.getMusic_title())
                .params("music_url", musicInfo.getMusic_url())
                .params("music_type", musicInfo.getMusic_type())
                .execute(new HttpStringCallback(null) {
                    @Override
                    protected void onSuccess(String msg, String response) {

                    }

                    @Override
                    protected void onError(String response) {

                    }
                });


    }

    private void checkPermission() {
        XXPermissions.with(this)
                // 申请单个权限
                // 申请多个权限
                .permission(Permission.Group.STORAGE)
                // 设置权限请求拦截器（局部设置）
                //.interceptor(new PermissionInterceptor())
                // 设置不触发错误检测机制（局部设置）
                //.unchecked()
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (!all) {
                            showToast("获取部分权限成功，但部分权限未正常授予");
                            return;
                        }

                        //这里做操作


                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            showToast("被永久拒绝授权，请手动授予录音和日历权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(mContext, permissions);
                        } else {
                            showToast("获取录音和日历权限失败");
                        }
                    }
                });
    }

    @Download.onWait
    public void onWait(DownloadTask task) {
        Log.d(TAG, "onWait: ");
    }

    @Download.onPre
    public void onPre(DownloadTask task) {
        Log.d(TAG, "onPre: ");
    }

    @Download.onTaskStart
    public void onTaskStart(DownloadTask task) {
        Log.d(TAG, "onTaskStart: ");
        showToast("开始下载~~~~~");
    }

    @Download.onTaskRunning
    public void onTaskRunning(DownloadTask task) {
        Log.d(TAG, "onTaskRunning: ");
    }

    @Download.onTaskResume
    public void onTaskResume(DownloadTask task) {
        Log.d(TAG, "onTaskResume: ");
    }

    @Download.onTaskStop
    public void onTaskStop(DownloadTask task) {
        Log.d(TAG, "onTaskStop: ");
    }

    @Download.onTaskCancel
    public void onTaskCancel(DownloadTask task) {
        Log.d(TAG, "onTaskCancel: ");
    }

    @Download.onTaskFail
    public void onTaskFail(DownloadTask task, Exception e) {
        Log.d(TAG, "onTaskFail: ");
    }

    @Download.onTaskComplete
    public void onTaskComplete(DownloadTask task) {
        Log.d(TAG, "onTaskComplete: ");
        mBinding.download.setClickable(false);
        mBinding.download.setImageResource(R.mipmap.ic_download_complete);
        showToast("下载完成~~~~~");

    }

    private void download() {
        if (null != musicInfo) {
            Aria.download(PlayMusicActivity.this)
                    .load(musicInfo.getMusic_url()) // 下载地址
                    .setFilePath(getExternalCacheDir().getPath() + musicInfo.getMusic_title() + ".mp3") // 设置文件保存路径
                    .setExtendField(GsonUtils.toJson(musicInfo))
                    .create();

        }
    }

}