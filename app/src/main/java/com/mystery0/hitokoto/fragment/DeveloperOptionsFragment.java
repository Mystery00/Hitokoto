package com.mystery0.hitokoto.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;

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
		isDebuggable.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object o)
			{
				boolean debuggable=!isDebuggable.isChecked();
				if (debuggable)
				{
					new AlertDialog.Builder(getActivity())
							.setTitle("Warning")
							.setMessage(R.string.summary_debug)
							.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialogInterface, int i)
								{
									App.getWidgetConfigure().setDebuggable(true);
								}
							})
							.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialogInterface, int i)
								{
									App.getWidgetConfigure().setDebuggable(false);
									isDebuggable.setChecked(false);
								}
							})
							.setOnDismissListener(new DialogInterface.OnDismissListener()
							{
								@Override
								public void onDismiss(DialogInterface dialogInterface)
								{
									isDebuggable.setChecked(App.getWidgetConfigure().getDebuggable());
								}
							})
							.show();
				}
				return true;
			}
		});
	}
}
