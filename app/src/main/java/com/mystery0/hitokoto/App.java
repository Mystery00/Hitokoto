package com.mystery0.hitokoto;

import android.app.Application;
import android.content.Context;

import com.mystery0.tools.CrashHandler.CrashHandler;
import com.mystery0.tools.Logs.Logs;

public class App extends Application
{
    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
        Logs.setLevel(Logs.LogLevel.Release);
        CrashHandler.getInstance()
                .setDirectory("hitokoto/log")
                .init(this);
    }

    public static Context getContext()
    {
        return context;
    }
}
