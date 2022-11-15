package com.music.play.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.XPopupUtils;
import com.lzy.okgo.OkGo;
import com.music.play.adapter.HomeListAdapter;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityMusicListManagerBinding;
import com.music.play.entity.MusicInfo;
import com.music.play.entity.MusicListInfo;
import com.music.play.entity.MusicTypeListInfo;
import com.music.play.http.HttpStringCallback;
import com.music.play.service.AudioPlayer;
import com.music.play.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐管理
 */
public class MusicListManagerActivity extends BaseActivity<ActivityMusicListManagerBinding> {
    private HomeListAdapter mHomeListAdapter;
    private int currentIndex = 0;
    private BasePopupView popupView;
    private List<String> items = new ArrayList<>();

    @Override
    protected ActivityMusicListManagerBinding getViewBinding() {
        return ActivityMusicListManagerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        mHomeListAdapter = new HomeListAdapter();
        mBinding.recyclerView.setAdapter(mHomeListAdapter);


        //点击事件
        mHomeListAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MusicListManagerActivity.this);
                builder.setTitle("操作");
                builder.setSingleChoiceItems(new String[]{"修改歌名", "移动音乐分类", "删除"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        currentIndex = position;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (currentIndex == 0) {
                            editMusic(mHomeListAdapter.getData().get(position));
                        } else if (currentIndex == 1) {
                            queryMusicTypeListData(mHomeListAdapter.getData().get(position));
                        } else {
                            delMusic(mHomeListAdapter.getData().get(position).getUid(), position);
                        }
                        currentIndex = 0;
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return true;
            }
        });

    }

    /**
     * 修改音乐
     */
    private void editMusic(MusicInfo music) {
        new XPopup.Builder(MusicListManagerActivity.this)
                .hasStatusBarShadow(false)
                .hasNavigationBar(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗对象，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(false)
                .asInputConfirm("修改歌名", null, music.getMusic_title(), "请输入歌曲名",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                if (TextUtils.isEmpty(text)) {
                                    showToast("输入内容不能为空~");
                                } else {
                                    editMusic(music.getUid(), text, null);
                                }
                            }
                        })
                .show();
    }

    /**
     * 修改音乐信息
     */
    private void editMusic(int uid, String music_title, String music_type) {
        OkGo.<String>get(ApiConstants.EDIT_MUSIC_URL)
                .params("uid", uid)
                .params("music_title", music_title)
                .params("music_type", music_type)
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

    @Override
    protected void initData() {
        getMusicListData();
    }

    /**
     * 获取平台所有音乐
     */
    private void getMusicListData() {
        OkGo.<String>get(ApiConstants.QUERY_MUSIC_URL)
                .execute(new HttpStringCallback(MusicListManagerActivity.this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        MusicListInfo musicListInfo = GsonUtils.parseJson(response, MusicListInfo.class);
                        mHomeListAdapter.setList(musicListInfo.getList());
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }

    /**
     * 获取音乐分类
     */
    private void queryMusicTypeListData(MusicInfo music) {
        OkGo.<String>get(ApiConstants.QUERY_MUSIC_TYPE_URL)
                .execute(new HttpStringCallback(MusicListManagerActivity.this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        MusicTypeListInfo musicTypeListInfo = GsonUtils.parseJson(response, MusicTypeListInfo.class);
                        items.clear();
                        for (int i = 0; i < musicTypeListInfo.getList().size(); i++) {
                            items.add(musicTypeListInfo.getList().get(i).getType_name());
                        }

                        popupView = new XPopup.Builder(MusicListManagerActivity.this)
                                .customHostLifecycle(getLifecycle())
                                .moveUpToKeyboard(false)
                                .isDestroyOnDismiss(false)
                                .borderRadius(XPopupUtils.dp2px(MusicListManagerActivity.this, 15))
                                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                .asBottomList("请选择移动分类", items.toArray(new String[items.size()]),
                                        new OnSelectListener() {
                                            @Override
                                            public void onSelect(int position, String text) {
                                                editMusic(music.getUid(), null, text);
                                            }
                                        });

                        popupView.show();

                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }

    /**
     * 删除音乐
     */

    private void delMusic(int uid, int position) {
        OkGo.<String>get(ApiConstants.DEL_MUSIC_URL)
                .params("uid", uid)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        showToast(msg);
                        mHomeListAdapter.removeAt(position);
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }
}