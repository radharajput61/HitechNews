package com.app.hitechnews.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.hitechnews.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

//import bg.devlabs.fullscreenvideoview.FullscreenVideoView;


public class PlayerActivity extends AppCompatActivity {

    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;

    String videoURL = "http://abdekhegabharat.sigmasoftwares.co/PostVideos/App_637641228021524348.MP4";
//    FullscreenVideoView fullscreenVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
//        fullscreenVideoView = findViewById(R.id.fullscreenVideoView);
//        fullscreenVideoView.videoUrl(videoURL)
//                .enableAutoStart();
    }
}