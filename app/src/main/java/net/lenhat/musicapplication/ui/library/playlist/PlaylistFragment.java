package net.lenhat.musicapplication.ui.library.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;
import net.lenhat.musicapplication.databinding.FragmentPlaylistBinding;
import net.lenhat.musicapplication.ui.dialog.PlaylistCreationDialog;
import net.lenhat.musicapplication.ui.library.playlist.detail.PlaylistDetailFragment;
import net.lenhat.musicapplication.ui.library.playlist.detail.PlaylistDetailViewModel;
import net.lenhat.musicapplication.ui.library.playlist.more.MorePlaylistFragment;
import net.lenhat.musicapplication.ui.library.playlist.more.MorePlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlaylistFragment extends Fragment {
    private FragmentPlaylistBinding mBinding;
    private PlaylistAdapter mAdapter;
    private PlaylistViewModel mViewModel;
    private PlaylistDetailViewModel mPlaylistDetailViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPlaylistBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        setupViewModel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void setupView() {
        mAdapter = new PlaylistAdapter(
                this::loadPlaylist,
                playlist -> {
                }
        );
        mBinding.rvPlaylist.setAdapter(mAdapter);
        mBinding.includeBtnAddPlaylist.btnAddPlaylist.setOnClickListener(v -> createPlaylist());
        mBinding.includeBtnAddPlaylist.textAddPlaylist.setOnClickListener(v -> createPlaylist());
        mBinding.btnMorePlaylist.setOnClickListener(v -> navigateToMorePlaylist());
        mBinding.textTitlePlaylist.setOnClickListener(v -> navigateToMorePlaylist());
    }

    private void setupViewModel() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        PlaylistViewModel.Factory factory =
                new PlaylistViewModel.Factory(application.getPlaylistRepository());
        MorePlaylistViewModel morePlaylistViewModel =
                new ViewModelProvider(requireActivity()).get(MorePlaylistViewModel.class);
        mViewModel = new ViewModelProvider(requireActivity(), factory).get(PlaylistViewModel.class);
        mPlaylistDetailViewModel =
                new ViewModelProvider(requireActivity()).get(PlaylistDetailViewModel.class);
        mViewModel.getPlaylists().observe(getViewLifecycleOwner(), playlistsWithSongs -> {
            List<PlaylistWithSongs> subList;
            if (playlistsWithSongs.size() > 10) {
                subList = playlistsWithSongs.subList(0, 10);
            } else {
                subList = new ArrayList<>(playlistsWithSongs);
            }
            mAdapter.updatePlaylists(subList);
            morePlaylistViewModel.setPlaylistLiveData(playlistsWithSongs);
        });
    }

    private void createPlaylist() {
        PlaylistCreationDialog.PlaylistDialogListener listener = playlistName -> {
            mDisposable.add(mViewModel.getPlaylistByName(playlistName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> checkAndCreatePlaylist(result, playlistName),
                            error -> checkAndCreatePlaylist(null, playlistName)));
        };
        PlaylistCreationDialog dialog = new PlaylistCreationDialog(listener);
        final String tag = PlaylistCreationDialog.TAG;
        dialog.show(requireActivity().getSupportFragmentManager(), tag);
    }

    private void checkAndCreatePlaylist(Playlist playlist, String playlistName) {
        if (playlist == null) {
            mDisposable.add(mViewModel.createPlaylist(playlistName)
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        } else {
            String errorMessage = getString(R.string.error_playlist_already_exists);
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPlaylist(Playlist playlist) {
        mDisposable.add(mViewModel.getPlaylistWithSongs(playlist.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    mPlaylistDetailViewModel.setPlaylistWithSongs(result);
                    navigateToPlaylistDetail();
                }, t -> {
                }));
    }

    private void navigateToMorePlaylist() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, MorePlaylistFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToPlaylistDetail() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, PlaylistDetailFragment.class, null)
                .addToBackStack(null)
                .commit();
    }
}