package net.lenhat.musicapplication.ui.home.recommended;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.databinding.ItemSongBinding;
import net.lenhat.musicapplication.ui.viewmodel.PermissionViewModel;

import java.util.ArrayList;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private final List<Song> mSongs = new ArrayList<>();
    private final OnSongItemClickListener mSongItemClickListener;
    private final OnSongMenuItemClickListener mSongMenuItemClickListener;

    public SongListAdapter(OnSongItemClickListener songItemClickListener,
                           OnSongMenuItemClickListener songMenuItemClickListener) {
        mSongItemClickListener = songItemClickListener;
        mSongMenuItemClickListener = songMenuItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSongBinding binding = ItemSongBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, mSongItemClickListener, mSongMenuItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mSongs.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public void updateSongs(List<Song> songs) {
        if (songs != null) {
            int oldSize = mSongs.size();
            mSongs.clear();
            mSongs.addAll(songs);
            if (oldSize > mSongs.size()) {
                notifyItemRangeRemoved(0, oldSize);
            }
            notifyItemRangeChanged(0, mSongs.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSongBinding mBinding;
        private final OnSongItemClickListener mSongItemClickListener;
        private final OnSongMenuItemClickListener mSongMenuItemClickListener;

        public ViewHolder(@NonNull ItemSongBinding binding,
                          OnSongItemClickListener songItemClickListener,
                          OnSongMenuItemClickListener songMenuItemClickListener) {
            super(binding.getRoot());
            mBinding = binding;
            mSongItemClickListener = songItemClickListener;
            mSongMenuItemClickListener = songMenuItemClickListener;
        }

        public void bind(Song song, int position) {
            mBinding.textItemSongTitle.setText(song.getTitle());
            mBinding.textItemSongArtist.setText(song.getArtist());
            Glide.with(mBinding.getRoot().getContext())
//                    .load(R.drawable.ic_music_node)
                    .load(song.getImage())
                    .into(mBinding.imageItemSongAvatar);
            mBinding.getRoot().setOnClickListener(v -> {
                Boolean isGranted = PermissionViewModel.getInstance()
                        .getPermissionGranted()
                        .getValue();
                if (isGranted == null || !isGranted) {
                    PermissionViewModel.getInstance().setPermissionAsked(true);
                }
                mSongItemClickListener.onSongItemClick(song, position);
            });
            mBinding.btnItemSongOption
                    .setOnClickListener(v -> mSongMenuItemClickListener.onSongMenuItemClick(song));
        }
    }

    public interface OnSongItemClickListener {
        void onSongItemClick(Song song, int position);
    }

    public interface OnSongMenuItemClickListener {
        void onSongMenuItemClick(Song song);
    }
}
