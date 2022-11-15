package com.music.play.activity;


import android.text.TextUtils;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.music.play.adapter.HomeListAdapter;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityRegisterBinding;
import com.music.play.http.HttpStringCallback;

import java.io.File;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {
    @Override
    protected ActivityRegisterBinding getViewBinding() {
        return ActivityRegisterBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {

        mBinding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mBinding.username.getText().toString().trim();
                String password = mBinding.password.getText().toString().trim();
                if (TextUtils.isEmpty(username)  || TextUtils.isEmpty(password)) {
                    showToast("请完善相关信息");
                } else {
                    register(username, password);
                }
            }
        });

    }

    @Override
    protected void initData() {

    }

    private void register(String username, String password) {
        GetRequest<String> stringGetRequest = OkGo.<String>get(ApiConstants.REGISTER_URL);
        stringGetRequest.params("username", username);
        stringGetRequest.params("password", password);
        stringGetRequest.params("register_type", 0);

        stringGetRequest.execute(new HttpStringCallback(this) {
            @Override
            protected void onSuccess(String msg, String response) {
                showToast(msg);
                finish();

            }

            @Override
            protected void onError(String response) {
                showToast(response);
            }
        });

    }
}