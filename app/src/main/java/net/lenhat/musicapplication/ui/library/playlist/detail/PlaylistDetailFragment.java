package net.lenhat.musicapplication.ui.library.playlist.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;
import net.lenhat.musicapplication.databinding.FragmentPlaylistDetailBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;

public class PlaylistDetailFragment extends AppBaseFragment {
    private FragmentPlaylistDetailBinding mBinding;
    private SongListAdapter mAdapter;
    private PlaylistDetailViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPlaylistDetailBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        setupViewModel();
    }

    private void setupView() {
        mBinding.includePlaylistDetail
                .toolbar
                .setNavigationOnClickListener(v -> {
                    requireActivity().getSupportFragmentManager().popBackStack();
                });
        String toolBarTitle = getString(R.string.title_playlist_detail);
        mBinding.includePlaylistDetail.textToolbarTitle.setText(toolBarTitle);
        mAdapter = new SongListAdapter(
                (song, index) -> {
                    PlaylistWithSongs playlistWithSongs =
                            mViewModel.getPlaylistWithSongs().getValue();
                    if (playlistWithSongs != null) {
                        String playlistName = playlistWithSongs.playlist.getName();
                        showAndPlay(song, index, playlistName);
                    }
                },
                this::showOptionMenu
        );
        mBinding.includePlaylistDetail
                .includePlaylistSongList
                .recyclerSongList.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        mViewModel =
                new ViewModelProvider(requireActivity()).get(PlaylistDetailViewModel.class);
        mViewModel.getPlaylistWithSongs().observe(getViewLifecycleOwner(), playlistWithSongs -> {
                    mAdapter.updateSongs(playlistWithSongs.songs);
                    showPlaylistInfo(playlistWithSongs);
                }
        );
    }

    private void showPlaylistInfo(PlaylistWithSongs playlistWithSongs) {
        String artworkUrl = null;
        if (!playlistWithSongs.songs.isEmpty()) {
            artworkUrl = playlistWithSongs.songs.get(0).getImage();
        }
        Glide.with(this)
                .load(artworkUrl)
                .error(R.drawable.ic_album)
                .into(mBinding.includePlaylistDetail.imagePlaylistAvatar);
        String playlistName = playlistWithSongs.playlist.getName();
        mBinding.includePlaylistDetail.textPlaylistTitle.setText(playlistName);
        String numOfSong = getString(R.string.text_number_of_song, playlistWithSongs.songs.size());
        mBinding.includePlaylistDetail.textPlaylistSize.setText(numOfSong);
    }
}