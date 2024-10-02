package net.lenhat.musicapplication.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PermissionViewModel extends ViewModel {
    public static boolean isRegistered = false;
    private static final PermissionViewModel sInstance = new PermissionViewModel();
    private final MutableLiveData<Boolean> mPermissionAsked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mPermissionGranted = new MutableLiveData<>();

    public static PermissionViewModel getInstance() {
        return sInstance;
    }

    private PermissionViewModel() {
    }

    public LiveData<Boolean> getPermissionAsked() {
        return mPermissionAsked;
    }

    public LiveData<Boolean> getPermissionGranted() {
        return mPermissionGranted;
    }

    public void setPermissionAsked(boolean permissionAsked) {
        mPermissionAsked.setValue(permissionAsked);
    }

    public void setPermissionGranted(boolean permissionGranted) {
        mPermissionGranted.setValue(permissionGranted);
    }
}
