package net.lenhat.musicapplication.data.repository.album;

import net.lenhat.musicapplication.data.model.AlbumList;
import net.lenhat.musicapplication.data.source.remote.AlbumRemoteDataSourceImpl;

import retrofit2.Callback;

public class AlbumRepositoryImpl implements AlbumRepository {
    private final AlbumRemoteDataSourceImpl albumRemoteDataSource = new AlbumRemoteDataSourceImpl();
    @Override
    public void loadAlbums(Callback<AlbumList> callback) {
        albumRemoteDataSource.loadAlbums(callback);
    }
}
