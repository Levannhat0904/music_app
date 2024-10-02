package net.lenhat.musicapplication.ui.discovery.foryou;

import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.FOR_YOU;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;
import net.lenhat.musicapplication.databinding.FragmentForYouBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.discovery.foryou.more.MoreForYouFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ForYouFragment extends AppBaseFragment {
    private FragmentForYouBinding mBinding;
    private ForYouViewModel mViewModel;
    private SongListAdapter mAdapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentForYouBinding.inflate(inflater, container, false);
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
        mAdapter = new SongListAdapter((song, index) -> {
            String playlistName = FOR_YOU.getValue();
            showAndPlay(song, index, playlistName);
        }, this::showOptionMenu);
        mBinding.includeForYou.recyclerSongList.setAdapter(mAdapter);
        mBinding.btnMoreForYou.setOnClickListener(v -> navigateToMoreForYou());
        mBinding.textTitleForYou.setOnClickListener(v -> navigateToMoreForYou());
    }

    private void navigateToMoreForYou() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, MoreForYouFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void observerData() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        SongRepositoryImpl songRepository = application.getSongRepository();
        ForYouViewModel.Factory factory = new ForYouViewModel.Factory(songRepository);
        mViewModel = new ViewModelProvider(requireActivity(), factory).get(ForYouViewModel.class);
        mViewModel.getSongs().observe(getViewLifecycleOwner(), mAdapter::updateSongs);
        mDisposable.add(mViewModel.loadTop15ForYouSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    mViewModel.setSongs(songs);
                    SharedViewModel.getInstance().setupPlaylist(songs, FOR_YOU.getValue());
                }, t -> mViewModel.setSongs(new ArrayList<>())));
    }
}