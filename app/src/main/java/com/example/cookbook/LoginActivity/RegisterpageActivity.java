package com.example.cookbook.LoginActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.cookbook.R;
import com.example.cookbook.activity.test;
import com.example.cookbook.user.UserFragment;
import com.example.cookbook.util.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterpageActivity extends Activity {
    private final int CODE_FOR_STORAGE = 1;
    private Map<String, Object> params;
    private ImageView userHeader;
    private Button handin;
    private Button getVerficode;
    private byte[] image=null;
    private String eamil;
    private String password;
    private String repassword;
    private int count=60;
    private int touch=0;
    private Bitmap bitmap = null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(RegisterpageActivity.this,"邮箱已存在",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(RegisterpageActivity.this,"头像上传失败",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(RegisterpageActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    RegisterpageActivity.this.finish();
                    break;
                case 3:
                    Toast.makeText(RegisterpageActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(RegisterpageActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    if(count>0){
                        getVerficode.setText("重新发送" +
                                "" +
                                ""+count+"s");
                        --count;
                        mHandler.sendEmptyMessageDelayed(5,1000);
                    }
                    else{
                        mHandler.removeCallbacksAndMessages(null);
                        touch=0;
                        getVerficode.setText("获取验证码");
                        count=60;
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage);
        userHeader=(ImageView) findViewById(R.id.userheader);
        handin=(Button)findViewById(R.id.handin);
        getVerficode = (Button)findViewById(R.id.getverficode);
        EditText password=(EditText)findViewById(R.id.password);
        EditText confirmpassword=(EditText)findViewById(R.id.confirmpassword);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        userHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectheader();
            }
        });
        handin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeVerifiCode();
            }
        });
        getVerficode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(touch==0) {
                    if(sendVerfiCode()) {
                        touch = 1;
                        mHandler.sendEmptyMessage(5);
                    }
                }
            }
        });

    }

    private boolean sendVerfiCode() {
        EditText username=(EditText)findViewById(R.id.username);
        eamil = username.getText().toString().trim();

        if (TextUtils.isEmpty(eamil)) {
            Toast.makeText(RegisterpageActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!PhoneNumberUtil.isPhone(eamil)) {
            Toast.makeText(RegisterpageActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;

        } else {

            new Thread() {
                @Override
                public void run() {
                    //把网络访问的代码放在这里

                    SendSms.sendCode(eamil);

                }
            }.start();
            return true;

        }
    }

    private void judgeVerifiCode() {
        EditText username=(EditText)findViewById(R.id.username);
        eamil = username.getText().toString().trim();
        EditText verficode = findViewById(R.id.verficode);
        String verificode = verficode.getText().toString().trim();

        if (TextUtils.isEmpty(eamil)) {
            Toast.makeText(RegisterpageActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(verificode)) {
            Toast.makeText(RegisterpageActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();


        }
        else if (verificode.equals(SendSms.getcode())) {
            submit();
        }else {
            Toast.makeText(RegisterpageActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
        }


    }

    public void submit(){
        EditText username=(EditText)findViewById(R.id.username);
        EditText password1=(EditText)findViewById(R.id.password);
        EditText confirmpassword1=(EditText)findViewById(R.id.confirmpassword);
        eamil=username.getText().toString();
        password=password1.getText().toString();
        repassword=confirmpassword1.getText().toString();
        if(eamil.equals("")){
            Toast.makeText(RegisterpageActivity.this, "手机号码为空", Toast.LENGTH_SHORT).show();
        }
        else if(password.equals("")){
            Toast.makeText(RegisterpageActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
        }
        else if(image==null){
            Toast.makeText(RegisterpageActivity.this, "未设置头像", Toast.LENGTH_SHORT).show();
        }
        else{
            Checkemail checkemail =new Checkemail();
            checkemail.start();
            try {
                checkemail.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                Toast.makeText(RegisterpageActivity.this, "请设置相应权限", Toast.LENGTH_SHORT).show();
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
        int version = android.os.Build.VERSION.SDK_INT;
        if (version > 24) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 1);
        }else{
            Intent intent = new Intent("android.intent.action.PICK");
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if (data != null) {
                int version = android.os.Build.VERSION.SDK_INT;
                if (version > 24) {
                    File file = null;
                    Uri uri = data.getData();
                    try {
                        file = F_GetByte.getFileFromMediaUri(this, uri);
                        Bitmap orginBitmap = F_GetByte.getBitmapFormUri(this, Uri.fromFile(file));
                        int degree = F_GetByte.getBitmapDegree(file.getAbsolutePath());
                        bitmap = F_GetByte.rotateBitmapByDegree(orginBitmap, degree);
                        userHeader.setImageBitmap(bitmap);
                        image = F_GetByte.Bitmap2Bytes(bitmap);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else{
                    if (data.getExtras() != null) {
                        bitmap = data.getExtras().getParcelable("data");
                        userHeader.setImageBitmap(bitmap);
                        image= F_GetByte.Bitmap2Bytes(bitmap);
                    }
                }
            }
        }
    }
    public class Checkemail extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("email", eamil);
                String res = RequestUtils.post("/user/getemail", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 400) {
                        if(!password.equals(repassword)){
                            mHandler.sendEmptyMessage(4);
                        }
                        else{
//                            Upload upload =new Upload();
//                            upload.start();
//                            try {
//                                upload.join();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            if(bitmap != null) {
                                RemotePictureOperation.uploadPicture(RegisterpageActivity.this, bitmap, eamil+".png");
                            }
                            Register register =new Register();
                            register.start();
                            try {
                                register.join();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mHandler.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Upload extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("photo", image);
                params.put("name",eamil+".png");
                String res1 = RequestUtils.post("/user/uploadheader", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        F_GetBitmap.setInSDBitmap(image,eamil+".png");
                        Register register =new Register();
                        register.start();
                        try {
                            register.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        mHandler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Register extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userEmail", eamil);
                params.put("userPassword",PasswordMD5.passwordMd5(password));
                params.put("userTaste",1);
                params.put("userCuisine",1);
                params.put("userOccasion",1);
                params.put("userPhoto",eamil+".png");
                params.put("userName","用户未设置用户名");
                params.put("userAddress","用户未设置地址");
                String res1 = RequestUtils.post("/user/register", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        mHandler.sendEmptyMessage(2);
                    } else {
                        mHandler.sendEmptyMessage(3);
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
