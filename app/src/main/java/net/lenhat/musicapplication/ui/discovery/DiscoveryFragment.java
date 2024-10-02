package net.lenhat.musicapplication.ui.discovery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.databinding.FragmentDiscoveryBinding;


public class DiscoveryFragment extends Fragment {
    private FragmentDiscoveryBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        mBinding = FragmentDiscoveryBinding.inflate(inflater, container, false);
        View root = mBinding.getRoot();

//        final TextView textView = mBinding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}