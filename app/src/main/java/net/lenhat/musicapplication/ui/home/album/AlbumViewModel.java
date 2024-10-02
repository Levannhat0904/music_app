package net.lenhat.musicapplication.ui.home.album;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.collect.Lists;

import net.lenhat.musicapplication.data.model.Album;
import net.lenhat.musicapplication.data.model.AlbumList;
import net.lenhat.musicapplication.data.repository.album.AlbumRepository;
import net.lenhat.musicapplication.data.repository.album.AlbumRepositoryImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumViewModel extends ViewModel {
    private final AlbumRepository mAlbumRepository = new AlbumRepositoryImpl();
    private final MutableLiveData<List<Album>> mAlbumList = new MutableLiveData<>();

    public AlbumViewModel() {
        loadAlbumList();
    }

    public void setAlbumList(List<Album> albumList) {
        mAlbumList.postValue(albumList);
    }

    public LiveData<List<Album>> getAlbumList() {
        return mAlbumList;
    }

    private void loadAlbumList() {
        mAlbumRepository.loadAlbums(new Callback<AlbumList>() {
            @Override
            public void onResponse(@NonNull Call<AlbumList> call, @NonNull Response<AlbumList> response) {
                AlbumList albumList = response.body();
                if (albumList != null) {
                    List<Album> albums = albumList.getAlbums();
                    albums.sort(Comparator.comparing(Album::getSize));
                    setAlbumList(Lists.reverse(albums));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AlbumList> call, @NonNull Throwable throwable) {
                mAlbumList.postValue(new ArrayList<>());
            }
        });
    }
}
