package com.music.play.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityAdminLoginBinding;
import com.music.play.entity.UserInfo;
import com.music.play.http.HttpStringCallback;
import com.music.play.utils.GsonUtils;

/**
 * 管理员登录
 */
public class AdminLoginActivity extends BaseActivity<ActivityAdminLoginBinding> {

    @Override
    protected ActivityAdminLoginBinding getViewBinding() {
        return ActivityAdminLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {

        mBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_str = mBinding.username.getText().toString().trim();
                String password_str = mBinding.password.getText().toString().trim();
                if (TextUtils.isEmpty(username_str) || TextUtils.isEmpty(password_str)) {
                    showToast("请完善登录信息");
                } else {
                    login(username_str, password_str);
                }
            }
        });


        mBinding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, AdminRegisterActivity.class));
            }
        });

    }

    @Override
    protected void initData() {

    }

    /**
     * 登录
     */
    private void login(String username, String password) {
        OkGo.<String>get(ApiConstants.LOGIN_URL)
                .params("username", username)
                .params("password", password)
                .params("register_type", 1)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        UserInfo userInfo = GsonUtils.parseJson(response, UserInfo.class);
                        ApiConstants.setUserInfo(userInfo);
                        if (userInfo != null) {
                            if (userInfo.getRegister_type() == 1) {
                                startActivity(new Intent(AdminLoginActivity.this, AdminMainActivity.class));
                            }
                        }
                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });

    }
}