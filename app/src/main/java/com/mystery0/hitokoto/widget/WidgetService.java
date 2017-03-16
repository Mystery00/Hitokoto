package com.mystery0.hitokoto.widget;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.mystery0.hitokoto.App;

public class WidgetService extends Service
{
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            WidgetConfigure.refreshText();
            if (WidgetConfigure.getAutoRefresh())
            {
                handler.postDelayed(runnable, WidgetConfigure.getRefreshTime() * 60000);
            } else
            {
                stopService(new Intent(App.getContext(), WidgetService.class));
            }
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
