package com.qian.zhihuribao.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qian.zhihuribao.NewsDetailActivity;
import com.qian.zhihuribao.R;
import com.qian.zhihuribao.bean.Editor;
import com.qian.zhihuribao.bean.Story;
import com.qian.zhihuribao.bean.ThemeContent;
import com.qian.zhihuribao.common.Config;
import com.qian.zhihuribao.utils.DateUtil;
import com.qian.zhihuribao.view.sidemenu.interfaces.ScreenShotable;
import com.qian.zhihuribao.webservice.JsonCallback;
import com.qian.zhihuribao.webservice.WebService;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ContentFragment extends Fragment implements ScreenShotable, SwipeRefreshLayout.OnRefreshListener {
    private Bitmap bitmap;
    private View containerView;
    SimpleDraweeView draweeView;
    ListView listView;
    ExAdapter adapter;
    AppCompatActivity activity;
    private boolean isLoading;
    private LayoutInflater _inflater;
    private String curDate;
    private int themeId;
    private String themeURL;
    private LinearLayout layout_icons;
    private TextView tv_description;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static ContentFragment newInstance(int resId) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curDate = DateUtil.formatDate(new Date(), "yyyyMMdd");
        themeId = getArguments().getInt(Integer.class.getName());
        themeURL = Config.URL_THEME_CONTENT + themeId;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity) activity;
        _inflater = LayoutInflater.from(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.containerView = inflater.inflate(R.layout.fragment_content, container, false);
        initView(containerView);
        onRefresh();
        return containerView;
    }

    private void initView(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.listView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_primary, R.color.title_gray, R.color.search_view_hint_color, R.color.color_primary_dark);

        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_content_head, null);
        draweeView = (SimpleDraweeView) view.findViewById(R.id.draweeView);
        layout_icons = (LinearLayout) view.findViewById(R.id.layout_icons);
        tv_description = (TextView) view.findViewById(R.id.description);
        listView.addHeaderView(view);
        adapter = new ExAdapter();
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new MOnScrollListener());
    }


    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                        containerView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                containerView.draw(canvas);
                ContentFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        WebService.getInstance().asyncGet(themeURL, new LatestJsonCallback());
    }

    class onItemClickListener implements View.OnClickListener {
        int id;

        public void setId(int id) {
            this.id = id;
        }

        public onItemClickListener(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(activity, NewsDetailActivity.class);
            i.putExtra("storyId", id);
            startActivity(i);
        }
    }

    private class LatestJsonCallback extends JsonCallback<ThemeContent> {

        public LatestJsonCallback() {
            super(ThemeContent.class);
        }

        @Override
        public void onSuccess(ThemeContent json) {
            mSwipeRefreshLayout.setRefreshing(false);
            tv_description.setText(json.getDescription());
            draweeView.setImageURI(Uri.parse(json.getImage()));
            List<Editor> editors = json.getEditors();
            layout_icons.removeAllViews();
            if (editors != null && editors.size() > 0) {
                for (Editor ts : editors) {
                    View view = _inflater.inflate(R.layout.layout_editor_icon, null);
                    SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.img_icon);
                    Uri uri = Uri.parse(ts.getAvatar());
                    imageView.setImageURI(uri);
                    layout_icons.addView(view);
                }
            }
            adapter.setLists(json.getStories());
        }

        @Override
        public void onError(Request request, IOException e) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    private class ExAdapter extends BaseAdapter {

        private List<Story> lists;

        public ExAdapter() {
            this.lists = new ArrayList<Story>();
        }

        public List<Story> getLists() {
            return this.lists;
        }

        public void add(List<Story> lists) {
            this.lists.addAll(lists);
            notifyDataSetChanged();
        }

        public void setLists(List<Story> lists) {
            this.lists = lists;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = _inflater.inflate(R.layout.fragment_main_item, null);
            Story s = lists.get(position);
            if (s != null) {
                TextView item_title = (TextView) view.findViewById(R.id.item_title);
                item_title.setText(s.getTitle());
                SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.item_image);
                List<String> imgs = s.getImages();
                if (imgs != null && imgs.size() > 0) {
                    Uri uri = Uri.parse(imgs.get(0));
                    draweeView.setImageURI(uri);
                } else {
                    draweeView.setVisibility(View.GONE);
                }
                view.setOnClickListener(new onItemClickListener(s.getId()));
            }


            return view;
        }

        public Story getLast() {
            if (lists.size() > 0) {
                return lists.get(lists.size() - 1);
            }
            return null;
        }
    }


    class MOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                // 当不滚动时
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    // 判断滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        Log.d("---'", "到底啦！" + isLoading);
                        Story b = adapter.getLast();
                        if (b != null) {
                            getBefore(b.getId());
                        }
                    }
                    break;
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }


    private void getBefore(int id) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        WebService.getInstance().asyncGet(themeURL + Config.URL_THEME_BEFORE + id, new BeforeJsonCallback());
    }

    private class BeforeJsonCallback extends JsonCallback<ThemeContent> {

        public BeforeJsonCallback() {
            super(ThemeContent.class);
        }

        @Override
        public void onSuccess(ThemeContent json) {
            adapter.add(json.getStories());
            isLoading = false;
        }

        @Override
        public void onError(Request request, IOException e) {
            isLoading = false;
        }
    }
}

