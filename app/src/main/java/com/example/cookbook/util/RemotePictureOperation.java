package com.example.cookbook.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import java.io.IOException;

public class RemotePictureOperation {
    private static String ROOT_URL = "https://qiandaobao.oss-cn-shanghai.aliyuncs.com/";

    /*
     * 将图片存到远程数据库，返回上传结果
     */
    public static void uploadPicture(final Context context, final Bitmap bitmap, final String pictureName) {
        F_GetBitmap.setInSDBitmap(F_GetByte.Bitmap2Bytes(bitmap), pictureName);
        OssService ossService=new OssService(context);
        ossService.initOSSClient();
        ossService.beginupload(context, pictureName, Environment.getExternalStorageDirectory()+ "/download_test"+"/"+pictureName);
    }

    /*
     * 通过图片名称获取图片
     */
    public static Bitmap downloadPicture(String pictureName, Context context) {
//        Log.d("pic", ROOT_URL + pictureName);
//        Glide.with(context).load(ROOT_URL + pictureName).into(imageView);
        Bitmap bitmap = null;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        Log.d("picture", "123");
        Log.d("picture", "从网上获取图片");
        try {
            bitmap = F_GetByte.getBitmap1(ROOT_URL + pictureName);
        } catch (IOException e) {
//[
        }
        return bitmap;
    }
}

