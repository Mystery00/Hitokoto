package com.mystery0.hitokoto.local;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.ShareFile;
import com.mystery0.tools.Logs.Logs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity implements DownloadItemListener
{
    private static final String TAG = "DownloadActivity";
    private List<ShareFile> list = new ArrayList<>();
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View coordinatorLayout;
    private TextView null_data;
    private DownloadAdapter adapter;

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
//        BmobQuery<ShareFile> query = new BmobQuery<>();
//        query.setLimit(50);
//        query.findObjects(new FindListener<ShareFile>()
//        {
//            @Override
//            public void done(List<ShareFile> list, BmobException e)
//            {
//                swipeRefreshLayout.setRefreshing(false);
//                if (e == null)
//                {
//                    Logs.i(TAG, "done: 获取到列表");
//                    DownloadActivity.this.list.clear();
//                    DownloadActivity.this.list.addAll(list);
//                    adapter.notifyDataSetChanged();
//                    null_data.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
//                    Snackbar.make(coordinatorLayout, R.string.hint_refresh_done, Snackbar.LENGTH_SHORT)
//                            .show();
//                } else
//                {
//                    Logs.e(TAG, "done: " + e.getMessage());
//                    Snackbar.make(coordinatorLayout, e.getErrorCode() == 9016 || e.getErrorCode() == 404 ? getString(R.string.hint_error_network) : e.getMessage(), Snackbar.LENGTH_SHORT)
//                            .show();
//                }
//            }
//        });
    }

    @Override
    public void onItemClick(final ShareFile shareFile, int position)
    {
        final String path = Environment.getExternalStorageDirectory().getPath() + "/hitokoto/";
//        final String filePath = path + shareFile.getGroup() + ".txt";
//        Logs.i(TAG, "onItemClick: " + shareFile.getGroup());
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
                        progressDialog.setMax(100);
                        progressDialog.show();
//                        BmobFile bmobFile = new BmobFile(shareFile.getGroup(), null, shareFile.getBmobFile().getUrl());
//                        bmobFile.download(new File(filePath), new DownloadFileListener()
//                        {
//                            @Override
//                            public void done(String s, BmobException e)
//                            {
//                                if (e == null)
//                                {
//                                    LocalConfigure.inputFile(filePath, new LocalListener()
//                                    {
//                                        @Override
//                                        public void done()
//                                        {
//                                            progressDialog.dismiss();
//                                            Snackbar.make(coordinatorLayout, R.string.hint_download_done, Snackbar.LENGTH_SHORT)
//                                                    .show();
//                                        }
//
//                                        @Override
//                                        public void error()
//                                        {
//                                            progressDialog.dismiss();
//                                            Snackbar.make(coordinatorLayout, R.string.hint_import_error, Snackbar.LENGTH_SHORT)
//                                                    .show();
//                                        }
//                                    });
//                                } else
//                                {
//                                    progressDialog.dismiss();
//                                    Logs.e(TAG, "done: " + e.getMessage());
//                                    Snackbar.make(coordinatorLayout, e.getErrorCode() == 9016 || e.getErrorCode() == 404 ? getString(R.string.hint_error_network) : e.getMessage(), Snackbar.LENGTH_SHORT)
//                                            .show();
//                                }
//                            }
//
//                            @Override
//                            public void onProgress(Integer integer, long l)
//                            {
//                                Logs.i(TAG, "onProgress: " + integer + "," + l);
//                                progressDialog.setProgress(integer);
//                                progressDialog.setMessage(integer + "%\n" + getString(R.string.hint_download_progress_message_hint) + l);
//                            }
//                        });
                    }
                })
                .show();
    }
}
