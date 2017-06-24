package com.mystery0.hitokoto.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoSource;
import com.mystery0.hitokoto.util.TestSource;
import com.mystery0.hitokoto.listener.TestSourceListener;

public class CustomSourceFragment extends PreferenceFragment
{
	private Preference customSourceNew;
	private Preference customSourceManager;
	private Preference customSourceHelper;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initialization();
		monitor();
	}

	private void initialization()
	{
		addPreferencesFromResource(R.xml.custom_source_preferences);

		customSourceNew = findPreference(getString(R.string.key_custom_source_new));
		customSourceManager = findPreference(getString(R.string.key_custom_source_manager));
		customSourceHelper = findPreference(getString(R.string.key_custom_source_helper));
	}

	private void monitor()
	{
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
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.content_wrapper,new CustomSourceManagerFragment(),getString(R.string.app_name))
						.addToBackStack(getString(R.string.app_name))
						.commit();
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

	private void customSourceNewDialog()
	{
		final int[] method = new int[1];
		//noinspection RestrictedApi
		@SuppressLint("RestrictedApi") ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
		@SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_custom_source_new, null);
		final TextInputLayout source_name = view.findViewById(R.id.custom_source_name);
		final TextInputLayout source_address = view.findViewById(R.id.custom_source_address);
		final TextInputLayout source_content = view.findViewById(R.id.custom_source_content);
		final TextInputLayout source_from = view.findViewById(R.id.custom_source_from);
		Button button = view.findViewById(R.id.test);
		Spinner spinner = view.findViewById(R.id.spinner);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.list_request_method));
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
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle)
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
		@SuppressLint("RestrictedApi") ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
		@SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_custom_source_helper, null);
		new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle)
				.setView(view)
				.setTitle(R.string.text_custom_source_helper)
				.setPositiveButton(android.R.string.ok, null)
				.show();
	}

	@SuppressWarnings("ConstantConditions")
	private boolean isFormat(TextInputLayout layout)
	{
		layout.setError(layout.getEditText().getText().length() != 0 ? null : getString(R.string.ErrorNull));
		return layout.getEditText().getText().toString().trim().length() != 0;
	}
}
