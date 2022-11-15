package com.music.play.activity;

import android.text.TextUtils;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityAdminRegisterBinding;
import com.music.play.http.HttpStringCallback;

/**
 *管理员注册
 */
public class AdminRegisterActivity extends BaseActivity<ActivityAdminRegisterBinding> {

    @Override
    protected ActivityAdminRegisterBinding getViewBinding() {
        return ActivityAdminRegisterBinding.inflate(getLayoutInflater());
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
        stringGetRequest.params("register_type", 1);

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