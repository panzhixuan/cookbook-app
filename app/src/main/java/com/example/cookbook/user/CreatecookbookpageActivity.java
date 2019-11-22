package com.example.cookbook.user;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.cookbook.R;
import com.example.cookbook.util.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatecookbookpageActivity extends Activity {
    private LinearLayout materialList;
    private LinearLayout stepList;
    private final int CODE_FOR_STORAGE = 1;
    private File file=null;
    private ImageView cover;
    private Button material_add;
    private Button add_step;
    private Button delete_step;
    private Button create_cookbook;
    private byte[] image;
    private Map<String, Object> params;
    private int occsionId;
    private int tasteId;
    private int cuisineId;
    private EditText cookbook_name;
    private EditText nutrition;
    private EditText tips;
    private Spinner caishispinner;
    private Spinner caixispinner;
    private Spinner changhespinner;
    private String time1;
    private String maname;
    private String manum;
    private String stInf;
    private int cookbookid;
    private Bitmap bitmap = null;
    public static ProgressDialog pd;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(CreatecookbookpageActivity.this,"创建失败",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(CreatecookbookpageActivity.this,"创建成功",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 2:
                    pd.show();
                    break;
                case 3:
                    pd.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createcookbookpage);
        initview();
    }
    public void initview(){
        pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage("上传中，请稍后........");
        caishispinner = (Spinner) findViewById(R.id.caishi);
        String[] caishitype = {"清淡", "辣","酸"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, caishitype);
        caishispinner.setAdapter(adapter);
        caixispinner = (Spinner) findViewById(R.id.caixi);
        String[] caixitype = {"川菜", "淮扬菜","徽菜","鲁菜","闽菜","粤菜","湘菜","浙菜"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, caixitype);
        caixispinner.setAdapter(adapter1);
        changhespinner = (Spinner) findViewById(R.id.changhe);
        String[] changhetype = {"家", "饭店","快餐店"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, changhetype);
        changhespinner.setAdapter(adapter2);
        materialList = (LinearLayout) findViewById(R.id.material_list);
        addMItem();
        stepList = (LinearLayout) findViewById(R.id.step_list);
        addSItem();
        cover=(ImageView)findViewById(R.id.cover);
        material_add=(Button)findViewById(R.id.material_add);
        add_step=(Button)findViewById(R.id.add_step);
        delete_step=(Button)findViewById(R.id.delete_step);
        create_cookbook=(Button)findViewById(R.id.create_cookbook);
        cookbook_name = (EditText) findViewById(R.id.cookbookname);
        nutrition = (EditText) findViewById(R.id.nutrition);
        tips= (EditText) findViewById(R.id.tips);
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcover();
            }
        });
        create_cookbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(CreatecookbookpageActivity.this);
                confirmDialog.setTitle("创建菜谱");
                confirmDialog.setMessage("确定要创建吗？");
                confirmDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        create();
                    }
                });
                confirmDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                confirmDialog.show();
            }
        });
        material_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMItem();
            }
        });
        add_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSItem();
            }
        });
        delete_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delSItem();
            }
        });
    }

    public void addMItem() {
        final View temp = View.inflate(this, R.layout.material_item, null);
        materialList.addView(temp, -1);
        Button delete = (Button) temp.findViewById(R.id.material_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialList.getChildCount() > 1) {
                    materialList.removeView(temp);
                }
            }
        });
    }
    public void addSItem() {
        final View temp = View.inflate(this, R.layout.add_cookbook_step, null);
        stepList.addView(temp, -1);
        TextView stepNum = (TextView) temp.findViewById(R.id.cookbook_step_number);
        stepNum.setText("" + stepList.getChildCount() + ":");
    }
    public void delSItem() {
        final View temp = stepList.getChildAt(stepList.getChildCount() - 1);
        if (stepList.getChildCount() > 1) {
            stepList.removeView(temp);
        }
    }

    public void addcover(){
        initPermission();
    }

    public void create(){
        if(tips.getText().toString().equals("")){
            Toast.makeText(CreatecookbookpageActivity.this,"小贴士不能为空",Toast.LENGTH_LONG).show();
        }
        else if(nutrition.getText().toString().equals("")){
            Toast.makeText(CreatecookbookpageActivity.this,"营养价值不能为空",Toast.LENGTH_LONG).show();
        }
        else if(cookbook_name.getText().toString().equals("")){
            Toast.makeText(CreatecookbookpageActivity.this,"菜谱名字不能为空",Toast.LENGTH_LONG).show();
        }
        else if(image==null){
            Toast.makeText(CreatecookbookpageActivity.this,"未选择菜谱图片",Toast.LENGTH_LONG).show();
        }
        else if(stepList.getChildCount()==0||materialList.getChildCount()==0){
            Toast.makeText(CreatecookbookpageActivity.this,"未添加步骤和材料",Toast.LENGTH_LONG).show();
        }
        else {
            for (int i = 0; i < materialList.getChildCount(); i++) {
                View temp = materialList.getChildAt(i);

                EditText mName = (EditText) temp.findViewById(R.id.material_name);
                if (mName.getText().toString().equals("")) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(CreatecookbookpageActivity.this);
                    normalDialog.setTitle("存在未填项");
                    normalDialog.setMessage("您未填写第" + (i + 1) + "项材料名称");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    normalDialog.show();
                    return;
                }

                EditText mNum = (EditText) temp.findViewById(R.id.material_quantity);
                if (mNum.getText().toString().equals("")) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(CreatecookbookpageActivity.this);
                    normalDialog.setTitle("存在未填项");
                    normalDialog.setMessage("您未填写第" + (i + 1) + "项材料用量");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    normalDialog.show();
                    return;
                }
            }
            for (int i = 0; i < stepList.getChildCount(); i++) {
                View temp = stepList.getChildAt(i);
                EditText stepDesc = (EditText) temp.findViewById(R.id.cookbook_step_desc);
                if (stepDesc.getText().toString().equals("")) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(CreatecookbookpageActivity.this);
                    normalDialog.setTitle("存在未填项");
                    normalDialog.setMessage("您未填写第" + (i + 1) + "步的具体内容");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    normalDialog.show();
                    return;
                }
            }
            mHandler.sendEmptyMessage(2);
            GetoccsionId getoccsionid = new GetoccsionId();
            getoccsionid.start();
            try {
                getoccsionid.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            GettasteId gettasteId = new GettasteId();
            gettasteId.start();
            try {
                gettasteId.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            GetcuisineId getcuisineId = new GetcuisineId();
            getcuisineId.start();
            try {
                getcuisineId.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Upload upload = new Upload();
            upload.start();
            try {
                upload.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Createcookbook createcookbook = new Createcookbook();
            createcookbook.start();
            try {
                createcookbook.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                Toast.makeText(CreatecookbookpageActivity.this, "请设置相应权限", Toast.LENGTH_SHORT).show();
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
                    Log.d("zhaopian", "111");
                    Uri uri = data.getData();
                    try {
                        file = F_GetByte.getFileFromMediaUri(this, uri);
                        Bitmap orginBitmap = F_GetByte.getBitmapFormUri(this, Uri.fromFile(file));
                        int degree = F_GetByte.getBitmapDegree(file.getAbsolutePath());
                        bitmap = F_GetByte.rotateBitmapByDegree(orginBitmap, degree);
                        cover.setImageBitmap(bitmap);
                        image = F_GetByte.Bitmap2Bytes(bitmap);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else{
                    if (data.getExtras() != null) {
                        bitmap = data.getExtras().getParcelable("data");
                        cover.setImageBitmap(bitmap);
                        image= F_GetByte.Bitmap2Bytes(bitmap);
                    }
                }
            }
        }
    }
    public class GetoccsionId extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("occasionName", changhespinner.getSelectedItem().toString());
                String res = RequestUtils.post("/occasion/NamefindId", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        occsionId=jsonObject1.getInt("data");
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
    public class GettasteId extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("tasteName", caishispinner.getSelectedItem().toString());
                String res = RequestUtils.post("/taste/NamefindId", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        tasteId=jsonObject1.getInt("data");
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
    public class GetcuisineId extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cuisineName", caixispinner.getSelectedItem().toString());
                String res = RequestUtils.post("/cuisine/NamefindId", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        cuisineId=jsonObject1.getInt("data");
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
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String t =time.format(new Date());
            time1=cookbook_name.getText().toString()+t+".png";
            RemotePictureOperation.uploadPicture(CreatecookbookpageActivity.this, bitmap, time1);
//            try {
//                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//                String t =time.format(new Date());
//                time1=cookbook_name.getText().toString()+t+".png";
//                params = new HashMap<>();
//                params.put("photo", image);
//                params.put("name", time1);
//                String res1 = RequestUtils.post("/cookbook/uploadcover", params);
//                try {
//                    JSONObject jsonObject1 = new JSONObject(res1);
//                    if (jsonObject1.getInt("code") == 200) {
//                        F_GetBitmap.setInSDBitmap(image,time1);
//                    } else {
//                        mHandler.sendEmptyMessage(0);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
    public class Createcookbook extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookLikenum",0);
                params.put("cookbookVisitnum",0);
                params.put("cookbookTip",tips.getText().toString());
                params.put("cookbookNutrition",nutrition.getText().toString());
                params.put("cookbookPhoto",time1);
                params.put("cookbookName",cookbook_name.getText().toString());
                params.put("userId",User.getInstance().getUserId());
                params.put("cookbookTaste",tasteId);
                params.put("cookbookCuisine",cuisineId);
                params.put("cookbookOccasion",occsionId);
                String res1 = RequestUtils.post("/cookbook/add", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        Uploadcontent();
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
    public class Getcookbook extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookPhoto",time1);
                String res1 = RequestUtils.post("/cookbook/findbyPhoto", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONObject cookbook=jsonObject1.getJSONObject("data");
                        cookbookid=cookbook.getInt("cookbookId");
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
    public void Uploadcontent(){
        Getcookbook getcookbook =new Getcookbook();
        getcookbook.start();
        try {
            getcookbook.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < materialList.getChildCount(); i++) {
            View temp = materialList.getChildAt(i);

            EditText mName = (EditText) temp.findViewById(R.id.material_name);
            EditText mNum = (EditText) temp.findViewById(R.id.material_quantity);
            maname=mName.getText().toString();
            manum=mNum.getText().toString();
            UploadM uploadm = new UploadM();
            uploadm.start();
            try {
                uploadm.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < stepList.getChildCount(); i++) {
            View temp = stepList.getChildAt(i);
            EditText stepDesc = (EditText) temp.findViewById(R.id.cookbook_step_desc);
            stInf=stepDesc.getText().toString();
            UploadS uploads = new UploadS();
            uploads.start();
            try {
                uploads.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mHandler.sendEmptyMessage(1);
        mHandler.sendEmptyMessage(3);
    }
    public class UploadM extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("ingredintName",maname);
                params.put("ingredintNum",manum);
                params.put("cookbookId",cookbookid);
                String res1 = RequestUtils.post("/ingredint/add", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {

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
    public class UploadS extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("stepInf",stInf);
                params.put("cookbookId",cookbookid);
                String res1 = RequestUtils.post("/step/add", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {

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
}
