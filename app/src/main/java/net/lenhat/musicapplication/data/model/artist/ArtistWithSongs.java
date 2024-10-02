package net.lenhat.musicapplication.data.model.artist;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import net.lenhat.musicapplication.data.model.Song;

import java.util.List;

public class ArtistWithSongs {
    @Embedded
    public Artist artist;

    @Relation(
            parentColumn = "artist_id",
            entityColumn = "song_id",
            associateBy = @Junction(ArtistSongCrossRef.class)
    )
    public List<Song> songs;
}
