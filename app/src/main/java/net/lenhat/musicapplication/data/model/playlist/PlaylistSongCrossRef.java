package net.lenhat.musicapplication.data.model.playlist;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        tableName = "playlist_song_cross_ref",
        primaryKeys = {
                "playlist_id",
                "song_id"
        },
        indices = {
                @Index("song_id"),
                @Index("playlist_id")
        }
)
public class PlaylistSongCrossRef {
    @NonNull
    @ColumnInfo(name = "song_id")
    public String songId = "";

    @ColumnInfo(name = "playlist_id")
    public int playlistId = 0;
}
