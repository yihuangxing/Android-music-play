package com.music.play.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.lzy.okgo.OkGo;
import com.music.play.adapter.UserListAdapter;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityUserListManagerBinding;
import com.music.play.entity.UserListInfo;
import com.music.play.http.HttpStringCallback;
import com.music.play.utils.GsonUtils;

/**
 * 用户管理
 */
public class UserListManagerActivity extends BaseActivity<ActivityUserListManagerBinding> {
    private UserListAdapter mUserListAdapter;

    @Override
    protected ActivityUserListManagerBinding getViewBinding() {
        return ActivityUserListManagerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        mUserListAdapter = new UserListAdapter();
        mBinding.recyclerview.setAdapter(mUserListAdapter);


        mUserListAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserListManagerActivity.this);
                builder.setTitle("操作");
                builder.setMessage("确定要删除平台注册用户吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delUser(mUserListAdapter.getData().get(position).getUid(), position);
                    }
                });
                builder.show();
                return true;
            }
        });

    }

    /**
     * 删除平台注册用户
     */
    private void delUser(int uid, int position) {
        OkGo.<String>get(ApiConstants.DEL_USER_URL)
                .params("uid", uid)
                .execute(new HttpStringCallback(null) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        mUserListAdapter.removeAt(position);
                        showToast(msg);
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }

    @Override
    protected void initData() {
        getUserListData();
    }

    /**
     * 获取平台注册用户
     */
    private void getUserListData() {
        OkGo.<String>get(ApiConstants.USER_LIST_URL)
                .execute(new HttpStringCallback(null) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        UserListInfo userListInfo = GsonUtils.parseJson(response, UserListInfo.class);
                        mUserListAdapter.setList(userListInfo.getList());
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }

}