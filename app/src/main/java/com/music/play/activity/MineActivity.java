package com.music.play.activity;


import android.content.Intent;
import android.view.View;

import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityMineBinding;

/**
 * 个人中心
 */
public class MineActivity extends BaseActivity<ActivityMineBinding> {


    @Override
    protected ActivityMineBinding getViewBinding() {
        return ActivityMineBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {

        mBinding.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, RecordListActivity.class));
            }
        });

        mBinding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DownLoadActivity.class));
            }
        });

    }

    @Override
    protected void initData() {
        if (ApiConstants.getUserInfo() != null) {
            mBinding.username.setText(ApiConstants.getUserInfo().getUsername());
        }

    }
}