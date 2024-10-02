package net.lenhat.musicapplication.ui.home.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.databinding.FragmentAlbumBinding;
import net.lenhat.musicapplication.ui.home.album.detail.DetailAlbumFragment;
import net.lenhat.musicapplication.ui.home.album.detail.DetailAlbumViewModel;
import net.lenhat.musicapplication.ui.home.album.more.MoreAlbumFragment;


public class AlbumFragment extends Fragment {
    private FragmentAlbumBinding mBinding;
    private AlbumAdapter mAlbumAdapter;
    private AlbumViewModel mAlbumViewModel;
    private DetailAlbumViewModel mDetailAlbumViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAlbumBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupComponents();
    }

    private void setupComponents() {
        mBinding.progressAlbum.setVisibility(View.VISIBLE);
        mAlbumAdapter = new AlbumAdapter(album -> {
            mDetailAlbumViewModel.setAlbum(album);
            mDetailAlbumViewModel.extractSongList(album);
            navigateToDetailAlbum();
        });
        mBinding.recyclerAlbumHot.setAdapter(mAlbumAdapter);
        mAlbumViewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);
        mDetailAlbumViewModel =
                new ViewModelProvider(requireActivity()).get(DetailAlbumViewModel.class);
        mAlbumViewModel.getAlbumList()
                .observe(getViewLifecycleOwner(), albumList -> {

                    if(albumList.size() > 10) {
                        mAlbumAdapter.updateAlbums(albumList.subList(0, 10));
                    } else {
                        mAlbumAdapter.updateAlbums(albumList);
                    }
                    mBinding.progressAlbum.setVisibility(View.GONE);
                });
        mBinding.btnMoreAlbumHot.setOnClickListener(v -> navigateToMoreAlbum());
        mBinding.textTitleAlbumHot.setOnClickListener(v -> navigateToMoreAlbum());
    }

    private void navigateToDetailAlbum() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, DetailAlbumFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToMoreAlbum() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, MoreAlbumFragment.class, null)
                .addToBackStack(null)
                .commit();
    }
}