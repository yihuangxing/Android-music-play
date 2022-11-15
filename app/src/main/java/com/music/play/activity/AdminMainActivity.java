package com.music.play.activity;


import android.content.Intent;
import android.view.View;

import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends BaseActivity<ActivityAdminMainBinding> {

    @Override
    protected ActivityAdminMainBinding getViewBinding() {
        return ActivityAdminMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {

        mBinding.classifyManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, ClassifyManagerActivity.class));
            }
        });

        mBinding.userList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, UserListManagerActivity.class));
            }
        });
        mBinding.musicManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, MusicListManagerActivity.class));
            }
        });
    }

    @Override
    protected void initData() {

    }
}