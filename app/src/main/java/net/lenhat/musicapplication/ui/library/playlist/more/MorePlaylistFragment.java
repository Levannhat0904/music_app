package net.lenhat.musicapplication.ui.library.playlist.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.databinding.FragmentMorePlaylistBinding;
import net.lenhat.musicapplication.ui.library.playlist.PlaylistAdapter;
import net.lenhat.musicapplication.ui.library.playlist.PlaylistViewModel;
import net.lenhat.musicapplication.ui.library.playlist.detail.PlaylistDetailFragment;
import net.lenhat.musicapplication.ui.library.playlist.detail.PlaylistDetailViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MorePlaylistFragment extends Fragment {
    private FragmentMorePlaylistBinding mBinding;
    private PlaylistAdapter mAdapter;
    private PlaylistDetailViewModel mPlaylistDetailViewModel;
    private PlaylistViewModel mPlaylistViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMorePlaylistBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        setupViewModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

    private void setupView() {
        mBinding.toolbarMorePlaylist.setNavigationOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());
        mAdapter = new PlaylistAdapter(this::loadPlaylist, playlist -> {
            // todo
        });
        mBinding.rvMorePlaylist.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        PlaylistViewModel.Factory factory =
                new PlaylistViewModel.Factory(application.getPlaylistRepository());
        mPlaylistViewModel = new ViewModelProvider(requireActivity(), factory)
                .get(PlaylistViewModel.class);
        mPlaylistDetailViewModel =
                new ViewModelProvider(requireActivity()).get(PlaylistDetailViewModel.class);
        MorePlaylistViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(MorePlaylistViewModel.class);
        viewModel.getPlaylistLiveData().observe(getViewLifecycleOwner(), playlists ->
                mAdapter.updatePlaylists(playlists));
    }

    private void loadPlaylist(Playlist playlist) {
        mDisposable.add(mPlaylistViewModel.getPlaylistWithSongs(playlist.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    mPlaylistDetailViewModel.setPlaylistWithSongs(result);
                    navigateToPlaylistDetail();
                }, t -> {
                }));
    }

    private void navigateToPlaylistDetail() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, PlaylistDetailFragment.class, null)
                .addToBackStack(null)
                .commit();
    }
}