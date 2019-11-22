package com.example.cookbook.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.cookbook.R;
import com.example.cookbook.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookbookdetailpageActivity extends Activity {
    private Map<String, Object> params;
    private String cookbook_id;
    private TextView cookbookname;
    private TextView username;
    private TextView caishi;
    private TextView caixi;
    private TextView changhe;
    private ImageView cover;
    private TextView nutrition;
    private TextView tips;
    private LinearLayout materialList;
    private LinearLayout stepList;
    private Button favor;
    private Button publish;
    private Button delpublish;
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
    private boolean judge=false;
    private boolean judge2=false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(CookbookdetailpageActivity.this,"获取失败",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(CookbookdetailpageActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 2:
                    if(judge2==false){
                        publish.setVisibility(View.VISIBLE);
                        delpublish.setVisibility(View.GONE);
                    }
                    else{
                        publish.setVisibility(View.GONE);
                        delpublish.setVisibility(View.VISIBLE);
                    }
                    CommentAdapter adapter = new CommentAdapter(CookbookdetailpageActivity.this, R.layout.commentlist, CommentList);
                    ListView listView = (ListView) findViewById(R.id.listview);
                    listView.setAdapter(adapter);
                    break;
                case 3:
                    loadComment();
                    break;
                case 4:
                    Toast.makeText(CookbookdetailpageActivity.this,"删除菜谱成功",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 5:
                    Toast.makeText(CookbookdetailpageActivity.this,"删除评论成功",Toast.LENGTH_LONG).show();
                    judge2=false;
                    break;
                default:
                    break;
            }
        }
    };
    private List<Comment> CommentList = new ArrayList<Comment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cookbookPhoto = intent.getStringExtra("cookbookPhoto");
        setContentView(R.layout.cookbookdetailpage);
        initview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(3);
    }

