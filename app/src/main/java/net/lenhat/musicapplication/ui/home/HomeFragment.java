package net.lenhat.musicapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.lenhat.musicapplication.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private static final String SCROLL_POSITION = "net.braniumacademy.musicapplication.ui.home.SCROLL_POSITION";
    private FragmentHomeBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBinding == null) {
            return;
        }
        int scrollY = mBinding.scrollViewHome.getScrollY();
        outState.putInt(SCROLL_POSITION, scrollY);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            int scrollY = savedInstanceState.getInt(SCROLL_POSITION, 0);
            mBinding.scrollViewHome.post(() -> mBinding.scrollViewHome.scrollTo(0, scrollY));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}