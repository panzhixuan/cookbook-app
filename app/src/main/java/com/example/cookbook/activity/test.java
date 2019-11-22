package com.example.cookbook.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.cookbook.R;
import com.example.cookbook.util.F_GetByte;
import com.example.cookbook.util.RequestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class test extends Activity {
    private ImageView imageView;
    private final int CODE_FOR_STORAGE = 1;
    private Map<String, Object> params;
    private byte[] a;
    private String name="123.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        imageView =(ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               selectheader();
            }
        });
    }

    public void selectheader() {
        initPermission();
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_FOR_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_FOR_STORAGE);
                Toast.makeText(test.this, "请设置相应权限", Toast.LENGTH_SHORT).show();
            }
        } else {
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_FOR_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }
    }

    private void selectImage() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bitmap bitmap = null;
            if (data.getExtras() != null) {
                bitmap = data.getExtras().getParcelable("data");
                imageView.setImageBitmap(bitmap);
                a= F_GetByte.Bitmap2Bytes(bitmap);
                Log.d("test123","到这里");
                Upload upload =new Upload();
                upload.start();
                try {
                    upload.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                file = new File(getFilesDir(), "register_user_header.jpg");
//                if (file.exists()) {
//                    file.delete();
//                }
//                try {
//                    file.createNewFile();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//                try {
//                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                    bos.flush();
//                    bos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }
    public class Upload extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("photo", a);
                params.put("name",name);
                String res1 = RequestUtils.post("/user/uploadheader", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        Looper.prepare();
                        Toast.makeText(test.this,"上传成功",Toast.LENGTH_LONG).show();
                    } else {
                        Looper.prepare();
                        Toast.makeText(test.this, "上传失败", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
