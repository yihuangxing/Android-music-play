package com.music.play.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lzy.okgo.OkGo;
import com.music.play.adapter.ClassifyManagerListAdapter;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityClassifyManagerBinding;
import com.music.play.entity.MusicTypeInfo;
import com.music.play.entity.MusicTypeListInfo;
import com.music.play.http.HttpStringCallback;
import com.music.play.utils.GsonUtils;

import java.util.ArrayList;

/**
 * 分类管理
 */
public class ClassifyManagerActivity extends BaseActivity<ActivityClassifyManagerBinding> {
    private ClassifyManagerListAdapter mListAdapter;
    private int currentIndex = 0;

    @Override
    protected ActivityClassifyManagerBinding getViewBinding() {
        return ActivityClassifyManagerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {


        mBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new XPopup.Builder(ClassifyManagerActivity.this)
                        .hasStatusBarShadow(false)
                        .hasNavigationBar(false)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗对象，推荐设置这个
                        .autoOpenSoftInput(true)
                        .isDarkTheme(false)
                        .asInputConfirm("添加音乐分类", null, null, "请输入音乐分类",
                                new OnInputConfirmListener() {
                                    @Override
                                    public void onConfirm(String text) {
                                        if (TextUtils.isEmpty(text)) {
                                            showToast("输入内容不能为空~");
                                        } else {
                                            addMusic(text);
                                        }
                                    }
                                })
                        .show();
            }
        });

        mListAdapter = new ClassifyManagerListAdapter();
        mListAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                MusicTypeInfo musicTypeInfo = mListAdapter.getData().get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ClassifyManagerActivity.this);
                builder.setTitle("操作");
                builder.setSingleChoiceItems(new String[]{"编辑", "删除"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        currentIndex = position;
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (currentIndex == 0) {
                            //编辑
                            new XPopup.Builder(ClassifyManagerActivity.this)
                                    .hasStatusBarShadow(false)
                                    .hasNavigationBar(false)
                                    //.dismissOnBackPressed(false)
                                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗对象，推荐设置这个
                                    .autoOpenSoftInput(true)
                                    .isDarkTheme(false)
                                    .asInputConfirm("删除操作", null, musicTypeInfo.getType_name(), "请输入音乐分类",
                                            new OnInputConfirmListener() {
                                                @Override
                                                public void onConfirm(String text) {
                                                    if (TextUtils.isEmpty(text)) {
                                                        showToast("输入内容不能为空~");
                                                    } else {
                                                        edit(musicTypeInfo.getUid(), text);
                                                    }
                                                }
                                            })
                                    .show();
                        } else {
                            //删除
                            del(musicTypeInfo.getUid(), position);
                        }
                        currentIndex = 0;
                    }
                });
                builder.show();
                return true;
            }
        });
        mBinding.recyclerview.setAdapter(mListAdapter);

    }

    @Override
    protected void initData() {
        getClassifyListData();
    }


    /**
     * 获取音乐分类
     */
    private void getClassifyListData() {
        OkGo.<String>get(ApiConstants.QUERY_MUSIC_TYPE_URL)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        MusicTypeListInfo musicTypeListInfo = GsonUtils.parseJson(response, MusicTypeListInfo.class);
                        mListAdapter.setList(musicTypeListInfo.getList());
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }

    /**
     * 删除音乐
     */
    private void del(int uid, int position) {
        OkGo.<String>get(ApiConstants.DEL_MUSIC_TYPE_URL)
                .params("uid", uid)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        mListAdapter.removeAt(position);
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }

    /**
     * 编辑音乐分类
     */
    private void edit(int uid, String music_type) {
        OkGo.<String>get(ApiConstants.EDIT_MUSIC_TYPE)
                .params("uid", uid)
                .params("type_name", music_type)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        showToast(msg);
                        initData();
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }

    /**
     * 添加音乐分类
     */
    private void addMusic(String type_name) {
        OkGo.<String>get(ApiConstants.ADD_MUSIC_TYPE_URL)
                .params("type_name", type_name)
                .execute(new HttpStringCallback(null) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        showToast(msg);
                        initData();
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }
}