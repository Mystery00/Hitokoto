package com.mystery0.hitokoto.custom;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.tools.Logs.Logs;

import org.litepal.crud.DataSupport;

import java.util.List;

public class CustomHitokotoActivity extends AppCompatActivity implements ShowItemListener
{
    private static final String TAG = "CustomHitokotoActivity";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initialize();
        monitor();


    }

    private void initialize()
    {
        List<HitokotoLocal> list = DataSupport.findAll(HitokotoLocal.class);
        setContentView(R.layout.activity_custom_hitokoto);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ShowAdapter adapter = new ShowAdapter(list, this);
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

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onItemClick(HitokotoLocal hitokotoLocal, int position)
    {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(App.getContext()).inflate(R.layout.dialog_edit_hitokoto, null);
        TextInputLayout content_layout = (TextInputLayout) view.findViewById(R.id.custom_content);
        TextInputLayout source_layout = (TextInputLayout) view.findViewById(R.id.custom_source);
        TextView textView = (TextView) findViewById(R.id.custom_date);
        content_layout.getEditText().setText(hitokotoLocal.getContent());
        source_layout.getEditText().setText(hitokotoLocal.getSource());
        textView.setText(hitokotoLocal.getDate());
        new AlertDialog.Builder(App.getContext())
                .setView(view)
                .setTitle("test")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Logs.i(TAG, "onClick: ");
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
