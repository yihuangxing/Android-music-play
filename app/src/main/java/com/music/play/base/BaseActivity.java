package com.music.play.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;

import com.music.play.R;

/**
 * desc   :
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected Context mContext;
    private Toolbar toolbar;
    protected T mBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =this;
        mBinding =getViewBinding();
        setContentView(mBinding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        if (null != toolbar) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        setListener();

        initData();
    }



    protected abstract T getViewBinding();

    protected abstract void setListener();

    protected abstract void initData();


    protected void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}