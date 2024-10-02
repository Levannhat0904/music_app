package net.lenhat.musicapplication.ui;

import android.content.Intent;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.ui.dialog.OptionMenuViewModel;
import net.lenhat.musicapplication.ui.dialog.SongOptionMenuDialogFragment;
import net.lenhat.musicapplication.ui.playing.NowPlayingActivity;
import net.lenhat.musicapplication.ui.viewmodel.PermissionViewModel;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;

public class AppBaseFragment extends Fragment {
    protected void showAndPlay(Song song, int index, String playlistName) {
        Boolean isPermissionGranted = PermissionViewModel.getInstance()
                .getPermissionGranted()
                .getValue();
        if (isPermissionGranted != null && isPermissionGranted) {
            doNavigate(index, playlistName);
        } else if (!PermissionViewModel.isRegistered) {
            PermissionViewModel.getInstance()
                    .getPermissionGranted()
                    .observe(requireActivity(), isGranted -> {
                        if (isGranted) {
                            doNavigate(index, playlistName);
                        }
                    });
            PermissionViewModel.isRegistered = true;
        }
    }

    protected void showOptionMenu(Song song) {
        OptionMenuViewModel optionMenuViewModel =
                new ViewModelProvider(requireActivity()).get(OptionMenuViewModel.class);
        optionMenuViewModel.setSong(song);
        SongOptionMenuDialogFragment dialog = SongOptionMenuDialogFragment.newInstance();
        dialog.show(requireActivity().getSupportFragmentManager(),
                SongOptionMenuDialogFragment.TAG);
    }

    private void doNavigate(int index, String playlistName) {
        SharedViewModel.getInstance().setCurrentPlaylist(playlistName);
        SharedViewModel.getInstance().setIndexToPlay(index);
        Intent intent = new Intent(requireContext(), NowPlayingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeCustomAnimation(requireContext(), R.anim.slide_up, R.anim.fade_out);
        requireContext().startActivity(intent, options.toBundle());
    }
}
