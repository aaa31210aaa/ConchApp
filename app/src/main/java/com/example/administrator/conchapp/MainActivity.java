package com.example.administrator.conchapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import adpter.ViewPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import tab.Blasthole;
import tab.Form;
import tab.Home;
import tab.Mine;
import tab.Production;
import utils.BaseActivity;
import utils.CheckVersion;
import utils.PermissionUtil;
import utils.ShowToast;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_viewpager)
    NoScrollViewPager main_viewpager;
    @BindView(R.id.main_tablayout)
    TabLayout main_tablayout;
//    private String downloadUrl = "";
//    private String content = "";
//    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定activity
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        //设置tab
        SetTab();

        AndPermission.with(this)
                .requestCode(200)
                .permission(PermissionUtil.WriteFilePermission).send();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == 200) {
                //检查版本
                CheckVersion checkVersion = new CheckVersion();
                checkVersion.CheckVersions(MainActivity.this, TAG);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == 200) {
                ShowToast.showShort(MainActivity.this, "拒绝权限会导致某些功能不可用。");
            }
        }
    };


    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 延迟发送退出
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            ShowToast.showShort(this, R.string.click_agin);
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, send_time);
        } else {
            finish();
            System.exit(0);
        }
    }

    private void SetTab() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Home());
        fragments.add(new Production());
        fragments.add(new Form());
        fragments.add(new Blasthole());
        fragments.add(new Mine());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"首页", "计划", "报表", "炮孔", "我的"});

        main_viewpager.setOffscreenPageLimit(4);

        main_viewpager.setAdapter(adapter);
        //关联图文
        main_tablayout.setupWithViewPager(main_viewpager);
        main_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                main_viewpager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < main_tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = main_tablayout.getTabAt(i);
            Drawable d = null;
            switch (i) {
                case 0:
                    d = ContextCompat.getDrawable(this, R.drawable.home_tab);
                    break;
                case 1:
                    d = ContextCompat.getDrawable(this, R.drawable.plan_tab);
                    break;
                case 2:
                    d = ContextCompat.getDrawable(this, R.drawable.form_tab);
                    break;
                case 3:
                    d = ContextCompat.getDrawable(this, R.drawable.blasthole_tab);
                    break;
                case 4:
                    d = ContextCompat.getDrawable(this, R.drawable.mine_tab);
                    break;
            }
            tab.setIcon(d);
        }
    }
}
