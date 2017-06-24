package com.mystery0.hitokoto.fragment;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoGroup;
import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.hitokoto.class_class.HitokotoSource;
import com.mystery0.hitokoto.test_source.TestSource;
import com.mystery0.hitokoto.test_source.TestSourceAdapter;
import com.mystery0.hitokoto.test_source.TestSourceListener;
import com.mystery0.hitokoto.widget.WidgetConfigure;
import com.mystery0.tools.Logs.Logs;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
	private static final String TAG = "MainFragment";
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
	private Preference customHitokoto;
	private Preference customSource;
	private Preference about;
	private Preference developerOptions;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initialization();
		monitor();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	private void initialization()
	{
		addPreferencesFromResource(R.xml.perferences);
		new HitokotoSource("online", getString(R.string.app_name), getString(R.string.request_url), getString(R.string.Wait), 1)
				.saveOrUpdate("address = ?", getString(R.string.request_url));
		new HitokotoSource("local", getString(R.string.Local), "Local", getString(R.string.Wait), 3)
				.saveOrUpdate("address = ?", "Local");
		new HitokotoGroup(getString(R.string.unclassified))
				.saveOrUpdate("name = ?", getString(R.string.unclassified));
		List<HitokotoLocal> list = DataSupport.findAll(HitokotoLocal.class);
		for (HitokotoLocal temp : list)
		{
			if (temp.getGroup() == null || temp.getGroup().equals(""))
			{
				temp.setGroup("unclassified");
				temp.saveOrUpdate("content = ?", temp.getContent());
			}
		}

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
		customHitokoto = findPreference(getString(R.string.key_local_hitokoto));
		customSource = findPreference(getString(R.string.key_custom_source));
		about = findPreference(getString(R.string.key_about));
		developerOptions = findPreference(getString(R.string.key_developer_options));

		autoRefresh.setChecked(App.getWidgetConfigure().getAutoRefresh());
		clickToRefresh.setChecked(App.getWidgetConfigure().getClickToRefresh());
		notShowSource.setChecked(App.getWidgetConfigure().getNotShowSource());
		textAligned.setSummary(getResources().getStringArray(R.array.list_aligned)[App.getWidgetConfigure().getTextAligned()]);
		textAligned.setValueIndex(App.getWidgetConfigure().getTextAligned());
		setTextColor.setSummary(App.getWidgetConfigure().getTextColor());
		setTextColor.setDefaultValue(Color.parseColor(App.getWidgetConfigure().getTextColor()));
		chooseSource.setValues(App.getWidgetConfigure().getChooseSource(WidgetConfigure.SourceType.STRING));
		setTextPadding.setSummary("" + App.getWidgetConfigure().getTextPadding());
		setTextPadding.setDefaultValue(App.getWidgetConfigure().getTextPadding());
		setRefreshTime.setSummary("" + App.getWidgetConfigure().getRefreshTime());
		setRefreshTime.setDefaultValue(App.getWidgetConfigure().getRefreshTime());
		textSize.setSummary("" + App.getWidgetConfigure().getTextSize());
		textSize.setDefaultValue(App.getWidgetConfigure().getTextSize());
		PreferenceManager.setDefaultValues(getActivity(), R.xml.perferences, false);
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
				App.getWidgetConfigure().setTextColor(temp);
				if (App.getWidgetConfigure().getEnable())
				{
					App.getWidgetConfigure().refreshText();
				}
				return true;
			}
		});
		refreshNow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				if (App.getWidgetConfigure().getEnable())
				{
					App.getWidgetConfigure().refreshText();
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
				@SuppressLint("RestrictedApi") ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
				@SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_show_currect, null);
				TextView content = view.findViewById(R.id.content);
				TextView source = view.findViewById(R.id.source);
				setOnClick(content);
				setOnClick(source);
				String[] temp = App.getWidgetConfigure().getTemp();
				content.setText(temp[0]);
				source.setText(temp[1]);
				new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle)
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
				@SuppressLint("RestrictedApi") ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
				@SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_test_source, null);
				RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
				Button button = view.findViewById(R.id.test);
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
				new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle)
						.setView(view)
						.setTitle(R.string.text_test_source)
						.setPositiveButton(android.R.string.ok, null)
						.show();
				return false;
			}
		});
		customHitokoto.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.content_wrapper, new CustomHitokotoFragment(), getString(R.string.app_name))
						.addToBackStack(getString(R.string.app_name))
						.commit();
				Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
				toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
				return false;
			}
		});
		customSource.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.content_wrapper, new CustomSourceFragment(), getString(R.string.app_name))
						.addToBackStack(getString(R.string.app_name))
						.commit();
				Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
				toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
				return false;
			}
		});
		about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.content_wrapper, new AboutFragment(), getString(R.string.app_name))
						.addToBackStack(getString(R.string.app_name))
						.commit();
				Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
				toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
				return false;
			}
		});
		developerOptions.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.content_wrapper, new DeveloperOptionsFragment(), getString(R.string.app_name))
						.addToBackStack(getString(R.string.app_name))
						.commit();
				Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
				toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
				return false;
			}
		});
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		Logs.i(TAG, "onSharedPreferenceChanged: " + key);
		if (key.equals(getString(R.string.key_choose_source)))
		{
			App.getWidgetConfigure().setChooseSource(chooseSource.getValues());
		} else if (key.equals(getString(R.string.key_auto_refresh)))
		{
			App.getWidgetConfigure().setAutoRefresh(autoRefresh.isChecked());
		} else if (key.equals(getString(R.string.key_click_to_refresh)))
		{
			App.getWidgetConfigure().setClickToRefresh(clickToRefresh.isChecked());
		} else if (key.equals(getString(R.string.key_text_aligned)))
		{
			App.getWidgetConfigure().setTextAligned(textAligned.findIndexOfValue(textAligned.getValue()));
			textAligned.setSummary(getResources().getStringArray(R.array.list_aligned)[App.getWidgetConfigure().getTextAligned()]);
		} else if (key.equals(getString(R.string.key_text_size)))
		{
			App.getWidgetConfigure().setTextSize(Integer.parseInt("0" + textSize.getEditText().getText().toString()));
			textSize.setSummary("" + App.getWidgetConfigure().getTextSize());
		} else if (key.equals(getString(R.string.key_not_show_source)))
		{
			App.getWidgetConfigure().setNotShowSource(notShowSource.isChecked());
		} else if (key.equals(getString(R.string.key_set_refresh_time)))
		{
			App.getWidgetConfigure().setRefreshTime(Integer.parseInt("0" + setRefreshTime.getEditText().getText().toString()));
			setRefreshTime.setSummary("" + App.getWidgetConfigure().getRefreshTime());
		} else if (key.equals(getString(R.string.key_set_text_padding)))
		{
			App.getWidgetConfigure().setTextPadding(Integer.parseInt("0" + setTextPadding.getEditText().getText().toString()));
			setTextPadding.setSummary("" + App.getWidgetConfigure().getTextPadding());
		}
		if (App.getWidgetConfigure().getEnable())
		{
			App.getWidgetConfigure().refreshText();
		}
	}

	private void setOnClick(final TextView view)
	{
		view.setOnClickListener(new View.OnClickListener()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v)
			{
				Logs.i(TAG, "onClick: 拷贝到剪切板");
				ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
				clipboardManager.setText(view.getText().toString());
				Toast.makeText(App.getContext(), R.string.hint_copy_text, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
}
