package com.mystery0.hitokoto.local;

import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.tools.Logs.Logs;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LocalConfigure
{
    private static final String TAG = "LocalConfigure";

    public static void saveToDatabase(String content, String source)
    {
        String time = (new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)).format(Calendar.getInstance().getTime());
        HitokotoLocal hitokotoLocal = new HitokotoLocal();
        hitokotoLocal.setContent(content);
        hitokotoLocal.setSource(source);
        hitokotoLocal.setDate(time);
        hitokotoLocal.save();
    }

    public static void saveToDatabase(List<HitokotoLocal> contents)
    {
        int number = contents.size();
        Logs.i(TAG, "saveToDatabase: 添加数量:" + number);
        for (HitokotoLocal hitokotoLocal : contents)
        {
            saveToDatabase(hitokotoLocal.getContent(), hitokotoLocal.getSource());
        }
    }

    public static HitokotoLocal getRandom()
    {
        List<HitokotoLocal> list = DataSupport.findAll(HitokotoLocal.class);
        Logs.i(TAG, "getRandom: " + list.size());
        if (list.size() == 0)
        {
            return null;
        } else
        {
            return list.get((int) (Math.random() * list.size()));
        }
    }
}
