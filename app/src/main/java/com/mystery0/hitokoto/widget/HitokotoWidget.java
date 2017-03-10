package com.mystery0.hitokoto.widget;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.Hitokoto;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.SettingsActivity;
import com.mystery0.tools.Logs.Logs;

import java.util.ArrayList;
import java.util.List;

public class HitokotoWidget extends AppWidgetProvider
{
    private static final String TAG = "HitokotoWidget";
    private static List<Integer> idsSet = new ArrayList<>();

    static void updateAllAppWidget(String text, Context context, AppWidgetManager appWidgetManager)
    {
        for (int id : idsSet)
        {
            RemoteViews remoteViews;
            switch (WidgetConfigure.getTextAligned())
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
            remoteViews.setTextViewText(R.id.appwidget_text, text);
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        for (int id : appWidgetIds)
        {
            idsSet.add(id);
            //initWidget();
        }
        updateAllAppWidget("", context, appWidgetManager);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        for (int id : appWidgetIds)
        {
            idsSet.remove(id);
        }
    }

    @Override
    public void onEnabled(Context context)
    {
        Logs.i(TAG, "onEnabled: ");
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            context.startActivity(new Intent(context, SettingsActivity.class));
            Toast.makeText(context, R.string.hint_permission, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onDisabled(Context context)
    {
        Logs.i(TAG, "onDisabled: ");
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if ("android.appwidget.action.APPWIDGET_UPDATE".equals(action))
        {
            Hitokoto hitokoto = (Hitokoto) intent.getSerializableExtra(context.getString(R.string.hitokoto_object));
            Logs.i(TAG, "onReceive: " + hitokoto.getHitokoto());
            Logs.i(TAG, "onReceive: " + hitokoto.getFrom());
        }
    }
}

