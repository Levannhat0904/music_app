package net.lenhat.musicapplication.ui.library.favorite.more;

import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.FAVORITE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.databinding.FragmentMoreFavoriteBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;

public class MoreFavoriteFragment extends AppBaseFragment {
    private FragmentMoreFavoriteBinding mBinding;
    private SongListAdapter mSongListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreFavoriteBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        setupViewModel();
    }

    private void setupView() {
        mSongListAdapter = new SongListAdapter(
                (song, index) -> {
                    String playlistName = FAVORITE.getValue();
                    showAndPlay(song, index, playlistName);
                },
                this::showOptionMenu
        );
        mBinding.includeMoreFavoriteSongList.recyclerSongList.setAdapter(mSongListAdapter);
        mBinding.toolbarMoreFavorite.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void setupViewModel() {
        MoreFavoriteViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(MoreFavoriteViewModel.class);
        viewModel.getFavoriteSongs().observe(getViewLifecycleOwner(), songs ->
                mSongListAdapter.updateSongs(songs));
    }
}