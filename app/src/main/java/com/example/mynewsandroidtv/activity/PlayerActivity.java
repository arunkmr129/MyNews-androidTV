package com.example.mynewsandroidtv.activity;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mynewsandroidtv.R;
import com.example.mynewsandroidtv.model.VideoItemModel;
import com.example.mynewsandroidtv.utility.AppConst;
import com.example.mynewsandroidtv.utility.UtilityClass;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

public class PlayerActivity extends Activity {


    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private ProgressBar progressBar;
    private PlayerView playerView;
    private TextView tvTitle;
    private UtilityClass utilityClass;
    private String VIDEO_URL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    private VideoItemModel videoItemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initializeComponent();
        getIntentData();
        setDataOnView();

        int colorCodeDark = Color.parseColor("#9E9E9E");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
        }
        if (videoItemModel != null && !utilityClass.isNullOrEmpty(videoItemModel.getVideoURL()))
            initialize(videoItemModel.getVideoURL());
        else
            initialize(VIDEO_URL);

    }

    private void initializeComponent() {
        progressBar = findViewById(R.id.progress_bar);
        playerView = findViewById(R.id.player_view);
        tvTitle = findViewById(R.id.tv_title);
        utilityClass = new UtilityClass(this);
    }

    private void getIntentData() {
        videoItemModel = (VideoItemModel) getIntent().getSerializableExtra(AppConst.ITEM);
    }

    private void setDataOnView() {
        if (videoItemModel != null && !utilityClass.isNullOrEmpty(videoItemModel.getName()))
            tvTitle.setText(videoItemModel.getName());
    }

    private void initialize(String videoUrl) {
        Uri mp4VideoUri = Uri.parse(Objects.requireNonNull(videoUrl));
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        //Set media controller
        playerView.setUseController(true);
        playerView.requestFocus();
        // Bind the player to the view.

        playerView.setPlayer(player);

        dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"));
        MediaSource contentMediaSource = buildMediaSource(mp4VideoUri);
        final LoopingMediaSource loopingSource = new LoopingMediaSource(contentMediaSource);
        // Prepare the player with the source.

        player.prepare(contentMediaSource);

        player.addListener(new ExoPlayer.EventListener() {

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
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                playerView.setKeepScreenOn(playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED && playWhenReady);

                if (playbackState == Player.STATE_ENDED) {
                    onBackPressed();
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
                player.stop();
                player.prepare(loopingSource);
                player.setPlayWhenReady(true);
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

        player.setPlayWhenReady(true);

        RelativeLayout layout = findViewById(R.id.exoplayerLayout);
        RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout_description.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layout.setLayoutParams(layout_description);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null)
            player.release();
    }

    private MediaSource buildMediaSource(Uri uri) {
        @C.ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void pausePlayer() {
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    private void startPlayer() {
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    private boolean isPlaying() {
        return player != null && player.getPlaybackState() != Player.STATE_ENDED
                && player.getPlaybackState() != Player.STATE_IDLE && player.getPlayWhenReady();
    }

}