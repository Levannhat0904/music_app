package net.lenhat.musicapplication.data.model.playlist;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import net.lenhat.musicapplication.data.model.Song;

import java.util.List;

public class PlaylistWithSongs {
    @Embedded
    public Playlist playlist;

    @Relation(
            parentColumn = "playlist_id",
            entityColumn = "song_id",
            associateBy = @Junction(PlaylistSongCrossRef.class)
    )
    public List<Song> songs;
}
