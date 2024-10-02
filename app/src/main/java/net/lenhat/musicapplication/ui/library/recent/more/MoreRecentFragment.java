package net.lenhat.musicapplication.ui.library.recent.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.databinding.FragmentMoreRecentBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;
import net.lenhat.musicapplication.utils.AppUtils;

public class MoreRecentFragment extends AppBaseFragment {
    private FragmentMoreRecentBinding mBinding;
    private SongListAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreRecentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        setupViewModel();
    }

    private void setupView() {
        mBinding.toolbarMoreRecent.setNavigationOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());
        mAdapter = new SongListAdapter(
                (song, index) -> {
                    String playlistName = AppUtils.DefaultPlaylistName.RECENT.getValue();
                    showAndPlay(song, index, playlistName);
                },
                this::showOptionMenu
        );
        mBinding.includeMoreRecentSongList.recyclerSongList.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        MoreRecentViewModel moreRecentViewModel = new ViewModelProvider(requireActivity())
                .get(MoreRecentViewModel.class);
        moreRecentViewModel.getRecentSongs().observe(getViewLifecycleOwner(), songs ->
                mAdapter.updateSongs(songs));
    }
}