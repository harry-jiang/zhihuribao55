package com.qian.zhihuribao;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.qian.zhihuribao.bean.Other;
import com.qian.zhihuribao.bean.Theme;
import com.qian.zhihuribao.common.Config;
import com.qian.zhihuribao.fragment.ContentFragment;
import com.qian.zhihuribao.fragment.MainFragment;
import com.qian.zhihuribao.view.sidemenu.interfaces.Resourceble;
import com.qian.zhihuribao.view.sidemenu.interfaces.ScreenShotable;
import com.qian.zhihuribao.view.sidemenu.model.SlideMenuItem;
import com.qian.zhihuribao.view.sidemenu.util.SupportAnimator;
import com.qian.zhihuribao.view.sidemenu.util.ViewAnimationUtils;
import com.qian.zhihuribao.view.sidemenu.util.ViewAnimator;
import com.qian.zhihuribao.webservice.JsonCallback;
import com.qian.zhihuribao.webservice.WebService;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener {
    private DrawerLayout drawerLayout;
    private List<SlideMenuItem> list = new ArrayList<SlideMenuItem>();
    private LinearLayout linearLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ViewAnimator viewAnimator;
    private ThemeJsonCallback themeJsonCallback;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainFragment mainFragment = MainFragment.newInstance(R.drawable.content_music);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mainFragment)
                .commit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        setActionBar();
        createMenuList(null);
        viewAnimator = new ViewAnimator<>(this, list, drawerLayout, this);
        themeJsonCallback = new ThemeJsonCallback();
        WebService.getInstance().asyncGet(Config.URL_THEMES, themeJsonCallback);
    }

    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("首页");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

    }

    private void createMenuList(List<Other> others) {
        list.clear();
        list.add(new SlideMenuItem("首页", 0));
        if (others == null || others.size() <= 0) {
            return;
        }
        for (int i = 0; i < others.size(); i++) {
            Other other = others.get(i);
            list.add(new SlideMenuItem(other.getName(), 0, other));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        Toast.makeText(MainActivity.this, android.R.id.home + "asdf" + id, Toast.LENGTH_SHORT).show();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ScreenShotable replaceFragment(SlideMenuItem slideMenuItem, int topPosition) {

        Other other = (Other) slideMenuItem.getTag();
        Fragment fragment;
        if (other == null) {
            fragment = MainFragment.newInstance(R.drawable.content_music);
        } else {
            fragment = ContentFragment.newInstance(other.getId());
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        return (ScreenShotable) fragment;
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, int position) {
        toolbar.setTitle(slideMenuItem.getName());
        return replaceFragment((SlideMenuItem) slideMenuItem, position);
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);
        if (list.size() < 2) {
            WebService.getInstance().asyncGet(Config.URL_THEMES, themeJsonCallback);
        }
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();
    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }


    private class ThemeJsonCallback extends JsonCallback<Theme> {

        public ThemeJsonCallback() {
            super(Theme.class);
        }

        @Override
        public void onSuccess(Theme json) {
            createMenuList(json.getOthers());
        }

        @Override
        public void onError(Request request, IOException e) {

        }

    }
}
