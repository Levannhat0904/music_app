package net.lenhat.musicapplication.data.source;

import net.lenhat.musicapplication.data.model.AlbumList;

import retrofit2.Callback;

public interface AlbumDataSource {
    interface Remote {
        void loadAlbums(Callback<AlbumList> callback);
    }

    interface Local {

    }
}
