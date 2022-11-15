package com.music.play.service;

import com.music.play.entity.MusicInfo;

/**
 * desc   :
 */
public interface OnPlayerEventListener {

    /**
     * 切换歌曲
     */
    void onChange(MusicInfo music);

    /**
     * 继续播放
     */
    void onPlayerStart(long duration);

    /**
     * 暂停播放
     */
    void onPlayerPause();

    /**
     * 更新进度
     */
    void onPublish(int progress);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);
}
