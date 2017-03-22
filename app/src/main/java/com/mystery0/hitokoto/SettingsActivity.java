package com.mystery0.hitokoto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mystery0.hitokoto.local.LocalHitokotoActivity;
import com.mystery0.hitokoto.local.LocalMultipleActivity;
import com.mystery0.hitokoto.local.LocalSingleActivity;
import com.mystery0.hitokoto.widget.WidgetConfigure;
import com.mystery0.tools.Logs.Logs;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

import java.io.File;

@SuppressWarnings("deprecation")
public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String TAG = "SettingsActivity";
    private static final int REQUEST_PERMISSION = 456;
    private Preference refreshNow;
    private Preference showCurrent;
    private Preference testSource;
    private MultiSelectListPreference chooseSource;
    private CheckBoxPreference autoRefresh;
    private CheckBoxPreference clickToRefresh;
    private EditTextPreference setTextPadding;
    private EditTextPreference setRefreshTime;
    private ColorPickerPreference setTextColor;
    private ListPreference textAligned;
    private EditTextPreference textSize;
    private CheckBoxPreference notShowSource;
    private Preference showCrashLog;
    private Preference contactMe;
    private Preference customSingleHitokoto;
    private Preference customMultipleHitokoto;
    private Preference showCustomHitokoto;

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
        autoRefresh = (CheckBoxPreference) findPreference(getString(R.string.key_auto_refresh));
        clickToRefresh = (CheckBoxPreference) findPreference(getString(R.string.key_click_to_refresh));
        setTextPadding = (EditTextPreference) findPreference(getString(R.string.key_set_text_padding));
        setRefreshTime = (EditTextPreference) findPreference(getString(R.string.key_set_refresh_time));
        setTextColor = (ColorPickerPreference) findPreference(getString(R.string.key_set_text_color));
        textAligned = (ListPreference) findPreference(getString(R.string.key_text_aligned));
        textSize = (EditTextPreference) findPreference(getString(R.string.key_text_size));
        notShowSource = (CheckBoxPreference) findPreference(getString(R.string.key_not_show_source));
        showCrashLog = findPreference(getString(R.string.key_show_crash_log));
        contactMe = findPreference(getString(R.string.key_contact_me));
        customSingleHitokoto = findPreference(getString(R.string.key_local_single_hitokoto));
        customMultipleHitokoto = findPreference(getString(R.string.key_local_multiple_hitokoto));
        showCustomHitokoto = findPreference(getString(R.string.key_local_show_hitokoto));

        autoRefresh.setChecked(WidgetConfigure.getAutoRefresh());
        clickToRefresh.setChecked(WidgetConfigure.getClickToRefresh());
        notShowSource.setChecked(WidgetConfigure.getNotShowSource());
        textAligned.setSummary(getResources().getStringArray(R.array.list_aligned)[WidgetConfigure.getTextAligned()]);
        textAligned.setValueIndex(WidgetConfigure.getTextAligned());
        setTextColor.setSummary(WidgetConfigure.getTextColor());
        setTextColor.setDefaultValue(Color.parseColor(WidgetConfigure.getTextColor()));
        chooseSource.setValues(WidgetConfigure.getChooseSource(WidgetConfigure.SourceType.STRING));
        setTextPadding.setSummary("" + WidgetConfigure.getTextPadding());
        setTextPadding.setDefaultValue(WidgetConfigure.getTextPadding());
        setRefreshTime.setSummary("" + WidgetConfigure.getRefreshTime());
        setRefreshTime.setDefaultValue(WidgetConfigure.getRefreshTime());
        textSize.setSummary("" + WidgetConfigure.getTextSize());
        textSize.setDefaultValue(WidgetConfigure.getTextSize());
        PreferenceManager.setDefaultValues(this, R.xml.perferences, false);
    }

    private void monitor()
    {
        setTextColor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                Logs.i(TAG, "onPreferenceChange: " + newValue.toString());
                String temp = ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue)));
                preference.setSummary(temp);
                WidgetConfigure.setTextColor(temp);
                if (WidgetConfigure.getEnable())
                {
                    WidgetConfigure.refreshText();
                }
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
                //noinspection RestrictedApi
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
                @SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_show_currect, null);
                TextView content = (TextView) view.findViewById(R.id.content);
                TextView source = (TextView) view.findViewById(R.id.source);
                setOnClick(content);
                setOnClick(source);
                String[] temp = WidgetConfigure.getTemp();
                content.setText(temp[0]);
                source.setText(temp[1]);
                new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogStyle)
                        .setView(view)
                        .setTitle(R.string.text_show_current)
                        .setMessage(R.string.hint_show_current)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return false;
            }
        });
        testSource.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
