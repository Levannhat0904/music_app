package net.lenhat.musicapplication.ui.playing;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;

import net.lenhat.musicapplication.data.model.PlayingSong;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlaybackService extends MediaSessionService {
    public static final int NOTIFICATION_ID = 9999;
    public static final String CHANNEL_ID = "music_app_notification_channel_id";
    private MediaSession mMediaSession;
    private Player.Listener mPlayerListener;
    private SharedViewModel mSharedViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onCreate() {
        super.onCreate();
        initSessionAndPlayer();
        setupViewModel();
        setupListener();
    }

    @Nullable
    @Override
    public MediaSession onGetSession(@NonNull MediaSession.ControllerInfo controllerInfo) {
        return mMediaSession;
    }

    @Override
    public void onDestroy() {
        mMediaSession.getPlayer().removeListener(mPlayerListener);
        mMediaSession.getPlayer().release();
        mMediaSession.release();
        mMediaSession = null;
        mDisposable.dispose();
        super.onDestroy();
    }

    private void initSessionAndPlayer() {
        ExoPlayer player = new ExoPlayer.Builder(this)
                .setAudioAttributes(AudioAttributes.DEFAULT, true)
                .build();
        MediaSession.Builder builder = new MediaSession.Builder(this, player);
        PendingIntent pendingIntent = getSingleTopActivity();
        if (pendingIntent != null) {
            builder.setSessionActivity(pendingIntent);
        }
        mMediaSession = builder.build();
    }

    private PendingIntent getSingleTopActivity() {
        Intent intent = new Intent(getApplicationContext(), NowPlayingActivity.class);
        return PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private void setupViewModel() {
        mSharedViewModel = SharedViewModel.getInstance();
    }

    private void setupListener() {
        Player player = mMediaSession.getPlayer();
        mPlayerListener = new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                boolean isPlaylistChanged =
                        reason == Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED;
                mSharedViewModel = SharedViewModel.getInstance();
                Integer indexToPlay = mSharedViewModel.getIndexToPlay().getValue();
                if (!isPlaylistChanged || indexToPlay != null && indexToPlay == 0) {
                    mSharedViewModel.setPlayingSong(player.getCurrentMediaItemIndex());
                    saveDataToDB();
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
            }
        };
        player.addListener(mPlayerListener);
    }

    private void saveDataToDB() {
        Song song = extractSong();
        if (song != null) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Player player = mMediaSession.getPlayer();
                if (player.isPlaying()) {
                    mDisposable.add(mSharedViewModel.saveRecentSong(song)
                            .subscribeOn(Schedulers.io())
                            .subscribe());
                    saveCounterToDB();
                }
            }, 5000);
        }
    }

    private void saveCounterToDB() {
        Song song = extractSong();
        if (song != null) {
            HandlerThread handlerThread = new HandlerThread("Thread Saving Counter",
                    Process.THREAD_PRIORITY_BACKGROUND);
            handlerThread.start();
            Handler handler = new Handler(handlerThread.getLooper());
            handler.post(() -> mDisposable.add(mSharedViewModel.updateSongInDB(song)
                    .subscribeOn(Schedulers.io())
                    .subscribe()));
        }
    }

    private Song extractSong() {
        PlayingSong playingSong = mSharedViewModel.getPlayingSong().getValue();
        return playingSong == null ? null : playingSong.getSong();
    }
}