package net.lenhat.musicapplication.utils;

public abstract class AppUtils {
    public static final String EXTRA_CURRENT_FRACTION = "current_fraction";
    public static boolean sIsConfigChanged = false;

    public enum DefaultPlaylistName {
        DEFAULT("default"),
        FAVORITE("favorite"),
        RECOMMENDED("recommended"),
        RECENT("recent"),
        SEARCHED("searched"),
        MOST_HEARD("most_heard"),
        FOR_YOU("for_you"),
        CUSTOM("custom");
        private final String value;

        DefaultPlaylistName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static float X_DPI = 0;
}
