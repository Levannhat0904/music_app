package net.lenhat.musicapplication.ui.playing;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;

import com.bumptech.glide.Glide;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.PlayingSong;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.databinding.ActivityNowPlayingBinding;
import net.lenhat.musicapplication.ui.dialog.OptionMenuViewModel;
import net.lenhat.musicapplication.ui.dialog.SongOptionMenuDialogFragment;
import net.lenhat.musicapplication.ui.viewmodel.MediaPlayerViewModel;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;
import net.lenhat.musicapplication.utils.AppUtils;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NowPlayingActivity extends AppCompatActivity
        implements View.OnClickListener {
    private ActivityNowPlayingBinding mBinding;
    private NowPlayingViewModel mNowPlayingViewModel;
    private SharedViewModel mSharedViewModel;
    private MediaController mMediaController;
    private Player.Listener mPlayerListener;
    private Handler mHandler;
    private Runnable mCallback;
    private Animator mPressedAnimator;
    private ObjectAnimator mRotationAnimator;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityNowPlayingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setupToolbar();
        setupAnimator();
        setupViewModel();
        setupView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.fade_in, R.anim.slide_down);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaController != null) {
            mMediaController.removeListener(mPlayerListener);
        }
        mHandler.removeCallbacks(mCallback);
        mMediaController = null;
        mPlayerListener = null;
        mDisposable.clear();
    }

    @Override
    public void onClick(View v) {
        mPressedAnimator.setTarget(v);
        mPressedAnimator.start();
        if (v.getId() == R.id.btn_play_pause) {
            setupActionPlayPause();
        } else if (v.getId() == R.id.btn_skip_next) {
            setupActionSkipNext();
        } else if (v.getId() == R.id.btn_skip_previous) {
            setupActionSkipPrevious();
        } else if (v.getId() == R.id.btn_repeat) {
            setupActionRepeat();
        } else if (v.getId() == R.id.btn_shuffle) {
            setupActionShuffle();
        } else if (v.getId() == R.id.btn_now_playing_favorite) {
            setupActionFavorite();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(mBinding.toolbarNowPlaying);
        mBinding.toolbarNowPlaying.setNavigationOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(AppUtils.EXTRA_CURRENT_FRACTION, mRotationAnimator.getAnimatedFraction());
            setResult(RESULT_OK, intent);
            getOnBackPressedDispatcher().onBackPressed();
        });
        mBinding.btnNowPlayingMoreOption.setOnClickListener(v -> showOptionMenuDialog());
    }

    private void setupViewModel() {
        mNowPlayingViewModel = new ViewModelProvider(this).get(NowPlayingViewModel.class);
        MediaPlayerViewModel mediaPlayerViewModel = MediaPlayerViewModel.getInstance();
        mediaPlayerViewModel.getMediaPlayerLiveData().observe(this, mediaPlayer -> {
            mMediaController = mediaPlayer;
            setupController();
            updateSeekBar();
        });
        mSharedViewModel = SharedViewModel.getInstance();
        mSharedViewModel.getPlayingSong().observe(this, playingSong -> {
            if (playingSong != null) {
                Song song = playingSong.getSong();
                showSongInfo(song);
            }
        });
        mNowPlayingViewModel.isPlaying().observe(this, isPlaying -> {
            if (isPlaying) {
                if (mRotationAnimator.isPaused()) {
                    mRotationAnimator.resume();
                } else if (!mRotationAnimator.isRunning()) {
                    mRotationAnimator.start();
                }
                mBinding.btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            } else {
                mBinding.btnPlayPause.setImageResource(R.drawable.ic_play_circle);
                mRotationAnimator.pause();
            }
        });
    }

    private void setupView() {
        mBinding.btnPlayPause.setOnClickListener(this);
        mBinding.btnSkipNext.setOnClickListener(this);
        mBinding.btnSkipPrevious.setOnClickListener(this);
        mBinding.btnRepeat.setOnClickListener(this);
        mBinding.btnShuffle.setOnClickListener(this);
        mBinding.btnNowPlayingFavorite.setOnClickListener(this);
        mBinding.seekBarNowPlaying.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String currentTimeLabel = mNowPlayingViewModel.getTimeLabel(progress);
                mBinding.textCurrentDuration.setText(currentTimeLabel);
                if (fromUser) {
                    mMediaController.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setupAnimator() {
        mPressedAnimator = AnimatorInflater.loadAnimator(this, R.animator.button_pressed);
        mRotationAnimator =
                ObjectAnimator.ofFloat(mBinding.imageNowPlayingArtwork, "rotation", 0f, 360f);
        mRotationAnimator.setDuration(12000);
        mRotationAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRotationAnimator.setInterpolator(new LinearInterpolator());
        float currentFraction = getIntent().getFloatExtra(AppUtils.EXTRA_CURRENT_FRACTION, 0f);
        mRotationAnimator.setCurrentFraction(currentFraction);
    }

    private void setupController() {
        registerMediaController();
        if (mMediaController != null) {
            if (!mMediaController.isPlaying()) {
                mMediaController.prepare();
                mMediaController.play();
            }
            if (mMediaController.isPlaying()) {
                mNowPlayingViewModel.setIsPlaying(true);
            }
        }
    }

    private void registerMediaController() {
        if (mMediaController != null) {
            mPlayerListener = new Player.Listener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    mNowPlayingViewModel.setIsPlaying(isPlaying);
                }

                @Override
                public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                    updateSeekBarMaxValue();
                    updateDuration();
                    if (mMediaController.isPlaying()) {
                        mRotationAnimator.start();
                    }
                }

                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    if (playbackState == Player.STATE_READY) {
                        updateSeekBarMaxValue();
                        updateDuration();
                    }
                }
            };
            mMediaController.addListener(mPlayerListener);
        }
    }

    private void showSongInfo(Song song) {
        if (song != null) {
            updateSeekBarMaxValue();
            updateDuration();
            showRepeatMode();
            showShuffleMode();
            showFavoriteMode(song.isFavorite());
            mBinding.textNowPlayingAlbum.setText(song.getAlbum());
            mBinding.textNowPlayingSongTitle.setText(song.getTitle());
            mBinding.textNowPlayingSongArtist.setText(song.getArtist());
            Glide.with(this)
                    .load(song.getImage())
                    .circleCrop()
                    .error(R.drawable.ic_album)
                    .into(mBinding.imageNowPlayingArtwork);
        }
    }

    private void showOptionMenuDialog() {
        OptionMenuViewModel optionMenuViewModel =
                new ViewModelProvider(this).get(OptionMenuViewModel.class);
        SongOptionMenuDialogFragment dialogFragment = SongOptionMenuDialogFragment.newInstance();
        Song song = null;
        if (mSharedViewModel.getPlayingSong().getValue() != null) {
            song = mSharedViewModel.getPlayingSong().getValue().getSong();
        }
        optionMenuViewModel.setSong(song);
        dialogFragment.show(getSupportFragmentManager(), SongOptionMenuDialogFragment.TAG);
    }

    private void setupActionPlayPause() {
        if (mMediaController != null) {
            if (mMediaController.isPlaying()) {
                mMediaController.pause();
            } else {
                mMediaController.play();
            }
        }
    }

    private void setupActionSkipNext() {
        if (mMediaController != null && mMediaController.hasNextMediaItem()) {
            mMediaController.seekToNextMediaItem();
            mRotationAnimator.end();
        }
    }

    private void setupActionSkipPrevious() {
        if (mMediaController != null && mMediaController.hasPreviousMediaItem()) {
            mMediaController.seekToPreviousMediaItem();
            mRotationAnimator.end();
        }
    }

    private void setupActionRepeat() {
        if (mMediaController != null) {
            int repeatMode = mMediaController.getRepeatMode();
            switch (repeatMode) {
                case Player.REPEAT_MODE_OFF:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_one);
                    mMediaController.setRepeatMode(Player.REPEAT_MODE_ONE);
                    break;
                case Player.REPEAT_MODE_ONE:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_all);
                    mMediaController.setRepeatMode(Player.REPEAT_MODE_ALL);
                    break;
                case Player.REPEAT_MODE_ALL:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_off);
                    mMediaController.setRepeatMode(Player.REPEAT_MODE_OFF);
                    break;
            }
        }
    }

    private void setupActionShuffle() {
        if (mMediaController != null) {
            boolean isShuffle = mMediaController.getShuffleModeEnabled();
            mMediaController.setShuffleModeEnabled(!isShuffle);
            showShuffleMode();
        }
    }

    private void setupActionFavorite() {
        PlayingSong playingSong = mSharedViewModel.getPlayingSong().getValue();
        Song song = null;
        if (playingSong != null) {
            song = playingSong.getSong();
        }
        if (song != null) {
            song.setFavorite(!song.isFavorite());
            mSharedViewModel.getPlayingSong().getValue().setSong(song);
            showFavoriteMode(song.isFavorite());
            mDisposable.add(mSharedViewModel.updateSongFavoriteStatus(song) // updateSongInDB(song)
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        }
    }

    private void showShuffleMode() {
        if (mMediaController != null) {
            boolean isShuffle = mMediaController.getShuffleModeEnabled();
            if (isShuffle) {
                mBinding.btnShuffle.setImageResource(R.drawable.ic_shuffle_on);
            } else {
                mBinding.btnShuffle.setImageResource(R.drawable.ic_shuffle_off);
            }
        }
    }

    private void showFavoriteMode(boolean isFavorite) {
        if (isFavorite) {
            mBinding.btnNowPlayingFavorite.setImageResource(R.drawable.ic_favorite_on);
        } else {
            mBinding.btnNowPlayingFavorite.setImageResource(R.drawable.ic_favorite_off);
        }
    }

    private void showRepeatMode() {
        if (mMediaController != null) {
            int repeatMode = mMediaController.getRepeatMode();
            switch (repeatMode) {
                case Player.REPEAT_MODE_OFF:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_off);
                    break;
                case Player.REPEAT_MODE_ONE:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_one);
                    break;
                case Player.REPEAT_MODE_ALL:
                    mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_all);
                    break;
            }
        }
    }

    private void updateSeekBar() {
        mHandler = new Handler();
        mCallback = new Runnable() {
            @Override
            public void run() {
                if (mMediaController != null) {
                    int currentPosition = (int) mMediaController.getCurrentPosition();
                    mBinding.seekBarNowPlaying.setProgress(currentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.post(mCallback);
    }

    private void updateSeekBarMaxValue() {
        long duration = mMediaController.getDuration();
        int seekBarMaxValue = 0;
        if (duration > 0 && duration <= Integer.MAX_VALUE) {
            seekBarMaxValue = (int) duration;
        }
        int progress = (int) mMediaController.getCurrentPosition();
        mBinding.seekBarNowPlaying.setProgress(progress);
        mBinding.seekBarNowPlaying.setMax(seekBarMaxValue);
    }

    private void updateDuration() {
        String timeLabel = mNowPlayingViewModel.getTimeLabel(mMediaController.getDuration());
        mBinding.textTotalDuration.setText(timeLabel);
    }
}