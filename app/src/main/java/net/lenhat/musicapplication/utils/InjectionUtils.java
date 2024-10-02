package net.lenhat.musicapplication.utils;

import android.content.Context;

import net.lenhat.musicapplication.data.repository.artist.ArtistRepository;
import net.lenhat.musicapplication.data.repository.artist.ArtistRepositoryImpl;
import net.lenhat.musicapplication.data.repository.playlist.PlaylistRepositoryImpl;
import net.lenhat.musicapplication.data.repository.recent.RecentSongRepository;
import net.lenhat.musicapplication.data.repository.recent.RecentSongRepositoryImpl;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;
import net.lenhat.musicapplication.data.source.ArtistDataSource;
import net.lenhat.musicapplication.data.source.PlaylistDataSource;
import net.lenhat.musicapplication.data.source.RecentSongDataSource;
import net.lenhat.musicapplication.data.source.local.AppDatabase;
import net.lenhat.musicapplication.data.source.local.artist.LocalArtistDataSource;
import net.lenhat.musicapplication.data.source.local.playlist.LocalPlaylistDataSource;
import net.lenhat.musicapplication.data.source.local.recent.LocalRecentSongDataSource;
import net.lenhat.musicapplication.data.source.local.song.LocalSongDataSource;

public abstract class InjectionUtils {
    public static RecentSongDataSource provideRecentSongDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return new LocalRecentSongDataSource(database.recentSongDao());
    }

    public static RecentSongRepository provideRecentSongRepository(RecentSongDataSource dataSource) {
        return new RecentSongRepositoryImpl(dataSource);
    }

    public static LocalSongDataSource provideLocalSongDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return new LocalSongDataSource(database.songDao());
    }

    public static SongRepositoryImpl provideSongRepository(LocalSongDataSource dataSource) {
        return new SongRepositoryImpl(dataSource);
    }

    public static PlaylistDataSource.Local provideLocalPlaylistDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return new LocalPlaylistDataSource(database.playlistDao());
    }

    public static PlaylistRepositoryImpl providePlaylistRepository(
            PlaylistDataSource.Local localDataSource) {
        return new PlaylistRepositoryImpl(localDataSource);
    }

    public static ArtistDataSource.Local provideLocalArtistDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return new LocalArtistDataSource(database.artistDao());
    }

    public static ArtistRepository provideArtistRepository(ArtistDataSource.Local localDataSource) {
        return new ArtistRepositoryImpl(localDataSource);
    }
}
