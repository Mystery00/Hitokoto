package com.mystery0.hitokoto.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.JsonSyntaxException;
import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.class_class.Hitokoto;
import com.mystery0.hitokoto.R;
import com.mystery0.tools.Logs.Logs;
import com.mystery0.tools.MysteryNetFrameWork.HttpUtil;
import com.mystery0.tools.MysteryNetFrameWork.ResponseListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressLint("StaticFieldLeak")
public class WidgetConfigure
{
    private static final String TAG = "WidgetConfigure";
    private static Context context = App.getContext();
    private static SharedPreferences sharedPreferences = context.getSharedPreferences(context
            .getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = sharedPreferences.edit();

    public enum SourceType
    {
        INT, STRING
    }

    public static boolean getEnable()
    {
        boolean temp = sharedPreferences.getBoolean(context.getString(R.string.hitokotoConfigEnable), false);
        Logs.i(TAG, "Enable: " + temp);
        return temp;
    }

    public static void setEnable(boolean temp)
    {
        editor.putBoolean(context.getString(R.string.hitokotoConfigEnable), temp);
        Logs.i(TAG, "setEnable: " + temp);
        editor.apply();
    }

    public static Set<String> getChooseSource(SourceType type)
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

    public static void setChooseSource(Set<String> temps)
    {
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

    public static boolean getAutoRefresh()
    {
        boolean temp = sharedPreferences.getBoolean(context.getString(R.string.key_auto_refresh), true);
        Logs.i(TAG, "AutoRefresh: " + temp);
        return temp;
    }

    public static void setAutoRefresh(boolean temp)
    {
        editor.putBoolean(context.getString(R.string.key_auto_refresh), temp);
        Logs.i(TAG, "setAutoRefresh: " + temp);
        editor.apply();
    }

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

    public static String getTextColor()
    {
        String temp = sharedPreferences.getString(context.getString(R.string.hitokotoConfigTextColor), "#FFFFFF");
        Logs.i(TAG, "TextColor: " + temp);
        return temp;
    }

    public static void setTextColor(String temp)
    {
        editor.putString(context.getString(R.string.hitokotoConfigTextColor), temp);
        Logs.i(TAG, "setTextColor: " + temp);
        editor.apply();
    }

    public static boolean getNotShowSource()
    {
        boolean temp = sharedPreferences.getBoolean(context.getString(R.string.hitokotoConfigNotShowSource), false);
        Logs.i(TAG, "NotShowSource: " + temp);
        return temp;
    }

    public static void setNotShowSource(boolean temp)
    {
        editor.putBoolean(context.getString(R.string.hitokotoConfigNotShowSource), temp);
        Logs.i(TAG, "setNotShowSource: " + temp);
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

    public static int getTextSize()
    {
        int temp = sharedPreferences.getInt(context.getString(R.string.hitokotoConfigTextSize), 16);
        Logs.i(TAG, "TextSize: " + temp);
        return temp;
    }

    public static void setTextSize(int temp)
    {
        editor.putInt(context.getString(R.string.hitokotoConfigTextSize), temp);
        Logs.i(TAG, "setTextSize: " + temp);
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

    public static String[] getTemp()
    {
        String text = sharedPreferences
                .getString(context.getString(R.string.hitokotoTemp), context.getString(R.string.default_temp));
        Hitokoto hitokoto;
        try
        {
            hitokoto = new HttpUtil(App.getContext()).fromJson(text, Hitokoto.class);
        } catch (JsonSyntaxException e)
        {
            Logs.e(TAG, "getTemp: " + e.getMessage());
            hitokoto = new HttpUtil(App.getContext()).fromJson(context.getString(R.string.default_temp), Hitokoto.class);
        }
        return new String[]{hitokoto.getHitokoto(), hitokoto.getFrom()};
    }

    public static Set<Integer> getSet()
    {
        Set<Integer> integerSet = new HashSet<>();
        Set<String> stringSet = sharedPreferences.getStringSet(context.getString(R.string.hitokotoIdSet), new HashSet<String>());
        for (String temp : stringSet)
        {
            try
            {
                integerSet.add(Integer.valueOf(temp));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return integerSet;
    }

    public static void saveSet(Set<Integer> set)
    {
        Set<String> stringSet = new HashSet<>();
        for (int temp : set)
        {
            stringSet.add(String.valueOf(temp));
        }
        editor.putStringSet(context.getString(R.string.hitokotoIdSet), stringSet);
        editor.apply();
    }

    public static void refreshText()
    {
        Logs.i(TAG, "refreshText: 刷新文本");
        Map<String, String> map = new HashMap<>();
        Set<String> stringSet = getChooseSource(SourceType.INT);
        String[] keys = new String[]{"a", "b", "c", "d", "e", "f", "g"};
        map.put("c", keys[(int) (Math.random() * stringSet.size())]);
        final HttpUtil httpUtil = new HttpUtil(App.getContext());
        httpUtil.setRequestMethod(HttpUtil.RequestMethod.GET)
                .setUrl(context.getString(R.string.request_url))
                .setResponseListener(new ResponseListener()
                {
                    @Override
                    public void onResponse(int i, String message)
                    {
                        Logs.i(TAG, "onResponse: " + message);
                        editor.putString(context.getString(R.string.hitokotoTemp), message);
                        editor.apply();
                        Hitokoto hitokoto = httpUtil.fromJson(message, Hitokoto.class);
                        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
                        intent.putExtra(context.getString(R.string.hitokoto_object), hitokoto);
                        context.sendBroadcast(intent);
                    }
                })
                .setMap(map)
                .open();
    }
}
