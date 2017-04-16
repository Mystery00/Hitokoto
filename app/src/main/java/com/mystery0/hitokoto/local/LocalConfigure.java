package com.mystery0.hitokoto.local;

import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.FileDo;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoGroup;
import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.hitokoto.class_class.ShareFile;
import com.mystery0.tools.Logs.Logs;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LocalConfigure
{
    private static final String TAG = "LocalConfigure";
    private static Gson gson = new Gson();

    public static void saveToDatabase(String content, String source, String group)
    {
        String time = (new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)).format(Calendar.getInstance().getTime());
        HitokotoLocal hitokotoLocal = new HitokotoLocal();
        hitokotoLocal.setContent(content);
        hitokotoLocal.setSource(source);
        if (group != null && group.length() != 0)
        {
            hitokotoLocal.setGroup(group);
        }
        hitokotoLocal.setDate(time);
        hitokotoLocal.saveOrUpdate("content = ?", content);
    }

    public static void saveToDatabase(List<HitokotoLocal> contents, String group)
    {
        int number = contents.size();
        Logs.i(TAG, "saveToDatabase: 添加数量:" + number);
        for (HitokotoLocal hitokotoLocal : contents)
        {
            saveToDatabase(hitokotoLocal.getContent(), hitokotoLocal.getSource(), group);
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void outputFile(String fileName,LocalListener listener)
    {
        String path = Environment.getExternalStorageDirectory().getPath() + "/hitokoto/";
        Logs.i(TAG, "exportHitokotos: " + fileName);
        List<HitokotoLocal> localList = DataSupport.where("group = ?", fileName).find(HitokotoLocal.class);
        File file = new File(path + fileName + ".txt");
        Logs.i(TAG, "exportHitokotos: " + file.getAbsolutePath());
        try
        {
            if (file.exists())
            {
                file.delete();
            }
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            for (HitokotoLocal hitokotoLocal : localList)
            {
                Logs.i(TAG, "exportHitokotos: " + gson.toJson(hitokotoLocal));
                fileWriter.write(gson.toJson(hitokotoLocal) + "\n");
            }
            fileWriter.close();
            listener.done();
        } catch (IOException e)
        {
            listener.error();
        }
    }

    public static void inputFile(String path,LocalListener listener)
    {
        String fileName = FileDo.getFileName(path);
        Logs.i(TAG, "onActivityResult: 选择的文件: " + path);
        try
        {
            Scanner scanner = new Scanner(new File(path));
            List<HitokotoLocal> list = new ArrayList<>();
            while (scanner.hasNext())
            {
                String temp = scanner.nextLine();
                HitokotoLocal hitokotoLocal = gson.fromJson(temp, HitokotoLocal.class);
                list.add(hitokotoLocal);
            }
            scanner.close();
            LocalConfigure.saveToDatabase(list, fileName);
            new HitokotoGroup(fileName).saveOrUpdate("name = ?", fileName);
            listener.done();
        } catch (Exception e)
        {
            Logs.e(TAG, "onActivityResult: " + e.getMessage());
            listener.error();
        }
    }
}
