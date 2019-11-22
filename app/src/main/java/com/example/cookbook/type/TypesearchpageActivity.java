package com.example.cookbook.type;

import android.app.Activity;
import android.app.LoaderManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.cookbook.R;
import com.example.cookbook.main.TypepageActivity;
import com.example.cookbook.user.BrowserpageActivity;
import com.example.cookbook.user.TypeRecyclerViewAdapter;
import com.example.cookbook.user.TypelistAdapter;
import com.example.cookbook.util.Cookbook;
import com.example.cookbook.util.RequestUtils;
import com.example.cookbook.util.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypesearchpageActivity extends Activity {
    private List<Cookbook> cookbookList = new ArrayList<Cookbook>();
    private String caishi;
    private String caixi;
    private String changhe;
    private int tasteId;
    private int cuisineId;
    private int changheId;
    private Map<String, Object> params;
    private TextView none;
    private boolean isRefresh = false;
    private boolean hasMore = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(TypesearchpageActivity.this,"加载失败",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    loadData();
                    break;
                case 2:
                    if(cookbookList.size()!=0) {
                        TypeRecyclerViewAdapter adapter = new TypeRecyclerViewAdapter (TypesearchpageActivity.this, R.layout.typelist,cookbookList);
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(TypesearchpageActivity.this);
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
                    break;
                default:
                    break;
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchpage);
        caishi=getIntent().getStringExtra("caishi");
        caixi=getIntent().getStringExtra("caixi");
        changhe=getIntent().getStringExtra("changhe");
        initview();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(1);
    }
    public void initview(){
        none=(TextView)findViewById(R.id.none);
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
    }
    private void loadData() {
        GetCookbook getCookbook =new GetCookbook();
        getCookbook.start();
        try {
            getCookbook.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isRefresh = false;
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
    public class GetoccsionId extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("occasionName", changhe);
                String res = RequestUtils.post("/occasion/NamefindId", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        changheId=jsonObject1.getInt("data");
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
                params.put("tasteName", caishi);
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
                params.put("cuisineName", caixi);
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

    public class GetCookbook extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookTaste",  tasteId);
                params.put("cookbookCuisine",  cuisineId);
                params.put("cookbookOccasion",  changheId);
                params.put("beginIndex",  0);
                String res = RequestUtils.post("/cookbook/findbyType", params);
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
                params.put("cookbookTaste",  tasteId);
                params.put("cookbookCuisine",  cuisineId);
                params.put("cookbookOccasion",  changheId);
                params.put("beginIndex", cookbookList.size());
                String res = RequestUtils.post("/cookbook/findbyType", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        if(list.length() != 0){
                            hasMore = true;
                            for (int i = 0; i < list.length(); i++) {
                                Cookbook cookbook = new Cookbook();
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
