package net.lenhat.musicapplication.data.source.local;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.AutoMigrationSpec;

import net.lenhat.musicapplication.data.model.Album;
import net.lenhat.musicapplication.data.model.RecentSong;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.model.artist.Artist;
import net.lenhat.musicapplication.data.model.artist.ArtistSongCrossRef;
import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.data.model.playlist.PlaylistSongCrossRef;
import net.lenhat.musicapplication.data.source.local.artist.ArtistDao;
import net.lenhat.musicapplication.data.source.local.playlist.PlaylistDao;
import net.lenhat.musicapplication.data.source.local.recent.RecentSongDao;
import net.lenhat.musicapplication.data.source.local.song.SongDao;

@Database(
        entities = {
                Album.class,
                Playlist.class,
                Song.class,
                RecentSong.class,
                PlaylistSongCrossRef.class,
                Artist.class,
                ArtistSongCrossRef.class
        },
        version = 5,
        exportSchema = true,
        autoMigrations = {
                @AutoMigration(
                        from = 4,
                        to = 5,
                        spec = AppDatabase.DbMigrationSpec.class
                )
        }
)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "music.db")
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract SongDao songDao();

    public abstract RecentSongDao recentSongDao();

    public abstract PlaylistDao playlistDao();

    public abstract AlbumDao albumDao();

    public abstract ArtistDao artistDao();

    static class DbMigrationSpec implements AutoMigrationSpec {
    }
}
