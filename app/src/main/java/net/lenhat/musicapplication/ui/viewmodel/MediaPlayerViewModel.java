package net.lenhat.musicapplication.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.media3.session.MediaController;

public class MediaPlayerViewModel extends ViewModel {
    private static final MediaPlayerViewModel sInstance = new MediaPlayerViewModel();
    private final MutableLiveData<MediaController> mediaPlayerLiveData = new MutableLiveData<>();

    private MediaPlayerViewModel() {
    }

    public void setMediaPlayer(MediaController mediaController) {
        mediaPlayerLiveData.setValue(mediaController);
    }

    public LiveData<MediaController> getMediaPlayerLiveData() {
        return mediaPlayerLiveData;
    }

    public static MediaPlayerViewModel getInstance() {
        return sInstance;
    }
}
