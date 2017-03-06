package com.mystery0.hitokoto;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment
{
    private static final String TAG = "SettingsFragment";
    private Preference refreshNow;
    private Preference showCurrent;
    private Preference testSource;
    private Preference chooseSource;
    private Preference clickToRefresh;
    private Preference setRefreshTime;
    private Preference setTextColor;
    private Preference textBold;
    private Preference textAligned;
    private Preference textSize;
    private Preference notShowSource;
    private Preference resourceAddress;
    private Preference about;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initialized();
        monitor();
    }

    private void initialized()
    {
        addPreferencesFromResource(R.xml.perferences);
        setHasOptionsMenu(true);

        refreshNow = findPreference(getString(R.string.key_refresh_now));
        showCurrent = findPreference(getString(R.string.key_show_current));
        testSource = findPreference(getString(R.string.key_test_source));
        chooseSource = findPreference(getString(R.string.key_choose_source));
        clickToRefresh = findPreference(getString(R.string.key_click_to_refresh));
        setRefreshTime = findPreference(getString(R.string.key_set_refresh_time));
        setTextColor = findPreference(getString(R.string.key_set_text_color));
        textBold = findPreference(getString(R.string.key_text_bold));
        textAligned = findPreference(getString(R.string.key_text_aligned));
        textSize = findPreference(getString(R.string.key_text_size));
        notShowSource = findPreference(getString(R.string.key_not_show_source));
        resourceAddress = findPreference(getString(R.string.key_resource_address));
        about = findPreference(getString(R.string.key_about));
    }

    private void monitor()
    {
        refreshNow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Log.i(TAG, "onPreferenceClick: " + preference.getTitle());
                return false;
            }
        });
    }
}
