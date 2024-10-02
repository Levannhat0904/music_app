package net.lenhat.musicapplication.ui.discovery.artist.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.artist.Artist;
import net.lenhat.musicapplication.databinding.FragmentMoreArtistBinding;
import net.lenhat.musicapplication.ui.discovery.artist.ArtistAdapter;
import net.lenhat.musicapplication.ui.discovery.artist.detail.DetailArtistFragment;

public class MoreArtistFragment extends Fragment {
    private FragmentMoreArtistBinding mBinding;
    private MoreArtistViewModel mViewModel;
    private ArtistAdapter mArtistAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMoreArtistBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        observeData();
    }

    private void setupView() {
        mArtistAdapter = new ArtistAdapter(this::navigateToDetailArtist);
        mBinding.includeMoreArtist.recyclerArtist.setAdapter(mArtistAdapter);
        mBinding.toolbarMoreArtist.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void observeData() {
        mViewModel = new ViewModelProvider(requireActivity()).get(MoreArtistViewModel.class);
        mViewModel.getArtist().observe(getViewLifecycleOwner(), mArtistAdapter::updateArtists);
    }

    private void navigateToDetailArtist(Artist artist) {
        Bundle bundle = new Bundle();
        bundle.putInt(DetailArtistFragment.EXTRA_ARTIST_ID, artist.getId());
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, DetailArtistFragment.class, bundle)
                .addToBackStack(null)
                .commit();
    }
}