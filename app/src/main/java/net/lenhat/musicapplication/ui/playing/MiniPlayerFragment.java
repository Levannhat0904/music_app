package net.lenhat.musicapplication.ui.playing;

import static android.app.Activity.RESULT_OK;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;

import com.bumptech.glide.Glide;

//import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.PlayingSong;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.databinding.FragmentMiniPlayerBinding;
import net.lenhat.musicapplication.ui.viewmodel.MediaPlayerViewModel;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;
import net.lenhat.musicapplication.utils.AppUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MiniPlayerFragment extends Fragment implements View.OnClickListener {
    private FragmentMiniPlayerBinding mBinding;
    private MiniPlayerViewModel mMiniPlayerViewModel;
    private MediaController mMediaController;
    private Player.Listener mPlayerListener;
    private Animator mAnimator;
    private ObjectAnimator mRotationAnimator;
    private SharedViewModel mSharedViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private final ActivityResultLauncher<Intent> nowPlayingActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    float currentFraction = 0f;
                    if (data != null) {
                        currentFraction = data.getFloatExtra(AppUtils.EXTRA_CURRENT_FRACTION, 0f);
                        mRotationAnimator.setCurrentFraction(currentFraction);
                    } else {
                        currentFraction = 0f;
                    }
                }
            }
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMiniPlayerBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupAnimator();
        setupViewModel();
        setupListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaController != null) {
            mMediaController.removeListener(mPlayerListener);
        }
        mDisposable.dispose();
    }

    @Override
    public void onClick(View v) {
        mAnimator.setTarget(v);
        mAnimator.start();
        if (v.getId() == R.id.btn_mini_player_play_pause) {
            setupPlayPauAction();
        } else if (v.getId() == R.id.btn_mini_player_skip_next) {
            setupNextAction();
        } else if (v.getId() == R.id.btn_mini_player_favorite) {
            setupFavorite();
        }
    }

    private void setupViewModel() {
        mSharedViewModel = SharedViewModel.getInstance();
        mMiniPlayerViewModel = MiniPlayerViewModel.getInstance();
        mSharedViewModel.getCurrentPlaylist().observe(getViewLifecycleOwner(), playlist -> {
            PlayingSong playingSong = mSharedViewModel.getPlayingSong().getValue();
            Playlist currentPlaylist = null;
            if (playingSong != null) {
                currentPlaylist = playingSong.getPlaylist();
            }
            if (mMediaController != null && (mMediaController.getMediaItemCount() == 0
                    || playlist != null && playlist.getMediaItems() != null
                    && !playlist.getMediaItems().isEmpty()
                    && (currentPlaylist == null || currentPlaylist.getId() != playlist.getId()
                    || playlist.getMediaItems().size() != mMediaController.getMediaItemCount()))) {
                mMiniPlayerViewModel.setMediaItems(playlist.getMediaItems());
            }
        });
        mSharedViewModel.getPlayingSong()
                .observe(getViewLifecycleOwner(), playingSong -> {
                    Song song = playingSong.getSong();
                    showSongInfo(song);
                });

        MediaPlayerViewModel.getInstance()
                .getMediaPlayerLiveData()
                .observe(getViewLifecycleOwner(), mediaPlayer -> {
                    if (mediaPlayer != null) {
                        setMediaController(mediaPlayer);
                        setObserveControllerData();
                    }
                });
        mMiniPlayerViewModel.isPlaying()
                .observe(getViewLifecycleOwner(), this::updatePlayingState);
    }

    private void setupPlayPauAction() {
        if (mMediaController.isPlaying()) {
            mMediaController.pause();
        } else {
            // do đã gọi prepare() ở trên (***) nên ta không cần gọi lại prepare()
            mMediaController.play();
        }
    }

    private void setupNextAction() {
        if (mMediaController.hasNextMediaItem()) {
            mMediaController.seekToNext();
            mRotationAnimator.end();
        }
    }

    private void setupFavorite() {
        PlayingSong playingSong = mSharedViewModel.getPlayingSong().getValue();
        if (playingSong != null) {
            Song song = playingSong.getSong();
            song.setFavorite(!song.isFavorite());
            mDisposable.add(mSharedViewModel.updateSongFavoriteStatus(song)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> updateFavoriteStatus(song)));
        }
    }

    private void updateFavoriteStatus(Song song) {
        if (song.isFavorite()) {
            mBinding.btnMiniPlayerFavorite.setImageResource(R.drawable.ic_favorite_on);
        } else {
            mBinding.btnMiniPlayerFavorite.setImageResource(R.drawable.ic_favorite_off);
        }
    }

    private void setupAnimator() {
        mAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.button_pressed);
        mRotationAnimator = ObjectAnimator
                .ofFloat(mBinding.imageMiniPlayerAvatar, "rotation", 0f, 360f);
        mRotationAnimator.setInterpolator(new LinearInterpolator());
        mRotationAnimator.setDuration(12000);
        mRotationAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    private void setupListener() {
        mBinding.getRoot().setOnClickListener(v -> navigateToNowPlaying());
        mBinding.btnMiniPlayerPlayPause.setOnClickListener(this);
        mBinding.btnMiniPlayerSkipNext.setOnClickListener(this);
        mBinding.btnMiniPlayerFavorite.setOnClickListener(this);
    }

    private void navigateToNowPlaying() {
        Intent intent = new Intent(requireContext(), NowPlayingActivity.class);
        intent.putExtra(AppUtils.EXTRA_CURRENT_FRACTION, mRotationAnimator.getAnimatedFraction());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeCustomAnimation(requireContext(), R.anim.slide_up, R.anim.fade_out);
        nowPlayingActivityLauncher.launch(intent, options);
    }

    private void updatePlayingState(Boolean isPlaying) {
        if (isPlaying) {
            mBinding.btnMiniPlayerPlayPause.setImageResource(R.drawable.ic_pause_circle);
            if (mRotationAnimator.isPaused()) {
                mRotationAnimator.resume();
            } else if (!mRotationAnimator.isRunning()) {
                mRotationAnimator.start();
            }
        } else {
            mBinding.btnMiniPlayerPlayPause.setImageResource(R.drawable.ic_play_circle);
            mRotationAnimator.pause();
        }
    }

    private void showSongInfo(Song song) {
        if (song != null) {
            Glide.with(this)
                    .load(song.getImage())
                    .error(R.drawable.ic_music_node)
                    .circleCrop()
                    .into(mBinding.imageMiniPlayerAvatar);
            mBinding.textMiniPlayerTitle.setText(song.getTitle());
            mBinding.textMiniPlayerArtist.setText(song.getArtist());
            updateFavoriteStatus(song);
        }
    }

    private void setMediaController(MediaController mediaController) {
        mMediaController = mediaController;
        mPlayerListener = new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                mMiniPlayerViewModel.setIsPlaying(isPlaying);
            }
        };
        mMediaController.addListener(mPlayerListener);
    }

    private void setObserveControllerData() {
        if (mMediaController != null && mMediaController.isPlaying() && AppUtils.sIsConfigChanged) {
            AppUtils.sIsConfigChanged = false;
            return;
        }
        mMiniPlayerViewModel.getMediaItems().observe(getViewLifecycleOwner(), mediaItems -> {
            if (mMediaController != null) {
                mMediaController.setMediaItems(mediaItems);
            }
        });
        observeIndexToPlay();
    }

    private void observeIndexToPlay() {
        mSharedViewModel.getIndexToPlay().observe(getViewLifecycleOwner(), index -> {
            PlayingSong playingSong = mSharedViewModel.getPlayingSong().getValue();
            Playlist currentPlaylist = null;
            if (playingSong != null) {
                currentPlaylist = playingSong.getPlaylist();
            }
            Playlist playlist = mSharedViewModel.getCurrentPlaylist().getValue();
            // TH1: cùng playlist, cùng index => KHÔNG phát lại mà tiếp tục
            // TH2: khác playlist, cùng index => PHÁT từ đầu bài hát
            if (mMediaController != null && index > -1) {
                boolean condition1 = mMediaController.getMediaItemCount() > index
                        && mMediaController.getCurrentMediaItemIndex() != index;
                boolean condition2 = playlist != null && currentPlaylist != null
                        && mMediaController.getCurrentMediaItemIndex() == index
                        && playlist.getId() != currentPlaylist.getId();
                if (condition1 || condition2) {
                    mMediaController.seekTo(index, 0);
                    mMediaController.prepare(); // (***)
                }
            }
        });
    }
}