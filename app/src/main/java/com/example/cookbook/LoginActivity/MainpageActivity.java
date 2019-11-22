package com.example.cookbook.LoginActivity;

import android.app.Activity;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.cookbook.R;
import com.example.cookbook.main.MainpageFragment;
import com.example.cookbook.user.UserFragment;
import com.example.cookbook.type.TypeFragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainpageActivity extends Activity implements BottomNavigationBar.OnTabSelectedListener {
    BottomNavigationBar mBottomNavigationBar;
    FrameLayout mFrameLayout;
    private MainpageFragment mBookFragment;
    private TypeFragment mLikeFragment;
    private UserFragment mUserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        closeAndroidPDialog();
        setContentView(R.layout.activity_main);
        mBottomNavigationBar=(BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        mFrameLayout=(FrameLayout)findViewById(R.id.fragment_container);
        InitNavigationBar();
        setDefaultFragment();
    }

    private void InitNavigationBar() {
        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.homefill, "首页").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.zoomin, "分类").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.userora, "用户").setActiveColorResource(R.color.orange))
                .setFirstSelectedPosition(0)
                .initialise();
    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mBookFragment = MainpageFragment.newInstance("1");
        transaction.replace(R.id.fragment_container, mBookFragment);
        transaction.commit();
    }
    @Override
    public void onTabSelected(int position) {
        Log.d("onTabSelected", "onTabSelected: " + position);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mBookFragment == null) {
                    mBookFragment = MainpageFragment.newInstance("1");
                }
                transaction.replace(R.id.fragment_container, mBookFragment);
                break;
            case 1:
                if (mLikeFragment == null) {
                    mLikeFragment = TypeFragment.newInstance("2");
                }
                transaction.replace(R.id.fragment_container, mLikeFragment);
                break;
            case 2:
                if (mUserFragment == null) {
                    mUserFragment = UserFragment.newInstance("3");
                }
                transaction.replace(R.id.fragment_container, mUserFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }
    private static void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTabUnselected(int position) {
        Log.d("onTabUnselected", "onTabUnselected: " + position);
    }

    @Override
    public void onTabReselected(int position) {
        Log.d("onTabReselected", "onTabReselected: " + position);
    }
}
