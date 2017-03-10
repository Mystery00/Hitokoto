package com.mystery0.hitokoto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        addPreferencesFromResource(R.xml.perferences);
        setHasOptionsMenu(true);
        initialized();
        monitor();
    }

    private void initialized()
    {
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
        notShowSource.setChecked(WidgetConfigure.getNotShowSource());
        textAligned.setValueIndex(WidgetConfigure.getTextAligned());
        chooseSource.setValues(WidgetConfigure.getChooseSource(WidgetConfigure.SourceType.STRING));
    }

    private void monitor()
    {
        refreshNow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                WidgetConfigure.refreshText();
                Toast.makeText(App.getContext(), R.string.hint_broadcast, Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        showCurrent.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
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
            @SuppressWarnings("ConstantConditions")
            @SuppressLint("InflateParams")
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                View view = LayoutInflater.from(App.getContext()).inflate(R.layout.dialog_refresh_time, null);
                final TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.text_time);
                textInputLayout.getEditText().setText(String.valueOf(WidgetConfigure.getRefreshTime()));
                new AlertDialog.Builder(App.getContext())
                        .setView(view)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.done, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                int time = Integer.parseInt(textInputLayout.getEditText().getText().toString());
                                if (time > 0)
                                {
                                    WidgetConfigure.setRefreshTime(time);
                                } else
                                {
                                    Toast.makeText(App.getContext(), R.string.ErrorRefreshTime, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        })
                        .show();
                return false;
            }
        });
    }
}
