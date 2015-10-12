package com.qian.zhihuribao;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.qian.zhihuribao.bean.StoryDetail;
import com.qian.zhihuribao.bean.StoryExtra;
import com.qian.zhihuribao.common.Config;
import com.qian.zhihuribao.utils.AssetsUtils;
import com.qian.zhihuribao.utils.NetWorkUtil;
import com.qian.zhihuribao.webservice.JsonCallback;
import com.qian.zhihuribao.webservice.WebService;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;

public class NewsDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    //手指在屏幕滑动，X轴最小变化值
    private static final int FLING_MIN_DISTANCE_X = 200;

    //手指在屏幕滑动，Y轴最小变化值
    private static final int FLING_MIN_DISTANCE = 10;

    //手指在屏幕滑动，最小速度
    private static final int FLING_MIN_VELOCITY = 1;
    Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WebView mWebView;
    private ArrayList<String> mDetailImageList = new ArrayList<String>();
    int id;
    StoryJsonCallback mStoryJsonCallback;
    StoryExtraJsonCallback mStoryExtraJsonCallback;
    StoryExtra storyExtra;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        id = getIntent().getIntExtra("storyId", 7124797);
        setActionBar();
        initView();
        mGestureDetector = new GestureDetector(this, mOnGestureListener);
        mStoryJsonCallback = new StoryJsonCallback();
        mStoryExtraJsonCallback = new StoryExtraJsonCallback();
        onRefresh();
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mWebView = (WebView) findViewById(R.id.webview);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        setUpWebViewDefaults();
    }

    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpWebViewDefaults() {

        mWebView.addJavascriptInterface(new JavaScriptObject(this), "injectedObject");

        // 设置缓存模式
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setJavaScriptEnabled(true);


        // Use WideViewport and Zoom out if there is no viewport defined
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        // 支持通过js打开新的窗口
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {

                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= 11) {
            mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }

        // mWebView.setWebViewClient(mWebViewClient);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_detail, menu);
        View view1 = menu.getItem(2).setActionView(R.layout.bar_menu_item).getActionView();
        view1.findViewById(R.id.menu_item_image).setBackgroundResource(R.drawable.comment);
        View view2 = menu.getItem(3).setActionView(R.layout.bar_menu_item).getActionView();
        view2.findViewById(R.id.menu_item_image).setBackgroundResource(R.drawable.praise);
        if (storyExtra != null) {
            if (storyExtra.getComments() > 0) {
                ((TextView) view1.findViewById(R.id.menu_item_title)).setText("" + storyExtra.getComments());
            }
            if (storyExtra.getPopularity() > 0) {
                ((TextView) view2.findViewById(R.id.menu_item_title)).setText("" + storyExtra.getPopularity());
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        WebService.getInstance().asyncGet(Config.URL_STORY + id, mStoryJsonCallback);
        WebService.getInstance().asyncGet(Config.URL_STORY_EXTRA + id, mStoryExtraJsonCallback);
    }

    public static class JavaScriptObject {

        private Activity mInstance;

        public JavaScriptObject(Activity instance) {
            mInstance = instance;
        }

        @JavascriptInterface
        public void openImage(String url) {

            if (mInstance != null && !mInstance.isFinishing()) {
//                Intent intent = new Intent(mInstance, NewsDetailImageActivity.class);
//                intent.putExtra("imageUrl", url);
//
//                mInstance.startActivity(intent);
            }
        }
    }

    private class StoryJsonCallback extends JsonCallback<StoryDetail> {

        public StoryJsonCallback() {
            super(StoryDetail.class);
        }

        @Override
        public void onSuccess(StoryDetail json) {
            mSwipeRefreshLayout.setRefreshing(false);
            setWebView(json);
        }

        @Override
        public void onError(Request request, IOException e) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    private class StoryExtraJsonCallback extends JsonCallback<StoryExtra> {

        public StoryExtraJsonCallback() {
            super(StoryExtra.class);
        }

        @Override
        public void onSuccess(StoryExtra json) {
            storyExtra = json;
            invalidateOptionsMenu();
        }

        @Override
        public void onError(Request request, IOException e) {
        }

    }


    /**
     * 设置WebView内容
     */
    private void setWebView(StoryDetail storyDetail) {
        String body = "";
        if (storyDetail == null || TextUtils.isEmpty(body = storyDetail.getBody())) {
            return;
        }

        String html = AssetsUtils.loadText(this, Config.TEMPLATE_DEF_URL);
        html = html.replace("{content}", body);

        String headerDef = "file:///android_asset/www/news_detail_header_def.jpg";

        if (NetWorkUtil.isMobile(this) && PreferenceManager.getDefaultSharedPreferences(this).getBoolean("noimage_nowifi?", false)) {

        } else {
            headerDef = storyDetail.getImage();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"img-wrap\">")
                .append("<h1 class=\"headline-title\">")
                .append(storyDetail.title).append("</h1>")
                .append("<span class=\"img-source\">")
                .append(storyDetail.image_source).append("</span>")
                .append("<img src=\"").append(headerDef)
                .append("\" alt=\"\">")
                .append("<div class=\"img-mask\"></div>");

        html = html.replace("<div class=\"img-place-holder\">", sb.toString());
        // String resultHTML = replaceImgTagFromHTML(html);

        mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        try {
            mGestureDetector.onTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.dispatchTouchEvent(ev);
    }

    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            boolean isXWell = Math.abs(e2.getX() - e1.getX()) < FLING_MIN_DISTANCE_X ? true : true;

            if (isXWell && e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                getSupportActionBar().hide();
            } else if (isXWell && e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                getSupportActionBar().show();
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    };


}
