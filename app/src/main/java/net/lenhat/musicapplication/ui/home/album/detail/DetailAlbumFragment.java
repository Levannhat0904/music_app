package net.lenhat.musicapplication.ui.home.album.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Album;
import net.lenhat.musicapplication.databinding.FragmentDetailAlbumBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;

public class DetailAlbumFragment extends AppBaseFragment {
    private FragmentDetailAlbumBinding mBinding;
    private DetailAlbumViewModel mDetailAlbumViewModel;
    private SongListAdapter mSongListAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDetailAlbumBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupComponents();
    }

    private void setupComponents() {
        mBinding.includeDetailPlaylist.toolbar.setNavigationOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());
        mSongListAdapter = new SongListAdapter((song, index) -> {
            Album album = mDetailAlbumViewModel.getAlbum().getValue();
            String playlistName = album == null ? " " : album.getName();
            SharedViewModel.getInstance().addPlaylist(mDetailAlbumViewModel.getPlaylist());
            showAndPlay(song, index, playlistName);
        }, this::showOptionMenu);
        mBinding.includeDetailPlaylist
                .includePlaylistSongList
                .recyclerSongList
                .setAdapter(mSongListAdapter);
        mDetailAlbumViewModel =
                new ViewModelProvider(requireActivity()).get(DetailAlbumViewModel.class);
        mDetailAlbumViewModel.getAlbum()
                .observe(getViewLifecycleOwner(), this::showAlbumInfo);
        mDetailAlbumViewModel.getAlbumSongs()
                .observe(getViewLifecycleOwner(), mSongListAdapter::updateSongs);
    }

    private void showAlbumInfo(Album album) {
        mBinding.includeDetailPlaylist.textPlaylistTitle.setText(album.getName());
        String numOfSong = getString(R.string.text_number_of_song, album.getSize());
        mBinding.includeDetailPlaylist.textPlaylistSize.setText(numOfSong);
        Glide.with(this)
                .load(album.getArtwork())
                .error(R.drawable.ic_album)
                .into(mBinding.includeDetailPlaylist.imagePlaylistAvatar);
    }
}