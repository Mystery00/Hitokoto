package com.mystery0.hitokoto;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FileTest
{
	public static void writeLog(String message)
	{
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
				.format(Calendar.getInstance().getTime());
		File file = new File(Environment.getExternalStorageDirectory().getPath() + "/hitokoto/log/test.txt");
		try
		{
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			printWriter.println(time + " " + message);
			printWriter.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
