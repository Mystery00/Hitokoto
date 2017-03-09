package com.mystery0.hitokoto;

import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mystery0.hitokoto.widget.WidgetConfigure;
import com.mystery0.tools.Logs.Logs;

public class SettingsActivity extends AppCompatActivity
{
    private static final String TAG = "SettingsActivity";
    private PreferenceFragment preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferenceManager = (PreferenceFragment) getFragmentManager().findFragmentById(R.id.settingsFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_done:
                Logs.i(TAG, "onOptionsItemSelected: Done! ");
                MultiSelectListPreference chooseSource = (MultiSelectListPreference) preferenceManager.findPreference(getString(R.string.key_choose_source));
                CheckBoxPreference clickToRefresh = (CheckBoxPreference) preferenceManager.findPreference(getString(R.string.key_click_to_refresh));
                CheckBoxPreference textBold = (CheckBoxPreference) preferenceManager.findPreference(getString(R.string.key_text_bold));
                ListPreference textAligned = (ListPreference) preferenceManager.findPreference(getString(R.string.key_text_aligned));
                CheckBoxPreference notShowSource = (CheckBoxPreference) preferenceManager.findPreference(getString(R.string.key_not_show_source));
                WidgetConfigure.setChooseSource(chooseSource.getValues());
                WidgetConfigure.setClickToRefresh(clickToRefresh.isChecked());
                WidgetConfigure.setTextBold(textBold.isChecked());
                WidgetConfigure.setTextAligned(textAligned.findIndexOfValue(textAligned.getValue()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
