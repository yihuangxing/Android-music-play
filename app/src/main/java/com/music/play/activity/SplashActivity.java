package com.music.play.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivitySplashBinding;
/**
 * 欢迎页
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    @Override
    protected ActivitySplashBinding getViewBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {


    }

    @Override
    protected void initData() {
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mContext,LoginActivity.class));
                finish();
            }
        },800);

    }
}