package com.mystery0.hitokoto.local;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoGroup;
import com.mystery0.tools.Logs.Logs;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LocalHitokotoManagerActivity extends AppCompatActivity implements ManagerItemListener
{
    private static final String TAG = "LocalHitokotoManagerAct";
    private Toolbar toolbar;
    private TextView null_data;
    private ManagerAdapter adapter;
    private List<HitokotoGroup> list;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initialize();
        monitor();
    }

    private void initialize()
    {
        list = DataSupport.findAll(HitokotoGroup.class);
        setContentView(R.layout.activity_custom_hitokoto);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        null_data = (TextView) findViewById(R.id.null_data);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        TextView textView = (TextView) findViewById(R.id.hint);
        textView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ManagerAdapter(list, this);
        recyclerView.setAdapter(adapter);
        if (list.size() == 0)
        {
            null_data.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(toolbar);
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
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //noinspection RestrictedApi
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
                @SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_custom_group, null);
                final TextInputLayout hitokotoGroup = (TextInputLayout) view.findViewById(R.id.group);
                new AlertDialog.Builder(LocalHitokotoManagerActivity.this, R.style.AlertDialogStyle)
                        .setView(view)
                        .setTitle(R.string.text_custom_multiple_hitokoto)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                        {
                            @SuppressWarnings("ConstantConditions")
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String temp = hitokotoGroup.getEditText().getText().toString();
                                if (temp.length() != 0)
                                {
                                    Logs.i(TAG, "onClick: " + new HitokotoGroup(temp).saveOrUpdate("name = ?", temp));
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.hitokoto_local, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Logs.i(TAG, "onQueryTextSubmit: " + query);
                if (query != null && !query.equals(""))
                {
                    list.clear();
                    list.addAll(DataSupport.where("name like ?", "%" + query + "%").find(HitokotoGroup.class));
                    adapter.notifyDataSetChanged();
                } else
                {
                    list.clear();
                    list.addAll(DataSupport.findAll(HitokotoGroup.class));
                    adapter.notifyDataSetChanged();
                }
                if (list.size() == 0)
                {
                    null_data.setVisibility(View.VISIBLE);
                } else
                {
                    null_data.setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onItemClick(final HitokotoGroup hitokotoGroup, final int position)
    {
        Intent intent = new Intent(App.getContext(), LocalHitokotoActivity.class);
        intent.putExtra("group", hitokotoGroup.getName());
        startActivity(intent);
    }
}
