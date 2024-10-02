package net.lenhat.musicapplication.ui.library;

import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.FAVORITE;
import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.RECENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.databinding.FragmentLibraryBinding;
import net.lenhat.musicapplication.ui.library.favorite.FavoriteViewModel;
import net.lenhat.musicapplication.ui.library.playlist.PlaylistViewModel;
import net.lenhat.musicapplication.ui.library.recent.RecentSongViewModel;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LibraryFragment extends Fragment {
    private static final String SCROLL_POSITION = "net.braniumacademy.musicapplication.ui.home.SCROLL_POSITION";
    private FragmentLibraryBinding mBinding;
    private LibraryViewModel mLibraryViewModel;
    private FavoriteViewModel mFavoriteViewModel;
    private RecentSongViewModel mRecentSongViewModel;
    private PlaylistViewModel mPlaylistViewModel;
    private SharedViewModel mSharedViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLibraryBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupObserver();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int scrollY = mBinding.scrollViewLibrary.getScrollY();
        outState.putInt(SCROLL_POSITION, scrollY);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            int scrollY = savedInstanceState.getInt(SCROLL_POSITION);
            mBinding.scrollViewLibrary.post(() ->
                    mBinding.scrollViewLibrary.scrollTo(0, scrollY));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

    private void setupViewModel() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        LibraryViewModel.Factory factory = new LibraryViewModel.Factory(
                application.getSongRepository(), application.getRecentSongRepository(),
                application.getPlaylistRepository()
        );
        mLibraryViewModel =
                new ViewModelProvider(requireActivity(), factory).get(LibraryViewModel.class);
        mSharedViewModel = SharedViewModel.getInstance();
        mFavoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        PlaylistViewModel.Factory playlistVMFactory =
                new PlaylistViewModel.Factory(application.getPlaylistRepository());
        mPlaylistViewModel = new ViewModelProvider(requireActivity(), playlistVMFactory)
                .get(PlaylistViewModel.class);
        mRecentSongViewModel =
                new ViewModelProvider(requireActivity()).get(RecentSongViewModel.class);
        mLibraryViewModel.getFavoriteSongs().observe(getViewLifecycleOwner(), songs -> {
            mFavoriteViewModel.setFavoriteSongs(songs);
            mSharedViewModel.setupPlaylist(songs, FAVORITE.getValue());
        });

        mLibraryViewModel.getRecentSongs().observe(getViewLifecycleOwner(), songs -> {
            mRecentSongViewModel.setRecentSongs(songs);
            mSharedViewModel.setupPlaylist(songs, RECENT.getValue());
        });
    }

    private void setupObserver() {
        mDisposable.add(mLibraryViewModel.loadRecentSongs()
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> mLibraryViewModel.setRecentSongs(songs),
                        throwable -> mLibraryViewModel.setRecentSongs(null)));
        mDisposable.add(mLibraryViewModel.loadFavoriteSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> mLibraryViewModel.setFavoriteSongs(songs),
                        throwable -> mLibraryViewModel.setFavoriteSongs(null)));
        mDisposable.add(mLibraryViewModel.loadPlaylistsWithSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playlists -> mPlaylistViewModel.setPlaylists(playlists),
                        throwable -> mPlaylistViewModel.setPlaylists(null)));
    }
}