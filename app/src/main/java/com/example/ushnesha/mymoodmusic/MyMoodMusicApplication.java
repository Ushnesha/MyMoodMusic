package com.example.ushnesha.mymoodmusic;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyMoodMusicApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }

}
