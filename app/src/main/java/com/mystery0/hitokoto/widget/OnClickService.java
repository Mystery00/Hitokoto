package com.mystery0.hitokoto.widget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.mystery0.hitokoto.App;

public class OnClickService extends Service
{
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        App.getWidgetConfigure().refreshText();
        stopService(new Intent(App.getContext(), OnClickService.class));
    }
}
