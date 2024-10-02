package net.lenhat.musicapplication.ui.home.album.more;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Album;
import net.lenhat.musicapplication.databinding.ItemMoreAlbumBinding;
import net.lenhat.musicapplication.ui.home.album.AlbumAdapter;

import java.util.ArrayList;
import java.util.List;

public class MoreAlbumAdapter extends RecyclerView.Adapter<MoreAlbumAdapter.ViewHolder> {
    private final List<Album> mAlbums = new ArrayList<>();
    private final AlbumAdapter.AlbumClickListener mListener;

    public MoreAlbumAdapter(AlbumAdapter.AlbumClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MoreAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMoreAlbumBinding binding =
                ItemMoreAlbumBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreAlbumAdapter.ViewHolder holder, int position) {
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
            notifyItemRangeChanged(0, mAlbums.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMoreAlbumBinding mBinding;
        public AlbumAdapter.AlbumClickListener mListener;

        public ViewHolder(@NonNull ItemMoreAlbumBinding binding,
                          AlbumAdapter.AlbumClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mListener = listener;
        }

        public void bind(Album album) {
            mBinding.textItemAlbum.setText(album.getName());
            Glide.with(itemView.getContext())
                    .load(album.getArtwork())
                    .error(R.drawable.ic_album)
                    .into(mBinding.imageItemAlbumDetail);
            mBinding.getRoot().setOnClickListener(v -> mListener.onAlbumClick(album));
        }
    }
}
