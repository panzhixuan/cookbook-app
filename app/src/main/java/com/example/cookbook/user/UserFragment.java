package com.example.cookbook.user;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.cookbook.LoginActivity.ForgetpasswordpageActivity;
import com.example.cookbook.LoginActivity.LogginpageActivity;
import com.example.cookbook.LoginActivity.MainpageActivity;
import com.example.cookbook.R;
import com.example.cookbook.activity.test;
import com.example.cookbook.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFragment extends Fragment {
    private MainpageActivity mainpageActivity;
    private final int CODE_FOR_STORAGE = 1;

    private Map<String, Object> params;
    private ImageView headshot;
    private EditText editTextUserName;
    private Button buttonCaishi;
    private Button buttonCaixi;
    private Button buttonChanghe;
    private EditText editTextRegion;
    private Button logout;
    private byte[] photo=null;
    private int occsionId;
    private int tasteId;
    private int cuisineId;
    private Bitmap bitmap = null;

    private TextView textViewMyCollection;
    private TextView textViewMyView;
    private TextView textViewMyCaipu;
    private View view;
    private Button edit;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(mainpageActivity,"加载失败",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(mainpageActivity,"修改失败",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(mainpageActivity,"修改成功",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainpageActivity = (MainpageActivity) context;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.get("name").toString();
            Log.d("MyFragment", name);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.userpage, null);
        headshot=view.findViewById(R.id.headshot);
        editTextUserName = view.findViewById(R.id.username);
        buttonCaishi = view.findViewById(R.id.caishi);
        buttonCaixi = view.findViewById(R.id.caixi);
        buttonChanghe = view.findViewById(R.id.changhe);
        editTextRegion = view.findViewById(R.id.region);
        textViewMyCollection = view.findViewById(R.id.my_collection);
        textViewMyView = view.findViewById(R.id.my_view);
        textViewMyCaipu = view.findViewById(R.id.my_caipu);
        edit=view.findViewById(R.id.edit);
        logout=(Button)view.findViewById(R.id.logout);
        textViewMyCaipu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,MycookbookActivity.class);
                startActivity(intent);
            }
        });
        textViewMyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,BrowserpageActivity.class);
                startActivity(intent);
            }
        });
        textViewMyCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,CollectpageActivity.class);
                startActivity(intent);
            }
        });
        headshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectheader();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(mainpageActivity);
                confirmDialog.setTitle("修改个人信息");
                confirmDialog.setMessage("确定要修改个人信息吗？");
                confirmDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Update();
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
        buttonCaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caishi();
            }
        });
        buttonCaixi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caixi();
            }
        });
        buttonChanghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changhe();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(mainpageActivity);
                confirmDialog.setTitle("注销");
                confirmDialog.setMessage("确定要注销吗？");
                confirmDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = LogginpageActivity.sp.edit();
                        editor.putBoolean("CHECK", false);
                        editor.commit();
                        Intent intent=null;
                        intent=new Intent(mainpageActivity,LogginpageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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
        init();
        return view;
    }

    public static UserFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void init() {
        User user=User.getInstance();
        editTextUserName.setText(user.getUserName());
        editTextRegion.setText(user.getUserAddress());
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
        Log.d("pic123", "123");
        if (!user.getUserPhoto().equals("")) {
            Log.d("pic123", "456");
            Bitmap bitmap = RemotePictureOperation.downloadPicture(user.getUserPhoto(), getContext());
            Log.d("pic123", bitmap.toString());
            if (bitmap != null) {
                Log.d("pic123", "111");
                headshot.setImageBitmap(bitmap);
            }
        }
//        Getphoto getphoto =new Getphoto();
//        getphoto.start();
//        try {
//            getphoto.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public class Gettaste extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("tasteId", User.getInstance().getUserTaste());
                String res = RequestUtils.post("/taste/IdfindName", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        buttonCaishi.setText(jsonObject1.getString("data"));
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
                params.put("cuisineId", User.getInstance().getUserCuisine());
                String res = RequestUtils.post("/cuisine/IdfindName", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        buttonCaixi.setText(jsonObject1.getString("data"));
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
                params.put("occasionId", User.getInstance().getUserOccasion());
                String res = RequestUtils.post("/occasion/IdfindName", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        buttonChanghe.setText(jsonObject1.getString("data"));
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
            try {
                //判断图片是否在手机中有缓存
                if (F_GetBitmap.isEmpty(User.getInstance().getUserPhoto())) {
                    //从后端获取图片，图片格式是Bitmap
                    params = new HashMap<>();
                    //这个参数是要获取图片的图片名称，在这里开始发出后端数据请求
                    params.put("photo", User.getInstance().getUserPhoto());
                    String res = RequestUtils.post("/user/findphoto", params);
                    try {
                        JSONObject jsonObject1 = new JSONObject(res);
                        if (jsonObject1.getInt("code") == 200) {
                            JSONArray list1 = (JSONArray) jsonObject1.get("data");
                            byte[] all_image = new byte[list1.length()];
                            for (int j = 0; j < list1.length(); j++) {
                                all_image[j] = (byte) list1.getInt(j);
                            }
                            F_GetBitmap.setInSDBitmap(all_image,User.getInstance().getUserPhoto() );
                            InputStream input = null;
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 1;
                            input = new ByteArrayInputStream(all_image);
                            @SuppressWarnings({ "rawtypes", "unchecked" })
                            SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(input, null, options));
                            Bitmap imageData = (Bitmap) softRef.get();
                            headshot.setImageBitmap(imageData);
                        } else {
                            //展示获取错误的消息
                            mHandler.sendEmptyMessage(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //表示图片已存在手机缓存中，只需要从手机中取出即可
                else{
                    Bitmap imageData = F_GetBitmap.getSDBitmap(User.getInstance().getUserPhoto() );// �õ�����BitMap���͵�ͼƬ����
                    if (F_GetBitmap.bitmap != null && !F_GetBitmap.bitmap.isRecycled()) {
                        F_GetBitmap.bitmap = null;
                    }
                    headshot.setImageBitmap(imageData);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void caishi(){
        final String[] caishitype = {"清淡", "辣","酸"};
        new AlertDialog.Builder(mainpageActivity).setTitle("选择口味").setSingleChoiceItems(caishitype, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                buttonCaishi.setText(caishitype[i]);
            }
        }).show();
    }
    public void caixi(){
        final String[] caixitype = {"川菜", "淮扬菜","徽菜","鲁菜","闽菜","粤菜","湘菜","浙菜"};
        new AlertDialog.Builder(mainpageActivity).setTitle("选择口味").setSingleChoiceItems(caixitype, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                buttonCaixi.setText(caixitype[i]);
            }
        }).show();
    }
    public void changhe(){
        final String[] changhetype = {"家", "饭店","快餐店"};
        new AlertDialog.Builder(mainpageActivity).setTitle("选择口味").setSingleChoiceItems(changhetype, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                buttonChanghe.setText(changhetype[i]);
            }
        }).show();
    }

    //开始从相册选择图片
    public void selectheader() {
        initPermission();
    }

    //设置相册和手机存储权限
    private void initPermission() {
        if (ContextCompat.checkSelfPermission(mainpageActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mainpageActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(mainpageActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_FOR_STORAGE);
            } else {
                ActivityCompat.requestPermissions(mainpageActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_FOR_STORAGE);
                Toast.makeText(mainpageActivity, "请设置相应权限", Toast.LENGTH_SHORT).show();
            }
        } else {
            selectImage();
        }
    }

    //设置确认获取权限后动作
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_FOR_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }
    }

    //从相册选择图片
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

    //根据选择的图片进行在页面的显示和获取图片的byte[]用于上传使用
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if (data != null) {
                int version = android.os.Build.VERSION.SDK_INT;
                if (version > 24) {
                    File file = null;
                    Log.d("zhaopian", "111");
                    Uri uri = data.getData();
                    try {
                        file = F_GetByte.getFileFromMediaUri(getActivity(), uri);
                        Bitmap orginBitmap = F_GetByte.getBitmapFormUri(getActivity(), Uri.fromFile(file));
                        int degree = F_GetByte.getBitmapDegree(file.getAbsolutePath());
                        bitmap = F_GetByte.rotateBitmapByDegree(orginBitmap, degree);
                        headshot.setImageBitmap(bitmap);
                        photo = F_GetByte.Bitmap2Bytes(bitmap);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else{
                    if (data.getExtras() != null) {
                        bitmap = data.getExtras().getParcelable("data");
                        headshot.setImageBitmap(bitmap);
                        photo= F_GetByte.Bitmap2Bytes(bitmap);
                    }
                }
            }
        }
    }

    public void Update(){
        if(editTextUserName.getText().toString().equals("")){
            Toast.makeText(mainpageActivity,"名字不能为空",Toast.LENGTH_LONG).show();
        }
        else if(editTextRegion.getText().toString().equals("")){
            Toast.makeText(mainpageActivity,"地区不能为空",Toast.LENGTH_LONG).show();
        }
        else{
            GetoccsionId getoccsionid =new GetoccsionId();
            getoccsionid.start();
            try {
                getoccsionid.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            GettasteId gettasteId =new GettasteId();
            gettasteId.start();
            try {
                gettasteId.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            GetcuisineId getcuisineId =new GetcuisineId();
            getcuisineId.start();
            try {
                getcuisineId.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Upload upload =new Upload();
//            upload.start();
//            try {
//                upload.join();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            Edit edit =new Edit();
            edit.start();
            try {
                edit.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bitmap != null) {
                RemotePictureOperation.uploadPicture(getContext(), bitmap, User.getInstance().getUserPhoto());
            }
        }
    }
    public class GetoccsionId extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("occasionName", buttonChanghe.getText().toString());
                String res = RequestUtils.post("/occasion/NamefindId", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        occsionId=jsonObject1.getInt("data");
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
    public class GettasteId extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("tasteName", buttonCaishi.getText().toString());
                String res = RequestUtils.post("/taste/NamefindId", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        tasteId=jsonObject1.getInt("data");
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
    public class GetcuisineId extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cuisineName", buttonCaixi.getText().toString());
                String res = RequestUtils.post("/cuisine/NamefindId", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        cuisineId=jsonObject1.getInt("data");
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
    public class Edit extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", User.getInstance().getUserId());
                params.put("userName", editTextUserName.getText().toString());
                params.put("userAddress",editTextRegion.getText().toString());
                params.put("userTaste",tasteId);
                params.put("userOccasion",occsionId);
                params.put("userCuisine",cuisineId);
                String res1 = RequestUtils.post("/user/update", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        User.getInstance().setUserName(editTextUserName.getText().toString());
                        User.getInstance().setUserAddress(editTextRegion.getText().toString());
                        User.getInstance().setUserTaste(tasteId);
                        User.getInstance().setUserOccasion(occsionId);
                        User.getInstance().setUserCuisine(cuisineId);
                        SharedPreferences.Editor editor = LogginpageActivity.sp.edit();
                        editor.putInt("userTaste",tasteId);
                        editor.putInt("userCuisine",cuisineId);
                        editor.putInt("userOccasion",occsionId);
                        editor.putString("userAddress",editTextRegion.getText().toString());
                        editor.putString("userName",editTextUserName.getText().toString());
                        editor.commit();
                        mHandler.sendEmptyMessage(2);
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

    //上传图片到后端
    public class Upload extends Thread{
        @Override
        public void run(){
            try {
                //参数photo是要上传图片的byte[]数组，name是上传图片的名称,参数的名字没有规定只要和后端一致就可以了
                params = new HashMap<>();
                params.put("photo", photo);
                params.put("name",User.getInstance().getUserPhoto());
                String res1 = RequestUtils.post("/user/uploadheader", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        F_GetBitmap.setInSDBitmap(photo,User.getInstance().getUserPhoto());
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
}
