package net.lenhat.musicapplication.ui.library.recent;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.databinding.FragmentRecentSongBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.library.recent.more.MoreRecentFragment;
import net.lenhat.musicapplication.ui.library.recent.more.MoreRecentViewModel;
import net.lenhat.musicapplication.utils.AppUtils;

import java.util.List;

public class RecentSongFragment extends AppBaseFragment {
    private FragmentRecentSongBinding mBinding;
    private RecentSongAdapter mRecentSongAdapter;
    private MoreRecentViewModel mMoreRecentViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentRecentSongBinding.inflate(inflater, container, false);
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

    private void setupView() {
        mBinding.progressBarRecentSong.setVisibility(View.VISIBLE);
        mRecentSongAdapter = new RecentSongAdapter(
                (song, index) -> {
                    String playlistName = AppUtils.DefaultPlaylistName.RECENT.getValue();
                    showAndPlay(song, index, playlistName);
                },
                this::showOptionMenu
        );
        mBinding.rvRecentSong.setAdapter(mRecentSongAdapter);
        MyLayoutManager layoutManager = new MyLayoutManager(
                requireContext(), 3, RecyclerView.HORIZONTAL, false
        );
        mBinding.rvRecentSong.setLayoutManager(layoutManager);
        mBinding.textTitleRecent.setOnClickListener(v -> navigateToMoreRecentScreen());
        mBinding.btnMoreRecent.setOnClickListener(v -> navigateToMoreRecentScreen());
    }

    private void setupViewModel() {
        RecentSongViewModel recentSongViewModel =
                new ViewModelProvider(requireActivity()).get(RecentSongViewModel.class);
        mMoreRecentViewModel =
                new ViewModelProvider(requireActivity()).get(MoreRecentViewModel.class);
        recentSongViewModel.getRecentSongs().observe(getViewLifecycleOwner(), recentSongs -> {
            mMoreRecentViewModel.setRecentSongs(recentSongs);
            List<Song> subList = recentSongs;
            if (recentSongs.size() >= 21) {
                subList = recentSongs.subList(0, 21);
            }
            mRecentSongAdapter.updateSongs(subList);
            mBinding.progressBarRecentSong.setVisibility(View.GONE);
        });
    }

    static class MyLayoutManager extends GridLayoutManager {
        public MyLayoutManager(Context context, int spanCount,
                               int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        @Override
        public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
            int dx = (int) (48 * AppUtils.X_DPI / 160);
            lp.width = getWidth() - dx;
            return true;
        }
    }

    private void navigateToMoreRecentScreen() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, MoreRecentFragment.class, null)
                .addToBackStack(null)
                .commit();
    }
}