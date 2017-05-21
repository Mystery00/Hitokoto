package com.mystery0.hitokoto;

import android.app.Application;
import android.content.Context;

import com.mystery0.tools.CrashHandler.CrashHandler;
import com.mystery0.tools.Logs.Logs;

import org.litepal.LitePal;


public class App extends Application
{
    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
        Logs.setLevel(Logs.LogLevel.Release);
        LitePal.initialize(this);
        CrashHandler.getInstance()
                .setDirectory("hitokoto/log")
                .init(this);
//        Bmob.initialize(context, "4d1df6dda88d30cc086908471a6664c8");
    }

    public static Context getContext()
    {
        return context;
    }
}
