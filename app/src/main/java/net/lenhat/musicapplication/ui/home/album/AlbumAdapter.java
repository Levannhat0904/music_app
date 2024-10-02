package net.lenhat.musicapplication.ui.home.album;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Album;
import net.lenhat.musicapplication.databinding.ItemAlbumBinding;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private final List<Album> mAlbums = new ArrayList<>();
    private final AlbumClickListener mListener;

    public AlbumAdapter(AlbumClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAlbumBinding binding = ItemAlbumBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mAlbums.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public void updateAlbums(List<Album> albums) {
        if (albums != null) {
            int oldSize = mAlbums.size();
            mAlbums.clear();
            mAlbums.addAll(albums);
            if (oldSize > mAlbums.size()) {
                notifyItemRangeRemoved(0, oldSize);
            }
            notifyItemRangeInserted(0, mAlbums.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAlbumBinding mBinding;
        private final AlbumClickListener mListener;

        public ViewHolder(ItemAlbumBinding binding, AlbumClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mListener = listener;
        }

        public void bind(Album album) {
            Glide.with(itemView.getContext())
                    .load(album.getArtwork())
                    .error(R.drawable.ic_music_node)
                    .into(mBinding.imageItemAlbumAvatar);
            mBinding.textItemAlbumTitle.setText(album.getName());
            mBinding.getRoot().setOnClickListener(v -> mListener.onAlbumClick(album));
        }
    }

    public interface AlbumClickListener {
        void onAlbumClick(Album album);
    }
}
