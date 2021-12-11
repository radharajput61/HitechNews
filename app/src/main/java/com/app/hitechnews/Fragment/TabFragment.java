package com.app.hitechnews.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hitechnews.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;


public class TabFragment extends Fragment {

    public TabFragment() {
        // Required empty public constructor
    }

    public static TabFragment newInstance(String name) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString("tabName",name);
        fragment.setArguments(args);
        return fragment;
    }
    TextView tvNum;


    private PlayerView playerView;
    private ProgressBar progressBar;
    private ImageView imgFullScreen;
    private SimpleExoPlayer simpleExoPlayer;
    private boolean flag = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_tab, container, false);
        tvNum=root.findViewById(R.id.tvNum);
        tvNum.setText(""+getArguments().getString("tabName"));
        InitializationFields(root);
        imgFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayer.setPlayWhenReady(false);
                if (flag) {
                    imgFullScreen.setImageResource(R.drawable.ic_baseline_fullscreen_24);
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    flag = false;
                } else {
                    imgFullScreen.setImageResource(R.drawable.ic_baseline_fullscreen_24);
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    flag = true;
                }

            }
        });
        playVideo();
        return root;
    }
    private void InitializationFields(View root) {
        playerView = root.findViewById(R.id.player_view);
        progressBar = root.findViewById(R.id.progress_bar);
        imgFullScreen = playerView.findViewById(R.id.btn_fullscreen);
    }

    private void playVideo() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Uri videoUri = Uri.parse("http://rpi.sigmasoftwares.org/Movie/file/637509730005536985.mp4");
        LoadControl loadControl = new DefaultLoadControl();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
         DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(videoUri, factory, extractorsFactory, null, null);
        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                Check Condition
                if (playbackState == Player.STATE_BUFFERING) {
//                    When Buffering
//                    Show Progress Bar
                    progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
//                    When Ready
//                    Hide Progress Bar
                    progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
////        Stop video when ready
//        simpleExoPlayer.setPlayWhenReady(false);
////        Get Playback state
//        simpleExoPlayer.getPlaybackState();
//    }


    @Override
    public void onStart() {
        Toast.makeText(getActivity(), "onStart", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Toast.makeText(getActivity(), "onAttach", Toast.LENGTH_SHORT).show();
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }
}