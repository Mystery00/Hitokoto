package com.mystery0.hitokoto;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mystery0.hitokoto.widget.WidgetConfigure;
import com.mystery0.tools.CrashHandler.CrashHandler;
import com.mystery0.tools.Logs.Logs;

import org.litepal.LitePal;

import java.util.HashSet;
import java.util.Set;


public class App extends Application
{
	private static Context context;
	private static Set<Integer> idsSet = new HashSet<>();
	private static RequestQueue requestQueue;
	private static SharedPreferences sharedPreferences;
	private static WidgetConfigure widgetConfigure;

	@Override
	public void onCreate()
	{
		super.onCreate();
		context = getApplicationContext();
		sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE);
		widgetConfigure=new WidgetConfigure(getApplicationContext());
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		Logs.setLevel(Logs.LogLevel.Release);
		LitePal.initialize(this);
		CrashHandler.getInstance(this)
				.isAutoClean(true)
				.setExtensionName("log")
				.setDirectory("hitokoto/log")
				.init();
	}

	public static Context getContext()
	{
		return context;
	}

	public static WidgetConfigure getWidgetConfigure()
	{
		return widgetConfigure;
	}

	public static RequestQueue getRequestQueue()
	{
		return requestQueue;
	}

	public static void addID(Integer integer)
	{
		idsSet.add(integer);
		Set<String> saveList = new HashSet<>();
		for (Integer id : idsSet)
		{
			saveList.add(String.valueOf(id));
		}
		sharedPreferences.edit().remove("ids").putStringSet("ids", saveList).apply();
	}

	public static void removeID(Integer integer)
	{
		idsSet.remove(integer);
		Set<String> saveList = new HashSet<>();
		for (Integer id : idsSet)
		{
			saveList.add(String.valueOf(id));
		}
		sharedPreferences.edit().remove("ids").putStringSet("ids", saveList).apply();
	}

	public static Set<Integer> getIdsSet()
	{
		StringBuilder message = new StringBuilder("存储的id：");
		Set<String> saveListTemp = sharedPreferences.getStringSet("ids", new HashSet<String>());
		Set<Integer> saveList = new HashSet<>();
		for (String temp : saveListTemp)
		{
			message.append(temp).append(" ");
			saveList.add(Integer.parseInt(temp));
		}//将StringSet转换成IntegerSet

		message.append("\n想要更新的id：");
		for (Integer temp : idsSet)
		{
			message.append(temp).append(" ");
		}

		if (!idsSet.containsAll(saveList))
		{
			message.append("\nid不匹配，尝试修复。");
			idsSet.clear();
			idsSet.addAll(saveList);
		}
		if (widgetConfigure.getDebuggable())
		{
			FileTest.writeLog(message.toString());
		}
		return idsSet;
	}
}
