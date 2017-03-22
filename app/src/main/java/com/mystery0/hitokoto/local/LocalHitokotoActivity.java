package com.mystery0.hitokoto.local;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
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
    private List<HitokotoLocal> list;
    private RecyclerView recyclerView;
    private ShowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initialize();
        monitor();


    }

    private void initialize()
    {
        list = DataSupport.findAll(HitokotoLocal.class);
        setContentView(R.layout.activity_custom_hitokoto);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShowAdapter(list, this);
        recyclerView.setAdapter(adapter);

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
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onItemClick(final HitokotoLocal hitokotoLocal, final int position)
    {
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
