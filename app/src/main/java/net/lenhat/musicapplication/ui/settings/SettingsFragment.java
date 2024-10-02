package net.lenhat.musicapplication.ui.settings;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import net.lenhat.musicapplication.R;
import net.lenhat.musicapplication.utils.AppUtils;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String KEY_PREF_DARK_MODE = "dark_mode";
    public static final String KEY_PREF_LANGUAGE = "language";
    public static final String KEY_PREF_FONT_SIZE = "font_size";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupPreferences();
    }

    private void setupPreferences() {
        SwitchPreferenceCompat darkModePref = findPreference(KEY_PREF_DARK_MODE);
        if (darkModePref != null) {
            darkModePref.setOnPreferenceChangeListener(((preference, newValue) -> {
                changeTheme(preference, newValue);
                return true;
            }));
        }

        ListPreference languagePref = findPreference(KEY_PREF_LANGUAGE);
        if (languagePref != null) {
            languagePref.setOnPreferenceChangeListener(((preference, newValue) -> {
                changeLanguage(preference, newValue);
                return true;
            }));
        }

        ListPreference fontSizePref = findPreference(KEY_PREF_FONT_SIZE);
        if (fontSizePref != null) {
            fontSizePref.setOnPreferenceChangeListener(((preference, newValue) -> {
                Intent intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
                startActivity(intent);
                return true;
            }));
        }
    }

    private void changeTheme(Preference preference, Object newValue) {
        SharedPreferences sharedPreferences = preference.getSharedPreferences();
        boolean oldDarkMode = false;
        if (sharedPreferences != null) {
            oldDarkMode = sharedPreferences.getBoolean(KEY_PREF_DARK_MODE, false);
        }
        boolean newDarkMode = Boolean.parseBoolean(newValue.toString());
        if (newDarkMode != oldDarkMode) {
            AppUtils.sIsConfigChanged = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                UiModeManager uiModeManager = requireActivity().getSystemService(UiModeManager.class);
                int uiMode = newDarkMode ? UiModeManager.MODE_NIGHT_YES : UiModeManager.MODE_NIGHT_NO;
                uiModeManager.setApplicationNightMode(uiMode);
            } else {
                int mode = newDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(mode);
            }
        }
    }

    private void changeLanguage(Preference preference, Object newValue) {
        String defaultLanguage = Locale.getDefault().getLanguage();
        String oldLanguage = defaultLanguage;
        String newLanguage = newValue.toString();
        SharedPreferences sharedPreferences = preference.getSharedPreferences();
        if (sharedPreferences != null) {
            oldLanguage = sharedPreferences.getString(KEY_PREF_LANGUAGE, defaultLanguage);
        }
        if (oldLanguage.compareTo(newLanguage) != 0) {
            AppUtils.sIsConfigChanged = true;
            LocaleListCompat localeListCompat = LocaleListCompat.forLanguageTags(newLanguage);
            AppCompatDelegate.setApplicationLocales(localeListCompat);
        }
    }
}