package com.example.cookbook.user;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cookbook.R;
import com.example.cookbook.main.SearchpageActivity;
import com.example.cookbook.type.TypesearchpageActivity;
import com.example.cookbook.util.Cookbook;
import com.example.cookbook.util.RequestUtils;
import com.example.cookbook.util.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class CollectpageActivity extends Activity {
    private List<Cookbook> cookbookList = new ArrayList<Cookbook>();
    private Map<String, Object> params;
    private TextView none;
    private boolean isRefresh = false;
    private boolean hasMore = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(CollectpageActivity.this,"加载失败",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    loadData();
                    break;
                case 2:
                    if(cookbookList.size()!=0) {
                        TypeRecyclerViewAdapter adapter = new TypeRecyclerViewAdapter (CollectpageActivity.this, R.layout.typelist,cookbookList);
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(CollectpageActivity.this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collectpage);
        initview();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(1);
    }
    public void initview(){
        none=(TextView)findViewById(R.id.none);
    }
    private void loadData() {
        Getlike getlike =new Getlike();
        getlike.start();
        try {
            getlike.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadMoreData(){
        loadcollect loadcollect =new loadcollect();
        loadcollect.start();
        try {
            loadcollect.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class Getlike extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", User.getInstance().getUserId());
                params.put("beginIndex", 0);
                String res = RequestUtils.post("/likes/getall", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        cookbookList.clear();
                        for(int i=0;i<list.length();i++){
                            params = new HashMap<>();
                            params.put("cookbookId", list.getJSONObject(i).getInt("cookbookId"));
                            String res1 = RequestUtils.post("/cookbook/findbycookbookId", params);
                            try {
                                JSONObject jsonObject2 = new JSONObject(res1);
                                if (jsonObject2.getInt("code") == 200) {
                                    JSONObject a=jsonObject2.getJSONObject("data");
                                    Cookbook cookbook=new Cookbook();
                                    cookbook.setCookbookCuisine(a.getInt("cookbookCuisine"));
                                    cookbook.setCookbookId(a.getInt("cookbookId"));
                                    cookbook.setCookbookLikenum(a.getInt("cookbookLikenum"));
                                    cookbook.setCookbookName(a.getString("cookbookName"));
                                    cookbook.setCookbookNutrition(a.getString("cookbookNutrition"));
                                    cookbook.setCookbookOccasion(a.getInt("cookbookOccasion"));
                                    cookbook.setCookbookPhoto(a.getString("cookbookPhoto"));
                                    cookbook.setCookbookTaste(a.getInt("cookbookTaste"));
                                    cookbook.setCookbookTip(a.getString("cookbookTip"));
                                    cookbook.setCookbookVisitnum(a.getInt("cookbookVisitnum"));
                                    cookbook.setUserId(a.getInt("userId"));
                                    cookbookList.add(cookbook);
                                } else {
                                    mHandler.sendEmptyMessage(0);
                                }
                            } catch (JSONException e) {
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

    public class loadcollect extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", User.getInstance().getUserId());
                params.put("beginIndex", cookbookList.size());
                String res = RequestUtils.post("/likes/getall", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        if(list.length() != 0) {
                            hasMore = true;
                            for (int i = 0; i < list.length(); i++) {
                                params = new HashMap<>();
                                params.put("cookbookId", list.getJSONObject(i).getInt("cookbookId"));
                                String res1 = RequestUtils.post("/cookbook/findbycookbookId", params);
                                try {
                                    JSONObject jsonObject2 = new JSONObject(res1);
                                    if (jsonObject2.getInt("code") == 200) {
                                        JSONObject a = jsonObject2.getJSONObject("data");
                                        Cookbook cookbook = new Cookbook();
                                        cookbook.setCookbookCuisine(a.getInt("cookbookCuisine"));
                                        cookbook.setCookbookId(a.getInt("cookbookId"));
                                        cookbook.setCookbookLikenum(a.getInt("cookbookLikenum"));
                                        cookbook.setCookbookName(a.getString("cookbookName"));
                                        cookbook.setCookbookNutrition(a.getString("cookbookNutrition"));
                                        cookbook.setCookbookOccasion(a.getInt("cookbookOccasion"));
                                        cookbook.setCookbookPhoto(a.getString("cookbookPhoto"));
                                        cookbook.setCookbookTaste(a.getInt("cookbookTaste"));
                                        cookbook.setCookbookTip(a.getString("cookbookTip"));
                                        cookbook.setCookbookVisitnum(a.getInt("cookbookVisitnum"));
                                        cookbook.setUserId(a.getInt("userId"));
                                        cookbookList.add(cookbook);
                                    } else {
                                        mHandler.sendEmptyMessage(0);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
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
