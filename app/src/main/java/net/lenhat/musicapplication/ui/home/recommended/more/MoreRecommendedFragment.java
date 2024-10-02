package net.lenhat.musicapplication.ui.home.recommended.more;

import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.RECOMMENDED;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.databinding.FragmentMoreRecommendedBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;

public class MoreRecommendedFragment extends AppBaseFragment {
    private FragmentMoreRecommendedBinding mBinding;
    private SongListAdapter mSongListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreRecommendedBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        setupViewModel();
    }

    private void setupView() {
        mBinding.includeToolbar
                .toolbar.setNavigationOnClickListener(
                        v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        mBinding.includeToolbar.textToolbarTitle.setText(R.string.title_recommended_songs);
        mSongListAdapter = new SongListAdapter(
                (song, index) -> showAndPlay(song, index, RECOMMENDED.getValue()),
                this::showOptionMenu);
        mBinding.includeSongList.recyclerSongList.setAdapter(mSongListAdapter);
    }

    private void setupViewModel() {
        MoreRecommendedViewModel recommendedViewModel =
                new ViewModelProvider(requireActivity()).get(MoreRecommendedViewModel.class);
        recommendedViewModel.getSongs()
                .observe(getViewLifecycleOwner(), mSongListAdapter::updateSongs);
    }
}