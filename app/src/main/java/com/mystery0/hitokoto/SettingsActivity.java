package com.mystery0.hitokoto;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.Toast;

import com.mystery0.hitokoto.widget.WidgetConfigure;
import com.mystery0.tools.Logs.Logs;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

@SuppressWarnings("deprecation")
public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String TAG = "SettingsActivity";
    private static final int REQUEST_PERMISSION = 456;
    private Preference refreshNow;
    private Preference showCurrent;
    private Preference testSource;
    private MultiSelectListPreference chooseSource;
    private CheckBoxPreference clickToRefresh;
    private EditTextPreference setRefreshTime;
    private ColorPickerPreference setTextColor;
    private CheckBoxPreference textBold;
    private ListPreference textAligned;
    private EditTextPreference textSize;
    private CheckBoxPreference notShowSource;
    private Preference resourceAddress;
    private Preference about;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perferences);
        CheckPermission();
        initialized();
        monitor();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void initialized()
    {
        refreshNow = findPreference(getString(R.string.key_refresh_now));
        showCurrent = findPreference(getString(R.string.key_show_current));
        testSource = findPreference(getString(R.string.key_test_source));
        chooseSource = (MultiSelectListPreference) findPreference(getString(R.string.key_choose_source));
        clickToRefresh = (CheckBoxPreference) findPreference(getString(R.string.key_click_to_refresh));
        setRefreshTime = (EditTextPreference) findPreference(getString(R.string.key_set_refresh_time));
        setTextColor = (ColorPickerPreference) findPreference(getString(R.string.key_set_text_color));
        textBold = (CheckBoxPreference) findPreference(getString(R.string.key_text_bold));
        textAligned = (ListPreference) findPreference(getString(R.string.key_text_aligned));
        textSize = (EditTextPreference) findPreference(getString(R.string.key_text_size));
        notShowSource = (CheckBoxPreference) findPreference(getString(R.string.key_not_show_source));
        resourceAddress = findPreference(getString(R.string.key_resource_address));
        about = findPreference(getString(R.string.key_about));

        clickToRefresh.setChecked(WidgetConfigure.getClickToRefresh());
        textBold.setChecked(WidgetConfigure.getTextBold());
        notShowSource.setChecked(WidgetConfigure.getNotShowSource());
        textAligned.setValueIndex(WidgetConfigure.getTextAligned());
        chooseSource.setValues(WidgetConfigure.getChooseSource(WidgetConfigure.SourceType.STRING));
        setRefreshTime.setDefaultValue(WidgetConfigure.getRefreshTime());
        textSize.setDefaultValue(WidgetConfigure.getTextSize());
        PreferenceManager.setDefaultValues(this, R.xml.perferences, false);

        setTextColor.setHexValueEnabled(true);
        setTextColor.setAlphaSliderEnabled(false);
    }

    private void monitor()
    {
        setTextColor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                Logs.i(TAG, "onPreferenceChange: " + newValue.toString());
                preference.setSummary(ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
                return true;
            }
        });
        refreshNow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                if (WidgetConfigure.getEnable())
                {
                    WidgetConfigure.refreshText();
                    Toast.makeText(App.getContext(), R.string.hint_broadcast, Toast.LENGTH_SHORT)
                            .show();
                } else
                {
                    Toast.makeText(App.getContext(), R.string.hint_add_widget, Toast.LENGTH_SHORT)
                            .show();
                }
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
        resourceAddress.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://github.com/Mystery00/Hitokoto");
                intent.setData(content_url);
                startActivity(intent);
                return false;
            }
        });
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {

                return false;
            }
        });
    }

    private void CheckPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION)
        {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(App.getContext(), R.string.hint_permission, Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        Logs.i(TAG, "onSharedPreferenceChanged: " + key);
        if (key.equals(getString(R.string.key_choose_source)))
        {
            WidgetConfigure.setChooseSource(chooseSource.getValues());
        } else if (key.equals(getString(R.string.key_click_to_refresh)))
        {
            WidgetConfigure.setClickToRefresh(clickToRefresh.isChecked());
        } else if (key.equals(getString(R.string.key_text_bold)))
        {
            WidgetConfigure.setTextBold(textBold.isChecked());
        } else if (key.equals(getString(R.string.key_text_aligned)))
        {
            WidgetConfigure.setTextAligned(textAligned.findIndexOfValue(textAligned.getValue()));
        } else if (key.equals(getString(R.string.key_text_size)))
        {
            WidgetConfigure.setTextSize(Integer.parseInt("0" + textSize.getEditText().getText().toString()));
        } else if (key.equals(getString(R.string.key_not_show_source)))
        {
            WidgetConfigure.setNotShowSource(notShowSource.isChecked());
        } else if (key.equals(getString(R.string.key_set_refresh_time)))
        {
            WidgetConfigure.setRefreshTime(Integer.parseInt("0" + setRefreshTime.getEditText().getText().toString()));
        }
        WidgetConfigure.refreshText();
    }
}
