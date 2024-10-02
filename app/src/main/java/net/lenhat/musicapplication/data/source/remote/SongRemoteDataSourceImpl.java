package net.lenhat.musicapplication.data.source.remote;

import androidx.annotation.NonNull;

import net.lenhat.musicapplication.data.model.SongList;
import net.lenhat.musicapplication.data.source.SongDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRemoteDataSourceImpl implements SongDataSource.Remote {
    @Override
    public void loadSongs(Callback<SongList> callback) {
        AppService appService = RetrofitHelper.getInstance();
        Call<SongList> call = appService.getSongs();
        call.enqueue(new Callback<SongList>() {
            @Override
            public void onResponse(@NonNull Call<SongList> call,
                                   @NonNull Response<SongList> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<SongList> call, @NonNull Throwable throwable) {
                callback.onFailure(call, throwable);
            }
        });
    }
}
