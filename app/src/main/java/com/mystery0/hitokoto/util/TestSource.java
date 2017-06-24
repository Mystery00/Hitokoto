package com.mystery0.hitokoto.util;

import com.google.gson.Gson;
import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.Hitokoto;
import com.mystery0.hitokoto.class_class.HitokotoSource;
import com.mystery0.hitokoto.listener.TestSourceListener;
import com.mystery0.tools.Logs.Logs;
import com.mystery0.tools.MysteryNetFrameWork.HttpUtil;
import com.mystery0.tools.MysteryNetFrameWork.ResponseListener;

import org.json.JSONObject;

public class TestSource
{
	private static final String TAG = "TestSource";

	public static void test(final HitokotoSource hitokotoSource, final TestSourceListener listener)
	{
		final HttpUtil httpUtil = new HttpUtil(App.getContext());
		httpUtil.setRequestQueue(App.getRequestQueue());
		String[] types = App.getContext().getResources().getStringArray(R.array.list_source_type);
		if (hitokotoSource.getSource().equals(types[0]))
		{
			switch (hitokotoSource.getMethod())
			{
				case 1://get
					httpUtil.setRequestMethod(HttpUtil.RequestMethod.GET);
					break;
				case 2://post
					httpUtil.setRequestMethod(HttpUtil.RequestMethod.POST);
					break;
			}
			httpUtil.setUrl(hitokotoSource.getAddress())
					.setResponseListener(new ResponseListener()
					{
						@Override
						public void onResponse(int i, String s)
						{
							if (i == 1)
							{
								try
								{
									new Gson().fromJson(s, Hitokoto.class);
									listener.result(true);
								} catch (Exception e)
								{
									listener.result(false);
								}
							} else
							{
								listener.result(false);
							}
						}
					})
					.open();
		} else if (hitokotoSource.getSource().equals(types[1]))
		{
			if (LocalConfigure.getRandom() == null)
			{
				listener.result(false);
			} else
			{
				listener.result(true);
			}
		} else if (hitokotoSource.getSource().equals(types[2]))
		{
			switch (hitokotoSource.getMethod())
			{
				case 1://get
					httpUtil.setRequestMethod(HttpUtil.RequestMethod.GET);
					break;
				case 2://post
					httpUtil.setRequestMethod(HttpUtil.RequestMethod.POST);
					break;
			}
			httpUtil.setUrl(hitokotoSource.getAddress())
					.setResponseListener(new ResponseListener()
					{
						@Override
						public void onResponse(int i, String s)
						{
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
									listener.result(true);
								} catch (Exception e)
								{
									Logs.e(TAG, "onResponse: " + e.getMessage());
									listener.result(false);
								}
							} else
							{
								listener.result(false);
							}
						}
					})
					.open();
		}
	}
}
