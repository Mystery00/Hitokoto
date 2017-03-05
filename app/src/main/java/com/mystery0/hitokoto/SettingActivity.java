package com.mystery0.hitokoto;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class SettingActivity extends AppCompatActivity
{
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content);
            switch (item.getItemId())
            {
                case R.id.navigation_settings:
                    frameLayout.removeAllViews();
                    frameLayout.addView(View.inflate(SettingActivity.this, R.layout.content_recycler_view, null));
                    setFrameLayoutRecycler(frameLayout);
                    return true;
                case R.id.navigation_custom_text:
                    return true;
                case R.id.navigation_custom_source:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setFrameLayoutRecycler(View rootView)
    {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.settings_list);

    }

}
