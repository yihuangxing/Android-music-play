package com.music.play.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.music.play.entity.MusicInfo;
import com.music.play.utils.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * desc   :
 */
public class AudioPlayer {
    private static final String TAG = "=====AudioPlayer";
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;

    private static final long TIME_UPDATE = 300L;

    private MediaPlayer mediaPlayer;
    private Handler handler;
    private List<MusicInfo> musicList = new ArrayList<>();
    private final List<OnPlayerEventListener> listeners = new ArrayList<>();
    private int state = STATE_IDLE;

    public static AudioPlayer get() {
        return SingletonHolder.instance;
    }


    private static class SingletonHolder {
        private static AudioPlayer instance = new AudioPlayer();
    }

    private AudioPlayer() {
    }

    public boolean isPreparing() {
        return state == STATE_PREPARING;
    }

    public boolean isPausing() {
        return state == STATE_PAUSE;
    }

    public int getPlayPosition() {
        int position = Preferences.getPlayPosition();
        if (position < 0 || position >= musicList.size()) {
            position = 0;
            Preferences.savePlayPosition(position);
        }
        return position;
    }

    private void setPlayPosition(int position) {
        Preferences.savePlayPosition(position);
    }

    public MusicInfo getPlayMusic() {
        if (musicList.isEmpty()) {
            return null;
        }
        return musicList.get(getPlayPosition());
    }

    public void setMusicList(List<MusicInfo> musicList) {
        this.musicList = musicList;
    }


    public void init(Context context) {
        Log.d(TAG, "init: AudioPlayer=========================");
        mediaPlayer = new MediaPlayer();
        handler = new Handler(Looper.getMainLooper());
        mediaPlayer.setOnCompletionListener(mp -> next());
        mediaPlayer.setOnPreparedListener(mp -> {
            if (isPreparing()) {
                startPlayer();
            }
        });
        mediaPlayer.setOnBufferingUpdateListener((mp, percent) -> {
            for (OnPlayerEventListener listener : listeners) {
                listener.onBufferingUpdate(percent);
            }
        });
    }

    public long getAudioPosition() {
        if (isPlaying() || isPausing()) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public void addAndPlay(MusicInfo music) {
        int position = musicList.indexOf(music);
        if (position < 0) {
            musicList.add(music);
            position = musicList.size() - 1;
        }
        play(position);
    }

    public void play(int position) {
        if (musicList.isEmpty()) {
            return;
        }

        if (position < 0) {
            position = musicList.size() - 1;
        } else if (position >= musicList.size()) {
            position = 0;
        }

        setPlayPosition(position);
        MusicInfo music = getPlayMusic();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getMusic_url());
            mediaPlayer.prepareAsync();
            state = STATE_PREPARING;
            for (OnPlayerEventListener listener : listeners) {
                listener.onChange(music);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "play: ");
        }
    }


    public void next() {
        if (musicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                play(new Random().nextInt(musicList.size()));
                break;
            case SINGLE:
                play(getPlayPosition());
                break;
            case LOOP:
            default:
                play(getPlayPosition() + 1);
                break;
        }
    }


    public void prev() {
        if (musicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                play(new Random().nextInt(musicList.size()));
                break;
            case SINGLE:
                play(getPlayPosition());
                break;
            case LOOP:
            default:
                play(getPlayPosition() - 1);
                break;
        }
    }


    public void startPlayer() {
        if (!isPreparing() && !isPausing()) {
            return;
        }
        mediaPlayer.start();
        state = STATE_PLAYING;
        handler.post(mPublishRunnable);
        for (OnPlayerEventListener listener : listeners) {
            listener.onPlayerStart(mediaPlayer.getDuration());
        }
    }

    public void pausePlayer() {
        pausePlayer(true);
    }

    public void pausePlayer(boolean abandonAudioFocus) {
        if (!isPlaying()) {
            return;
        }

        mediaPlayer.pause();
        state = STATE_PAUSE;
        handler.removeCallbacks(mPublishRunnable);
        for (OnPlayerEventListener listener : listeners) {
            listener.onPlayerPause();
        }
    }

    public void playPause() {
        if (isPreparing()) {
            stopPlayer();
        } else if (isPlaying()) {
            pausePlayer();
        } else if (isPausing()) {
            startPlayer();
        } else {
            play(getPlayPosition());
        }
    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPausing()) {
            mediaPlayer.seekTo(msec);
            for (OnPlayerEventListener listener : listeners) {
                listener.onPublish(msec);
            }
        }
    }

    public void stopPlayer() {
        if (isIdle()) {
            return;
        }

        pausePlayer();
        mediaPlayer.reset();
        state = STATE_IDLE;
    }

    public boolean isIdle() {
        return state == STATE_IDLE;
    }

    public boolean isPlaying() {
        return state == STATE_PLAYING;
    }

    private Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying()) {
                for (OnPlayerEventListener listener : listeners) {
                    listener.onPublish(mediaPlayer.getCurrentPosition());
                }
            }
            handler.postDelayed(this, TIME_UPDATE);
        }
    };


    public void addOnPlayEventListener(OnPlayerEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeOnPlayEventListener(OnPlayerEventListener listener) {
        listeners.remove(listener);
    }

}
