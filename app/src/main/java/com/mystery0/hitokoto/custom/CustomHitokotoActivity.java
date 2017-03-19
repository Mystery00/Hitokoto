package com.mystery0.hitokoto.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoLocal;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CustomHitokotoActivity extends AppCompatActivity implements ShowItemListener
{
    private List<HitokotoLocal> list = new ArrayList<>();
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
        list = DataSupport.findAll(HitokotoLocal.class);
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

    @Override
    public void onItemClick(HitokotoLocal hitokotoLocal, int position)
    {

    }
}
