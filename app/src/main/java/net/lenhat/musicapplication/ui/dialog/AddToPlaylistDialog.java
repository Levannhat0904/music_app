package net.lenhat.musicapplication.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import net.lenhat.musicapplication.MusicApplication;
import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.ui.library.playlist.PlaylistAdapter;
import net.lenhat.musicapplication.ui.library.playlist.PlaylistViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddToPlaylistDialog extends DialogFragment {
    public static final String TAG = "AddToPlaylistDialog";
    private final OnPlaylistSelectedListener mListener;
    private PlaylistViewModel mPlaylistViewModel;
    private PlaylistAdapter mAdapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public AddToPlaylistDialog(@NonNull OnPlaylistSelectedListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setupComponents();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View rootView = layoutInflater.inflate(R.layout.dialog_add_to_playlist, null);
        // khai báo các biến liên kết tới các view
        RecyclerView recyclerViewPlaylist = rootView.findViewById(R.id.rv_existed_playlist);
        recyclerViewPlaylist.setAdapter(mAdapter);
        MaterialButton btnCancel = rootView.findViewById(R.id.btn_cancel_playlist);
        MaterialButton btnCreate = rootView.findViewById(R.id.btn_create_playlist);
        TextInputEditText edtPlaylistName = rootView.findViewById(R.id.edit_playlist_name);

        btnCancel.setOnClickListener(v -> dismiss());
        btnCreate.setOnClickListener(v -> {
            String playlistName;
            if (edtPlaylistName.getText() != null) {
                playlistName = edtPlaylistName.getText().toString().trim();
                if (playlistName.isEmpty()) {
                    edtPlaylistName.setError(getString(R.string.error_empty_playlist_name));
                } else {
                    checkAndCreatePlaylist(playlistName);
                    closeKeyboard(edtPlaylistName);
                    edtPlaylistName.getText().clear();
                }
            }
        });
        builder.setView(rootView);
        loadPlaylist();
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void setupComponents() {
        MusicApplication application = (MusicApplication) requireActivity().getApplication();
        PlaylistViewModel.Factory factory =
                new PlaylistViewModel.Factory(application.getPlaylistRepository());
        mPlaylistViewModel =
                new ViewModelProvider(requireActivity(), factory).get(PlaylistViewModel.class);
        mAdapter = new PlaylistAdapter(
                playlist -> {
                    mListener.onPlaylistSelected(playlist);
                    dismiss();
                },
                playlist -> {
                    // todo
                }
        );
    }

    private void loadPlaylist() {
        mDisposable.add(mPlaylistViewModel.getAllPlaylistWithSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mPlaylistViewModel::setPlaylists, t -> {
                }));
        mPlaylistViewModel.getPlaylists()
                .observe(requireActivity(), mAdapter::updatePlaylists);
    }

    private void checkAndCreatePlaylist(String playlistName) {
        mDisposable.add(mPlaylistViewModel.getPlaylistByName(playlistName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> doCreatePlaylist(result, playlistName),
                        error -> doCreatePlaylist(null, playlistName)));
    }

    private void doCreatePlaylist(Playlist playlist, String playlistName) {
        if (playlist == null) {
            mDisposable.add(mPlaylistViewModel.createPlaylist(playlistName)
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        } else {
            String errorMessage = getString(R.string.error_playlist_already_exists);
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void closeKeyboard(TextInputEditText view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public interface OnPlaylistSelectedListener {
        void onPlaylistSelected(Playlist playlist);
    }
}
