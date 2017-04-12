package com.lishide.scrollrecyclerview;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.lishide.recyclerview.scroll.ScrollRecyclerView;
import com.lishide.recyclerview.scroll.SpaceItemDecoration;
import com.lishide.recyclerview.scroll.listener.OnItemClickListener;
import com.lishide.recyclerview.scroll.listener.OnItemKeyListener;
import com.lishide.recyclerview.scroll.listener.OnItemLongClickListener;
import com.lishide.recyclerview.scroll.listener.OnItemSelectedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ScrollRecyclerView mScrollRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScrollRecyclerView = (ScrollRecyclerView) findViewById(R.id.scroll_recycler_view);
        List<AppBean> mList = getAllApk();
        AppBeanAdapter adapter = new AppBeanAdapter(this, mList);
        mScrollRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mScrollRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.HORIZONTAL));

        int right = 20;
        int bottom = 20;
        RecyclerView.ItemDecoration spacingInPixel = new SpaceItemDecoration(right, bottom);
        mScrollRecyclerView.addItemDecoration(spacingInPixel);
        mScrollRecyclerView.setAdapter(adapter);
        adapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void OnItemSelected(View view, int position) {
            }
        });
        adapter.setOnItemSelectedListener(mOnItemSelectedListener);
        adapter.setOnItemClickListener(mOnItemClickListener);
        adapter.setOnItemLongClickListener(mOnItemLongClickListener);
        adapter.setOnItemKeyListener(mOnItemKeyListener);
    }

    public List<AppBean> getAllApk() {
        List<AppBean> appBeanList = new ArrayList<>();
        AppBean bean;
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (PackageInfo p : list) {
            bean = new AppBean();
            bean.setAppIcon(p.applicationInfo.loadIcon(packageManager));
            bean.setAppName(packageManager.getApplicationLabel(p.applicationInfo).toString());
            bean.setAppPackageName(p.applicationInfo.packageName);
            bean.setApkPath(p.applicationInfo.sourceDir);
            File file = new File(p.applicationInfo.sourceDir);
            bean.setAppSize((int) file.length());
            int flags = p.applicationInfo.flags;
            //判断是否是属于系统的apk
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                bean.setSystem(true);
            } else {
                bean.setSd(true);
            }
            appBeanList.add(bean);
        }
        return appBeanList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.d(TAG, "按下导航键<-左键->");
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.d(TAG, "按下导航键<-右键->");
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                Log.d(TAG, "按下导航键<-上键->");
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.d(TAG, "按下导航键<-下键->");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void OnItemSelected(View view, int position) {
            mScrollRecyclerView.smoothHorizontalScrollToNext(position);
        }
    };

    OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Log.d(TAG, "<--1--> click position = " + position);
        }
    };

    OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public void onItemLongClick(View view, int position) {
            Log.d(TAG, "<--2--> Long click position = " + position);
        }
    };

    OnItemKeyListener mOnItemKeyListener = new OnItemKeyListener() {
        @Override
        public void OnItemKey(View view, int keyCode, KeyEvent event, int position) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                Log.d(TAG, "KEYCODE_DPAD_CENTER");
            } else if (keyCode == KeyEvent.KEYCODE_MENU) {
                Log.d(TAG, "KEYCODE_MENU");
            }
        }
    };
}
