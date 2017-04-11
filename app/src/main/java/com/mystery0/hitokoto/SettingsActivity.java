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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.hitokoto.class_class.HitokotoSource;
import com.mystery0.hitokoto.custom.CustomSourceActivity;
import com.mystery0.hitokoto.local.LocalConfigure;
import com.mystery0.hitokoto.local.LocalHitokotoManagerActivity;
import com.mystery0.hitokoto.test_source.TestSource;
import com.mystery0.hitokoto.test_source.TestSourceAdapter;
import com.mystery0.hitokoto.test_source.TestSourceListener;
import com.mystery0.hitokoto.widget.WidgetConfigure;
import com.mystery0.tools.Logs.Logs;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

import org.litepal.crud.DataSupport;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@SuppressWarnings({"deprecation", "ConstantConditions"})
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
    private Preference exportHitokotos;
    private Preference importHitokotos;
    private Preference customSourceNew;
    private Preference customSourceManager;
    private Preference customSourceHelper;

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
        new HitokotoSource("online", getString(R.string.app_name), getString(R.string.request_url), getString(R.string.Wait), 1)
                .saveOrUpdate("address = ?", getString(R.string.request_url));
        new HitokotoSource("local", getString(R.string.Local), "Local", getString(R.string.Wait), 3)
                .saveOrUpdate("address = ?", "Local");