//    public void setListeners(View.OnClickListener onClickListener){
//        findViewById(R.id.favor).setOnClickListener(onClickListener);
//        findViewById(R.id.publish).setOnClickListener(onClickListener);
//    }

    private void initview(){
        Getcookbook getcookbook =new Getcookbook();
        getcookbook.start();
        try {
            getcookbook.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Checkbrowser checkbrowser =new Checkbrowser();
        checkbrowser.start();
        try {
            checkbrowser.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cookbookname=findViewById(R.id.cookbookname);
        username=findViewById(R.id.username);
        caishi=findViewById(R.id.caishi);
        caixi=findViewById(R.id.caixi);
        changhe=findViewById(R.id.changhe);
        cover=findViewById(R.id.cover);
        nutrition=findViewById(R.id.nutrition);
        tips=findViewById(R.id.tips);
        materialList=findViewById(R.id.material_list);
        stepList=findViewById(R.id.step_list);
        favor=findViewById(R.id.favor);
        publish=findViewById(R.id.publish);
        delpublish=findViewById(R.id.delpublish);
        cookbookname.setText(cookbookName);
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
        Checkfavor checkfavor =new Checkfavor();
        checkfavor.start();
        try {
            checkfavor.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Getuser getuser =new Getuser();
        getuser.start();
        try {
            getuser.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(judge==true) {
                    Addfavor addfavor= new Addfavor();
                    addfavor.start();
                    try {
                        addfavor.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    judge=false;
                    favor.setText("取消收藏");
                    Toast.makeText(CookbookdetailpageActivity.this,"收藏成功",Toast.LENGTH_LONG).show();
                }
                else {
                    Delfavor delfavor = new Delfavor();
                    delfavor.start();
                    try {
                        delfavor.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    judge=true;
                    favor.setText("添加收藏");
                    Toast.makeText(CookbookdetailpageActivity.this,"取消收藏成功",Toast.LENGTH_LONG).show();
                }
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CookbookdetailpageActivity.this,CommentpageActivity.class);
                intent.putExtra("cookbook_id",cookbookId);
                startActivity(intent);
            }
        });
        delpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delcomment delcomment =new Delcomment();
                delcomment.start();
                try {
                    delcomment.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class Delcomment extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", User.getInstance().getUserId());
                params.put("cookbookId", cookbookId);
                String res1 = RequestUtils.post("/comment/delete", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        mHandler.sendEmptyMessage(5);
                        finish();
                        Intent intent=new Intent(CookbookdetailpageActivity.this,CookbookdetailpageActivity.class);
                        intent.putExtra("cookbookPhoto",cookbookPhoto);
                        startActivity(intent);
                    }
                    else {
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

    public class Getuser extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", userId);
                String res1 = RequestUtils.post("/user/getuserbyId", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        username.setText(jsonObject1.getJSONObject("data").getString("userName"));
                    }
                    else {
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

    public class Checkbrowser extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", userId);
                params.put("cookbookId", cookbookId);
                String res1 = RequestUtils.post("/visit/get", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 400) {
                        Addbrowser addbrowser =new Addbrowser();
                        addbrowser.start();
                        try {
                            addbrowser.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if(jsonObject1.getInt("code") == 200){

                    }
                    else {
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
    public class Checkfavor extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", userId);
                params.put("cookbookId", cookbookId);
                String res1 = RequestUtils.post("/likes/get", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 400) {
                        favor.setText("添加收藏");
                        judge=true;
                    }
                    else if(jsonObject1.getInt("code") == 200){
                        favor.setText("取消收藏");
                        judge=false;
                    }
                    else {
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
    public class Addfavor extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", userId);
                params.put("cookbookId", cookbookId);
                params.put("cookbookLikenum", cookbookLikenum);
                String res1 = RequestUtils.post("/likes/add", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {

                    }
                    else {
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
    public class Delfavor extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", userId);
                params.put("cookbookId", cookbookId);
                params.put("cookbookLikenum", cookbookLikenum);
                String res1 = RequestUtils.post("/likes/delete", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {

                    }
                    else {
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
    public class Addbrowser extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", userId);
                params.put("cookbookId", cookbookId);
                params.put("cookbookVisitnum", cookbookVisitnum);
                String res1 = RequestUtils.post("/visit/add", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {

                    }
                    else {
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
                        caishi.setText(jsonObject1.getString("data"));
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
                        caixi.setText(jsonObject1.getString("data"));
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
                        changhe.setText(jsonObject1.getString("data"));
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
            Bitmap bitmap = RemotePictureOperation.downloadPicture(cookbookPhoto, CookbookdetailpageActivity.this);
            if(bitmap != null){
                cover.setImageBitmap(bitmap);
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
                            final View temp = View.inflate(CookbookdetailpageActivity.this, R.layout.material_item1, null);
                            TextView materialName=(TextView) temp.findViewById(R.id.material_name);
                            materialName.setText(name);
                            TextView materialNumber=(TextView) temp.findViewById(R.id.material_quantity);
                            materialNumber.setText(num);
                            Button delete = (Button) temp.findViewById(R.id.material_delete);
                            delete.setVisibility(View.GONE);
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
                            final View temp = View.inflate(CookbookdetailpageActivity.this, R.layout.add_cookbook_step1, null);
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
    public class Getcomment extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookId", cookbookId);
                String res = RequestUtils.post("/comment/get", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        CommentList.clear();
                        for(int i=0;i<list.length();i++){
                            if(list.getJSONObject(i).getInt("userId")==User.getInstance().getUserId())
                                judge2=true;
                            Comment comment=new Comment();
                            comment.setCommentInf(list.getJSONObject(i).getString("commentInf"));
                            comment.setCookbookId(list.getJSONObject(i).getInt("cookbookId"));
                            comment.setUserId(list.getJSONObject(i).getInt("userId"));
                            CommentList.add(comment);
                        }
                        mHandler.sendEmptyMessage(2);
                    }
                    else if(jsonObject1.getInt("code") == 400){}
                    else {
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

    private void loadComment() {
        Getcomment getcomment =new Getcomment();
        getcomment.start();
        try {
            getcomment.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void addbrowse(String id,Integer num){
//        Cookbook p2 = new Cookbook();
//        p2.setCb_browse(num);
//        p2.update(id, new UpdateListener() {
//
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    Log.i("BMOB", "addbrowse");
//                }else{
//                    Log.i("BMOB", e.toString());
//                }
//            }
//
//        });
//    }

//    @Override
//    public void favorite() {
//        BmobQuery<Cookbook> bmobQuery = new BmobQuery<Cookbook>();
//        bmobQuery.getObject(cookbook_id, new QueryListener<Cookbook>() {
//            @Override
//            public void done(final Cookbook cookbook, BmobException e) {
//                if (e == null) {
//                    if(favor.getText().equals("取消收藏")) {
//                        Integer a = cookbook.getCb_favorite();
//                        a -= 1;
//                        dellikes(cookbook);
//                        dellikes1(cookbook);
//                        updatefavarite(cookbook, a);
//                        favor.setText("添加收藏");
//                        Toast.makeText(CookbookdetailpageActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Integer a = cookbook.getCb_favorite();
//                        a += 1;
//                        addlikes(cookbook);
//                        addlikes1(cookbook);
//                        updatefavarite(cookbook, a);
//                        favor.setText("取消收藏");
//                        Toast.makeText(CookbookdetailpageActivity.this, "添加收藏成功", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Log.i("bmob", "失败：" + e.getMessage());
//                }
//            }
//        });
//    }
//    private void loadlikes(Cookbook cookbook) {
//        final User user=BmobUser.getCurrentUser(User.class);
//        BmobQuery<User> query = new BmobQuery<User>();
//        Cookbook cookbook1 = new Cookbook();
//        cookbook1.setObjectId(cookbook.getObjectId());
//        query.addWhereRelatedTo("likes", new BmobPointer(cookbook1));
//        query.findObjects(new FindListener<User>() {
//            @Override
//            public void done(List<User> object,BmobException e) {
//                if(e==null){
//                    int n = object.size();
//                    String s=String.valueOf(n);
//                    for (int i = 0; i < n; i++) {
//                        User usertemp = object.get(i);
//                        if(usertemp.getObjectId().equals(user.getObjectId()))
//                            favor.setText("取消收藏");
//                        else
//                            favor.setText("添加收藏");
//                    }
//                    Log.i("bmob",s);
//                }else{
//                    Log.i("bmob","失败："+e.getMessage());
//                }
//            }
//
//        });
//    }
//    private void addlikes(Cookbook cookbook) {
//        User user=BmobUser.getCurrentUser(User.class);
//        BmobRelation relation = new BmobRelation();
//        relation.add(user);
//        cookbook.setLikes(relation);
//        cookbook.update(new UpdateListener() {
//
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    Log.i("bmob","用户B和该帖子关联成功");
//                }else{
//                    Log.i("bmob","失败："+e.getMessage());
//                }
//            }
//
//        });
//    }
//    private void addlikes1(Cookbook cookbook) {
//        User user=BmobUser.getCurrentUser(User.class);
//        BmobRelation relation = new BmobRelation();
//        relation.add(cookbook);
//        user.setLikes(relation);
//        user.update(new UpdateListener() {
//
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    Log.i("bmob","该帖子和用户关联成功");
//                }else{
//                    Log.i("bmob","失败："+e.getMessage());
//                }
//            }
//
//        });
//    }
//    private void dellikes(Cookbook cookbook){
//        User user=BmobUser.getCurrentUser(User.class);
//        BmobRelation relation = new BmobRelation();
//        relation.remove(user);
//        cookbook.setLikes(relation);
//        cookbook.update(new UpdateListener() {
//
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    Log.i("bmob","关联关系删除成功");
//                }else{
//                    Log.i("bmob","失败："+e.getMessage());
//                }
//            }
//
//        });
//
//    }
//    private void dellikes1(Cookbook cookbook){
//        User user=BmobUser.getCurrentUser(User.class);
//        BmobRelation relation = new BmobRelation();
//        relation.remove(cookbook);
//        user.setLikes(relation);
//        user.update(new UpdateListener() {
//
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    Log.i("bmob","关联关系111删除成功");
//                }else{
//                    Log.i("bmob","失败："+e.getMessage());
//                }
//            }
//
//        });
//
//    }
//    private void updatefavarite(Cookbook cookbook,Integer favorite_num){
//        cookbook.setCb_favorite(favorite_num);
//        cookbook.update(cookbook.getObjectId(), new UpdateListener() {
//
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    System.out.println("done");
//                }else{
//                    System.out.println("false");
//                }
//            }
//
//        });
//    }
//    private void adduserbrowse(Cookbook cookbook) {
//        User user=BmobUser.getCurrentUser(User.class);
//        BmobRelation relation = new BmobRelation();
//        relation.add(cookbook);
//        user.setBrowse(relation);
//        user.update(new UpdateListener() {
//
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    Log.i("bmob","该帖子和用户关联成功");
//                }else{
//                    Log.i("bmob","失败："+e.getMessage());
//                }
//            }
//
//        });
//    }
}
