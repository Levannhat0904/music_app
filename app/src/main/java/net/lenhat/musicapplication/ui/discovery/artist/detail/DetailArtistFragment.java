package net.lenhat.musicapplication.ui.discovery.artist.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.model.artist.Artist;
import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.data.repository.artist.ArtistRepository;
import net.lenhat.musicapplication.databinding.FragmentDetailArtistBinding;
import net.lenhat.musicapplication.ui.AppBaseFragment;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailArtistFragment extends AppBaseFragment {
    public static final String EXTRA_ARTIST_ID = "extra_artist_id";
    private DetailArtistViewModel mViewModel;
    private FragmentDetailArtistBinding mBinding;
    private SongListAdapter mSongAdapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDetailArtistBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        observeData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void setupView() {
        mBinding.toolbarDetailArtist.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
        mSongAdapter = new SongListAdapter(this::playSong, this::showOptionMenu);
        mBinding.includeArtistSong.recyclerSongList.setAdapter(mSongAdapter);
    }

    private void playSong(Song song, int index) {
        Playlist playlist = mViewModel.createPlaylist();
        String playlistName = playlist.getName();
        showAndPlay(song, index, playlistName);
    }

    private void observeData() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        ArtistRepository repository = application.getArtistRepository();
        DetailArtistViewModel.Factory factory = new DetailArtistViewModel.Factory(repository);
        mViewModel = new ViewModelProvider(requireActivity(), factory).get(DetailArtistViewModel.class);
        if (getArguments() != null) {
            int artistId = getArguments().getInt(EXTRA_ARTIST_ID, -1);
            mDisposable.add(mViewModel.getArtistWithSongs(artistId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(artistWithSong -> mViewModel.setArtistWithSongs(artistWithSong),
                            throwable -> mViewModel.setArtistWithSongs(null)));
        }
        mViewModel.getArtistWithSongs()
                .observe(getViewLifecycleOwner(), artistWithSong -> {
                    if (artistWithSong != null) {
                        mSongAdapter.updateSongs(artistWithSong.songs);
                        showArtist(artistWithSong.artist);
                    }
                });
    }

    private void showArtist(Artist artist) {
        Glide.with(requireContext())
                .load(artist.getAvatar())
                .error(R.drawable.ic_singer)
                .circleCrop()
                .into(mBinding.imageDetailArtistAvatar);
        mBinding.textDetailArtistName
                .setText(getString(R.string.detail_artist_name, artist.getName()));
        mBinding.textDetailArtistInterest
                .setText(getString(R.string.detail_number_of_interest, artist.getInterested()));
        String yourInterest = "No";
        mBinding.textDetailArtistYourInterest
                .setText(getString(R.string.detail_your_interested_artist, yourInterest));
    }
}