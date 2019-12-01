package com.example.cookbook.main;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cookbook.R;
import com.example.cookbook.user.TypeRecyclerViewAdapter;
import com.example.cookbook.util.Cookbook;
import com.example.cookbook.util.RequestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotosearchActivity extends AppCompatActivity {
    private List<Cookbook> cookbookList = new ArrayList<Cookbook>();
    private List<Integer> cookbookIdList = new ArrayList<>();
    private Map<String, Object> params;
    private String name;
    private TextView none;
    private boolean isRefresh = false;
    private boolean hasMore = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(PhotosearchActivity.this,"加载失败",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    loadData();
                    break;
                case 2:
                    if(cookbookList.size()!=0) {
                        TypeRecyclerViewAdapter adapter = new TypeRecyclerViewAdapter (PhotosearchActivity.this, R.layout.typelist,cookbookList);
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(PhotosearchActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if(dy> 0 && !recyclerView.canScrollVertically(1) && isRefresh == false && hasMore == true){
                                    isRefresh = true;
                                    loadMoreData();
                                    adapter.hasMore(hasMore);
                                    adapter.notifyDataSetChanged();
                                    isRefresh = false;
                                }
                            }
                        });
                    }
                    else{
                        none.setVisibility(View.VISIBLE);
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        recyclerView.setVisibility(View.GONE);
                    }
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photosearch);
        name=getIntent().getStringExtra("name");
        none=(TextView)findViewById(R.id.none);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(1);
    }
    private void loadData() {
        GetCookbookId getCookbookid =new GetCookbookId();
        getCookbookid.start();
        try {
            getCookbookid.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadMoreData(){
        loadCookbookId loadCookbookid =new loadCookbookId();
        loadCookbookid.start();
        try {
            loadCookbookid.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class GetCookbookId extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("ingredintName", name);
                params.put("beginIndex", 0);
                String res = RequestUtils.post("/ingredint/getCookbookList", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        cookbookIdList.clear();
                        cookbookList.clear();
                        for(int i = 0 ; i < list.length(); i++){
                            cookbookIdList.add(list.getInt(i));
                            GetCookbook getCookbook =new GetCookbook(cookbookIdList.get(i));
                            getCookbook.start();
                            try {
                                getCookbook.join();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mHandler.sendEmptyMessage(2);
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

    public class GetCookbook extends Thread{
        private int id;
        GetCookbook(int id){
            this.id = id;
        }
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookId", id);
                String res = RequestUtils.post("/cookbook/findbycookbookId", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONObject object = jsonObject1.getJSONObject("data");
                        Cookbook cookbook=new Cookbook();
                        cookbook.setCookbookCuisine(object.getInt("cookbookCuisine"));
                        cookbook.setCookbookId(object.getInt("cookbookId"));
                        cookbook.setCookbookLikenum(object.getInt("cookbookLikenum"));
                        cookbook.setCookbookName(object.getString("cookbookName"));
                        cookbook.setCookbookNutrition(object.getString("cookbookNutrition"));
                        cookbook.setCookbookOccasion(object.getInt("cookbookOccasion"));
                        cookbook.setCookbookPhoto(object.getString("cookbookPhoto"));
                        cookbook.setCookbookTaste(object.getInt("cookbookTaste"));
                        cookbook.setCookbookTip(object.getString("cookbookTip"));
                        cookbook.setCookbookVisitnum(object.getInt("cookbookVisitnum"));
                        cookbook.setUserId(object.getInt("userId"));
                        cookbookList.add(cookbook);
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

    public class loadCookbookId extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("ingredintName", name);
                params.put("beginIndex", cookbookIdList.size());
                String res = RequestUtils.post("/ingredint/getCookbookList", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        if(list.length() != 0){
                            if(list.length() < 6) {
                                hasMore = false;
                            }
                            else{
                                hasMore = true;
                            }
                            for(int i = 0 ; i < list.length(); i++){
                                cookbookIdList.add(list.getInt(i));
                                GetCookbook getCookbook =new GetCookbook(list.getInt(i));
                                getCookbook.start();
                                try {
                                    getCookbook.join();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }else {
                        hasMore = false;
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
