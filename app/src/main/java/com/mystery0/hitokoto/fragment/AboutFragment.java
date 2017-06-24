package com.mystery0.hitokoto.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.tools.Logs.Logs;

import java.io.File;

public class AboutFragment extends PreferenceFragment
{
	private static final String TAG = "AboutFragment";
	private Preference showCrashLog;
	private Preference contactMe;
	private Preference howToUse;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initialization();
		monitor();
	}

	private void initialization()
	{
		addPreferencesFromResource(R.xml.about_preferences);

		showCrashLog = findPreference(getString(R.string.key_show_crash_log));
		contactMe = findPreference(getString(R.string.key_contact_me));
		howToUse = findPreference(getString(R.string.key_how_to_use));
	}

	private void monitor()
	{
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
			@SuppressWarnings("deprecation")
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				Logs.i(TAG, "onPreferenceClick: 拷贝到剪切板");
				ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
				clipboardManager.setText(getString(R.string.e_mail_address));
				Toast.makeText(App.getContext(), R.string.hint_copy_address, Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});
		howToUse.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				new AlertDialog.Builder(getActivity())
						.setTitle(" ")
						.setView(LayoutInflater.from(getActivity()).inflate(R.layout.dialog_how_to_use, null))
						.setPositiveButton(android.R.string.ok, null)
						.show();
				return false;
			}
		});
	}
}
