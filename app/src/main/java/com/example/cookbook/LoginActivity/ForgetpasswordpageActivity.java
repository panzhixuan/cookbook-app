package com.example.cookbook.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cookbook.R;
import com.example.cookbook.util.PasswordMD5;
import com.example.cookbook.util.PhoneNumberUtil;
import com.example.cookbook.util.RequestUtils;
import com.example.cookbook.util.SendSms;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgetpasswordpageActivity extends Activity {
    private Map<String, Object> params;
    private EditText email;
    private EditText newpassword;
    private Button getVerficode;
    private Button handin;
    private String userEmail;
    private String userPassword;
    private int count=60;
    private int touch=0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ForgetpasswordpageActivity.this,"用户不存在",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(ForgetpasswordpageActivity.this,"修改密码成功",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(ForgetpasswordpageActivity.this, "修改密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    if(count>0){
                        getVerficode.setText("重新发送" +
                                "" +
                                ""+count+"s");
                        --count;
                        mHandler.sendEmptyMessageDelayed(3,1000);
                    }
                    else{
                        mHandler.removeCallbacksAndMessages(null);
                        touch=0;
                        getVerficode.setText("获取验证码");
                        count=60;
                    }
                    break;
                case 4:
                    Toast.makeText(ForgetpasswordpageActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(ForgetpasswordpageActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(ForgetpasswordpageActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpasswordpage);
        initview();
    }
    public void initview(){
        email=(EditText)findViewById(R.id.username);
        newpassword=(EditText)findViewById(R.id.password);
        newpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        getVerficode = (Button)findViewById(R.id.getverficode);
        handin=(Button)findViewById(R.id.handin);
        handin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail=email.getText().toString();
                userPassword=newpassword.getText().toString();
                if(userEmail.equals("")){
                    Toast.makeText(ForgetpasswordpageActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                else if(userPassword.equals("")){
                    Toast.makeText(ForgetpasswordpageActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                }
                else {
                    Checkemail checkemail = new Checkemail();
                    checkemail.start();
                    try {
                        checkemail.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        getVerficode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(touch==0) {
                    if(sendVerfiCode()) {
                        touch = 1;
                        mHandler.sendEmptyMessage(3);
                    }
                }
            }
        });
    }

    private boolean sendVerfiCode() {

        userEmail=email.getText().toString();
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(ForgetpasswordpageActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!PhoneNumberUtil.isPhone(userEmail)) {
            Toast.makeText(ForgetpasswordpageActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;

        } else {

            new Thread() {
                @Override
                public void run() {
                    //把网络访问的代码放在这里

                    SendSms.sendCode(userEmail);

                }
            }.start();
            return true;

        }
    }

    private void judgeVerifiCode() {
        EditText verficode = findViewById(R.id.verficode);
        String verificode = verficode.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)) {
            mHandler.sendEmptyMessage(5);

        } else if (TextUtils.isEmpty(verificode)) {
            mHandler.sendEmptyMessage(4);


        }
        else if (verificode.equals(SendSms.getcode())) {
            Update update =new Update();
            update.start();
            try {
                update.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            mHandler.sendEmptyMessage(6);
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
//                        Update update =new Update();
//                        update.start();
//                        try {
//                            update.join();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        judgeVerifiCode();
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
    public class Update extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userEmail", userEmail);
                params.put("userPassword", PasswordMD5.passwordMd5(userPassword));
                String res = RequestUtils.post("/user/forgetpassword", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        mHandler.sendEmptyMessage(1);
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
}
