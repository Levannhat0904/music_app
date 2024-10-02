package net.lenhat.musicapplication.data.source;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.model.SongList;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Callback;

public interface SongDataSource {
    interface Remote {
        void loadSongs(Callback<SongList> callback);
    }

    interface Local {
        Single<List<Song>> getSongs();

        Flowable<List<Song>> getFavoriteSongs();

        Flowable<List<Song>> getTopNMostHeardSongs(int limit);

        Flowable<List<Song>> getTopNForYouSongs(int limit);

        Completable saveSongs(Song... songs);

        Completable updateSong(Song song); // mới bổ sung
    }
}
