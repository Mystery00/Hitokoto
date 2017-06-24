package com.mystery0.hitokoto.widget;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.class_class.Hitokoto;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.SettingsActivity;
import com.mystery0.tools.Logs.Logs;

public class HitokotoWidget extends AppWidgetProvider
{
	private static final String TAG = "HitokotoWidget";

	static void updateAllAppWidget(String text, String source)
	{
		Context context = App.getContext();
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		for (int id : App.getIdsSet())
		{
			RemoteViews remoteViews;
			switch (App.getWidgetConfigure().getTextAligned())
			{
				case 0:
					remoteViews = new RemoteViews(context.getPackageName(), R.layout.hitokoto_widget_left);
					break;
				case 2:
					remoteViews = new RemoteViews(context.getPackageName(), R.layout.hitokoto_widget_right);
					break;
				default:
					remoteViews = new RemoteViews(context.getPackageName(), R.layout.hitokoto_widget_center);
					break;
			}
			remoteViews.setViewPadding(R.id.appwidget_source, 0, App.getWidgetConfigure().getTextPadding(), 0, 0);
			remoteViews.setTextViewText(R.id.appwidget_text, text);
			remoteViews.setTextViewText(R.id.appwidget_source, source);
			remoteViews.setTextColor(R.id.appwidget_text, Color.parseColor(App.getWidgetConfigure().getTextColor()));
			remoteViews.setTextColor(R.id.appwidget_source, Color.parseColor(App.getWidgetConfigure().getTextColor()));
			if (App.getWidgetConfigure().getNotShowSource())
			{
				remoteViews.setViewVisibility(R.id.appwidget_source, View.GONE);
			} else
			{
				remoteViews.setViewVisibility(R.id.appwidget_source, View.VISIBLE);
			}
			remoteViews.setTextViewTextSize(R.id.appwidget_text, TypedValue.COMPLEX_UNIT_SP, App.getWidgetConfigure().getTextSize());
			remoteViews.setTextViewTextSize(R.id.appwidget_source, TypedValue.COMPLEX_UNIT_SP, App.getWidgetConfigure().getTextSize());
			if (App.getWidgetConfigure().getClickToRefresh())
			{
				Intent topIntent = new Intent(context, OnClickService.class);
				PendingIntent topPendingIntent = PendingIntent.getService(context, 0, topIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.appwidget_text, topPendingIntent);
				remoteViews.setOnClickPendingIntent(R.id.appwidget_source, topPendingIntent);
			}
			appWidgetManager.updateAppWidget(id, remoteViews);
		}
	}

	private void initWidget(Context context, int appId, AppWidgetManager appWidgetManager)
	{
		RemoteViews remoteViews;
		String[] strings = App.getWidgetConfigure().getTemp();
		switch (App.getWidgetConfigure().getTextAligned())
		{
			case 0:
				remoteViews = new RemoteViews(context.getPackageName(), R.layout.hitokoto_widget_left);
				break;
			case 2:
				remoteViews = new RemoteViews(context.getPackageName(), R.layout.hitokoto_widget_right);
				break;
			default:
				remoteViews = new RemoteViews(context.getPackageName(), R.layout.hitokoto_widget_center);
				break;
		}
		String[] texts = App.getWidgetConfigure().getTemp();
		remoteViews.setViewPadding(R.id.appwidget_source, 0, App.getWidgetConfigure().getTextPadding(), 0, 0);
		remoteViews.setTextViewText(R.id.appwidget_text, texts[0]);
		remoteViews.setTextViewText(R.id.appwidget_source, texts[1]);
		remoteViews.setTextColor(R.id.appwidget_text, Color.parseColor(App.getWidgetConfigure().getTextColor()));
		remoteViews.setTextColor(R.id.appwidget_source, Color.parseColor(App.getWidgetConfigure().getTextColor()));
		if (App.getWidgetConfigure().getNotShowSource())
		{
			remoteViews.setViewVisibility(R.id.appwidget_source, View.GONE);
		} else
		{
			remoteViews.setViewVisibility(R.id.appwidget_source, View.VISIBLE);
		}
		if (App.getWidgetConfigure().getClickToRefresh())
		{
			Intent topIntent = new Intent(context, OnClickService.class);
			PendingIntent topPendingIntent = PendingIntent.getService(context, 0, topIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.appwidget_text, topPendingIntent);
			remoteViews.setOnClickPendingIntent(R.id.appwidget_source, topPendingIntent);
		}
		remoteViews.setTextViewTextSize(R.id.appwidget_text, TypedValue.COMPLEX_UNIT_SP, App.getWidgetConfigure().getTextSize());
		remoteViews.setTextViewTextSize(R.id.appwidget_source, TypedValue.COMPLEX_UNIT_SP, App.getWidgetConfigure().getTextSize());
		appWidgetManager.updateAppWidget(appId, remoteViews);
		if (strings[1].equals("开发者"))
		{
			App.getWidgetConfigure().refreshText();
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		Logs.i(TAG, "onUpdate: ");
		for (int id : appWidgetIds)
		{
			App.addID(id);
			initWidget(context, id, appWidgetManager);
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		Logs.i(TAG, "onDeleted: ");
		for (int id : appWidgetIds)
		{
			App.removeID(id);
		}
	}

	@Override
	public void onEnabled(Context context)
	{
		Logs.i(TAG, "onEnabled: ");
		App.getWidgetConfigure().setEnable(true);
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED)
		{
			context.startActivity(new Intent(context, SettingsActivity.class));
			Toast.makeText(context, R.string.hint_permission, Toast.LENGTH_SHORT)
					.show();
		}
		Intent intent = new Intent(context, WidgetService.class);
		context.startService(intent);
	}

	@Override
	public void onDisabled(Context context)
	{
		App.getWidgetConfigure().setEnable(false);
		Logs.i(TAG, "onDisabled: ");
		Intent intent = new Intent(context, WidgetService.class);
		context.stopService(intent);
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);
		String action = intent.getAction();
		Logs.i(TAG, "onReceive: " + action);
		if ("android.appwidget.action.APPWIDGET_UPDATE".equals(action))
		{
			Hitokoto hitokoto = (Hitokoto) intent.getSerializableExtra(context.getString(R.string.hitokoto_object));
			if (hitokoto != null)
			{
				Logs.i(TAG, "onReceive: " + hitokoto.getHitokoto());
				Logs.i(TAG, "onReceive: " + hitokoto.getFrom());
				updateAllAppWidget(hitokoto.getHitokoto(), "————" + hitokoto.getFrom());
			}
		}
	}
}

