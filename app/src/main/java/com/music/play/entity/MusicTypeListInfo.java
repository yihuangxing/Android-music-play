package com.music.play.entity;

import org.w3c.dom.TypeInfo;

import java.util.List;

public class MusicTypeListInfo {
    private List<MusicTypeInfo> list;

    public MusicTypeListInfo(List<MusicTypeInfo> list) {
        this.list = list;
    }

    public List<MusicTypeInfo> getList() {
        return list;
    }

    public void setList(List<MusicTypeInfo> list) {
        this.list = list;
    }
}
