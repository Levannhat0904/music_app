package net.lenhat.musicapplication.ui.playing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class NowPlayingViewModel extends ViewModel {
    private final MutableLiveData<Boolean> mIsPlaying = new MutableLiveData<>();

    public void setIsPlaying(boolean isPlaying) {
        mIsPlaying.setValue(isPlaying);
    }

    public LiveData<Boolean> isPlaying() {
        return mIsPlaying;
    }

    public String getTimeLabel(long duration) {
        long minutes = duration / 60000;
        long seconds = (duration % 60000) / 1000;
        if (duration < 0 || duration > Integer.MAX_VALUE) {
            return "00:00";
        }
        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
    }
}
