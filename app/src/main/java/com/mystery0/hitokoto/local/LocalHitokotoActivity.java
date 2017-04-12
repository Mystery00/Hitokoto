package com.mystery0.hitokoto.local;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.tools.Logs.Logs;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class LocalHitokotoActivity extends AppCompatActivity implements ShowItemListener
{
    private static final String TAG = "LocalHitokotoActivity";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView null_data;
    private ShowAdapter adapter;
    private List<HitokotoLocal> list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initialize();
        monitor();
    }

    private void initialize()
    {
        String group = getIntent().getStringExtra("group");
        list = DataSupport.where("group = ?", group).find(HitokotoLocal.class);
        setContentView(R.layout.activity_custom_hitokoto);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        null_data = (TextView) findViewById(R.id.null_data);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShowAdapter(list, this);
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
                if (query != null && query.length() != 0)
                {
                    list.clear();
                    list.addAll(DataSupport.where("content like ? or source like ?", "%" + query + "%", "%" + query + "%").find(HitokotoLocal.class));
                    adapter.notifyDataSetChanged();
                } else
                {
                    list.clear();
                    list.addAll(DataSupport.findAll(HitokotoLocal.class));
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
                Logs.i(TAG, "onQueryTextChange: " + newText);
                if (newText.length() == 0)
                {
                    list.clear();
                    list.addAll(DataSupport.findAll(HitokotoLocal.class));
                    adapter.notifyDataSetChanged();
                }
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
    public void onItemClick(final HitokotoLocal hitokotoLocal, final int position)
    {
        //noinspection RestrictedApi
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_edit_hitokoto, null);
        final TextInputLayout content_layout = (TextInputLayout) view.findViewById(R.id.custom_content);
        final TextInputLayout source_layout = (TextInputLayout) view.findViewById(R.id.custom_source);
        TextView text_date = (TextView) view.findViewById(R.id.custom_date);
        content_layout.getEditText().setText(hitokotoLocal.getContent());
        source_layout.getEditText().setText(hitokotoLocal.getSource());
        text_date.setText(hitokotoLocal.getDate());
        new AlertDialog.Builder(LocalHitokotoActivity.this, R.style.AlertDialogStyle)
                .setView(view)
                .setTitle(R.string.hint_title_edit_hitokoto)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String time = (new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)).format(Calendar.getInstance().getTime());
                        hitokotoLocal.setContent(content_layout.getEditText().getText().toString());
                        hitokotoLocal.setSource(source_layout.getEditText().getText().toString());
                        hitokotoLocal.setDate(time);
                        boolean result = hitokotoLocal.save();
                        list = DataSupport.findAll(HitokotoLocal.class);
                        adapter.notifyItemChanged(position);
                        Logs.i(TAG, "onClick: " + result);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
