package com.music.play.entity;

import java.util.List;

public class MusicListInfo {
    private List<MusicInfo> list;

    public MusicListInfo(List<MusicInfo> list) {
        this.list = list;
    }

    public List<MusicInfo> getList() {
        return list;
    }

    public void setList(List<MusicInfo> list) {
        this.list = list;
    }
}
