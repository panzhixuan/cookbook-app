package com.example.cookbook.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.cookbook.R;
import com.example.cookbook.util.F_GetByte;
import com.example.cookbook.view.TagLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhotopageActivity extends AppCompatActivity {

    private File mTmpFile;
    private Uri imageUri;
    private static int PIC_REQUEST_CODE = 100;
    private static int CAMERA_REQUEST_CODE = 101;
    private ImageView photoResult;
    private Button selectPicture;
    private LinearLayout waiting;
    private LinearLayout failed;
    private TagLayout tag;
    private List<Button> tagBtnList = new ArrayList<>();
    private List<String> tagStrList = new ArrayList<>();
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopage);
        selectPicture = findViewById(R.id.selectPicture);
        photoResult = findViewById(R.id.photoResult);
        waiting = findViewById(R.id.waiting);
        failed = findViewById(R.id.failed);
        tag = findViewById(R.id.tag);
        waiting.setVisibility(View.GONE);
        failed.setVisibility(View.GONE);
        tag.setVisibility(View.GONE);
        setPhotoClick(true);
    }

    private void setPhotoClick(boolean sign){
        if(sign){
            selectPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String[] starr = new String[]{"相册", "拍照"};
                    AlertDialog adlg;
                    adlg=new AlertDialog.Builder(PhotopageActivity.this).setTitle("图片选择")
                            .setItems(starr, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:
                                            selectImage();
                                            break;
                                        case 1:
                                            takePhoto();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            })
                            .create();
                    adlg.show();
                }
            });
        }
        else{
            selectPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    private boolean hasPhotoPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        }else {
            return true;
        }
    }
    private boolean hasPicPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }else {
            return true;
        }
    }

    private void takePhoto(){

        if (!hasPhotoPermission()) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/img";
        if (new File(path).exists()) {
            try {
                new File(path).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mTmpFile = new File(path, filename + ".jpg");
        mTmpFile.getParentFile().mkdirs();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String authority = getPackageName() + ".provider";
            imageUri = FileProvider.getUriForFile(this, authority, mTmpFile);
        } else {
            imageUri = Uri.fromFile(mTmpFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }

    private void selectImage() {
        if (!hasPicPermission()) {
            return;
        }
        int version = android.os.Build.VERSION.SDK_INT;
        if (version > 24) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, PIC_REQUEST_CODE );
        }else{
            Intent intent = new Intent("android.intent.action.PICK");
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PIC_REQUEST_CODE );
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            Log.d("1234", "resultCode");
            if (resultCode == RESULT_OK){
                Bitmap orginBitmap = BitmapFactory.decodeFile(mTmpFile.getAbsolutePath());
                int degree = F_GetByte.getBitmapDegree(mTmpFile.getAbsolutePath());
                bitmap = F_GetByte.rotateBitmapByDegree(orginBitmap, degree);
//                mPresenter.getRecognitionResultByImage(photo);
//                imageView.setImageBitmap(photo);
                photoResult.setVisibility(View.VISIBLE);
                photoResult.setImageBitmap(bitmap);
                waiting.setVisibility(View.VISIBLE);
                MyTask myTask = new MyTask();
                myTask.execute();
            }
        }
        else if(requestCode == PIC_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
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
                            photoResult.setVisibility(View.VISIBLE);
                            photoResult.setImageBitmap(bitmap);
                            waiting.setVisibility(View.VISIBLE);
                            MyTask myTask = new MyTask();
                            myTask.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (data.getExtras() != null) {
                            bitmap = data.getExtras().getParcelable("data");
                            photoResult.setVisibility(View.VISIBLE);
                            photoResult.setImageBitmap(bitmap);
                            waiting.setVisibility(View.VISIBLE);
                            MyTask myTask = new MyTask();
                            myTask.execute();
                        }
                    }
                }
            }
        }
    }

    private class MyTask extends AsyncTask<String, Void, ArrayList<String>> {


        // 方法1：onPreExecute（）
        // 作用：执行 线程任务前的操作
        // 注：根据需求复写
        @Override
        protected void onPreExecute() {
           waiting.setVisibility(View.VISIBLE);
           failed.setVisibility(View.GONE);
           tag.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> result = new ArrayList<>();
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            for(int i = 0 ; i < 7 ; i++){
                result.add("材料"+ i);
            }
            result.add("安娜");
            result.add("计算");
            result.add("啊哈哈");
            return result;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if(result == null || result.size()==0){
                waiting.setVisibility(View.GONE);
                tag.setVisibility(View.GONE);
                failed.setVisibility(View.VISIBLE);
            }
            else{
                waiting.setVisibility(View.GONE);
                failed.setVisibility(View.GONE);
                tag.setVisibility(View.VISIBLE);
                tagStrList = result;
                tagButtonInit();
            }
        }

        @Override
        protected void onCancelled() {
            waiting.setVisibility(View.GONE);
            failed.setCameraDistance(View.VISIBLE);
            tag.setVisibility(View.GONE);
        }
    }

    private void tagButtonInit(){
        if(tagStrList == null && tagStrList.size() == 0){
            Log.e("ButtonError", "TagStrList的size存在问题");
            return;
        }
        else{
            tagBtnList = new ArrayList<>();
            for(int i = 0; i < tagStrList.size(); i++){
                tagBtnList.add(initTagBtn(tagStrList.get(i)));
                tag.addView(tagBtnList.get(i));
            }
        }
    }

    private Button initTagBtn(String word){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,5,5,20);
        Button button = new Button(this);
        button.setLayoutParams(params);
        button.setText(word);
        button.setBackground(getResources().getDrawable(R.drawable.transparent1));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(PhotopageActivity.this, PhotosearchActivity.class);
                intent.putExtra("name", word);
                startActivity(intent);
            }
        });
        return button;
    }
}
