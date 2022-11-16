package com.music.play.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
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


        mBinding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MineActivity.this);
                builder.setTitle("确定要退出登录吗？");
                builder.setMessage("退出登录，将清空用户登录数据");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToast("退出登录成功");
                        setResult(20002);
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
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