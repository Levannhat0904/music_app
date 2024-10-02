package net.lenhat.musicapplication.ui.discovery.mostheard;

import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.MOST_HEARD;

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
import net.lenhat.musicapplication.databinding.FragmentMostHeardBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.discovery.mostheard.more.MoreMostHeardFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MostHeardFragment extends AppBaseFragment {
    private FragmentMostHeardBinding mBinding;
    private MostHeardViewModel mViewModel;
    private SongListAdapter mAdapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMostHeardBinding.inflate(inflater, container, false);
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
            String playlistName = MOST_HEARD.getValue();
            showAndPlay(song, index, playlistName);
        }, this::showOptionMenu);
        mBinding.includeMostHeard.recyclerSongList.setAdapter(mAdapter);
        mBinding.btnMoreMostHeard.setOnClickListener(v -> navigateToMoreMostHeard());
        mBinding.textTitleMostHeard.setOnClickListener(v -> navigateToMoreMostHeard());
    }

    private void navigateToMoreMostHeard() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, MoreMostHeardFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void observerData() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        SongRepositoryImpl songRepository = application.getSongRepository();
        MostHeardViewModel.Factory factory = new MostHeardViewModel.Factory(songRepository);
        mViewModel = new ViewModelProvider(requireActivity(), factory).get(MostHeardViewModel.class);
        mViewModel.getSongs().observe(getViewLifecycleOwner(), mAdapter::updateSongs);
        mDisposable.add(mViewModel.loadTop15MostHeardSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    mViewModel.setSongs(songs);
                    SharedViewModel.getInstance().setupPlaylist(songs, MOST_HEARD.getValue());
                }, t -> mViewModel.setSongs(new ArrayList<>())));
    }
}