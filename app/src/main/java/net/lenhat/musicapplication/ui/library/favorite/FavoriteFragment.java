package net.lenhat.musicapplication.ui.library.favorite;

import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.FAVORITE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.databinding.FragmentFavoriteBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;
import net.lenhat.musicapplication.ui.library.favorite.more.MoreFavoriteFragment;
import net.lenhat.musicapplication.ui.library.favorite.more.MoreFavoriteViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends AppBaseFragment {
    private FragmentFavoriteBinding mBinding;
    private SongListAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        setupViewModel();
    }

    private void setupView() {
        mAdapter = new SongListAdapter(
                (song, index) -> {
                    String playlistName = FAVORITE.getValue();
                    showAndPlay(song, index, playlistName);
                },
                this::showOptionMenu
        );
        mBinding.includeFavorite.recyclerSongList.setAdapter(mAdapter);
        mBinding.btnMoreFavorite.setOnClickListener(v -> navigateToMoreFavorite());
        mBinding.textTitleFavorite.setOnClickListener(v -> navigateToMoreFavorite());
    }

    private void setupViewModel() {
        FavoriteViewModel favoriteViewModel =
                new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        MoreFavoriteViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(MoreFavoriteViewModel.class);
        favoriteViewModel.getFavoriteSongs().observe(getViewLifecycleOwner(), songs -> {
            viewModel.setFavoriteSongs(songs);
            List<Song> favoriteSongs = new ArrayList<>();
            if (songs != null) {
                if (songs.size() < 10) {
                    favoriteSongs.addAll(songs);
                } else {
                    favoriteSongs.addAll(songs.subList(0, 10));
                }
            }
            mAdapter.updateSongs(favoriteSongs);
        });
    }

    private void navigateToMoreFavorite() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, MoreFavoriteFragment.class, null)
                .addToBackStack(null)
                .commit();
    }
}