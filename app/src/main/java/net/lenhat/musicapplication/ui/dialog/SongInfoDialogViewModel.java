package net.lenhat.musicapplication.ui.dialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.lenhat.musicapplication.data.model.Song;

public class SongInfoDialogViewModel extends ViewModel {
    private final MutableLiveData<Song> mSong = new MutableLiveData<>();

    public void setSong(Song song) {
        mSong.setValue(song);
    }

    public LiveData<Song> getSong() {
        return mSong;
    }
}