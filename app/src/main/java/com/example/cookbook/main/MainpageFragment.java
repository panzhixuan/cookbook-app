package com.example.cookbook.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.cookbook.LoginActivity.MainpageActivity;
import com.example.cookbook.R;
import com.example.cookbook.user.CookbookdetailpageActivity;
import com.example.cookbook.user.MycookbookActivity;
import com.example.cookbook.user.MycookbooklistAdapter;
import com.example.cookbook.util.*;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.*;

public class MainpageFragment extends Fragment{
    public static ProgressDialog pd;
    private Map<String, Object> params;
    private MainpageActivity mainpageActivity;
    private boolean actIsAlive=true;
    private List<ImageView> viewPagerData;
    private PagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private String[] imagename;
    private ImageView[] tips = new ImageView[3];
    private boolean isRunning = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 执行滑动到下一个页面
            if (isRunning) {
                // 在发一个handler延时
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        };
    };
    private int currentPosition;
    private View view;
    private ImageView sync;
    private FrameLayout type1;
    private FrameLayout type2;
    private FrameLayout type3;
    private FrameLayout type4;
    private FrameLayout type5;
    private FrameLayout type6;
    private FrameLayout type7;
    private FrameLayout type8;
    private ImageView comfirm;
    private ImageView photo;
    private Bitmap imageData[]=null;
    private ImageView[] mImageViews;
    private int currentItem;
    private static int status=1;
    private List<Cookbook> cookbookList = new ArrayList<Cookbook>();
    private List<CookbookInfo> cookbookInfoList = new ArrayList<CookbookInfo>();
    private List<String> mStrings = new ArrayList<String>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(mainpageActivity,"加载失败",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    loadmainData();
                case 2:
                    int n=cookbookList.size();
                    Log.d("RECOMMED123", "xianzai:"+cookbookInfoList.size());
                    if(cookbookInfoList.size() <4) {
                        while (cookbookInfoList.size() < 4) {
                            while (true) {
                                Random random = new Random();
                                int a = random.nextInt(n );
                                Cookbook cookbook = cookbookList.get(a);
                                boolean flag = false;
                                for (int i = 0; i < cookbookInfoList.size(); i++) {
                                    if (cookbookInfoList.get(i).getCookbook().getCookbookId() == cookbook.getCookbookId()) {
                                        flag = true;
                                        break;
                                    }
                                }
                                if(!flag) {
                                    CookbookInfo cookbookInfo = new CookbookInfo();
                                    cookbookInfo.setCookbook(cookbook);
                                    cookbookInfo.setScore(0);
                                    cookbookInfoList.add(cookbookInfo);
                                    break;
                                }
                            }
                        }
                    }
                    else if(cookbookInfoList.size() >= 4){
                        while (true) {
                            Random random = new Random();
                            int a = random.nextInt(n);
                            Cookbook cookbook = cookbookList.get(a);
                            boolean flag = false;
                            for (int i = 0; i < cookbookInfoList.size(); i++) {
                                if (cookbookInfoList.get(i).getCookbook().getCookbookId() == cookbook.getCookbookId()) {
                                    flag = true;
                                    break;
                                }
                            }
                            if(!flag) {
                                CookbookInfo cookbookInfo = new CookbookInfo();
                                cookbookInfo.setCookbook(cookbook);
                                cookbookInfo.setScore(0);
                                cookbookInfoList.add(cookbookInfo);
                                break;
                            }
                        }
                    }
                    Collections.shuffle(cookbookInfoList);
                    ImageView cover1=(ImageView)view.findViewById(R.id.recdish1cover);
                    TextView dishname1=(TextView)view.findViewById(R.id.recdish1name);
                    TextView username1=(TextView)view.findViewById(R.id.recdish1username);
                    TextView rating1 = (TextView)view.findViewById(R.id.rating1);
                    ImageView cover2=(ImageView)view.findViewById(R.id.recdish2cover);
                    TextView dishname2=(TextView)view.findViewById(R.id.recdish2name);
                    TextView username2=(TextView)view.findViewById(R.id.recdish2username);
                    TextView rating2 = (TextView)view.findViewById(R.id.rating2);
                    ImageView cover3=(ImageView)view.findViewById(R.id.recdish3cover);
                    TextView dishname3=(TextView)view.findViewById(R.id.recdish3name);
                    TextView username3=(TextView)view.findViewById(R.id.recdish3username);
                    TextView rating3 = (TextView)view.findViewById(R.id.rating3);
                    ImageView cover4=(ImageView)view.findViewById(R.id.recdish4cover);
                    TextView dishname4=(TextView)view.findViewById(R.id.recdish4name);
                    TextView username4=(TextView)view.findViewById(R.id.recdish4username);
                    TextView rating4 = (TextView)view.findViewById(R.id.rating4);
                    Getphoto getphoto =new Getphoto(cookbookInfoList.get(0).getCookbook().getCookbookPhoto());
                    getphoto.start();
                    try {
                        getphoto.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cover1.setImageBitmap(getphoto.getBitmap());
                    Getphoto getphoto1 =new Getphoto(cookbookInfoList.get(1).getCookbook().getCookbookPhoto());
                    getphoto1.start();
                    try {
                        getphoto1.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cover2.setImageBitmap(getphoto1.getBitmap());
                    Getphoto getphoto2 =new Getphoto(cookbookInfoList.get(2).getCookbook().getCookbookPhoto());
                    getphoto2.start();
                    try {
                        getphoto2.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cover3.setImageBitmap(getphoto2.getBitmap());
                    Getphoto getphoto3 =new Getphoto(cookbookInfoList.get(3).getCookbook().getCookbookPhoto());
                    getphoto3.start();
                    try {
                        getphoto3.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cover4.setImageBitmap(getphoto3.getBitmap());
                    dishname1.setText(cookbookInfoList.get(0).getCookbook().getCookbookName());
                    dishname2.setText(cookbookInfoList.get(1).getCookbook().getCookbookName());
                    dishname3.setText(cookbookInfoList.get(2).getCookbook().getCookbookName());
                    dishname4.setText(cookbookInfoList.get(3).getCookbook().getCookbookName());
                    Getuser getuser =new Getuser(cookbookInfoList.get(0).getCookbook().getUserId());
                    getuser.start();
                    try {
                        getuser.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Getuser getuser1 =new Getuser(cookbookInfoList.get(1).getCookbook().getUserId());
                    getuser1.start();
                    try {
                        getuser1.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Getuser getuser2 =new Getuser(cookbookInfoList.get(2).getCookbook().getUserId());
                    getuser2.start();
                    try {
                        getuser2.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Getuser getuser3 =new Getuser(cookbookInfoList.get(3).getCookbook().getUserId());
                    getuser3.start();
                    try {
                        getuser3.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    username1.setText(getuser.getUserName());
                    username2.setText(getuser1.getUserName());
                    username3.setText(getuser2.getUserName());
                    username4.setText(getuser3.getUserName());
                    rating1.setText(String.valueOf(cookbookInfoList.get(0).getScore()));
                    rating2.setText(String.valueOf(cookbookInfoList.get(1).getScore()));
                    rating3.setText(String.valueOf(cookbookInfoList.get(2).getScore()));
                    rating4.setText(String.valueOf(cookbookInfoList.get(3).getScore()));
                    view.findViewById(R.id.recdish1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mainpageActivity,CookbookdetailpageActivity.class);
                            intent.putExtra("cookbookPhoto",cookbookInfoList.get(0).getCookbook().getCookbookPhoto());
                            startActivity(intent);
                        }
                    });
                    view.findViewById(R.id.recdish2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mainpageActivity,CookbookdetailpageActivity.class);
                            intent.putExtra("cookbookPhoto",cookbookInfoList.get(1).getCookbook().getCookbookPhoto());
                            startActivity(intent);
                        }
                    });
                    view.findViewById(R.id.recdish3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mainpageActivity,CookbookdetailpageActivity.class);
                            intent.putExtra("cookbookPhoto",cookbookInfoList.get(2).getCookbook().getCookbookPhoto());
                            startActivity(intent);
                        }
                    });
                    view.findViewById(R.id.recdish4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mainpageActivity, CookbookdetailpageActivity.class);
                            intent.putExtra("cookbookPhoto",cookbookInfoList.get(3).getCookbook().getCookbookPhoto());
                            startActivity(intent);
                        }
                    });
                case 3:
                    break;
                case 4:
                    pd.dismiss();
                default:
                    break;
            }
        }
    };
    //onAttach(),当fragment被绑定到activity时被调用(Activity会被传入.).
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
        pd = new ProgressDialog(mainpageActivity);
        pd.setMax(100);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage("加载中...请稍后");
        pd.show();
        view = inflater.inflate(R.layout.mainpage, null);
        loadData();
        loadmainData();
        ViewGroup group = (ViewGroup) view.findViewById(R.id.viewGroup3);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager3);
        tips = new ImageView[imageData.length];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(mainpageActivity);
            imageView.setLayoutParams(new Gallery.LayoutParams(30, 30));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused1);
            }else {
                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused1);
            }
            ViewGroup parent = (ViewGroup) imageView.getParent();
            if (parent != null) {
                parent.removeAllViewsInLayout();
            }
            group.addView(imageView);
        }
        // 将图片装载到数组中
        //用数据的装载
        mImageViews = new ImageView[imageData.length];
        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(mainpageActivity);
            mImageViews[i] = imageView;
            //imageView.setBackgroundResource(imgIdArray[i]);
            imageView.setImageBitmap(imageData[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        // 设置Adapter
        viewPager.setAdapter(new MyAdapter());
        // 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        viewPager.setCurrentItem((mImageViews.length) * 100);
        // 设置监听，主要是设置点点的背景
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setImageBackground(position % mImageViews.length);
                // lastPosition = position;
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 页面正在滑动时间回调
            }
            @Override
            public void onPageScrollStateChanged(int state) {
               if(state == 2){
                   handler.sendEmptyMessageDelayed(0, 5000);
               }
               else if(state == 1){
                   handler.removeCallbacksAndMessages(null);
               }
            }
        });
        if(status==1) {
            handler.sendEmptyMessageDelayed(0, 5000);
            status=2;
        }
        pd.dismiss();
        final AutoCompleteTextView search=(AutoCompleteTextView)view.findViewById(R.id.search);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(mainpageActivity,android.R.layout.simple_dropdown_item_1line,mStrings);
        search.setAdapter(adapter);
        sync=(ImageView)view.findViewById(R.id.sync);
        type1=(FrameLayout)view.findViewById(R.id.type1);
        type2=(FrameLayout)view.findViewById(R.id.type2);
        type3=(FrameLayout)view.findViewById(R.id.type3);
        type4=(FrameLayout)view.findViewById(R.id.type4);
        type5=(FrameLayout)view.findViewById(R.id.type5);
        type6=(FrameLayout)view.findViewById(R.id.type6);
        type7=(FrameLayout)view.findViewById(R.id.type7);
        type8=(FrameLayout)view.findViewById(R.id.type8);
        comfirm=(ImageView)view.findViewById(R.id.searchconfirm);
        photo = (ImageView)view.findViewById(R.id.photo);
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,TypepageActivity.class);
                intent.putExtra("typeId",8);
                startActivity(intent);
            }
        });
        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,TypepageActivity.class);
                intent.putExtra("typeId",7);
                startActivity(intent);
            }
        });
        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,TypepageActivity.class);
                intent.putExtra("typeId",6);
                startActivity(intent);
            }
        });
        type4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,TypepageActivity.class);
                intent.putExtra("typeId",5);
                startActivity(intent);
            }
        });
        type5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,TypepageActivity.class);
                intent.putExtra("typeId",4);
                startActivity(intent);
            }
        });
        type6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,TypepageActivity.class);
                intent.putExtra("typeId",3);
                startActivity(intent);
            }
        });
        type7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,TypepageActivity.class);
                intent.putExtra("typeId",2);
                startActivity(intent);
            }
        });
        type8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(mainpageActivity,TypepageActivity.class);
                intent.putExtra("typeId",1);
                startActivity(intent);
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditText search=(EditText)view.findViewById(R.id.search);
                if(search.getText().toString().equals("")) {
                    Toast.makeText(mainpageActivity, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = null;
                    intent = new Intent(mainpageActivity, SearchpageActivity.class);
                    intent.putExtra("name", search.getText().toString());
                    startActivity(intent);
                }
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(mainpageActivity, PhotopageActivity.class);
                startActivity(intent);
            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pd = new ProgressDialog(mainpageActivity);
//                pd.setMax(100);
//                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                pd.setCancelable(false);
//                pd.setMessage("加载中，请稍后........");
//                pd.show();
//                mHandler.sendEmptyMessage(2);
//                pd.dismiss();
                new AysncTask_load().execute();
            }
        });
        return view;
    }

    public static MainpageFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        MainpageFragment fragment = new MainpageFragment();
        fragment.setArguments(args);
        return fragment;
    }


