package com.example.youtubeapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youtubeapp.utilits.Config;
import com.example.youtubeapp.utilits.Constants;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;


import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.youtubeapp.utilits.Constants.RECOVERY_REQUEST;


public class DialogActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    @BindView(R.id.youtube_player_view)
    YouTubePlayerView mYouTubePlayerView;

    @BindView(R.id.seek_bar_youtube_player)
    SeekBar mSeekBar;

    @BindView(R.id.text_view_time_play)
    TextView mTextTimePlay;

    @BindView(R.id.text_view_video_length)
    TextView mTextVideoLength;


    private YouTubePlayer mPlayer;
    private Handler mHandler;

    private static String sCueVideo = "PbiynussSgs";
    private int mMaxSeekbar = 0;
    private int mSeekbarProgress = 0;
    private boolean mFullscreen;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);

        ButterKnife.bind(this);
        mYouTubePlayerView.initialize(Config.YOUTUBE_API_KEY, this);

        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        mHandler = new Handler();

        sCueVideo = getIntent().getStringExtra(Constants.EXTRA_VIDEO_ID_NAME);

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if (youTubePlayer == null) return;
        mPlayer = youTubePlayer;
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        displayVideoLength();

        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        if (!b) {
            youTubePlayer.cueVideo(sCueVideo);
        }

        youTubePlayer.setPlaybackEventListener(mPlaybackEventListener);
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_REQUEST) {
            getYoutubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYoutubePlayerProvider() {
        return mYouTubePlayerView;
    }

    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();
            displayVideoLength();
        }

        @Override
        public void onPaused() {
            mHandler.removeCallbacks(runnable);
        }

        @Override
        public void onStopped() {
            mHandler.removeCallbacks(runnable);
        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {
            displayCurrentTime();
            mHandler.postDelayed(runnable, 100);
        }

    };

    SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mSeekbarProgress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mPlayer.pause();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mPlayer.seekToMillis(mSeekbarProgress);
            if (mSeekbarProgress != mMaxSeekbar) mPlayer.play();

        }

    };
    private void displayCurrentTime() {

        if (mPlayer == null) {
            return;
        }
        String formattedTime = convertTime(mPlayer.getCurrentTimeMillis());
        mSeekBar.setProgress(mPlayer.getCurrentTimeMillis());
        mTextTimePlay.setText(formattedTime);

    }

    private void displayVideoLength() {
        if (mPlayer == null) {
            return;
        }
        String formattedTime = convertTime(mPlayer.getDurationMillis());
        mMaxSeekbar = mPlayer.getDurationMillis();
        mSeekBar.setMax(mMaxSeekbar);
        mTextVideoLength.setText(formattedTime);

    }

    private String convertTime(int millis) {

        int second = millis / 1000;
        int minute = second / 60;
        int hour = minute / 60;

        return hour < 1 ? String.format(Locale.US, "%d%s%02d", minute, ":", second % 60) :
                String.format(Locale.US, "%d%s%02d%s%02d", hour, ":", minute % 60, ":", second % 60);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };

}
