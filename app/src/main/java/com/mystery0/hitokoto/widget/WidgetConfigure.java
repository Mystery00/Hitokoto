package com.mystery0.hitokoto.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.tools.Logs.Logs;

import java.util.Set;

public class WidgetConfigure
{
    private static final String TAG = "WidgetConfigure";
    private static Context context = App.getContext();
    private static SharedPreferences sharedPreferences = context.getSharedPreferences(context
            .getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = sharedPreferences.edit();

    public static boolean getClickToRefresh()
    {
        boolean temp = sharedPreferences.getBoolean(context.getString(R.string.hitokotoConfigClickToRefresh), true);
        Logs.i(TAG, "ClickToRefresh: " + temp);
        return temp;
    }

    public static void setClickToRefresh(boolean temp)
    {
        editor.putBoolean(context.getString(R.string.hitokotoConfigClickToRefresh), temp);
        Logs.i(TAG, "setClickToRefresh: " + temp);
        editor.apply();
    }

    public static boolean getTextBold()
    {
        boolean temp = sharedPreferences.getBoolean(context.getString(R.string.hitokotoConfigTextBold), false);
        Logs.i(TAG, "TextBold: " + temp);
        return temp;
    }

    public static void setTextBold(boolean temp)
    {
        editor.putBoolean(context.getString(R.string.hitokotoConfigTextAligned), temp);
        Logs.i(TAG, "setTextBold: " + temp);
        editor.apply();
    }

    public static int getTextAligned()
    {
        int temp = sharedPreferences.getInt(context.getString(R.string.hitokotoConfigTextAligned), 1);
        Logs.i(TAG, "TextAligned: " + temp);
        return temp;
    }

    public static void setTextAligned(int temp)
    {
        editor.putInt(context.getString(R.string.hitokotoConfigTextAligned), temp);
        Logs.i(TAG, "setTextAligned: " + temp);
        editor.apply();
    }

    public static int getRefreshTime()
    {
        long temp = sharedPreferences.getLong(context.getString(R.string.hitokotoConfigRefreshTime), 300000);
        Logs.i(TAG, "RefreshTime: " + temp);
        return (int) (temp / 60000);
    }

    public static void setRefreshTime(int temp)
    {
        editor.putLong(context.getString(R.string.hitokotoConfigRefreshTime), temp * 60000);
        Logs.i(TAG, "setRefreshTime: " + temp);
        editor.apply();
    }


    public static void updateAllAppWidgets(String word, Context context, AppWidgetManager appWidgetManager, Set<Integer> idsSet)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.file_sharedPreferences_widget), Context.MODE_PRIVATE);
        if (word == null)
        {
            word = sharedPreferences.getString(context.getString(R.string.name_widget_text_now), context.getString(R.string.error_widget));
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.name_widget_text_now), word);
        editor.apply();
        int appID;
        // 迭代器，用于遍历所有保存的widget的id
        for (Integer anIdsSet : idsSet)
        {
            appID = anIdsSet;
            RemoteViews remoteViews;
            switch (sharedPreferences.getInt(context.getString(R.string.name_widget_text_alignment), 1))
            {
                case 0://left
                    remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_start);
                    break;
                case 2://right
                    remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_end);
                    break;
                default:
                    remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_center);
                    break;
            }
            remoteViews.setTextViewText(R.id.widget_tv, word);
            remoteViews.setTextColor(R.id.widget_tv, getTextColor(context));
            remoteViews.setOnClickPendingIntent(R.id.widget_tv, WidgetUtil.getPendingIntent(context));

            appWidgetManager.updateAppWidget(appID, remoteViews);
        }
    }
}
