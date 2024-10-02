package net.lenhat.musicapplication.ui.library.playlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;
import net.lenhat.musicapplication.databinding.ItemPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private final List<PlaylistWithSongs> mPlaylist = new ArrayList<>();
    private final OnPlaylistItemClickListener mItemClickListener;
    private final OnPlaylistOptionMenuClickListener mMenuClickListener;

    public PlaylistAdapter(OnPlaylistItemClickListener itemClickListener,
                           OnPlaylistOptionMenuClickListener menuClickListener) {
        mItemClickListener = itemClickListener;
        mMenuClickListener = menuClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding, mItemClickListener, mMenuClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mPlaylist.get(position));
    }

    @Override
    public int getItemCount() {
        return mPlaylist.size();
    }

    public void updatePlaylists(List<PlaylistWithSongs> playlists) {
        if (playlists != null) {
            int oldSize = mPlaylist.size();
            mPlaylist.clear();
            mPlaylist.addAll(playlists);
            if (oldSize > mPlaylist.size()) {
                notifyItemRangeRemoved(0, oldSize);
            }
            notifyItemRangeChanged(0, mPlaylist.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlaylistBinding mBinding;
        private final OnPlaylistItemClickListener mItemClickListener;
        private final OnPlaylistOptionMenuClickListener mMenuClickListener;

        public ViewHolder(@NonNull ItemPlaylistBinding binding,
                          OnPlaylistItemClickListener itemClickListener,
                          OnPlaylistOptionMenuClickListener menuClickListener) {
            super(binding.getRoot());
            mBinding = binding;
            mItemClickListener = itemClickListener;
            mMenuClickListener = menuClickListener;
        }

        public void bind(PlaylistWithSongs playlist) {
            mBinding.textItemPlaylistName.setText(playlist.playlist.getName());
            String content = mBinding.getRoot()
                    .getContext()
                    .getString(R.string.text_number_of_song, playlist.songs.size());
            mBinding.textItemPlaylistCounter.setText(content);
            Glide.with(mBinding.getRoot())
                    .load(playlist.playlist.getArtwork())
                    .error(R.drawable.ic_album)
                    .into(mBinding.imageItemPlaylistAvatar);
            mBinding.getRoot().setOnClickListener(v -> mItemClickListener.onClick(playlist.playlist));
            mBinding.btnItemPlaylistOption.setOnClickListener(v ->
                    mMenuClickListener.onClick(playlist.playlist));
        }
    }

    public interface OnPlaylistItemClickListener {
        void onClick(Playlist playlist);
    }

    public interface OnPlaylistOptionMenuClickListener {
        void onClick(Playlist playlist);
    }
}
