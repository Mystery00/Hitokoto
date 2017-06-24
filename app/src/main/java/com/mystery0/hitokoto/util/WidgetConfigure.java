package com.mystery0.hitokoto.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.class_class.Hitokoto;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.hitokoto.class_class.HitokotoSource;
import com.mystery0.tools.Logs.Logs;
import com.mystery0.tools.MysteryNetFrameWork.HttpUtil;
import com.mystery0.tools.MysteryNetFrameWork.ResponseListener;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressLint("StaticFieldLeak")
public class WidgetConfigure
{
	private final String TAG = "WidgetConfigure";
	private Context context;
	private Gson gson = new Gson();
	private SharedPreferences sharedPreferences;

	public WidgetConfigure(Context context)
	{
		this.context=context;
		sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE);
	}

	public enum SourceType
	{
		INT, STRING
	}

	public boolean getEnable()
	{
		boolean temp = sharedPreferences.getBoolean(context.getString(R.string.hitokotoConfigEnable), false);
		Logs.i(TAG, "Enable: " + temp);
		return temp;
	}

	public void setEnable(boolean temp)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putBoolean(context.getString(R.string.hitokotoConfigEnable), temp);
		Logs.i(TAG, "setEnable: " + temp);
		editor.apply();
	}

	public Set<String> getChooseSource(SourceType type)
	{
		Set<String> defaults = new HashSet<>();
		Set<String> stringSet = new HashSet<>();
		String[] strings = context.getResources().getStringArray(R.array.list_source);
		for (int i = 0; i < strings.length; i++)
		{
			defaults.add(String.valueOf(i));
		}
		Set<String> temps = sharedPreferences.getStringSet(context.getString(R.string.hitokotoConfigChooseSource), defaults);
		if (type == SourceType.INT)
		{
			return temps;
		}
		for (String temp : temps)
		{
			stringSet.add(strings[Integer.parseInt(temp)]);
		}
		return stringSet;
	}

	public void setChooseSource(Set<String> temps)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		Set<String> stringSet = new HashSet<>();
		String[] strings = context.getResources().getStringArray(R.array.list_source);
		for (String temp : temps)
		{
			for (int i = 0; i < strings.length; i++)
			{
				if (strings[i].equals(temp))
				{
					stringSet.add(String.valueOf(i));
					break;
				}
			}
		}
		editor.putStringSet(context.getString(R.string.hitokotoConfigChooseSource), stringSet);
		editor.apply();
	}

	public boolean getAutoRefresh()
	{
		boolean temp = sharedPreferences.getBoolean(context.getString(R.string.key_auto_refresh), true);
		Logs.i(TAG, "AutoRefresh: " + temp);
		return temp;
	}

	public void setAutoRefresh(boolean temp)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putBoolean(context.getString(R.string.key_auto_refresh), temp);
		Logs.i(TAG, "setAutoRefresh: " + temp);
		editor.apply();
	}

	public boolean getClickToRefresh()
	{
		boolean temp = sharedPreferences.getBoolean(context.getString(R.string.hitokotoConfigClickToRefresh), true);
		Logs.i(TAG, "ClickToRefresh: " + temp);
		return temp;
	}

	public void setClickToRefresh(boolean temp)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putBoolean(context.getString(R.string.hitokotoConfigClickToRefresh), temp);
		Logs.i(TAG, "setClickToRefresh: " + temp);
		editor.apply();
	}

	public String getTextColor()
	{
		String temp = sharedPreferences.getString(context.getString(R.string.hitokotoConfigTextColor), "#FFFFFF");
		Logs.i(TAG, "TextColor: " + temp);
		return temp;
	}

	public void setTextColor(String temp)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putString(context.getString(R.string.hitokotoConfigTextColor), temp);
		Logs.i(TAG, "setTextColor: " + temp);
		editor.apply();
	}

	public boolean getNotShowSource()
	{
		boolean temp = sharedPreferences.getBoolean(context.getString(R.string.hitokotoConfigNotShowSource), false);
		Logs.i(TAG, "NotShowSource: " + temp);
		return temp;
	}

	public void setNotShowSource(boolean temp)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putBoolean(context.getString(R.string.hitokotoConfigNotShowSource), temp);
		Logs.i(TAG, "setNotShowSource: " + temp);
		editor.apply();
	}

	public int getTextAligned()
	{
		int temp = sharedPreferences.getInt(context.getString(R.string.hitokotoConfigTextAligned), 1);
		Logs.i(TAG, "TextAligned: " + temp);
		return temp;
	}

	public void setTextAligned(int temp)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putInt(context.getString(R.string.hitokotoConfigTextAligned), temp);
		Logs.i(TAG, "setTextAligned: " + temp);
		editor.apply();
	}

	public int getTextSize()
	{
		int temp = sharedPreferences.getInt(context.getString(R.string.hitokotoConfigTextSize), 16);
		Logs.i(TAG, "TextSize: " + temp);
		return temp;
	}

	public void setTextSize(int temp)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putInt(context.getString(R.string.hitokotoConfigTextSize), temp);
		Logs.i(TAG, "setTextSize: " + temp);
		editor.apply();
	}

	public int getTextPadding()
	{
		int temp = sharedPreferences.getInt(context.getString(R.string.hitokotoConfigTextPadding), 20);
		Logs.i(TAG, "TextPadding: " + temp);
		return temp;
	}

	public void setTextPadding(int temp)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putInt(context.getString(R.string.hitokotoConfigTextPadding), temp);
		Logs.i(TAG, "setTextPadding: " + temp);
		editor.apply();
	}

	public int getRefreshTime()
	{
		long temp = sharedPreferences.getLong(context.getString(R.string.hitokotoConfigRefreshTime), 300000);
		Logs.i(TAG, "RefreshTime: " + temp);
		return (int) (temp / 60000);
	}

	public void setRefreshTime(int temp)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putLong(context.getString(R.string.hitokotoConfigRefreshTime), temp * 60000);
		Logs.i(TAG, "setRefreshTime: " + temp);
		editor.apply();
	}

	public Boolean getDebuggable()
	{
		Boolean temp = sharedPreferences.getBoolean(context.getString(R.string.isDebuggable), false);
		Logs.i(TAG, "Debuggable: " + temp);
		return temp;
	}

	public void setDebuggable(boolean isDebuggable)
	{
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putBoolean(context.getString(R.string.isDebuggable), isDebuggable);
		Logs.i(TAG, "setDebuggable: " + isDebuggable);
		editor.apply();
	}

	public String[] getTemp()
	{
		String text = sharedPreferences
				.getString(context.getString(R.string.hitokotoTemp), context.getString(R.string.default_temp));
		Hitokoto hitokoto;
		try
		{
			hitokoto = gson.fromJson(text, Hitokoto.class);
		} catch (JsonSyntaxException e)
		{
			Logs.e(TAG, "getTemp: " + e.getMessage());
			hitokoto = gson.fromJson(context.getString(R.string.default_temp), Hitokoto.class);
		}
		return new String[]{hitokoto.getHitokoto(), hitokoto.getFrom()};
	}

	public void refreshText()
	{
		Logs.i(TAG, "refreshText: 刷新文本");
		Map<String, String> map = new HashMap<>();
		Set<String> stringSet = getChooseSource(SourceType.INT);
		List<Integer> integerList = new ArrayList<>();
		for (String t : stringSet)
		{
			integerList.add(Integer.valueOf(t));
		}
		String[] keys = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i"};
		String temp;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable())
		{
			temp = keys[integerList.get((int) (Math.random() * stringSet.size()))];
		} else
		{
			Logs.i(TAG, "refreshText: 无网络连接");
			temp = keys[7];
		}
		Logs.i(TAG, "refreshText: " + temp);
		if (temp.equals("h"))
		{
			HitokotoLocal hitokotoLocal = LocalConfigure.getRandom();
			if (hitokotoLocal != null)
			{
				Hitokoto hitokoto = new Hitokoto();
				hitokoto.setHitokoto(hitokotoLocal.getContent());
				hitokoto.setFrom(hitokotoLocal.getSource());
				Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
				intent.putExtra(context.getString(R.string.hitokoto_object), hitokoto);
				context.sendBroadcast(intent);
				return;
			}
		}
		if (temp.equals("i"))
		{
			List<HitokotoSource> list = DataSupport
					.where("source = ?", context.getResources().getStringArray(R.array.list_source_type)[2])
					.find(HitokotoSource.class);
			if (list.size() != 0)
			{
				final HitokotoSource hitokotoSource = list.get((int) (Math.random() * (list.size() - 1)));
				Logs.i(TAG, "refreshText: " + hitokotoSource.getName());
				Logs.i(TAG, "refreshText: " + hitokotoSource.getAddress());
				final HttpUtil httpUtil = new HttpUtil(App.getContext());
				switch (hitokotoSource.getMethod())
				{
					case 1://get
						httpUtil.setRequestMethod(HttpUtil.RequestMethod.GET);
						break;
					case 2://post
						httpUtil.setRequestMethod(HttpUtil.RequestMethod.POST);
						break;
				}
				httpUtil.setRequestQueue(App.getRequestQueue())
						.setUrl(hitokotoSource.getAddress())
						.setResponseListener(new ResponseListener()
						{
							@Override
							public void onResponse(int i, String s)
							{
								Hitokoto hitokoto;
								if (i == 1)
								{
									Logs.i(TAG, "onResponse: " + s);
									try
									{
										JSONObject jsonObject = new JSONObject(s);
										String content = jsonObject.getString(hitokotoSource.getContent_key());
										String from = jsonObject.getString(hitokotoSource.getFrom_key());
										Logs.i(TAG, "onResponse: content: " + content);
										Logs.i(TAG, "onResponse: from: " + from);
										hitokoto = new Hitokoto();
										hitokoto.setHitokoto(content);
										hitokoto.setFrom(from);
									} catch (Exception e)
									{
										hitokoto = gson.fromJson(context.getString(R.string.network_error), Hitokoto.class);
										Logs.e(TAG, "onResponse: " + e.getMessage());
										Toast.makeText(App.getContext(), context.getString(R.string.hint_network_error), Toast.LENGTH_SHORT)
												.show();
									}
								} else
								{
									hitokoto = gson.fromJson(context.getString(R.string.network_error), Hitokoto.class);
									Toast.makeText(App.getContext(), context.getString(R.string.hint_network_error), Toast.LENGTH_SHORT)
											.show();
								}
								sharedPreferences.edit().putString(context.getString(R.string.hitokotoTemp), gson.toJson(hitokoto)).apply();
								Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
								intent.putExtra(context.getString(R.string.hitokoto_object), hitokoto);
								context.sendBroadcast(intent);
							}
						})
						.open();
				return;
			}
		}
		map.put("c", temp.equals("h") || temp.equals("i") ? "a" : temp);
		final HttpUtil httpUtil = new HttpUtil(App.getContext());
		httpUtil.setRequestQueue(App.getRequestQueue())
				.setRequestMethod(HttpUtil.RequestMethod.GET)
				.setUrl(context.getString(R.string.request_url))
				.setResponseListener(new ResponseListener()
				{
					@Override
					public void onResponse(int i, String message)
					{
						Hitokoto hitokoto;
						if (i == 1)
						{
							Logs.i(TAG, "onResponse: " + message);
							try
							{
								hitokoto = gson.fromJson(message, Hitokoto.class);
							} catch (Exception e)
							{
								hitokoto = gson.fromJson(context.getString(R.string.network_error), Hitokoto.class);
								Logs.e(TAG, "onResponse: " + e.getMessage());
								Toast.makeText(App.getContext(), context.getString(R.string.hint_network_error), Toast.LENGTH_SHORT)
										.show();
							}
						} else
						{
							hitokoto = gson.fromJson(context.getString(R.string.network_error), Hitokoto.class);
							Logs.e(TAG, "onResponse: " + message);
							Toast.makeText(App.getContext(), context.getString(R.string.hint_network_error), Toast.LENGTH_SHORT)
									.show();
						}
						sharedPreferences.edit().putString(context.getString(R.string.hitokotoTemp), message).apply();
						Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
						intent.putExtra(context.getString(R.string.hitokoto_object), hitokoto);
						context.sendBroadcast(intent);
					}
				})
				.setMap(map)
				.open();
	}
}