//                new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogStyle)
//                        .setView(view)
//                        .setTitle(R.string.text_show_current)
//                        .setPositiveButton(android.R.string.ok, null)
//                        .show();
                return false;
            }
        });
        showCrashLog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/hitokoto/log/crash2017-03-15 12:31:39.txt");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //判断是否是AndroidN以及更高的版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    Uri contentUri = FileProvider.getUriForFile(
                            App.getContext(), "com.mystery0.hitokoto.fileProvider", file);
                    Logs.i(TAG, "onPreferenceClick: " + contentUri.getPath());
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(contentUri, "text/*");
                } else
                {
                    intent.setDataAndType(Uri.fromFile(file), "text/*");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
                return false;
            }
        });
        contactMe.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Logs.i(TAG, "onPreferenceClick: 拷贝到剪切板");
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setText(getString(R.string.e_mail_address));
                Toast.makeText(App.getContext(), R.string.hint_copy_address, Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        customSingleHitokoto.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                startActivity(new Intent(App.getContext(), LocalSingleActivity.class));
                return false;
            }
        });
        customMultipleHitokoto.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                startActivity(new Intent(App.getContext(), LocalMultipleActivity.class));
                return false;
            }
        });
        showCustomHitokoto.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                startActivity(new Intent(App.getContext(), LocalHitokotoActivity.class));
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
        } else if (key.equals(getString(R.string.key_auto_refresh)))
        {
            WidgetConfigure.setAutoRefresh(autoRefresh.isChecked());
        } else if (key.equals(getString(R.string.key_click_to_refresh)))
        {
            WidgetConfigure.setClickToRefresh(clickToRefresh.isChecked());
        } else if (key.equals(getString(R.string.key_text_aligned)))
        {
            WidgetConfigure.setTextAligned(textAligned.findIndexOfValue(textAligned.getValue()));
            textAligned.setSummary(getResources().getStringArray(R.array.list_aligned)[WidgetConfigure.getTextAligned()]);
        } else if (key.equals(getString(R.string.key_text_size)))
        {
            WidgetConfigure.setTextSize(Integer.parseInt("0" + textSize.getEditText().getText().toString()));
            textSize.setSummary("" + WidgetConfigure.getTextSize());
        } else if (key.equals(getString(R.string.key_not_show_source)))
        {
            WidgetConfigure.setNotShowSource(notShowSource.isChecked());
        } else if (key.equals(getString(R.string.key_set_refresh_time)))
        {
            WidgetConfigure.setRefreshTime(Integer.parseInt("0" + setRefreshTime.getEditText().getText().toString()));
            setRefreshTime.setSummary("" + WidgetConfigure.getRefreshTime());
        } else if (key.equals(getString(R.string.key_set_text_padding)))
        {
            WidgetConfigure.setTextPadding(Integer.parseInt("0" + setTextPadding.getEditText().getText().toString()));
            setTextPadding.setSummary("" + WidgetConfigure.getTextPadding());
        }
        if (WidgetConfigure.getEnable())
        {
            WidgetConfigure.refreshText();
        }
    }

    private void setOnClick(final TextView view)
    {
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Logs.i(TAG, "onClick: 拷贝到剪切板");
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setText(view.getText().toString());
                Toast.makeText(App.getContext(), R.string.hint_copy_text, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
