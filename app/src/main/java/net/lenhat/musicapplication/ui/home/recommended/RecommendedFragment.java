package net.lenhat.musicapplication.ui.home.recommended;

import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.DEFAULT;
import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.RECOMMENDED;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.databinding.FragmentRecommendedBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.home.album.detail.DetailAlbumViewModel;
import net.lenhat.musicapplication.ui.home.recommended.more.MoreRecommendedFragment;
import net.lenhat.musicapplication.ui.home.recommended.more.MoreRecommendedViewModel;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecommendedFragment extends AppBaseFragment {
    private FragmentRecommendedBinding mBinding;
    private SongListAdapter mSongListAdapter;
    private DetailAlbumViewModel mAlbumViewModel;
    RecommendedSongViewModel mRecommendedViewModel;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRecommendedBinding.inflate(inflater, container, false);
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
        mCompositeDisposable.dispose();
    }

    private void setupView() {
        mBinding.progressRecommendedSong.setVisibility(View.VISIBLE);
        mSongListAdapter = new SongListAdapter(
                (song, index) -> showAndPlay(song, index, RECOMMENDED.getValue()),
                this::showOptionMenu);
        mBinding.includeRecommendedSong.recyclerSongList.setAdapter(mSongListAdapter);
        mAlbumViewModel = new ViewModelProvider(requireActivity()).get(DetailAlbumViewModel.class);
        mBinding.btnMoreRecommendedSongs.setOnClickListener(v -> navigateToMoreRecommended());
        mBinding.textTitleRecommendedSongs.setOnClickListener(v -> navigateToMoreRecommended());
    }

    private void setupViewModel() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        RecommendedSongViewModel.Factory factory =
                new RecommendedSongViewModel.Factory(application.getSongRepository());
        mRecommendedViewModel = new ViewModelProvider(requireActivity(), factory)
                .get(RecommendedSongViewModel.class);
        MoreRecommendedViewModel moreRecommendedSongViewModel =
                new ViewModelProvider(requireActivity()).get(MoreRecommendedViewModel.class);
        mRecommendedViewModel.getSongs().observe(getViewLifecycleOwner(), songs -> {
            saveSongIntoDB(songs);
            mAlbumViewModel.setSongs(songs);
            if(songs.size() > 15) {
                mSongListAdapter.updateSongs(songs.subList(0, 15));
            } else {
                mSongListAdapter.updateSongs(songs);
            }
            moreRecommendedSongViewModel.setSongs(songs);
            SharedViewModel.getInstance().setupPlaylist(songs, DEFAULT.getValue());
            SharedViewModel.getInstance().setupPlaylist(songs, RECOMMENDED.getValue());
            mBinding.progressRecommendedSong.setVisibility(View.GONE);
            SharedViewModel.getInstance().setSongLoaded(true);
        });
    }

    private void saveSongIntoDB(List<Song> songs) {
        mCompositeDisposable.add(mRecommendedViewModel.saveSongIntoDB(songs)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    private void navigateToMoreRecommended() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, MoreRecommendedFragment.class, null)
                .addToBackStack(null)
                .commit();
    }
}