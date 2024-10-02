package net.lenhat.musicapplication.data.source.remote;

import androidx.annotation.NonNull;

import net.lenhat.musicapplication.data.model.AlbumList;
import net.lenhat.musicapplication.data.source.AlbumDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumRemoteDataSourceImpl implements AlbumDataSource.Remote {
    @Override
    public void loadAlbums(Callback<AlbumList> callback) {
        AppService appService = RetrofitHelper.getInstance();
        Call<AlbumList> call = appService.getAlbums();
        call.enqueue(new Callback<AlbumList>() {
            @Override
            public void onResponse(@NonNull Call<AlbumList> call,
                                   @NonNull Response<AlbumList> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<AlbumList> call, @NonNull Throwable throwable) {
                callback.onFailure(call, throwable);
            }
        });
    }
}
