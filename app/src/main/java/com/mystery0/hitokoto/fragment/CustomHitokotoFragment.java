package com.mystery0.hitokoto.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.FileDo;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoGroup;
import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.hitokoto.class_class.Response;
import com.mystery0.hitokoto.local.DownloadActivity;
import com.mystery0.hitokoto.local.LocalConfigure;
import com.mystery0.hitokoto.local.LocalHitokotoManagerActivity;
import com.mystery0.hitokoto.local.LocalListener;
import com.mystery0.tools.Logs.Logs;
import com.mystery0.tools.MysteryNetFrameWork.HttpUtil;
import com.mystery0.tools.MysteryNetFrameWork.ResponseListener;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class CustomHitokotoFragment extends PreferenceFragment
{
	private static final String TAG = "CustomHitokotoFragment";
	private static final int REQUEST_IMPORT = 123;
	private Preference customSingleHitokoto;
	private Preference customMultipleHitokoto;
	private Preference showCustomHitokoto;
	private Preference exportHitokotos;
	private Preference importHitokotos;
	private Preference uploadHitokotos;
	private Preference downloadHitokotos;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initialization();
		monitor();
	}

	private void initialization()
	{
		addPreferencesFromResource(R.xml.custom_hitokoto_preferences);

		customSingleHitokoto = findPreference(getString(R.string.key_local_single_hitokoto));
		customMultipleHitokoto = findPreference(getString(R.string.key_local_multiple_hitokoto));
		showCustomHitokoto = findPreference(getString(R.string.key_local_show_hitokoto));
		exportHitokotos = findPreference(getString(R.string.key_local_export_hitokotos));
		importHitokotos = findPreference(getString(R.string.key_local_import_hitokotos));
		uploadHitokotos = findPreference(getString(R.string.key_local_upload_hitokotos));
		downloadHitokotos = findPreference(getString(R.string.key_local_download_hitokotos));
	}

	private void monitor()
	{
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
		uploadHitokotos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				uploadHitokotos();
				return false;
			}
		});
		downloadHitokotos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				startActivity(new Intent(getActivity(), DownloadActivity.class));
				return false;
			}
		});
	}

	private void customSingleHitokotoDialog()
	{
		//noinspection RestrictedApi
		@SuppressLint("RestrictedApi") ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle);
		@SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_custom_single, null);
		final TextInputLayout hitokotoContent = view.findViewById(R.id.custom_content);
		final TextInputLayout hitokotoSource = view.findViewById(R.id.custom_source);
		final Spinner spinner = view.findViewById(R.id.group);
		final String[] group = new String[]{getString(R.string.unclassified)};
		final List<HitokotoGroup> hitokotoGroups = DataSupport.findAll(HitokotoGroup.class);
		final List<String> list = new ArrayList<>();
		for (HitokotoGroup hitokotoGroup : hitokotoGroups)
		{
			list.add(hitokotoGroup.getName().equals(getString(R.string.unclassified)) ?
					getString(R.string.unclassified) : hitokotoGroup.getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				group[0] = list.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		check(hitokotoContent);
		check(hitokotoSource);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle)
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
								hitokotoSource.getEditText().getText().toString(),
								group[0]);
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
		@SuppressLint("RestrictedApi") ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle);
		@SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_custom_multiple, null);
		final TextInputLayout hitokotoContent = view.findViewById(R.id.text);
		final Spinner spinner = view.findViewById(R.id.group);
		final String[] group = new String[1];
		final List<HitokotoGroup> hitokotoGroups = DataSupport.findAll(HitokotoGroup.class);
		final List<String> list = new ArrayList<>();
		for (HitokotoGroup hitokotoGroup : hitokotoGroups)
		{
			list.add(hitokotoGroup.getName().equals(getString(R.string.unclassified)) ?
					getString(R.string.Unclassified) : hitokotoGroup.getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				group[0] = list.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		check(hitokotoContent);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle)
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
						LocalConfigure.saveToDatabase(Analysis(hitokotoContent.getEditText().getText().toString()), group[0]);
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

	private void exportHitokotos()
	{
		List<HitokotoGroup> groupList = DataSupport.findAll(HitokotoGroup.class);
		for (HitokotoGroup group : groupList)
		{
			LocalConfigure.outputFile(group.getName(), new LocalListener()
			{
				@Override
				public void done()
				{
				}

				@Override
				public void error()
				{
					Toast.makeText(App.getContext(), R.string.hint_export_error, Toast.LENGTH_SHORT)
							.show();
				}
			});
		}
		Logs.i(TAG, "exportHitokotos: 成功");
		Toast.makeText(App.getContext(), getString(R.string.hint_export_success) + "\n" + Environment.getExternalStorageDirectory().getPath() + "/hitokoto/", Toast.LENGTH_SHORT)
				.show();
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private void importHitokotos()
	{
		Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
		intent1.setType("*/*");
		intent1.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent1, REQUEST_IMPORT);
	}

	private void uploadHitokotos()
	{
		final String path = Environment.getExternalStorageDirectory().getPath() + "/hitokoto/";
		final List<HitokotoGroup> groupList = DataSupport.findAll(HitokotoGroup.class);
		final List<String> selectList = new ArrayList<>();
		final String[] strings = new String[groupList.size() - 1];
		boolean[] booleans = new boolean[groupList.size() - 1];
		int index = 0;
		for (int i = 0; i < groupList.size(); i++)
		{
			if (groupList.get(i).getName().equals(getString(R.string.unclassified)))
			{
				continue;
			}
			strings[index] = groupList.get(i).getName();
			booleans[index] = false;
			index++;
		}
		new AlertDialog.Builder(getActivity())
				.setTitle(getString(R.string.hint_choose_group))
				.setMultiChoiceItems(strings, booleans, new DialogInterface.OnMultiChoiceClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked)
					{
						if (isChecked)
						{
							selectList.add(strings[which]);
						} else
						{
							selectList.remove(strings[which]);
						}
					}
				})
				.setNegativeButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						for (final String fileName : selectList)
						{
							final ProgressDialog progressDialog = new ProgressDialog(getActivity());
							progressDialog.setTitle(getString(R.string.hint_upload_progress_title));
							progressDialog.setMessage(getString(R.string.hint_upload_progress_message) + " " + fileName);
							progressDialog.setMax(100);
							progressDialog.show();
							LocalConfigure.outputFile(fileName, new LocalListener()
							{
								@Override
								public void done()
								{
								}

								@Override
								public void error()
								{
									progressDialog.dismiss();
									Toast.makeText(App.getContext(), R.string.hint_export_error, Toast.LENGTH_SHORT)
											.show();
								}
							});
							File file = new File(path + fileName + ".txt");
							if (file.exists())
							{
								Map<String, String> stringMap = new HashMap<>();
								Map<String, File> fileMap = new HashMap<>();
								fileMap.put("upload_file", file);
								stringMap.put("method", "uploadFile");
								stringMap.put("group", fileName);
								new HttpUtil(App.getContext())
										.setRequestQueue(App.getRequestQueue())
										.setUrl(getString(R.string.file_request_url))
										.setRequestMethod(HttpUtil.RequestMethod.POST)
										.setFileRequest(HttpUtil.FileRequest.UPLOAD)
										.isFileRequest(true)
										.setMap(stringMap)
										.setFileMap(fileMap)
										.setResponseListener(new ResponseListener()
										{
											@Override
											public void onResponse(int i, String s)
											{
												final Gson gson = new Gson();
												Response response = gson.fromJson(s, Response.class);
												if (response.getCode() == 0)
												{
													Map<String, String> map = new HashMap<>();
													map.put("fileUrl", fileName + ".txt");
													map.put("method", "insert");
													map.put("group", fileName);
													map.put("model", Build.MODEL);
													map.put("vendor", Build.MANUFACTURER);
													map.put("OS_Version", Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT);
													new HttpUtil(App.getContext())
															.setRequestQueue(App.getRequestQueue())
															.setRequestMethod(HttpUtil.RequestMethod.POST)
															.setMap(map)
															.setUrl(getString(R.string.file_request_url))
															.setResponseListener(new ResponseListener()
															{
																@Override
																public void onResponse(int i, String s)
																{
																	progressDialog.dismiss();
																	Logs.i(TAG, "onResponse: " + s);
																	Response response2 = gson.fromJson(s, Response.class);
																	if (response2.getCode() == 0)
																	{
																		Toast.makeText(App.getContext(), R.string.hint_upload_done, Toast.LENGTH_SHORT)
																				.show();
																	} else
																	{
																		Toast.makeText(App.getContext(), response2.getContent(), Toast.LENGTH_SHORT)
																				.show();
																	}
																}
															})
															.open();
												} else
												{
													progressDialog.dismiss();
													Toast.makeText(App.getContext(), response.getContent(), Toast.LENGTH_SHORT)
															.show();
												}
											}
										})
										.open();
							}
						}
					}
				}).show();
	}

	@SuppressWarnings("ConstantConditions")
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

	@SuppressWarnings("ConstantConditions")
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case REQUEST_IMPORT:
				if (data != null)
				{
					String path = FileDo.getPath(data.getData());
					LocalConfigure.inputFile(path, new LocalListener()
					{
						@Override
						public void done()
						{
							Toast.makeText(App.getContext(), R.string.hint_import_success, Toast.LENGTH_SHORT)
									.show();
						}

						@Override
						public void error()
						{
							Toast.makeText(App.getContext(), R.string.hint_import_error, Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
				break;
		}
	}
}
