package com.example.cookbook.LoginActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.*;
import com.example.cookbook.R;
import com.example.cookbook.util.PasswordMD5;
import com.example.cookbook.util.RequestUtils;
import com.example.cookbook.util.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogginpageActivity extends Activity {
    private Map<String, Object> params;
    private Button confirm;
    private TextView forgetpassword;
    private TextView register;
    private EditText password;
    public static SharedPreferences sp;
    private EditText username;
    private Integer userId;
    private String userEmail;
    private String userPassword;
    private Integer userTaste;
    private Integer userCuisine;
    private Integer userOccasion;
    private String userPhoto;
    private String userName;
    private String userAddress;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(LogginpageActivity.this,"用户不存在",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(LogginpageActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                    Intent intent=null;
                    intent=new Intent(LogginpageActivity.this,MainpageActivity.class);
                    finish();
                    startActivity(intent);
                    break;
                case 2:
                    Toast.makeText(LogginpageActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(LogginpageActivity.this, "请设置相应权限才能登录", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logginpage);
        sp=this.getSharedPreferences("userInfo",MODE_PRIVATE);
        initview();
        if(sp.getBoolean("CHECK", false))
        {
            userId=sp.getInt("userId",0);
            userEmail=sp.getString("userEmail","");
            userPassword=sp.getString("userPassword","");
            userTaste=sp.getInt("userTaste",0);
            userCuisine=sp.getInt("userCuisine",0);
            userOccasion=sp.getInt("userOccasion",0);
            userPhoto=sp.getString("userPhoto","");
            userName=sp.getString("userName","");
            userAddress=sp.getString("userAddress","");
            User.getInstance().init(userId,userEmail,userPassword,userTaste,userCuisine,userOccasion,userPhoto,userName,userAddress);
            mHandler.sendEmptyMessage(1);
        }
    }
    public void initview(){
        confirm=(Button)findViewById(R.id.confirm);
        forgetpassword=(TextView) findViewById(R.id.forgetpassword);
        register=(TextView)findViewById(R.id.register);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        username.setText(sp.getString("userEmail",""));
        password.setText(sp.getString("userPassword",""));
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                intent=new Intent(LogginpageActivity.this,ForgetpasswordpageActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                intent=new Intent(LogginpageActivity.this,RegisterpageActivity.class);
                startActivity(intent);
            }
        });
    }
    public void Login(){
        userEmail=username.getText().toString();
        userPassword=password.getText().toString();
        if(userEmail.equals("")){
            Toast.makeText(LogginpageActivity.this,"请输入手机号码",Toast.LENGTH_LONG).show();
        }
        else if(userPassword.equals("")){
            Toast.makeText(LogginpageActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
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
    private boolean hasPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }else {
            return true;
        }
    }
    public class Checkemail extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("email", userEmail);
                String res = RequestUtils.post("/user/getemail", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        Getin getin =new Getin();
                        getin.start();
                        try {
                            getin.join();
                        } catch (Exception e) {
                            e.printStackTrace();
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
    public class Getin extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userEmail", userEmail);
                params.put("userPassword", PasswordMD5.passwordMd5(userPassword));
                String res1 = RequestUtils.post("/user/login", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
//                        if (ContextCompat.checkSelfPermission(LogginpageActivity.this,
//                                Manifest.permission.READ_EXTERNAL_STORAGE)
//                                != PackageManager.PERMISSION_GRANTED) {
//                            // Should we show an explanation?
//                            if (ActivityCompat.shouldShowRequestPermissionRationale(LogginpageActivity.this,
//                                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                                ActivityCompat.requestPermissions(LogginpageActivity.this,
//                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                                        1);
//                            } else {
//                                mHandler.sendEmptyMessage(3);
//                            }
                        if(!hasPermission()){
                            mHandler.sendEmptyMessage(3);
                        } else {
                            JSONObject user=jsonObject1.getJSONObject("data");
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("userEmail", user.getString("userEmail"));
                            editor.putString("userPassword",userPassword);
                            editor.putInt("userId",user.getInt("userId"));
                            editor.putInt("userTaste",user.getInt("userTaste"));
                            editor.putInt("userCuisine",user.getInt("userCuisine"));
                            editor.putInt("userOccasion",user.getInt("userOccasion"));
                            editor.putString("userPhoto",user.getString("userPhoto"));
                            editor.putString("userAddress",user.getString("userAddress"));
                            editor.putString("userName",user.getString("userName"));
                            editor.putBoolean("CHECK", true);
                            User.getInstance().init(user.getInt("userId"),user.getString("userEmail"),user.getString("userPassword"),user.getInt("userTaste"),user.getInt("userCuisine"),user.getInt("userOccasion"),user.getString("userPhoto"),user.getString("userName"),user.getString("userAddress"));
                            editor.commit();
                            mHandler.sendEmptyMessage(1);
                        }
                    } else {
                        mHandler.sendEmptyMessage(2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    private void initPermission() {
//        if (ContextCompat.checkSelfPermission(LogginpageActivity.this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(LogginpageActivity.this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    1);
//        } else {
//            JSONObject user=jsonObject1.getJSONObject("data");
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("userEmail", user.getString("userEmail"));
//            editor.putString("userPassword",user.getString("userPassword"));
//            editor.putInt("userId",user.getInt("userId"));
//            editor.putInt("userTaste",user.getInt("userTaste"));
//            editor.putInt("userCuisine",user.getInt("userCuisine"));
//            editor.putInt("userOccasion",user.getInt("userOccasion"));
//            editor.putString("userPhoto",user.getString("userPhoto"));
//            editor.putString("userAddress",user.getString("userAddress"));
//            editor.putString("userName",user.getString("userName"));
//            editor.putBoolean("CHECK", true);
//            User.getInstance().init(user.getInt("userId"),user.getString("userEmail"),user.getString("userPassword"),user.getInt("userTaste"),user.getInt("userCuisine"),user.getInt("userOccasion"),user.getString("userPhoto"),user.getString("userName"),user.getString("userAddress"));
//            editor.commit();
//            mHandler.sendEmptyMessage(1);
//        }
//    }
}
