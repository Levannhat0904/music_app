package net.lenhat.musicapplication.ui.dialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.utils.OptionMenuUtils;

import java.util.List;

public class OptionMenuViewModel extends ViewModel {
    private final MutableLiveData<List<OptionMenuItem>> mMenuItems = new MutableLiveData<>();
    private final MutableLiveData<Song> mSong = new MutableLiveData<>();

    public OptionMenuViewModel() {
        mMenuItems.setValue(OptionMenuUtils.getSongOptionMenuItems());
    }

    public LiveData<Song> getSong() {
        return mSong;
    }

    public LiveData<List<OptionMenuItem>> getMenuItems() {
        return mMenuItems;
    }

    public void setSong(Song song) {
        mSong.setValue(song);
    }

    public void setMenuItems(List<OptionMenuItem> menuItems) {
        if (menuItems != null) {
            mMenuItems.setValue(menuItems);
        }
    }
}
