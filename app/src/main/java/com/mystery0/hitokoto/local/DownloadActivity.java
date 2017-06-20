package com.mystery0.hitokoto.local;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.DownloadList;
import com.mystery0.hitokoto.class_class.ShareFile;
import com.mystery0.tools.Logs.Logs;
import com.mystery0.tools.MysteryNetFrameWork.FileResponseListener;
import com.mystery0.tools.MysteryNetFrameWork.HttpUtil;
import com.mystery0.tools.MysteryNetFrameWork.ResponseListener;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadActivity extends AppCompatActivity implements DownloadItemListener
{
	private static final String TAG = "DownloadActivity";
	private List<ShareFile> list = new ArrayList<>();
	private Toolbar toolbar;
	private SwipeRefreshLayout swipeRefreshLayout;
	private View coordinatorLayout;
	private TextView null_data;
	private DownloadAdapter adapter;
	private Gson gson = new Gson();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initialize();
		monitor();
	}

	private void initialize()
	{
		setContentView(R.layout.activity_download);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
		coordinatorLayout = findViewById(R.id.coordinatorLayout);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		null_data = (TextView) findViewById(R.id.null_data);

		swipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new DownloadAdapter(list, this);
		recyclerView.setAdapter(adapter);

		setSupportActionBar(toolbar);
		swipeRefreshLayout.setRefreshing(true);
		refresh();
		null_data.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
	}

	private void monitor()
	{
		//noinspection ConstantConditions
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				refresh();
			}
		});
	}

	private void refresh()
	{
		Logs.i(TAG, "onRefresh: 刷新");
		Map<String, String> stringMap = new HashMap<>();
		stringMap.put("method", "getList");
		new HttpUtil(DownloadActivity.this)
				.setUrl(getString(R.string.file_request_url))
				.setRequestMethod(HttpUtil.RequestMethod.POST)
				.setMap(stringMap)
				.setResponseListener(new ResponseListener()
				{
					@Override
					public void onResponse(int code, @Nullable String message)
					{
						swipeRefreshLayout.setRefreshing(false);
						if (code == 1)
						{
							DownloadList downloadList = gson.fromJson(message, DownloadList.class);
							Logs.i(TAG, "done: 获取到列表");
							list.clear();
							Collections.addAll(list, downloadList.getFiles());
							adapter.notifyDataSetChanged();
							null_data.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
							Snackbar.make(coordinatorLayout, R.string.hint_refresh_done, Snackbar.LENGTH_SHORT)
									.show();
						} else
						{
							Logs.e(TAG, "done: " + message);
							Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT)
									.show();
						}
					}
				})
				.open();
	}

	@Override
	public void onItemClick(final ShareFile shareFile, int position)
	{
		final String path = Environment.getExternalStorageDirectory().getPath() + "/hitokoto/";
		final String filePath = path + shareFile.getFileUrl();
		Logs.i(TAG, "onItemClick: " + shareFile.getGroup());
		new AlertDialog.Builder(DownloadActivity.this)
				.setMessage(getString(R.string.hint_download_sure))
				.setNegativeButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						final ProgressDialog progressDialog = new ProgressDialog(DownloadActivity.this);
						progressDialog.setTitle(getString(R.string.hint_download_progress_message));
						progressDialog.setMessage(getString(R.string.hint_download_progress_message));
						progressDialog.setCancelable(false);
						progressDialog.show();
						String url = Uri.encode(("http://123.206.186.70/uploads/" + shareFile.getFileUrl()), "-![.:/,%?&=]");
						new HttpUtil(DownloadActivity.this)
								.setUrl(url)
								.isFileRequest(true)
								.setFileRequest(HttpUtil.FileRequest.DOWNLOAD)
								.setDownloadFilePath(path)
								.setDownloadFileName(shareFile.getFileUrl())
								.setFileResponseListener(new FileResponseListener()
								{
									@Override
									public void onResponse(int code, @Nullable File file, @Nullable String message)
									{
										progressDialog.dismiss();
										if (code == 1)
										{
											LocalConfigure.inputFile(filePath, new LocalListener()
											{
												@Override
												public void done()
												{
													progressDialog.dismiss();
													Snackbar.make(coordinatorLayout, R.string.hint_download_done, Snackbar.LENGTH_SHORT)
															.show();
												}

												@Override
												public void error()
												{
													progressDialog.dismiss();
													Snackbar.make(coordinatorLayout, R.string.hint_import_error, Snackbar.LENGTH_SHORT)
															.show();
												}
											});
										} else
										{
											progressDialog.dismiss();
											Logs.e(TAG, "done: " + message);
											Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT)
													.show();
										}
									}
								})
								.open();
					}
				})
				.show();
	}
}