//    private void initData() {
//        viewPagerData = new ArrayList<>();
//        ImageView imageView = new ImageView(mainpageActivity);
//        imageView.setBackgroundResource(R.drawable.chuancai);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        viewPagerData.add(imageView);
//
//        ImageView imageView2 = new ImageView(mainpageActivity);
//        imageView2.setBackgroundResource(R.drawable.huaicai);
//        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
//        viewPagerData.add(imageView2);
//
//        ImageView imageView3 = new ImageView(mainpageActivity);
//        imageView3.setBackgroundResource(R.drawable.huicai);
//        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
//        viewPagerData.add(imageView3);
//
//        ImageView imageView4 = new ImageView(mainpageActivity);
//        imageView4.setBackgroundResource(R.drawable.lucai);
//        imageView4.setScaleType(ImageView.ScaleType.FIT_XY);
//        viewPagerData.add(imageView4);
//
//        ImageView imageView5 = new ImageView(mainpageActivity);
//        imageView5.setBackgroundResource(R.drawable.mincai);
//        imageView5.setScaleType(ImageView.ScaleType.FIT_XY);
//        viewPagerData.add(imageView5);
//
//        ImageView imageView6 = new ImageView(mainpageActivity);
//        imageView6.setBackgroundResource(R.drawable.yuecai);
//        imageView6.setScaleType(ImageView.ScaleType.FIT_XY);
//        viewPagerData.add(imageView6);
//
//        ImageView imageView7 = new ImageView(mainpageActivity);
//        imageView7.setBackgroundResource(R.drawable.xiangcai);
//        imageView7.setScaleType(ImageView.ScaleType.FIT_XY);
//        viewPagerData.add(imageView7);
//
//        ImageView imageView8 = new ImageView(mainpageActivity);
//        imageView8.setBackgroundResource(R.drawable.zhecai);
//        imageView8.setScaleType(ImageView.ScaleType.FIT_XY);
//        viewPagerData.add(imageView8);
//    }
//    private void initViewpager() {
//        //数据适配器
//        viewPagerAdapter = new PagerAdapter() {
//            private int mChildCount = 0;
//
//            @Override
//            public void notifyDataSetChanged() {
//                mChildCount = getCount();
//                super.notifyDataSetChanged();
//            }
//
//            @Override
//            public int getItemPosition(Object object) {
//                if (mChildCount > 0) {
//                    mChildCount--;
//                    return POSITION_NONE;
//                }
//                return super.getItemPosition(object);
//            }
//
//            @Override
//            //获取当前窗体界面数
//            public int getCount() {
//                // TODO Auto-generated method stub
//                return viewPagerData.size();
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return view == object;
//            }
//
//            //是从ViewGroup中移出当前View
//            public void destroyItem(View arg0, int arg1, Object arg2) {
//                ((ViewPager) arg0).removeView(viewPagerData.get(arg1));
//            }
//
//            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
//            public Object instantiateItem(View arg0, int arg1) {
//                ((ViewPager) arg0).addView(viewPagerData.get(arg1));
//                return viewPagerData.get(arg1);
//            }
//        };
//
//        viewpager.setAdapter(viewPagerAdapter);
//        viewpager.setCurrentItem(0);
//        viewpager.setOffscreenPageLimit(7);
//        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                // 没有滑动的时候 切换页面
//            }
//        });
//    }
//    private void initHandler() {
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (msg.what == 1) {
//                    if (currentPosition==viewPagerData.size()-1){          // 如果当前位置是轮播图的最后一个位置，则调到轮播图数据源的第一张图片
//                        currentPosition = 0 ;
//                        viewpager.setCurrentItem(0,false);
//                    }else{
//                        currentPosition ++;                                // 否则切换到下一张图片
//                        viewpager.setCurrentItem(currentPosition,true);
//                    }
//                }
//            }
//        };
//    }
//    private void autoViewPager() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                while (actIsAlive) {
//                    try {
//                        sleep(5000);
//                        handler.sendEmptyMessage(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//    }
//    @Override
//    public void toTypeActivity(String typename) {
//        Intent intent=null;
//        intent=new Intent(mainpageActivity,TypepageActivity.class);
//        intent.putExtra("typename",typename);
//        startActivity(intent);
//    }
//    @Override
//    public void toSearchActivity() {
//        EditText search=(EditText)view.findViewById(R.id.search);
//        if(search.getText().toString().equals("")) {
//            Toast.makeText(mainpageActivity, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Intent intent = null;
//            intent = new Intent(mainpageActivity, SearchpageActivity.class);
//            intent.putExtra("name",search.getText().toString());
//            startActivity(intent);
//        }
//    }
//    @Override
//    public void toCookbookdetail(){
////        Intent intent=null;
////        intent=new Intent(mainpageActivity,CookbookdetailpageActivity.class);
////        startActivity(intent);
//    }
//
//    @Override
//    public void sync(){
//        loadmainData();
//    }
//
    private void loadData() {
        GetCookbookname getCookbookname =new GetCookbookname();
        getCookbookname.start();
        try {
            getCookbookname.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class GetCookbookname extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                String res = RequestUtils.post("/cookbook/getall", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        imagename=new String[3];
                        imageData=new Bitmap[3];
                        for(int i=0;i<list.length();i++){
                            mStrings.add(list.getJSONObject(i).getString("cookbookName"));
                            if(i<3){
                                Getphoto getphoto =new Getphoto(list.getJSONObject(i).getString("cookbookPhoto"));
                                getphoto.start();
                                try {
                                    getphoto.join();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                imagename[i]=list.getJSONObject(i).getString("cookbookPhoto");
                                imageData[i]=getphoto.getBitmap();
                            }
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void loadmainData() {
        GetRecommandCookbook getRecommandCookbook =new GetRecommandCookbook();
        getRecommandCookbook.start();
        try {
            getRecommandCookbook.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetCookbook getCookbook =new GetCookbook();
        getCookbook.start();
        try {
            getCookbook.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class GetCookbook extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                String res = RequestUtils.post("/cookbook/getall", params);
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class GetRecommandCookbook extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("userId", User.getInstance().getUserId());
                String res = RequestUtils.post("/cookbook/recommand", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        JSONArray list = (JSONArray) jsonObject1.get("data");
                        cookbookInfoList.clear();
                        for(int i=0;i<list.length();i++){
                            GetCookbookById getCookbookById = new GetCookbookById(list.getJSONObject(i).getInt("cookBookId"), (int)list.getJSONObject(i).getDouble("recommandScore"));
                            getCookbookById.start();
                            try {
                                getCookbookById.join();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class GetCookbookById extends Thread{
        private int cookbookId;
        private int score;
        GetCookbookById(int cookbookId, int score){
            this.cookbookId = cookbookId;
            this.score = score;
        }
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookId", cookbookId);
                String res = RequestUtils.post("/cookbook/findbycookbookId", params);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        Cookbook cookbook = new Cookbook();
                        cookbook.setCookbookCuisine(jsonObject1.getInt("cookbookCuisine"));
                        cookbook.setCookbookId(jsonObject1.getInt("cookbookId"));
                        cookbook.setCookbookLikenum(jsonObject1.getInt("cookbookLikenum"));
                        cookbook.setCookbookName(jsonObject1.getString("cookbookName"));
                        cookbook.setCookbookNutrition(jsonObject1.getString("cookbookNutrition"));
                        cookbook.setCookbookOccasion(jsonObject1.getInt("cookbookOccasion"));
                        cookbook.setCookbookPhoto(jsonObject1.getString("cookbookPhoto"));
                        cookbook.setCookbookTaste(jsonObject1.getInt("cookbookTaste"));
                        cookbook.setCookbookTip(jsonObject1.getString("cookbookTip"));
                        cookbook.setCookbookVisitnum(jsonObject1.getInt("cookbookVisitnum"));
                        cookbook.setUserId(jsonObject1.getInt("userId"));
                        CookbookInfo cookbookInfo = new CookbookInfo();
                        cookbookInfo.setCookbook(cookbook);
                        cookbookInfo.setScore(score);
                        cookbookInfoList.add(cookbookInfo);
                        Log.d("RECOMMED", "xianzai:"+cookbookInfoList.size());
                    } else {
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
//            try {
                Bitmap bitmap = RemotePictureOperation.downloadPicture(photoname, getContext());
                if(bitmap != null){
                    this.bitmap = bitmap;
                }
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

//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
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
    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mImageViews[position % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, final int position) {
            if (mImageViews[position % mImageViews.length].getParent() != null) {
                ((ViewGroup) mImageViews[position % mImageViews.length].getParent())
                        .removeView(mImageViews[position % mImageViews.length]);
            }

            ((ViewPager) container).addView(mImageViews[position % mImageViews.length], 0);
            currentItem = position;
            mImageViews[position % mImageViews.length].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mainpageActivity, CookbookdetailpageActivity.class);
                    intent.putExtra("cookbookPhoto",imagename[position % mImageViews.length]);
                    startActivity(intent);
                }
            });

            return mImageViews[position % mImageViews.length];

        }
    }
    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused1);
            } else {
                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused1);
            }
        }
    }
    class AysncTask_load extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            //数据加载时再测试
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
//            initList();
            loadmainData();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pd.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 进行数据加载完成后的UI操作
            Toast.makeText(mainpageActivity,"已刷新",Toast.LENGTH_LONG).show();
            pd.dismiss();
        }
    }

}