//        HitokotoLocal hitokotoLocal = new HitokotoLocal();
//        hitokotoLocal.setGroup("unclassified");
//        hitokotoLocal.updateAll("group = ?", "");

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
        exportHitokotos = findPreference(getString(R.string.key_local_export_hitokotos));
        importHitokotos = findPreference(getString(R.string.key_local_import_hitokotos));
        customSourceNew = findPreference(getString(R.string.key_custom_source_new));
        customSourceManager = findPreference(getString(R.string.key_custom_source_manager));
        customSourceHelper = findPreference(getString(R.string.key_custom_source_helper));

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
                final List<HitokotoSource> list = DataSupport.findAll(HitokotoSource.class);
                //noinspection RestrictedApi
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
                @SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_test_source, null);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                Button button = (Button) view.findViewById(R.id.test);
                recyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
                final TestSourceAdapter adapter = new TestSourceAdapter(list);
                button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        for (int i = 0; i < list.size(); i++)
                        {
                            final int finalI = i;
                            TestSource.test(list.get(i), new TestSourceListener()
                            {
                                @Override
                                public void result(boolean result)
                                {
                                    if (result)
                                    {
                                        list.get(finalI).setEnable(getString(R.string.Enable));
                                    } else
                                    {
                                        list.get(finalI).setEnable(getString(R.string.Disable));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
                recyclerView.setAdapter(adapter);
                new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogStyle)
                        .setView(view)
                        .setTitle(R.string.text_test_source)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return false;
            }
        });
        showCrashLog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/hitokoto/log/");
                File[] logs = file.listFiles();
                File log = logs[logs.length - 1];
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //判断是否是AndroidN以及更高的版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    Uri contentUri = FileProvider.getUriForFile(
                            App.getContext(), "com.mystery0.hitokoto.fileProvider", log);
                    Logs.i(TAG, "onPreferenceClick: " + contentUri.getPath());
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(contentUri, "text/*");
                } else
                {
                    intent.setDataAndType(Uri.fromFile(log), "text/*");
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
                customSingleHitokotoDialog();
                return false;
            }
        });
        customMultipleHitokoto.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                customMultipleHitokotoDialog();
                return false;
            }
        });
        showCustomHitokoto.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                startActivity(new Intent(App.getContext(), LocalHitokotoManagerActivity.class));
                return false;
            }
        });
        exportHitokotos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                exportHitokotos();
                return false;
            }
        });
        importHitokotos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                importHitokotos();
                return false;
            }
        });
        customSourceNew.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                customSourceNewDialog();
                return false;
            }
        });
        customSourceManager.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                startActivity(new Intent(App.getContext(), CustomSourceActivity.class));
                return false;
            }
        });
        customSourceHelper.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                customSourceHelperDialog();
                return false;
            }
        });
    }

    private void customSingleHitokotoDialog()
    {
        //noinspection RestrictedApi
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_custom_single, null);
        final TextInputLayout hitokotoContent = (TextInputLayout) view.findViewById(R.id.custom_content);
        final TextInputLayout hitokotoSource = (TextInputLayout) view.findViewById(R.id.custom_source);
        check(hitokotoContent);
        check(hitokotoSource);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogStyle)
                .setView(view)
                .setTitle(R.string.text_custom_single_hitokoto)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null)
        {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (isFormat(hitokotoContent) & isFormat(hitokotoSource))
                    {
                        LocalConfigure.saveToDatabase(
                                hitokotoContent.getEditText().getText().toString(),
                                hitokotoSource.getEditText().getText().toString());
                        Logs.i(TAG, "onOptionsItemSelected: 存储");
                        Toast.makeText(App.getContext(), R.string.hint_save_custom_done, Toast.LENGTH_SHORT)
                                .show();
                    } else
                    {
                        Toast.makeText(App.getContext(), R.string.ErrorFormat, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }
    }

    private void customMultipleHitokotoDialog()
    {
        //noinspection RestrictedApi
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_custom_multiple, null);
        final TextInputLayout hitokotoContent = (TextInputLayout) view.findViewById(R.id.text);
        check(hitokotoContent);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogStyle)
                .setView(view)
                .setTitle(R.string.text_custom_multiple_hitokoto)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null)
        {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (isFormat(hitokotoContent))
                    {
                        LocalConfigure.saveToDatabase(Analysis(hitokotoContent.getEditText().getText().toString()));
                        Toast.makeText(App.getContext(), R.string.hint_save_custom_done, Toast.LENGTH_SHORT)
                                .show();
                    } else
                    {
                        Toast.makeText(App.getContext(), R.string.ErrorFormat, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }
    }

    private void importHitokotos()
    {

    }

    private void exportHitokotos()
    {

    }

    private void customSourceNewDialog()
    {
        final int[] method = new int[1];
        //noinspection RestrictedApi
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_custom_source_new, null);
        final TextInputLayout source_name = (TextInputLayout) view.findViewById(R.id.custom_source_name);
        final TextInputLayout source_address = (TextInputLayout) view.findViewById(R.id.custom_source_address);
        final TextInputLayout source_content = (TextInputLayout) view.findViewById(R.id.custom_source_content);
        final TextInputLayout source_from = (TextInputLayout) view.findViewById(R.id.custom_source_from);
        Button button = (Button) view.findViewById(R.id.test);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.list_request_method));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                method[0] = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HitokotoSource hitokotoSource = new HitokotoSource(
                        getResources().getStringArray(R.array.list_source_type)[2],
                        source_name.getEditText().getText().length() > 0 ? source_name.getEditText().getText().toString() : getString(R.string.Custom),
                        source_address.getEditText().getText().toString(),
                        getString(R.string.Wait),
                        method[0]
                );
                hitokotoSource.setContent_key(source_content.getEditText().getText().toString());
                hitokotoSource.setFrom_key(source_from.getEditText().getText().toString());
                TestSource.test(hitokotoSource, new TestSourceListener()
                {
                    @Override
                    public void result(boolean result)
                    {
                        source_address.setError(result ? getString(R.string.Enable) : getString(R.string.Disable));
                    }
                });
            }
        });
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogStyle)
                .setView(view)
                .setTitle(R.string.text_custom_source_new)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null)
        {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (isFormat(source_address) && isFormat(source_content) && isFormat(source_from))
                    {
                        final HitokotoSource hitokotoSource = new HitokotoSource(
                                getResources().getStringArray(R.array.list_source_type)[2],
                                source_name.getEditText().getText().length() > 0 ? source_name.getEditText().getText().toString() : getString(R.string.Custom),
                                source_address.getEditText().getText().toString(),
                                getString(R.string.Wait),
                                method[0]
                        );
                        hitokotoSource.setContent_key(source_content.getEditText().getText().toString());
                        hitokotoSource.setFrom_key(source_from.getEditText().getText().toString());
                        TestSource.test(hitokotoSource, new TestSourceListener()
                        {
                            @Override
                            public void result(boolean result)
                            {
                                source_address.setError(result ? getString(R.string.Enable) : getString(R.string.Disable));
                                Toast.makeText(App.getContext(),
                                        result ?
                                                (hitokotoSource.saveOrUpdate("address = ?", hitokotoSource.getAddress()) ?
                                                        getString(R.string.hint_custom_add_source_done) :
                                                        getString(R.string.hint_custom_add_source_error)) :
                                                getString(R.string.hint_invalid_source),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    } else
                    {
                        Toast.makeText(App.getContext(), getString(R.string.ErrorFormat), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }
    }

    private void customSourceHelperDialog()
    {//noinspection RestrictedApi
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_custom_source_helper, null);
        new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogStyle)
                .setView(view)
                .setTitle(R.string.text_custom_source_helper)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void check(final TextInputLayout layout)
    {
        layout.getEditText().addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                layout.setError(s.toString().length() == 0 ? getString(R.string.ErrorNull) : null);
            }
        });
    }

    private boolean isFormat(TextInputLayout layout)
    {
        layout.setError(layout.getEditText().getText().length() != 0 ? null : getString(R.string.ErrorNull));
        return layout.getEditText().getText().toString().trim().length() != 0;
    }

    private List<HitokotoLocal> Analysis(String content)
    {
        List<HitokotoLocal> list = new ArrayList<>();
        String time = (new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)).format(Calendar.getInstance().getTime());
        Scanner scanner = new Scanner(content);
        try
        {
            while (scanner.hasNext())
            {
                String[] temp = scanner.nextLine().split(" ");
                list.add(new HitokotoLocal(temp[0], temp[1], time));
            }
        } catch (ArrayIndexOutOfBoundsException e)
        {
            Logs.e(TAG, "Analysis: " + e.getMessage());
            Toast.makeText(App.getContext(), R.string.ErrorData, Toast.LENGTH_LONG)
                    .show();
        }
        scanner.close();
        return list;
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
