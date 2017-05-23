package com.mystery0.hitokoto;

import android.app.Application;
import android.content.Context;

import com.mystery0.tools.CrashHandler.CrashHandler;
import com.mystery0.tools.Logs.Logs;

import org.litepal.LitePal;

import java.util.HashSet;
import java.util.Set;


public class App extends Application
{
	private static Context context;
	private static Set<Integer> idsSet = new HashSet<>();

	@Override
	public void onCreate()
	{
		super.onCreate();
		context = getApplicationContext();
		Logs.setLevel(Logs.LogLevel.Debug);
		LitePal.initialize(this);
		CrashHandler.getInstance()
				.setDirectory("hitokoto/log")
				.init(this);
	}

	public static Context getContext()
	{
		return context;
	}

	public static void addID(Integer integer)
	{
		idsSet.add(integer);
	}

	public static void removeID(Integer integer)
	{
		idsSet.remove(integer);
	}

	public static Set<Integer> getIdsSet()
	{
		return idsSet;
	}
}
