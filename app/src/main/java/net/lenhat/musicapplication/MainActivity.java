package net.lenhat.musicapplication;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;

import net.lenhat.musicapplication.data.model.PlayingSong;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.repository.recent.RecentSongRepository;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;
import net.lenhat.musicapplication.databinding.ActivityMainBinding;
import net.lenhat.musicapplication.ui.library.playlist.PlaylistViewModel;
import net.lenhat.musicapplication.ui.viewmodel.PermissionViewModel;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;
import net.lenhat.musicapplication.utils.AppUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String PREF_SONG_ID = "net.braniumacademy.musicapplication.PREF_SONG_ID";
    public static final String PREF_PLAYLIST_NAME = "net.braniumacademy.musicapplication.PREF_PLAYLIST_NAME";
    private ActivityMainBinding mBinding;
    private SharedViewModel mSharedViewModel;
    private SharedPreferences mSharedPreferences;
    private PlaylistViewModel mPlaylistViewModel;
    private boolean isFirstLoad = true;
    private final ActivityResultLauncher<String> mResultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    PermissionViewModel.getInstance().setPermissionGranted(isGranted);
                } else {
                    Snackbar snackBar = Snackbar.make(mBinding.getRoot(),
                            R.string.permission_denied_message, Snackbar.LENGTH_LONG);
                    snackBar.setAnchorView(R.id.nav_view);
                    snackBar.show();
                }
            }
    );
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setupToolbar();
        setupViewModel();
        setupSharedPreferences();
        setupComponents();
        observeData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSharedViewModel.getPlayingSong().observe(this, playingSong -> {
            if (playingSong != null) {
                if (playingSong.getSong() != null) {
                    mBinding.fcvMiniPlayer.setVisibility(View.VISIBLE);
                } else {
                    mBinding.fcvMiniPlayer.setVisibility(View.GONE);
                }
            } else {
                mBinding.fcvMiniPlayer.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCurrentSong();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void setupToolbar() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(mBinding.navView, navController);
        }
    }

    private void setupViewModel() {
        MusicApplication application = ((MusicApplication) getApplication());
        RecentSongRepository recentSongRepository = application.getRecentSongRepository();
        SongRepositoryImpl songRepository = application.getSongRepository();

        SharedViewModel.Factory factory =
                new SharedViewModel.Factory(songRepository, recentSongRepository);
        mSharedViewModel =
                new ViewModelProvider(this, factory).get(SharedViewModel.class);

        PlaylistViewModel.Factory playlistVMFactory =
                new PlaylistViewModel.Factory(application.getPlaylistRepository());
        mPlaylistViewModel =
                new ViewModelProvider(this, playlistVMFactory).get(PlaylistViewModel.class);

        mSharedViewModel.isSongLoaded().observe(this, isLoaded -> {
            if (isLoaded && isFirstLoad) {
                readPreviousSessionPlayingSong();
                isFirstLoad = false;
            }
        });
        PermissionViewModel.getInstance().getPermissionAsked().observe(this, isAsked -> {
            if (isAsked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkPermission();
                }
            }
        });
    }

    private void setupSharedPreferences() {
        mSharedPreferences = getApplicationContext()
                .getSharedPreferences("MUSIC_APP_PREFERENCES", MODE_PRIVATE);
    }

    private void setupComponents() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        AppUtils.X_DPI = displayMetrics.xdpi;
    }

    private void observeData() {
        mDisposable.add(mPlaylistViewModel.getAllPlaylistWithSongs()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(playlistWithSongs -> {
                    mPlaylistViewModel.setPlaylists(playlistWithSongs);
                    mSharedViewModel.setPlaylistSongs(playlistWithSongs);
                }));
    }

    private void saveCurrentSong() {
        PlayingSong playingSong = mSharedViewModel.getPlayingSong().getValue();
        if (playingSong != null) {
            Song song = playingSong.getSong();
            if (song != null) {
                mSharedPreferences.edit()
                        .putString(PREF_SONG_ID, song.getId())
                        .putString(PREF_PLAYLIST_NAME, playingSong.getPlaylist().getName())
                        .apply();
            }
        }
    }

    private void readPreviousSessionPlayingSong() {
        String songId = mSharedPreferences.getString(PREF_SONG_ID, null);
        String playlistName = mSharedPreferences.getString(PREF_PLAYLIST_NAME, null);
        if (songId != null && playlistName != null) {
            mSharedViewModel.setupPreviousSessionPlayingSong(songId, playlistName);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkPermission() {
        final String permission = android.Manifest.permission.POST_NOTIFICATIONS;
        boolean isGranted = ActivityCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED;
        if (isGranted) {
            PermissionViewModel.getInstance().setPermissionGranted(true);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Snackbar snackBar = Snackbar.make(mBinding.getRoot(),
                    R.string.permission_description, Snackbar.LENGTH_LONG);
            snackBar.setAnchorView(R.id.nav_view);
            snackBar.setAction(R.string.action_agree, v -> mResultLauncher.launch(permission));
            snackBar.show();
        } else {
            mResultLauncher.launch(permission);
        }
    }
}