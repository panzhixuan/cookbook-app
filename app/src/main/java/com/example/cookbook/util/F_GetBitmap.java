package com.example.cookbook.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.*;
import java.lang.ref.SoftReference;

public class F_GetBitmap {
    public static Bitmap bitmap = null;
    public static String filePath;
    public static String picFilePath;
    public static File file;
    public static boolean isEmpty(String picName) {

        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            filePath = Environment.getExternalStorageDirectory()
                    + "/download_test";
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            picFilePath = Environment.getExternalStorageDirectory().toString()
                    + "/download_test" + "/" + picName;
            file = new File(picFilePath);
            if (file.exists()) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public static Bitmap getSDBitmap(String picName) {
        picFilePath = Environment.getExternalStorageDirectory()+ "/download_test" + "/" + picName;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false; /* ������ͼƬ�������� */
        options.inPreferredConfig = null; /* �����ý���������ѷ�ʽ���� */
        options.inSampleSize = 1; /* ͼƬ���?����С���� */
        Bitmap bit = BitmapFactory.decodeFile(picFilePath, options);
        return bit;
    }

    public static void setInSDBitmap(byte[] bb, String picName) {
        filePath = Environment.getExternalStorageDirectory()+ "/download_test/";
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        input = new ByteArrayInputStream(bb);
        @SuppressWarnings({ "rawtypes", "unchecked" })
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (bb != null) {
            bb = null;
        }try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        FileOutputStream fos = null;
        file = new File(filePath + "/" + picName);
        try {
            fos = new FileOutputStream(file); // ����SD����
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            bitmap.recycle();
            System.gc();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
