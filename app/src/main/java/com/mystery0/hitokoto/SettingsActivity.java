package com.mystery0.hitokoto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mystery0.hitokoto.fragment.MainFragment;

@SuppressLint("ExportedPreferenceActivity")
public class SettingsActivity extends PreferenceActivity
{
	private static final int REQUEST_PERMISSION = 456;
	private Toolbar toolbar;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		CheckPermission();
		toolbar.setTitle(getTitle());
		if (savedInstanceState == null)
		{
			getFragmentManager()
					.beginTransaction().add(R.id.content_wrapper, new MainFragment())
					.commit();
		}
		if (App.getWidgetConfigure().isFirstRun())
		{
			new AlertDialog.Builder(SettingsActivity.this)
					.setTitle(" ")
					.setView(LayoutInflater.from(SettingsActivity.this).inflate(R.layout.dialog_how_to_use, null))
					.setPositiveButton(android.R.string.ok, null)
					.setOnDismissListener(new DialogInterface.OnDismissListener()
					{
						@Override
						public void onDismiss(DialogInterface dialogInterface)
						{
							App.getWidgetConfigure().setFirstRun();
						}
					})
					.show();
		}
	}

	@Override
	public void setContentView(int layoutResID)
	{
		ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_settings, new LinearLayout(this), false);
		toolbar = contentView.findViewById(R.id.toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				onBackPressed();
			}
		});

		ViewGroup contentWrapper = contentView.findViewById(R.id.content_wrapper);
		LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);

		getWindow().setContentView(contentView);
	}

	@Override
	public void onBackPressed()
	{
		// this if statement is necessary to navigate through nested and main fragments
		if (getFragmentManager().getBackStackEntryCount() == 0)
		{
			super.onBackPressed();
		} else
		{
			if (getFragmentManager().getBackStackEntryCount()==1)
			{
				toolbar.setNavigationIcon(null);
			}
			getFragmentManager().popBackStack();
		}
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
}
