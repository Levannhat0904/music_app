package net.lenhat.musicapplication.ui.discovery.foryou.more;

import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.FOR_YOU;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;
import net.lenhat.musicapplication.databinding.FragmentMoreForYouBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoreForYouFragment extends AppBaseFragment {
    private FragmentMoreForYouBinding mBinding;
    private MoreForYouViewModel mViewModel;
    private SongListAdapter mAdapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreForYouBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        observerData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void setupView() {
        mBinding.toolbarMoreForYou.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
        mAdapter = new SongListAdapter((song, index) -> {
            String playlistName = FOR_YOU.getValue();
            showAndPlay(song, index, playlistName);
        }, this::showOptionMenu);
        mBinding.includeMoreForYouSongList.recyclerSongList.setAdapter(mAdapter);
    }

    private void observerData() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        SongRepositoryImpl songRepository = application.getSongRepository();
        MoreForYouViewModel.Factory factory = new MoreForYouViewModel.Factory(songRepository);
        mViewModel = new ViewModelProvider(requireActivity(), factory).get(MoreForYouViewModel.class);
        mViewModel.getSongs().observe(getViewLifecycleOwner(), mAdapter::updateSongs);
        mDisposable.add(mViewModel.loadTop40ForYouSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    mViewModel.setSongs(songs);
                    SharedViewModel.getInstance().setupPlaylist(songs, FOR_YOU.getValue());
                }, t -> mViewModel.setSongs(new ArrayList<>())));
    }
}