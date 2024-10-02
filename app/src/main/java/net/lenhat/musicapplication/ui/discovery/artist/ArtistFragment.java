package net.lenhat.musicapplication.ui.discovery.artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.model.artist.Artist;
import net.lenhat.musicapplication.data.repository.artist.ArtistRepository;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;
import net.lenhat.musicapplication.databinding.FragmentArtistBinding;
import net.lenhat.musicapplication.ui.discovery.artist.detail.DetailArtistFragment;
import net.lenhat.musicapplication.ui.discovery.artist.more.MoreArtistFragment;
import net.lenhat.musicapplication.ui.discovery.artist.more.MoreArtistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArtistFragment extends Fragment {
    private FragmentArtistBinding mBinding;
    private ArtistAdapter mArtistAdapter;
    private ArtistViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentArtistBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        observeData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposable.dispose();
    }

    private void setupView() {
        mArtistAdapter = new ArtistAdapter(this::navigateToDetailArtist);
        mBinding.includeArtist.recyclerArtist.setAdapter(mArtistAdapter);
        mBinding.textTitleArtist.setOnClickListener(v -> navigateToMoreArtist());
        mBinding.btnMoreArtist.setOnClickListener(v -> navigateToMoreArtist());
    }

    private void observeData() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        ArtistRepository repository = application.getArtistRepository();
        SongRepositoryImpl songRepository = application.getSongRepository();
        ArtistViewModel.Factory factory = new ArtistViewModel.Factory(repository, songRepository);
        mViewModel = new ViewModelProvider(requireActivity(), factory).get(ArtistViewModel.class);
        mViewModel.getArtist().observe(getViewLifecycleOwner(), artist ->
                mDisposable.add(mViewModel.saveArtistToLocalDB(artist)
                        .subscribeOn(Schedulers.io())
                        .subscribe()));
        mDisposable.add(mViewModel.loadLocalArtists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artists -> mViewModel.setLocalArtists(artists),
                        throwable -> mViewModel.setLocalArtists(new ArrayList<>())));
        mViewModel.getLocalArtists()
                .observe(getViewLifecycleOwner(), mArtistAdapter::updateArtists);
        mDisposable.add(mViewModel.loadAllLocalArtists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setupMoreArtist,
                        throwable -> setupMoreArtist(new ArrayList<>())));
    }

    private void setupMoreArtist(List<Artist> artists) {
        MoreArtistViewModel moreArtistViewModel =
                new ViewModelProvider(requireActivity()).get(MoreArtistViewModel.class);
        moreArtistViewModel.setArtist(artists);
        mDisposable.add(mViewModel.loadAllSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> saveArtistSongCrossRef(artists, songs), throwable -> {
                }));
    }

    private void saveArtistSongCrossRef(List<Artist> artists, List<Song> songs) {
        mDisposable.add(mViewModel.saveArtistSongCrossRef(artists, songs)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    private void navigateToMoreArtist() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, MoreArtistFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToDetailArtist(Artist artist) {
        Bundle bundle = new Bundle();
        bundle.putInt(DetailArtistFragment.EXTRA_ARTIST_ID, artist.getId());
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, DetailArtistFragment.class, bundle)
                .addToBackStack(null)
                .commit();
    }
}