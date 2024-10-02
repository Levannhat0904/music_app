package net.lenhat.musicapplication.ui.playing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.media3.common.MediaItem;

import java.util.List;

public class MiniPlayerViewModel extends ViewModel {
    private static final MiniPlayerViewModel sInstance = new MiniPlayerViewModel();
    private final MutableLiveData<Boolean> mIsPlaying = new MutableLiveData<>();
    private final MutableLiveData<List<MediaItem>> mMediaItems = new MutableLiveData<>();

    public static MiniPlayerViewModel getInstance() {
        return sInstance;
    }

    private MiniPlayerViewModel() {
    }

    public LiveData<Boolean> isPlaying() {
        return mIsPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        mIsPlaying.setValue(isPlaying);
    }

    public LiveData<List<MediaItem>> getMediaItems() {
        return mMediaItems;
    }

    public void setMediaItems(List<MediaItem> mediaItems) {
        mMediaItems.setValue(mediaItems);
    }
}
