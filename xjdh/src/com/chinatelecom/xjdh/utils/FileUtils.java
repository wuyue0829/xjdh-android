package com.chinatelecom.xjdh.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * 文件处理工具包
 * 
 * @author peter
 * @version 1.1
 * @created 2015-07-08
 */
public class FileUtils {
	public static String getFromAssets(Context ctx, String fileName) {
		String Result = "";
		try {
			InputStreamReader inputReader = new InputStreamReader(ctx.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result;
	}

	public static boolean setToData(Context ctx, String fileName, byte[] data) {
		try {
			FileOutputStream fos = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(data);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static byte[] getFileData(Context ctx, String fileName) {
		try {
			FileInputStream fis = ctx.openFileInput(fileName);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = fis.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			return outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "".getBytes();
	}

	public static Uri getTakePhotoUri() {
		SimpleDateFormat imageDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault());
		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
		pathBuilder.append('/');
		pathBuilder.append("Camera");
		pathBuilder.append('/');
		pathBuilder.append("IMG_" + imageDateFormat.format(new Date()) + ".jpg");
		Uri uri = Uri.parse("file://" + pathBuilder.toString());
		File file = new File(uri.toString());
		file.getParentFile().mkdirs();
		return uri;
	}
}
