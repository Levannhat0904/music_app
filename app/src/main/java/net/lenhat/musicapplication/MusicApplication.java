package net.lenhat.musicapplication;

import android.app.Application;
import android.content.ComponentName;
import android.content.SharedPreferences;

import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;
import androidx.preference.PreferenceManager;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import net.lenhat.musicapplication.data.repository.artist.ArtistRepository;
import net.lenhat.musicapplication.data.repository.playlist.PlaylistRepositoryImpl;
import net.lenhat.musicapplication.data.repository.recent.RecentSongRepository;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;
import net.lenhat.musicapplication.data.source.ArtistDataSource;
import net.lenhat.musicapplication.data.source.PlaylistDataSource;
import net.lenhat.musicapplication.data.source.RecentSongDataSource;
import net.lenhat.musicapplication.data.source.local.song.LocalSongDataSource;
import net.lenhat.musicapplication.ui.playing.PlaybackService;
import net.lenhat.musicapplication.ui.settings.SettingsFragment;
import net.lenhat.musicapplication.ui.viewmodel.MediaPlayerViewModel;
import net.lenhat.musicapplication.utils.InjectionUtils;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MusicApplication extends Application {
    private ListenableFuture<MediaController> mControllerFuture;
    private MediaController mMediaController;
    private RecentSongRepository mRecentSongRepository;
    private SongRepositoryImpl mSongRepository;
    private PlaylistRepositoryImpl mPlaylistRepository;
    private ArtistRepository mArtistRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        setupLanguage();
        setupMediaController();
        setupComponents();
    }

    private void setupLanguage() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String defaultLanguage = Locale.getDefault().getLanguage();
        String language = sharedPreferences.getString(SettingsFragment.KEY_PREF_LANGUAGE, defaultLanguage);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsFragment.KEY_PREF_LANGUAGE, language);
        editor.apply();
    }

    private void setupMediaController() {
        SessionToken sessionToken = new SessionToken(getApplicationContext(),
                new ComponentName(getApplicationContext(), PlaybackService.class));
        mControllerFuture = new MediaController
                .Builder(getApplicationContext(), sessionToken).buildAsync();
        Runnable listener = () -> {
            if (mControllerFuture.isDone() && !mControllerFuture.isCancelled()) {
                try {
                    mMediaController = mControllerFuture.get();
                    MediaPlayerViewModel.getInstance().setMediaPlayer(mMediaController);
                } catch (ExecutionException | InterruptedException ignored) {
                }
            } else {
                mMediaController = null;
            }
        };
        mControllerFuture.addListener(listener, MoreExecutors.directExecutor());
    }

    private void setupComponents() {
        RecentSongDataSource recentSongDataSource =
                InjectionUtils.provideRecentSongDataSource(getApplicationContext());
        mRecentSongRepository = InjectionUtils.provideRecentSongRepository(recentSongDataSource);
        LocalSongDataSource localSongDataSource =
                InjectionUtils.provideLocalSongDataSource(getApplicationContext());
        mSongRepository = InjectionUtils.provideSongRepository(localSongDataSource);
        PlaylistDataSource.Local localPlaylistDataSource =
                InjectionUtils.provideLocalPlaylistDataSource(getApplicationContext());
        mPlaylistRepository = InjectionUtils.providePlaylistRepository(localPlaylistDataSource);
        ArtistDataSource.Local localArtistDataSource =
                InjectionUtils.provideLocalArtistDataSource(getApplicationContext());
        mArtistRepository = InjectionUtils.provideArtistRepository(localArtistDataSource);
    }

    public RecentSongRepository getRecentSongRepository() {
        return mRecentSongRepository;
    }

    public SongRepositoryImpl getSongRepository() {
        return mSongRepository;
    }

    public PlaylistRepositoryImpl getPlaylistRepository() {
        return mPlaylistRepository;
    }

    public ArtistRepository getArtistRepository() {
        return mArtistRepository;
    }
}
