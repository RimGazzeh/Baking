package com.geekgirl.android.baking.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekgirl.android.baking.R;
import com.geekgirl.android.baking.databinding.FragmentStepBinding;
import com.geekgirl.android.baking.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by Rim Gazzah on 10/10/18
 */
public class StepDetailFragment extends Fragment implements Player.EventListener {


    private static final String STEP_EXTRA = "step_extra";
    private static final String PLAYING_STATE = "playing_state";
    private FragmentStepBinding mFragmentStepBinding;
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private Step mStep;
    private long mPlayerPosition;
    private AudioManager mAudioManager;
    private ComponentName mReceiverComponent;
    private boolean mIsPlaying = true;


    public static StepDetailFragment newInstance(Step step) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(STEP_EXTRA, step);
        stepDetailFragment.setArguments(args);
        return stepDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        mReceiverComponent = new ComponentName(getActivity(), MediaReceiver.class);
        mAudioManager.registerMediaButtonEventReceiver(mReceiverComponent);
        initData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mIsPlaying = savedInstanceState.getBoolean(PLAYING_STATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentStepBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);
        initView();
        initEvent();

        return mFragmentStepBinding.getRoot();
    }

    private void initView() {
        if (mFragmentStepBinding.stepTitle != null && !TextUtils.isEmpty(mStep.getShortDescription())) {
            mFragmentStepBinding.stepTitle.setText(mStep.getShortDescription());
        }
        if (TextUtils.isEmpty(mStep.getVideoURL())) {
            mFragmentStepBinding.stepVideo.setVisibility(View.GONE);
        } else {
            mFragmentStepBinding.stepVideo.setVisibility(View.VISIBLE);
            initializeMediaSession();
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        }
        if (mFragmentStepBinding.stepDescription != null && !TextUtils.isEmpty(mStep.getDescription())) {
            mFragmentStepBinding.stepDescription.setText(mStep.getDescription());
        }
    }

    private void initEvent() {

    }

    private void initData() {
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(STEP_EXTRA);
        }
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mExoPlayer.addListener(this);
            mFragmentStepBinding.stepVideo.setPlayer(mExoPlayer);
            MediaSource mediaSource = buildMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource, true, false);
            mExoPlayer.setPlayWhenReady(mIsPlaying);
            mExoPlayer.seekTo(mPlayerPosition);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), userAgent);
        return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mExoPlayer != null) {
            if (this.isVisible()) {
                if (!isVisibleToUser) {
                    mExoPlayer.setPlayWhenReady(false);
                } else {
                    mExoPlayer.setPlayWhenReady(true);
                }
            }
        }
    }

    /**
     * Release the player when the fragment is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
        mAudioManager.unregisterMediaButtonEventReceiver(mReceiverComponent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayerPosition = mExoPlayer != null ? mExoPlayer.getCurrentPosition() : 0;
        releasePlayer();
    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
            mIsPlaying = true;
        } else if ((playbackState == Player.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
            mIsPlaying = false;
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PLAYING_STATE, mIsPlaying);
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
