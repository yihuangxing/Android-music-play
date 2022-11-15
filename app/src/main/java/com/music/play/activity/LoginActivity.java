package com.music.play.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityLoginBinding;
import com.music.play.entity.UserInfo;
import com.music.play.http.HttpStringCallback;
import com.music.play.utils.GsonUtils;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {


    @Override
    protected ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        //注册点击事件
        mBinding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //切换
        mBinding.switchover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,AdminLoginActivity.class));
            }
        });

        mBinding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, RegisterActivity.class));
            }
        });
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
                .params("register_type", 0)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        UserInfo userInfo = GsonUtils.parseJson(response, UserInfo.class);
                        ApiConstants.setUserInfo(userInfo);
                        if (userInfo != null) {
                            if (userInfo.getRegister_type() == 0) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
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