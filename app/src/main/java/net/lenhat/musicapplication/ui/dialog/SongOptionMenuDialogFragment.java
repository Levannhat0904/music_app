package net.lenhat.musicapplication.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.databinding.FragmentSongOptionMenuDialogBinding;
import net.lenhat.musicapplication.databinding.ItemOptionMenuBinding;
import net.lenhat.musicapplication.ui.library.playlist.PlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SongOptionMenuDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = "SongOptionMenuDialogFragment";
    private FragmentSongOptionMenuDialogBinding mBinding;
    private OptionMenuAdapter mAdapter;
    private OptionMenuViewModel mOptionMenuViewModel;
    private PlaylistViewModel mPlaylistViewModel;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public static SongOptionMenuDialogFragment newInstance() {
        return new SongOptionMenuDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSongOptionMenuDialogBinding
                .inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        setupViewModel();
    }

    private void setupView() {
        mAdapter = new OptionMenuAdapter(this::handleMenuItemClick);
        mBinding.recyclerOptionMenu.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        mOptionMenuViewModel =
                new ViewModelProvider(requireActivity()).get(OptionMenuViewModel.class);
        PlaylistViewModel.Factory factory =
                new PlaylistViewModel.Factory(application.getPlaylistRepository());
        mPlaylistViewModel =
                new ViewModelProvider(requireActivity(), factory).get(PlaylistViewModel.class);
        SongInfoDialogViewModel songInfoDialogViewModel =
                new ViewModelProvider(requireActivity()).get(SongInfoDialogViewModel.class);
        mOptionMenuViewModel.getMenuItems()
                .observe(getViewLifecycleOwner(), mAdapter::updateOptionMenuItems);
        mOptionMenuViewModel.getSong()
                .observe(getViewLifecycleOwner(), song -> {
                    showSongInfo(song);
                    songInfoDialogViewModel.setSong(song);
                });
    }

    private void handleMenuItemClick(OptionMenuItem optionMenuItem) {
        switch (optionMenuItem.getOption()) {
            case VIEW_DETAILS:
                SongInfoDialogFragment.newInstance()
                        .show(requireActivity().getSupportFragmentManager(),
                                SongInfoDialogFragment.TAG);
                break;
            case ADD_TO_PLAYLIST:
                showAddToPlaylistDialog();
                break;
            default:
                Toast.makeText(requireContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddToPlaylistDialog() {
        AddToPlaylistDialog dialog = new AddToPlaylistDialog(playlist -> {
            Song song = mOptionMenuViewModel.getSong().getValue();
            if (song != null) {
                mDisposable.add(mPlaylistViewModel.createPlaylistSongCrossRef(playlist, song)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            String message = getString(
                                    R.string.add_to_playlist_success, playlist.getName());
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }, t -> {
                            String message = getString(
                                    R.string.error_add_to_playlist);
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }));
            }
        });
        dialog.show(requireActivity().getSupportFragmentManager(), AddToPlaylistDialog.TAG);
    }

    private void showSongInfo(Song song) {
        mBinding.includeSongOptionMenu.textItemSongOptionMenuTitle.setText(song.getTitle());
        mBinding.includeSongOptionMenu.textItemSongOptionMenuArtist.setText(song.getArtist());
        Glide.with(this)
                .load(song.getImage())
                .error(R.drawable.ic_music_node)
                .into(mBinding.includeSongOptionMenu.imageItemSongOptionMenuAvatar);
    }

    private static class OptionMenuAdapter
            extends RecyclerView.Adapter<OptionMenuAdapter.ViewHolder> {
        private final List<OptionMenuItem> mOptionMenuItems = new ArrayList<>();
        private final OnOptionMenuItemClickListener mListener;

        public OptionMenuAdapter(OnOptionMenuItemClickListener listener) {
            mListener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemOptionMenuBinding binding =
                    ItemOptionMenuBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(binding, mListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(mOptionMenuItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mOptionMenuItems.size();
        }

        public void updateOptionMenuItems(List<OptionMenuItem> optionMenuItems) {
            mOptionMenuItems.clear();
            mOptionMenuItems.addAll(optionMenuItems);
            notifyItemRangeChanged(0, mOptionMenuItems.size());
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private final ItemOptionMenuBinding mBinding;
            private final OnOptionMenuItemClickListener mListener;

            public ViewHolder(ItemOptionMenuBinding binding,
                              OnOptionMenuItemClickListener listener) {
                super(binding.getRoot());
                mBinding = binding;
                mListener = listener;
            }

            public void bind(OptionMenuItem optionMenuItem) {
                mBinding.textOptionMenuItemTitle.setText(optionMenuItem.getMenuItemTitle());
                Glide.with(mBinding.getRoot())
                        .load(optionMenuItem.getIconId())
                        .into(mBinding.imageOptionMenuItemIcon);
                mBinding.getRoot()
                        .setOnClickListener(v -> mListener.onOptionMenuItemClick(optionMenuItem));
            }
        }
    }

    public interface OnOptionMenuItemClickListener {
        void onOptionMenuItemClick(OptionMenuItem optionMenuItem);
    }
}
