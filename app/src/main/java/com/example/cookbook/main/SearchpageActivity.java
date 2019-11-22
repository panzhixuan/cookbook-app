package com.example.cookbook.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cookbook.R;
import com.example.cookbook.type.TypesearchpageActivity;
import com.example.cookbook.user.TypeRecyclerViewAdapter;
import com.example.cookbook.user.TypelistAdapter;
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

public class SearchpageActivity extends Activity {
    private List<Cookbook> cookbookList = new ArrayList<Cookbook>();
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
                    Toast.makeText(SearchpageActivity.this,"加载失败",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    loadData();
                    break;
                case 2:
                    if(cookbookList.size()!=0) {
                        TypeRecyclerViewAdapter adapter = new TypeRecyclerViewAdapter (SearchpageActivity.this, R.layout.typelist,cookbookList);
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(SearchpageActivity.this);
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
        setContentView(R.layout.searchpage);
        name=getIntent().getStringExtra("name");
        none=(TextView)findViewById(R.id.none);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(1);
    }
    private void loadData() {
        GetCookbook getCookbook =new GetCookbook();
        getCookbook.start();
        try {
            getCookbook.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadMoreData(){
        loadCookbook loadCookbook =new loadCookbook();
        loadCookbook.start();
        try {
            loadCookbook.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class GetCookbook extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookName", name);
                params.put("beginIndex", 0);
                String res = RequestUtils.post("/cookbook/findbyName", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        cookbookList.clear();
                        for(int i=0;i<list.length();i++){
                            Cookbook cookbook=new Cookbook();
                            cookbook.setCookbookCuisine(list.getJSONObject(i).getInt("cookbookCuisine"));
                            cookbook.setCookbookId(list.getJSONObject(i).getInt("cookbookId"));
                            cookbook.setCookbookLikenum(list.getJSONObject(i).getInt("cookbookLikenum"));
                            cookbook.setCookbookName(list.getJSONObject(i).getString("cookbookName"));
                            cookbook.setCookbookNutrition(list.getJSONObject(i).getString("cookbookNutrition"));
                            cookbook.setCookbookOccasion(list.getJSONObject(i).getInt("cookbookOccasion"));
                            cookbook.setCookbookPhoto(list.getJSONObject(i).getString("cookbookPhoto"));
                            cookbook.setCookbookTaste(list.getJSONObject(i).getInt("cookbookTaste"));
                            cookbook.setCookbookTip(list.getJSONObject(i).getString("cookbookTip"));
                            cookbook.setCookbookVisitnum(list.getJSONObject(i).getInt("cookbookVisitnum"));
                            cookbook.setUserId(list.getJSONObject(i).getInt("userId"));
                            cookbookList.add(cookbook);
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

    public class loadCookbook extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookName", name);
                params.put("beginIndex", cookbookList.size());
                String res = RequestUtils.post("/cookbook/findbyName", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        if(list.length() != 0){
                            hasMore = true;
                            for (int i = 0; i < list.length(); i++) {
                                Cookbook cookbook=new Cookbook();
                                cookbook.setCookbookCuisine(list.getJSONObject(i).getInt("cookbookCuisine"));
                                cookbook.setCookbookId(list.getJSONObject(i).getInt("cookbookId"));
                                cookbook.setCookbookLikenum(list.getJSONObject(i).getInt("cookbookLikenum"));
                                cookbook.setCookbookName(list.getJSONObject(i).getString("cookbookName"));
                                cookbook.setCookbookNutrition(list.getJSONObject(i).getString("cookbookNutrition"));
                                cookbook.setCookbookOccasion(list.getJSONObject(i).getInt("cookbookOccasion"));
                                cookbook.setCookbookPhoto(list.getJSONObject(i).getString("cookbookPhoto"));
                                cookbook.setCookbookTaste(list.getJSONObject(i).getInt("cookbookTaste"));
                                cookbook.setCookbookTip(list.getJSONObject(i).getString("cookbookTip"));
                                cookbook.setCookbookVisitnum(list.getJSONObject(i).getInt("cookbookVisitnum"));
                                cookbook.setUserId(list.getJSONObject(i).getInt("userId"));
                                cookbookList.add(cookbook);
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
