package com.example.cookbook.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.cookbook.R;
import com.example.cookbook.util.Cookbook;
import com.example.cookbook.util.F_GetBitmap;
import com.example.cookbook.util.RemotePictureOperation;
import com.example.cookbook.util.RequestUtils;
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

public class TypeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int resourceId;
    private Context context;
    private View view;
    private Map<String, Object> params;
    private List<Cookbook> cookbooks;

    private static int TYPE_FOOT = 1;
    private static int TYPE_ITEM = 0;
    private boolean hasmore = true;

    public TypeRecyclerViewAdapter(Context context, int textViewResourceId, List<Cookbook> objects) {
        this.context=context;
        resourceId = textViewResourceId;
        this.cookbooks = objects;
    }

    class viewHolder extends RecyclerView.ViewHolder{
        private ImageView cover;
        private TextView name;
        private TextView username;
        private TextView browsenum;
        private TextView collectnum;
        private FrameLayout frameLayout;
        public viewHolder(View view){
            super(view);
            cover=(ImageView)view.findViewById(R.id.cover);
            name=(TextView)view.findViewById(R.id.name);
            username=(TextView)view.findViewById(R.id.username);
            browsenum=(TextView)view.findViewById(R.id.totalbrowsenum);
            collectnum=(TextView)view.findViewById(R.id.totalcolnum);
            frameLayout=(FrameLayout)view.findViewById(R.id.typelist);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        private LinearLayout loading;
        private LinearLayout noneload;

        public FootHolder(View itemView) {
            super(itemView);
            loading = itemView.findViewById(R.id.loading);
            noneload = itemView.findViewById(R.id.noneload);
        }
    }

    public void hasMore(boolean hasmore){
        this.hasmore = hasmore;
    }

    @Override
    public int getItemViewType(int position){
        if(position == getItemCount() - 1){
            return TYPE_FOOT;
        }
        return TYPE_ITEM;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
            holder = new viewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loadmorelist, parent, false);
            holder = new FootHolder(view);
        }
        return holder;
    }

    public Cookbook getItem(int position){
        if(position >= cookbooks.size() || position < 0){
            return null;
        }
        return cookbooks.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof viewHolder) {
            final Cookbook cookbook = (Cookbook) getItem(position);
            Getphoto getphoto = new Getphoto(cookbook.getCookbookPhoto());
            getphoto.start();
            try {
                getphoto.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((viewHolder)holder).cover.setImageBitmap(getphoto.getBitmap());
            Getuser getuser = new Getuser(cookbook.getUserId());
            getuser.start();
            try {
                getuser.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((viewHolder)holder).username.setText(getuser.getUserName());
            ((viewHolder)holder).name.setText(cookbook.getCookbookName());
            ((viewHolder)holder).browsenum.setText(String.valueOf(cookbook.getCookbookVisitnum()));
            ((viewHolder)holder).collectnum.setText(String.valueOf(cookbook.getCookbookLikenum()));
            ((viewHolder)holder).frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CookbookdetailpageActivity.class);
                    intent.putExtra("cookbookPhoto", cookbook.getCookbookPhoto());
                    context.startActivity(intent);
                }
            });
        }
        else{
            if(getItemCount() < 7 || hasmore == false){
                ((FootHolder)holder).noneload.setVisibility(View.VISIBLE);
                ((FootHolder)holder).loading.setVisibility(View.GONE);
            }
            else{
                ((FootHolder)holder).noneload.setVisibility(View.GONE);
                ((FootHolder)holder).loading.setVisibility(View.VISIBLE);
            }
        }
    }



    @Override
    public int getItemCount() {
        return cookbooks.size()+1;
    }

    public class Getuser extends Thread{
        private int userId;
        private String userName;
        public Getuser(int userId){this.userId=userId;}


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
        private String photoname;
        private Bitmap bitmap;
        public Getphoto(String photoname){
            this.photoname=photoname;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        @Override
        public void run(){
            Bitmap bitmap = RemotePictureOperation.downloadPicture(photoname, context);
            if(bitmap != null){
                this.bitmap = bitmap;
            }
//            try {
//                if (F_GetBitmap.isEmpty(photoname)) {
//                    params = new HashMap<>();
//                    params.put("photo", photoname);
//                    String res = RequestUtils.post("/cookbook/findphoto", params);
//                    try {
//                        JSONObject jsonObject1 = new JSONObject(res);
//                        if (jsonObject1.getInt("code") == 200) {
//                            JSONArray list1 = (JSONArray) jsonObject1.get("data");
//                            byte[] all_image = new byte[list1.length()];
//                            for (int j = 0; j < list1.length(); j++) {
//                                all_image[j] = (byte) list1.getInt(j);
//                            }
//                            F_GetBitmap.setInSDBitmap(all_image,photoname );
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
//                    Bitmap imageData = F_GetBitmap.getSDBitmap(photoname );// �õ�����BitMap���͵�ͼƬ����
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
