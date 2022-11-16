package com.music.play;

import android.app.Application;
import android.content.Intent;

import com.arialyy.aria.core.Aria;
import com.music.play.service.PlayService;
import com.music.play.utils.Preferences;

/**
 * desc   :
 */
public class MusicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.init(this);
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);

        Aria.init(this);
    }
}
