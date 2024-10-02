package net.lenhat.musicapplication.data.source.remote;

import androidx.annotation.NonNull;

import net.lenhat.musicapplication.data.model.artist.ArtistList;
import net.lenhat.musicapplication.data.source.ArtistDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteArtistDataSource implements ArtistDataSource.Remote {
    @Override
    public void loadArtists(Callback<ArtistList> callback) {
        AppService appService = RetrofitHelper.getInstance();
        Call<ArtistList> call = appService.getArtists();
        call.enqueue(new Callback<ArtistList>() {
            @Override
            public void onResponse(@NonNull Call<ArtistList> call,
                                   @NonNull Response<ArtistList> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<ArtistList> call, @NonNull Throwable throwable) {
                callback.onFailure(call, throwable);
            }
        });
    }
}
