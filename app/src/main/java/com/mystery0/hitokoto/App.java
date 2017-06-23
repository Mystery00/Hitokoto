package com.mystery0.hitokoto;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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

	@Override
	public void onCreate()
	{
		super.onCreate();
		context = getApplicationContext();
		sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE);
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
		Set<String> saveList=sharedPreferences.getStringSet("ids",new HashSet<String>());
		Set<Integer> newIdList=new HashSet<>();
		for (Integer integer : idsSet)
		{
			for (String temp:saveList)
			{
				if (temp.equals(String.valueOf(integer)))
				{
					newIdList.add(integer);
					break;
				}
			}
		}

		String message = "\n存储的id：" + sharedPreferences.getString("id", "null") + "\n" + "想要更新的id：" + string.toString();
		if (!sharedPreferences.getString("id", "null").equals(String.valueOf(id)))
		{
			message += "\nid不同，尝试修正。";
			idsSet.clear();
			idsSet.add(Integer.parseInt(sharedPreferences.getString("id", "0")));
			message += "\n修复完成！";
		}
		FileTest.writeLog(message);
		return idsSet;
	}
}
