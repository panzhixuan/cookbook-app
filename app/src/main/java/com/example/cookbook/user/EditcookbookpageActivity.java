package com.example.cookbook.user;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditcookbookpageActivity extends Activity {
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
    private int cookbookId;
    private int cookbookLikenum;
    private int cookbookVisitnum;
    private String cookbookTip;
    private String cookbookNutrition;
    private String cookbookPhoto;
    private String cookbookName;
    private int userId;
    private int cookbookTaste;
    private int cookbookCuisine;
    private int cookbookOccasion;
    private Button delete;
    public static ProgressDialog pd;
    private Bitmap bitmap = null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(EditcookbookpageActivity.this,"修改失败",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(EditcookbookpageActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 2:
                    pd.show();
                    break;
                case 3:
                    pd.dismiss();
                    break;
                case 4:
                    Toast.makeText(EditcookbookpageActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcookbookpage);
        Intent intent = getIntent();
        cookbookPhoto = intent.getStringExtra("cookbookPhoto");
        pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage("加载中，请稍后........");
        initview();
    }
    public void initview(){
        mHandler.sendEmptyMessage(2);
        Getcookbook getcookbook =new Getcookbook();
        getcookbook.start();
        try {
            getcookbook.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        stepList = (LinearLayout) findViewById(R.id.step_list);
        cover=(ImageView)findViewById(R.id.cover);
        material_add=(Button)findViewById(R.id.material_add);
        add_step=(Button)findViewById(R.id.add_step);
        delete_step=(Button)findViewById(R.id.delete_step);
        create_cookbook=(Button)findViewById(R.id.create_cookbook);
        cookbook_name = (EditText) findViewById(R.id.cookbookname);
        nutrition = (EditText) findViewById(R.id.nutrition);
        tips= (EditText) findViewById(R.id.tips);
        delete=(Button) findViewById(R.id.delete);
        cookbook_name.setText(cookbookName);
        nutrition.setText(cookbookNutrition);
        tips.setText(cookbookTip);
        Gettaste gettaste =new Gettaste();
        gettaste.start();
        try {
            gettaste.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Getcuisine getcuisine =new Getcuisine();
        getcuisine.start();
        try {
            getcuisine.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Getoccsion getoccsion =new Getoccsion();
        getoccsion.start();
        try {
            getoccsion.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Getphoto getphoto =new Getphoto();
        getphoto.start();
        try {
            getphoto.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetM getM =new GetM();
        getM.start();
        try {
            getM.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetS getS =new GetS();
        getS.start();
        try {
            getS.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcover();
            }
        });
        create_cookbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(EditcookbookpageActivity.this);
                confirmDialog.setTitle("修改菜谱");
                confirmDialog.setMessage("确定要修改菜谱吗？");
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
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(EditcookbookpageActivity.this);
                confirmDialog.setTitle("删除菜谱");
                confirmDialog.setMessage("确定要删除菜谱吗？");
                confirmDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Delete delete =new Delete();
                        delete.start();
                        try {
                            delete.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        mHandler.sendEmptyMessage(3);
    }
    public class Delete extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookId", cookbookId);
                String res = RequestUtils.post("/cookbook/delete", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        mHandler.sendEmptyMessage(4);
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
    public class Gettaste extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("tasteId", cookbookTaste);
                String res = RequestUtils.post("/taste/IdfindName", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        String[] caishitype = {"清淡", "辣","酸"};
                        int pos1=0;
                        for(int i=0;i<caishitype.length;i++){
                            if(caishitype[i].equals(jsonObject1.getString("data"))){
                                pos1=i;
                                break;
                            }
                        }
                        caishispinner.setSelection(pos1,true);
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

    public class Getcuisine extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cuisineId", cookbookCuisine);
                String res = RequestUtils.post("/cuisine/IdfindName", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        String[] caixitype = {"川菜", "淮扬菜","徽菜","鲁菜","闽菜","粤菜","湘菜","浙菜"};
                        int pos2=0;
                        for(int i=0;i<caixitype.length;i++){
                            if(caixitype[i].equals(jsonObject1.getString("data"))){
                                pos2=i;
                                break;
                            }
                        }
                        caixispinner.setSelection(pos2,true);
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

    public class Getoccsion extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("occasionId", cookbookOccasion);
                String res = RequestUtils.post("/occasion/IdfindName", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        String[] changhetype = {"家", "饭店","快餐店"};
                        int pos=0;
                        for(int i=0;i<changhetype.length;i++){
                            if(changhetype[i].equals(jsonObject1.getString("data"))){
                                pos=i;
                                break;
                            }
                        }
                        changhespinner.setSelection(pos,true);
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

    public class Getphoto extends Thread{
        @Override
        public void run(){
            Bitmap bitmap1 = RemotePictureOperation.downloadPicture(cookbookPhoto, EditcookbookpageActivity.this);
            if(bitmap1 != null){
                cover.setImageBitmap(bitmap1);
                bitmap = bitmap1;
            }
//            try {
//                if (F_GetBitmap.isEmpty(cookbookPhoto)) {
//                    params = new HashMap<>();
//                    params.put("photo", cookbookPhoto);
//                    String res = RequestUtils.post("/cookbook/findphoto", params);
//                    try {
//                        JSONObject jsonObject1 = new JSONObject(res);
//                        if (jsonObject1.getInt("code") == 200) {
//                            JSONArray list1 = (JSONArray) jsonObject1.get("data");
//                            byte[] all_image = new byte[list1.length()];
//                            for (int j = 0; j < list1.length(); j++) {
//                                all_image[j] = (byte) list1.getInt(j);
//                            }
//                            F_GetBitmap.setInSDBitmap(all_image,cookbookPhoto );
//                            InputStream input = null;
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = 1;
//                            input = new ByteArrayInputStream(all_image);
//                            @SuppressWarnings({ "rawtypes", "unchecked" })
//                            SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(input, null, options));
//                            Bitmap imageData = (Bitmap) softRef.get();
//                            cover.setImageBitmap(imageData);
//                        } else {
//                            mHandler.sendEmptyMessage(0);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    Bitmap imageData = F_GetBitmap.getSDBitmap(cookbookPhoto );// �õ�����BitMap���͵�ͼƬ����
//                    if (F_GetBitmap.bitmap != null && !F_GetBitmap.bitmap.isRecycled()) {
//                        F_GetBitmap.bitmap = null;
//                    }
//                    cover.setImageBitmap(imageData);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public class GetM extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookId", cookbookId);
                String res = RequestUtils.post("/ingredint/get", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject jsonObject=list.getJSONObject(i);
                            String name=jsonObject.getString("ingredintName");
                            String num=jsonObject.getString("ingredintNum");
                            final View temp = View.inflate(EditcookbookpageActivity.this, R.layout.material_item, null);
                            EditText materialName=(EditText)temp.findViewById(R.id.material_name);
                            materialName.setText(name);
                            EditText materialNumber=(EditText)temp.findViewById(R.id.material_quantity);
                            materialNumber.setText(num);
                            Button delete = (Button) temp.findViewById(R.id.material_delete);
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (materialList.getChildCount() > 1) {
                                        materialList.removeView(temp);
                                    }
                                }
                            });
                            materialList.addView(temp, -1);
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

    public class GetS extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookId", cookbookId);
                String res = RequestUtils.post("/step/get", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        for (int i = 0; i <list.length(); i++) {
                            JSONObject jsonObject=list.getJSONObject(i);
                            String step=jsonObject.getString("stepInf");
                            final View temp = View.inflate(EditcookbookpageActivity.this, R.layout.add_cookbook_step, null);
                            TextView stepnum = (TextView) temp.findViewById(R.id.cookbook_step_number);
                            TextView stepDesc = (TextView) temp.findViewById(R.id.cookbook_step_desc);
                            String num="" +String.valueOf(i+1)+ ":";
                            stepnum.setText(num);
                            stepDesc.setText(step);
                            stepList.addView(temp, -1);
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
            Toast.makeText(EditcookbookpageActivity.this,"小贴士不能为空",Toast.LENGTH_LONG).show();
        }
        else if(nutrition.getText().toString().equals("")){
            Toast.makeText(EditcookbookpageActivity.this,"营养价值不能为空",Toast.LENGTH_LONG).show();
        }
        else if(cookbook_name.getText().toString().equals("")){
            Toast.makeText(EditcookbookpageActivity.this,"菜谱名字不能为空",Toast.LENGTH_LONG).show();
        }
        else if(bitmap==null){
            Toast.makeText(EditcookbookpageActivity.this,"未选择菜谱图片",Toast.LENGTH_LONG).show();
        }
        else if(stepList.getChildCount()==0||materialList.getChildCount()==0){
            Toast.makeText(EditcookbookpageActivity.this,"未添加步骤和材料",Toast.LENGTH_LONG).show();
        }
        else {
            for (int i = 0; i < materialList.getChildCount(); i++) {
                View temp = materialList.getChildAt(i);

                EditText mName = (EditText) temp.findViewById(R.id.material_name);
                if (mName.getText().toString().equals("")) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(EditcookbookpageActivity.this);
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
                            new AlertDialog.Builder(EditcookbookpageActivity.this);
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
                            new AlertDialog.Builder(EditcookbookpageActivity.this);
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
            Updatecookbook updatecookbook= new Updatecookbook();
            updatecookbook.start();
            try {
                updatecookbook.join();
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
                Toast.makeText(EditcookbookpageActivity.this, "请设置相应权限", Toast.LENGTH_SHORT).show();
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
            RemotePictureOperation.uploadPicture(EditcookbookpageActivity.this, bitmap, cookbookPhoto);
//            try {
//                params = new HashMap<>();
//                params.put("photo", image);
//                params.put("name", cookbookPhoto);
//                String res1 = RequestUtils.post("/cookbook/uploadcover", params);
//                try {
//                    JSONObject jsonObject1 = new JSONObject(res1);
//                    if (jsonObject1.getInt("code") == 200) {
//                        F_GetBitmap.setInSDBitmap(image,cookbookPhoto);
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
    public class Updatecookbook extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookId",cookbookId);
                params.put("cookbookLikenum",cookbookLikenum);
                params.put("cookbookVisitnum",cookbookVisitnum);
                params.put("cookbookTip",tips.getText().toString());
                params.put("cookbookNutrition",nutrition.getText().toString());
                params.put("cookbookPhoto",cookbookPhoto);
                params.put("cookbookName",cookbook_name.getText().toString());
                params.put("userId", userId);
                params.put("cookbookTaste",tasteId);
                params.put("cookbookCuisine",cuisineId);
                params.put("cookbookOccasion",occsionId);
                String res1 = RequestUtils.post("/cookbook/update", params);
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
                params.put("cookbookPhoto",cookbookPhoto);
                String res1 = RequestUtils.post("/cookbook/findbyPhoto", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONObject cookbook=jsonObject1.getJSONObject("data");
                        cookbookId=cookbook.getInt("cookbookId");
                        cookbookLikenum=cookbook.getInt("cookbookLikenum");
                        cookbookVisitnum=cookbook.getInt("cookbookVisitnum");
                        cookbookTip=cookbook.getString("cookbookTip");
                        cookbookNutrition=cookbook.getString("cookbookNutrition");
                        cookbookName=cookbook.getString("cookbookName");
                        userId=cookbook.getInt("userId");
                        cookbookTaste=cookbook.getInt("cookbookTaste");
                        cookbookCuisine=cookbook.getInt("cookbookCuisine");
                        cookbookOccasion=cookbook.getInt("cookbookOccasion");
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
        DeleteM deleteM = new DeleteM();
        deleteM.start();
        try {
            deleteM.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DeleteS deleteS = new DeleteS();
        deleteS.start();
        try {
            deleteS.join();
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
                params.put("cookbookId",cookbookId);
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
    public class DeleteM extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookId",cookbookId);
                String res1 = RequestUtils.post("/ingredint/delete", params);
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
    public class DeleteS extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookId",cookbookId);
                String res1 = RequestUtils.post("/step/delete", params);
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
                params.put("cookbookId",cookbookId);
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
