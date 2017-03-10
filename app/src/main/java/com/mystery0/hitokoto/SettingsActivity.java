package com.mystery0.hitokoto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mystery0.hitokoto.widget.WidgetConfigure;
import com.mystery0.tools.Logs.Logs;

public class SettingsActivity extends AppCompatActivity
{
    private static final String TAG = "SettingsActivity";
    private static final int REQUEST_PERMISSION = 456;
    private PreferenceFragment preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        CheckPermission();
        preferenceManager = (PreferenceFragment) getFragmentManager().findFragmentById(R.id.settingsFragment);
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
                WidgetConfigure.setNotShowSource(notShowSource.isChecked());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
