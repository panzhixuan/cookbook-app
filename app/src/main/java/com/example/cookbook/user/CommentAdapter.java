package com.example.cookbook.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cookbook.R;
import com.example.cookbook.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentAdapter extends ArrayAdapter {
    private final int resourceId;
    private Context context;
    private View view;
    private Map<String, Object> params;

    public CommentAdapter(Context context, int textViewResourceId, List<Comment> objects) {
        super(context, textViewResourceId,objects);
        this.context=context;
        resourceId = textViewResourceId;
    }
    static class ViewHolder {
        private ImageView header;
        private TextView username;
        private TextView commentcontent;
    }
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Comment comment = (Comment) getItem(position);
        ViewHolder myViews;
        if (convertView == null) {
            myViews = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            myViews.header=(ImageView)convertView.findViewById(R.id.cookbook_head_image);
            myViews.username=(TextView)convertView.findViewById(R.id.cookbook_username);
            myViews.commentcontent=(TextView)convertView.findViewById(R.id.cookbook_comment);
            convertView.setTag(myViews);
        } else {
            myViews = (ViewHolder) convertView.getTag();
        }
        Getuser getuser =new Getuser(comment.getUserId());
        getuser.start();
        try {
            getuser.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myViews.username.setText(getuser.getUserName());
        Getphoto getphoto =new Getphoto(getuser.getUserPhoto());
        getphoto.start();
        try {
            getphoto.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myViews.header.setImageBitmap(getphoto.getBitmap());
        myViews.commentcontent.setText(comment.getCommentInf());
        return convertView;
    }

    public class Getuser extends Thread{
        private int userId;
        private String userName;
        private String userPhoto;
        public Getuser(int userId){this.userId=userId;}

        public String getUserPhoto() {
            return userPhoto;
        }

        public String getUserName() {
            return userName;
        }

        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", userId);
                String res1 = RequestUtils.post("/user/getuserbyId", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res1);
                    if (jsonObject1.getInt("code") == 200) {
                        this.userName=jsonObject1.getJSONObject("data").getString("userName");
                        this.userPhoto=jsonObject1.getJSONObject("data").getString("userPhoto");
                    }
                    else {

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
        private String userPhoto;
        private Bitmap bitmap;
        public Getphoto(String userPhoto){this.userPhoto=userPhoto;}

        public Bitmap getBitmap() {
            return bitmap;
        }

        @Override
        public void run(){
            Bitmap bitmap = RemotePictureOperation.downloadPicture(userPhoto, getContext());
            if(bitmap != null){
                this.bitmap = bitmap;
            }
//            try {
//                if (F_GetBitmap.isEmpty(userPhoto)) {
//                    params = new HashMap<>();
//                    params.put("photo", userPhoto);
//                    String res = RequestUtils.post("/user/findphoto", params);
//                    try {
//                        JSONObject jsonObject1 = new JSONObject(res);
//                        if (jsonObject1.getInt("code") == 200) {
//                            JSONArray list1 = (JSONArray) jsonObject1.get("data");
//                            byte[] all_image = new byte[list1.length()];
//                            for (int j = 0; j < list1.length(); j++) {
//                                all_image[j] = (byte) list1.getInt(j);
//                            }
//                            F_GetBitmap.setInSDBitmap(all_image,userPhoto );
//                            InputStream input = null;
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = 1;
//                            input = new ByteArrayInputStream(all_image);
//                            @SuppressWarnings({ "rawtypes", "unchecked" })
//                            SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(input, null, options));
//                            Bitmap imageData = (Bitmap) softRef.get();
//                            this.bitmap=imageData;
//                        } else {
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    Bitmap imageData = F_GetBitmap.getSDBitmap(userPhoto);// �õ�����BitMap���͵�ͼƬ����
//                    if (F_GetBitmap.bitmap != null && !F_GetBitmap.bitmap.isRecycled()) {
//                        F_GetBitmap.bitmap = null;
//                    }
//                    this.bitmap=imageData;
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}
