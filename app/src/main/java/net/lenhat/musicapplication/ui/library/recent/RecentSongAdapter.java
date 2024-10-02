package net.lenhat.musicapplication.ui.library.recent;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.databinding.ItemSongBinding;
import net.lenhat.musicapplication.ui.home.recommended.SongListAdapter;
import net.lenhat.musicapplication.ui.viewmodel.PermissionViewModel;
import net.lenhat.musicapplication.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class RecentSongAdapter extends RecyclerView.Adapter<RecentSongAdapter.ViewHolder> {
    private final List<Song> mSongs = new ArrayList<>();
    private final SongListAdapter.OnSongItemClickListener mSongItemClickListener;
    private final SongListAdapter.OnSongMenuItemClickListener mSongMenuItemClickListener;

    public RecentSongAdapter(SongListAdapter.OnSongItemClickListener songItemClickListener,
                             SongListAdapter.OnSongMenuItemClickListener songMenuItemClickListener) {
        mSongItemClickListener = songItemClickListener;
        mSongMenuItemClickListener = songMenuItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSongBinding binding = ItemSongBinding.inflate(layoutInflater, parent, false);
        ConstraintLayout layout = binding.getRoot();
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();
                int deltaX = (int) (48 * AppUtils.X_DPI / 160);
                binding.layoutItemSong.getLayoutParams().width = width - deltaX;
                binding.layoutItemSong.getLayoutParams().height = height;
            }
        });
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
        private final SongListAdapter.OnSongItemClickListener mSongItemClickListener;
        private final SongListAdapter.OnSongMenuItemClickListener mSongMenuItemClickListener;

        public ViewHolder(ItemSongBinding binding,
                          SongListAdapter.OnSongItemClickListener songItemClickListener,
                          SongListAdapter.OnSongMenuItemClickListener songMenuItemClickListener) {
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
}
