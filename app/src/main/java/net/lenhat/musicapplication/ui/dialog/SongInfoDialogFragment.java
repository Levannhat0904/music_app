package net.lenhat.musicapplication.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.databinding.FragmentSongInfoDialogBinding;

public class SongInfoDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = "SongInfoDialogFragment";
    private FragmentSongInfoDialogBinding mBinding;

    public static SongInfoDialogFragment newInstance() {
        return new SongInfoDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSongInfoDialogBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SongInfoDialogViewModel mViewModel =
                new ViewModelProvider(requireActivity()).get(SongInfoDialogViewModel.class);
        mViewModel.getSong().observe(getViewLifecycleOwner(), this::showSongInfo);
    }

    private void showSongInfo(Song song) {
        Glide.with(this)
                .load(song.getImage())
                .error(R.drawable.ic_music_node)
                .circleCrop()
                .into(mBinding.imageSongInfoAvatar);
        String title = getString(R.string.text_song_detail_title, song.getTitle());
        String artist = getString(R.string.text_song_detail_artist, song.getArtist());
        String album = getString(R.string.text_song_detail_album, song.getAlbum());
        String duration = getString(R.string.text_song_detail_duration, song.getDuration());
        String counter = getString(R.string.text_song_detail_counter, song.getCounter());
        String replay = getString(R.string.text_song_detail_replay, song.getReplay());
        String favorite = getString(R.string.text_song_detail_favorite,
                song.isFavorite() ? getString(R.string.text_yes) : getString(R.string.text_no));
        String genre = getString(R.string.text_song_detail_genre, getString(R.string.text_na));
        String year = getString(R.string.text_song_detail_published_year, getString(R.string.text_na));
        mBinding.textSongDetailTitle.setText(title);
        mBinding.textSongDetailAlbum.setText(album);
        mBinding.textSongDetailArtist.setText(artist);
        mBinding.textSongDetailDuration.setText(duration);
        mBinding.textSongDetailCounter.setText(counter);
        mBinding.textSongDetailReplay.setText(replay);
        mBinding.textSongDetailFavorite.setText(favorite);
        mBinding.textSongDetailGenre.setText(genre);
        mBinding.textSongDetailPublishedYear.setText(year);
    }
}