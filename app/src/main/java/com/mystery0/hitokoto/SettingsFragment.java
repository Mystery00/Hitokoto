package com.mystery0.hitokoto;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import com.mystery0.hitokoto.widget.WidgetConfigure;
import com.mystery0.tools.Logs.Logs;

public class SettingsFragment extends PreferenceFragment
{
    private static final String TAG = "SettingsFragment";
    private Preference refreshNow;
    private Preference showCurrent;
    private Preference testSource;
    private MultiSelectListPreference chooseSource;
    private CheckBoxPreference clickToRefresh;
    private Preference setRefreshTime;
    private Preference setTextColor;
    private CheckBoxPreference textBold;
    private ListPreference textAligned;
    private Preference textSize;
    private CheckBoxPreference notShowSource;
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
        chooseSource = (MultiSelectListPreference) findPreference(getString(R.string.key_choose_source));
        clickToRefresh = (CheckBoxPreference) findPreference(getString(R.string.key_click_to_refresh));
        setRefreshTime = findPreference(getString(R.string.key_set_refresh_time));
        setTextColor = findPreference(getString(R.string.key_set_text_color));
        textBold = (CheckBoxPreference) findPreference(getString(R.string.key_text_bold));
        textAligned = (ListPreference) findPreference(getString(R.string.key_text_aligned));
        textSize = findPreference(getString(R.string.key_text_size));
        notShowSource = (CheckBoxPreference) findPreference(getString(R.string.key_not_show_source));
        resourceAddress = findPreference(getString(R.string.key_resource_address));
        about = findPreference(getString(R.string.key_about));

        clickToRefresh.setChecked(WidgetConfigure.getClickToRefresh());
        textBold.setChecked(WidgetConfigure.getTextBold());
        textAligned.setValueIndex(WidgetConfigure.getTextAligned());
    }

    private void monitor()
    {
        refreshNow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Logs.i(TAG, "onPreferenceClick: " + preference.getTitle());
                return false;
            }
        });
        showCurrent.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Logs.i(TAG, textAligned.getValue());

                return false;
            }
        });
        testSource.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                return false;
            }
        });
        setRefreshTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                new AlertDialog.Builder(App.getContext())
                        .show();
                return false;
            }
        });
    }
}
