package com.example.cookbook.user;

import android.content.Context;
import android.content.Intent;
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
import com.example.cookbook.util.Cookbook;
import com.example.cookbook.util.F_GetBitmap;
import com.example.cookbook.util.RequestUtils;
import com.example.cookbook.util.User;
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

public class MycookbooklistAdapter extends ArrayAdapter {
    private final int resourceId;
    private Context context;
    private View view;
    private Map<String, Object> params;

    public MycookbooklistAdapter(Context context, int textViewResourceId, List<Cookbook> objects) {
        super(context, textViewResourceId,objects);
        this.context=context;
        resourceId = textViewResourceId;
    }

    static class ViewHolder {
        private ImageView cover;
        private TextView name;
        private TextView username;
        private TextView browsenum;
        private TextView collectnum;
        private FrameLayout frameLayout;
        private ImageView eidt;
    }
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Cookbook cookbook = (Cookbook) getItem(position);
        ViewHolder myViews;
        if (convertView == null) {
            myViews = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            myViews.cover=(ImageView)convertView.findViewById(R.id.cover);
            myViews.name=(TextView)convertView.findViewById(R.id.name);
            myViews.username=(TextView)convertView.findViewById(R.id.username);
            myViews.browsenum=(TextView)convertView.findViewById(R.id.totalbrowsenum);
            myViews.collectnum=(TextView)convertView.findViewById(R.id.totalcolnum);
            myViews.frameLayout=(FrameLayout) convertView.findViewById(R.id.mycookbooklist);
            myViews.eidt=(ImageView)convertView.findViewById(R.id.edit);
            convertView.setTag(myViews);
        } else {
            myViews = (ViewHolder) convertView.getTag();

        }
        Log.d("test123123",cookbook.getCookbookPhoto());
        Getphoto getphoto =new Getphoto(cookbook.getCookbookPhoto());
        getphoto.start();
        try {
            getphoto.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myViews.cover.setImageBitmap(getphoto.getBitmap());
        myViews.name.setText(cookbook.getCookbookName());
        myViews.username.setText(User.getInstance().getUserName());
        myViews.browsenum.setText(String.valueOf(cookbook.getCookbookVisitnum()));
        myViews.collectnum.setText(String.valueOf(cookbook.getCookbookLikenum()));
        myViews.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,CookbookdetailpageActivity.class);
                intent.putExtra("cookbookPhoto",cookbook.getCookbookPhoto());
                context.startActivity(intent);
            }
        });
        myViews.eidt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,EditcookbookpageActivity.class);
                intent.putExtra("cookbookPhoto",cookbook.getCookbookPhoto());
                context.startActivity(intent);
            }
        });
        return convertView;
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
            try {
                if (F_GetBitmap.isEmpty(photoname)) {
                    params = new HashMap<>();
                    params.put("photo", photoname);
                    String res = RequestUtils.post("/cookbook/findphoto", params);
                    try {
                        JSONObject jsonObject1 = new JSONObject(res);
                        if (jsonObject1.getInt("code") == 200) {
                            JSONArray list1 = (JSONArray) jsonObject1.get("data");
                            byte[] all_image = new byte[list1.length()];
                            for (int j = 0; j < list1.length(); j++) {
                                all_image[j] = (byte) list1.getInt(j);
                            }
                            F_GetBitmap.setInSDBitmap(all_image,photoname );
                            InputStream input = null;
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 1;
                            input = new ByteArrayInputStream(all_image);
                            @SuppressWarnings({ "rawtypes", "unchecked" })
                            SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(input, null, options));
                            Bitmap imageData = (Bitmap) softRef.get();
                            this.bitmap=imageData;
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Bitmap imageData = F_GetBitmap.getSDBitmap(photoname );// �õ�����BitMap���͵�ͼƬ����
                    if (F_GetBitmap.bitmap != null && !F_GetBitmap.bitmap.isRecycled()) {
                        F_GetBitmap.bitmap = null;
                    }
                    this.bitmap=imageData;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
