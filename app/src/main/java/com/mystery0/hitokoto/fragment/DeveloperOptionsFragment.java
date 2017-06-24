package com.mystery0.hitokoto.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;

public class DeveloperOptionsFragment extends PreferenceFragment
{
	private SwitchPreference isDebuggable;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initialization();
		monitor();
	}

	private void initialization()
	{
		addPreferencesFromResource(R.xml.developer_options_preferences);

		isDebuggable = (SwitchPreference) findPreference(getString(R.string.key_debug));
		isDebuggable.setChecked(App.getWidgetConfigure().getDebuggable());
	}

	private void monitor()
	{

	}
}
