package net.lenhat.musicapplication.data.source.remote;

import net.lenhat.musicapplication.data.model.AlbumList;
import net.lenhat.musicapplication.data.model.SongList;
import net.lenhat.musicapplication.data.model.artist.ArtistList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AppService {
    @GET("/resources/braniumapis/playlist.json")
    Call<AlbumList> getAlbums();

    @GET("/resources/braniumapis/songs.json")
    Call<SongList> getSongs();

    @GET("/resources/braniumapis/artists.json")
    Call<ArtistList> getArtists();
}
