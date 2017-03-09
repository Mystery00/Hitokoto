package com.mystery0.hitokoto.widget;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class WidgetService extends Service
{
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            WidgetConfigure.refreshText();
            handler.postDelayed(runnable, WidgetConfigure.getRefreshTime());
        }
    };

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        handler.post(runnable);
        super.onCreate();
    }
}
