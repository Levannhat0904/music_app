package net.lenhat.musicapplication.ui.dialog;

import net.lenhat.musicapplication.utils.OptionMenuUtils;

public class OptionMenuItem {
    private final OptionMenuUtils.MenuOption mOption;
    private final int mIconId;
    private final int mMenuItemTitle;

    public OptionMenuItem(OptionMenuUtils.MenuOption option, int iconId, int menuItemTitle) {
        mOption = option;
        mIconId = iconId;
        mMenuItemTitle = menuItemTitle;
    }

    public OptionMenuUtils.MenuOption getOption() {
        return mOption;
    }

    public int getIconId() {
        return mIconId;
    }

    public int getMenuItemTitle() {
        return mMenuItemTitle;
    }
}
