package net.lenhat.musicapplication.ui.discovery.artist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.artist.Artist;
import net.lenhat.musicapplication.databinding.ItemArtistBinding;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private final List<Artist> mArtists = new ArrayList<>();
    private final OnArtistClickListener mListener;

    public ArtistAdapter(OnArtistClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemArtistBinding binding = ItemArtistBinding.inflate(inflater, parent, false);
        return new ArtistViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = mArtists.get(position);
        holder.bind(artist);
    }

    @Override
    public int getItemCount() {
        return mArtists.size();
    }

    public void updateArtists(List<Artist> artists) {
        int oldSize = mArtists.size();
        mArtists.clear();
        mArtists.addAll(artists);
        notifyItemRangeRemoved(0, oldSize);
        notifyItemRangeInserted(0, mArtists.size());
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        private final ItemArtistBinding mBinding;
        private final OnArtistClickListener mListener;

        public ArtistViewHolder(ItemArtistBinding binding, OnArtistClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mListener = listener;
        }

        public void bind(Artist artist) {
            Glide.with(mBinding.getRoot())
                    .load(artist.getAvatar())
                    .error(R.drawable.ic_singer)
                    .circleCrop()
                    .into(mBinding.imageItemArtistAvatar);
            mBinding.textItemArtistName.setText(artist.getName());
            String interest = mBinding.getRoot()
                    .getContext()
                    .getString(R.string.number_of_interest, artist.getInterested());
            mBinding.textItemArtistInterest.setText(interest);
            mBinding.getRoot().setOnClickListener(v -> mListener.onClick(artist));
        }
    }

    public interface OnArtistClickListener {
        void onClick(Artist artist);
    }
}
