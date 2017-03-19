package com.mystery0.hitokoto.custom;

import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.tools.Logs.Logs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomConfigure
{
    private static final String TAG = "CustomConfigure";

    public static void saveToDatabase(String content, String source)
    {
        String time = (new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)).format(Calendar.getInstance().getTime());
        HitokotoLocal hitokotoLocal = new HitokotoLocal();
        hitokotoLocal.setContent(content);
        hitokotoLocal.setSource(source);
        hitokotoLocal.setDate(time);
        Logs.i(TAG, "saveToDatabase: " + hitokotoLocal);
        hitokotoLocal.save();
    }

    public static void saveToDatabase(String[] contents, String[] sources)
    {
        if (contents.length != sources.length)
        {
            throw new ArrayIndexOutOfBoundsException("Number Error!");
        }
        int number = contents.length;
        Logs.i(TAG, "saveToDatabase: 添加数量:" + number);
        for (int i = 0; i < number; i++)
        {
            saveToDatabase(contents[i], sources[i]);
        }
    }
}
