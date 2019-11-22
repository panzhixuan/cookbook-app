package com.example.cookbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.cookbook.util.F_GetByte;
import com.example.cookbook.view.TagLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestMain extends AppCompatActivity {



    private TagLayout tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        tag = findViewById(R.id.tag);
        Button button = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,5,5,5);
        button.setLayoutParams(params);
        button.setText("尼玛死了");
        button.setBackground(getResources().getDrawable(R.drawable.transparent1));
        Button button2 = new Button(this);
        button2.setLayoutParams(params);
        button2.setText("尼玛没了");
        button2.setBackground(getResources().getDrawable(R.drawable.transparent1));
        tag.addView(button);
        tag.addView(button2);
    }

}
