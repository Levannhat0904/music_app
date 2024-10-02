package net.lenhat.musicapplication.ui.discovery.artist;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.model.artist.Artist;
import net.lenhat.musicapplication.data.model.artist.ArtistList;
import net.lenhat.musicapplication.data.model.artist.ArtistSongCrossRef;
import net.lenhat.musicapplication.data.repository.artist.ArtistRepository;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistViewModel extends ViewModel {
    private final ArtistRepository mRepository;
    private final SongRepositoryImpl mSongRepository;
    private final MutableLiveData<List<Artist>> mArtists = new MutableLiveData<>();
    private final MutableLiveData<List<Artist>> mLocalArtists = new MutableLiveData<>();

    public ArtistViewModel(ArtistRepository repository, SongRepositoryImpl songRepository) {
        mRepository = repository;
        mSongRepository = songRepository;
        loadArtists();
    }

    public LiveData<List<Artist>> getArtist() {
        return mArtists;
    }

    public LiveData<List<Artist>> getLocalArtists() {
        return mLocalArtists;
    }

    public void setArtist(List<Artist> artist) {
        mArtists.postValue(artist);
    }

    public void setLocalArtists(List<Artist> artists) {
        mLocalArtists.postValue(artists);
    }

    public void loadArtists() {
        mRepository.loadArtists(new Callback<ArtistList>() {
            @Override
            public void onResponse(@NonNull Call<ArtistList> call, @NonNull Response<ArtistList> response) {
                if (response.isSuccessful()) {
                    ArtistList artistList = response.body();
                    if (artistList != null) {
                        setArtist(artistList.artists);
                    }
                } else {
                    setArtist(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArtistList> call, @NonNull Throwable throwable) {
                mArtists.postValue(new ArrayList<>());
            }
        });
    }

    public Completable saveArtistToLocalDB(List<Artist> artists) {
        return mRepository.insertArtist(artists);
    }

    public Single<List<Song>> loadAllSongs() {
        return mSongRepository.getSongs();
    }

    public Completable saveArtistSongCrossRef(List<Artist> artists, List<Song> songs) {
        if (artists != null) {
            List<ArtistSongCrossRef> artistSongCrossRefList =
                    getArtistSongCrossRef(artists, songs);
            return mRepository.insertArtistSongCrossRef(artistSongCrossRefList);
        }
        return Completable.complete();
    }

    public Flowable<List<Artist>> loadLocalArtists() {
        return mRepository.getTopNArtists(15);
    }

    public Flowable<List<Artist>> loadAllLocalArtists() {
        return mRepository.getAllArtists();
    }

    private List<ArtistSongCrossRef> getArtistSongCrossRef(List<Artist> artists, List<Song> songs) {
        List<ArtistSongCrossRef> result = new ArrayList<>();
        for (Artist artist : artists) {
            for (Song song : songs) {
                String key = ".*" + artist.getName().toLowerCase() + ".*";
                if (song.getArtist().toLowerCase().matches(key)) {
                    result.add(new ArtistSongCrossRef(artist.getId(), song.getId()));
                }
            }
        }
        return result;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final ArtistRepository mRepository;
        private final SongRepositoryImpl mSongRepository;

        public Factory(ArtistRepository repository, SongRepositoryImpl songRepository) {
            mRepository = repository;
            mSongRepository = songRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ArtistViewModel.class)) {
                return (T) new ArtistViewModel(mRepository, mSongRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}