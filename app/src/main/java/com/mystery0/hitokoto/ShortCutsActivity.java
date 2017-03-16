package com.mystery0.hitokoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mystery0.hitokoto.widget.OnClickService;
import com.mystery0.hitokoto.widget.WidgetConfigure;

public class ShortCutsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (WidgetConfigure.getEnable())
        {
            startService(new Intent(App.getContext(), OnClickService.class));
            Toast.makeText(App.getContext(), R.string.hint_broadcast, Toast.LENGTH_SHORT)
                    .show();
        } else
        {
            Toast.makeText(App.getContext(), R.string.hint_add_widget, Toast.LENGTH_SHORT)
                    .show();
        }
        finish();
    }
}
