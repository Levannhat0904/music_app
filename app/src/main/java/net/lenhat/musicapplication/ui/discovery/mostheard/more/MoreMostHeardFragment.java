package net.lenhat.musicapplication.ui.discovery.mostheard.more;

import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.MOST_HEARD;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;
import net.lenhat.musicapplication.databinding.FragmentMoreMostHeardBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoreMostHeardFragment extends AppBaseFragment {
    private FragmentMoreMostHeardBinding mBinding;
    private MoreMostHeardViewModel mViewModel;
    private SongListAdapter mAdapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreMostHeardBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        observerData();
    }

    private void setupView() {
        mBinding.toolbarMoreMostHeard
                .setNavigationOnClickListener(v ->
                        requireActivity().getSupportFragmentManager().popBackStack());
        mAdapter = new SongListAdapter((song, index) -> {
            String playlistName = MOST_HEARD.getValue();
            showAndPlay(song, index, playlistName);
        }, this::showOptionMenu);
        mBinding.includeMoreMostHeardSongList.recyclerSongList.setAdapter(mAdapter);
    }

    private void observerData() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        SongRepositoryImpl songRepository = application.getSongRepository();
        MoreMostHeardViewModel.Factory factory = new MoreMostHeardViewModel.Factory(songRepository);
        mViewModel = new ViewModelProvider(requireActivity(), factory)
                .get(MoreMostHeardViewModel.class);
        mDisposable.add(mViewModel.loadTop40MostHeardSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    mAdapter.updateSongs(songs);
                    mViewModel.setSongs(songs);
                    SharedViewModel.getInstance().setupPlaylist(songs, MOST_HEARD.getValue());
                }, t -> {
                    mAdapter.updateSongs(new ArrayList<>());
                    mViewModel.setSongs(new ArrayList<>());
                }));
    }
}