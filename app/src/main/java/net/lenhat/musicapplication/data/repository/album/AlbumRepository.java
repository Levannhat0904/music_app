package net.lenhat.musicapplication.data.repository.album;

import net.lenhat.musicapplication.data.model.AlbumList;

import retrofit2.Callback;


public interface AlbumRepository {
    void loadAlbums(Callback<AlbumList> callback);
}
