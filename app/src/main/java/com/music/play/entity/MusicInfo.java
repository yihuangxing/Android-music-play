package com.music.play.entity;

import java.io.Serializable;

public class MusicInfo implements Serializable {
    private int uid;
    private String music_title;
    private String music_url;
    private String music_type;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMusic_title() {
        return music_title;
    }

    public void setMusic_title(String music_title) {
        this.music_title = music_title;
    }

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
        this.music_url = music_url;
    }

    public String getMusic_type() {
        return music_type;
    }

    public void setMusic_type(String music_type) {
        this.music_type = music_type;
    }
}
