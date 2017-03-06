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
