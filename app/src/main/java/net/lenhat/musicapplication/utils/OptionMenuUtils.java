package net.lenhat.musicapplication.utils;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.ui.dialog.OptionMenuItem;

import java.util.ArrayList;
import java.util.List;

public abstract class OptionMenuUtils {
    private static final List<OptionMenuItem> sMenuItems = new ArrayList<>();

    static {
        createMenuItems();
    }

    private static void createMenuItems() {
        sMenuItems.add(new OptionMenuItem(MenuOption.DOWNLOAD, R.drawable.ic_menu_download, R.string.action_download));
        sMenuItems.add(new OptionMenuItem(MenuOption.ADD_TO_FAVORITE, R.drawable.ic_menu_favorite, R.string.action_favorite));
        sMenuItems.add(new OptionMenuItem(MenuOption.ADD_TO_PLAYLIST, R.drawable.ic_menu_add_to_playlist, R.string.action_add_to_playlist));
        sMenuItems.add(new OptionMenuItem(MenuOption.PLAY_NEXT, R.drawable.ic_menu_play_next, R.string.action_add_to_play_next));
        sMenuItems.add(new OptionMenuItem(MenuOption.VIEW_ALBUM, R.drawable.ic_menu_album, R.string.action_view_album));
        sMenuItems.add(new OptionMenuItem(MenuOption.VIEW_ARTIST, R.drawable.ic_singer, R.string.action_view_artist));
        sMenuItems.add(new OptionMenuItem(MenuOption.BLOCK, R.drawable.ic_menu_block, R.string.action_block));
        sMenuItems.add(new OptionMenuItem(MenuOption.REPORT_ERROR, R.drawable.ic_menu_report_error, R.string.action_report_error));
        sMenuItems.add(new OptionMenuItem(MenuOption.VIEW_DETAILS, R.drawable.ic_menu_info, R.string.action_view_song_info));
    }

    public static List<OptionMenuItem> getSongOptionMenuItems() {
        return sMenuItems;
    }

    public enum MenuOption {
        DOWNLOAD("download"),
        ADD_TO_FAVORITE("add_to_favorite"),
        ADD_TO_PLAYLIST("add_to_playlist"),
        PLAY_NEXT("play_next"),
        VIEW_ALBUM("view_album"),
        VIEW_ARTIST("view_artist"),
        BLOCK("block"),
        REPORT_ERROR("report_error"),
        VIEW_DETAILS("view_details");

        private final String mValue;

        MenuOption(String value) {
            mValue = value;
        }

        public String getValue() {
            return mValue;
        }
    }
}
