package com.mystery0.hitokoto.local;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoGroup;
import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.tools.Logs.Logs;

import org.litepal.crud.DataSupport;

import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class LocalHitokotoManagerActivity extends AppCompatActivity implements ManagerItemListener
{
    private static final String TAG = "LocalHitokotoManagerAct";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView null_data;
    private ManagerAdapter adapter;
    private List<HitokotoGroup> list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initialize();
        monitor();
    }

    private void initialize()
    {
        new HitokotoGroup("unclassified").saveOrUpdate("name = ?", "unclassified");
        list = DataSupport.findAll(HitokotoGroup.class);
        setContentView(R.layout.activity_custom_hitokoto);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        null_data = (TextView) findViewById(R.id.null_data);
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
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(0, RIGHT)
        {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
            {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                int position = viewHolder.getAdapterPosition();
                list.remove(position).delete();
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Logs.i(TAG, "onSwiped: 滑动删除");
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
                    list.addAll(DataSupport.where("name like ?", query+"%").find(HitokotoGroup.class));
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
                }else
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
