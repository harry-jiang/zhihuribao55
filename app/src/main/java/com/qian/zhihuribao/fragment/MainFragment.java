package com.qian.zhihuribao.fragment;

import android.app.Activity;
import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qian.zhihuribao.NewsDetailActivity;
import com.qian.zhihuribao.NewsDetailActivity2;
import com.qian.zhihuribao.R;
import com.qian.zhihuribao.bean.Before;
import com.qian.zhihuribao.bean.Latest;
import com.qian.zhihuribao.bean.Story;
import com.qian.zhihuribao.bean.TopStory;
import com.qian.zhihuribao.common.Config;
import com.qian.zhihuribao.utils.DateUtil;
import com.qian.zhihuribao.view.SliderLayout.Animations.ChildAnimationExample;
import com.qian.zhihuribao.view.SliderLayout.PagerIndicator;
import com.qian.zhihuribao.view.SliderLayout.SliderAdapter;
import com.qian.zhihuribao.view.SliderLayout.SliderLayout;
import com.qian.zhihuribao.view.SliderLayout.transformers.TabletTransformer;
import com.qian.zhihuribao.view.sidemenu.interfaces.ScreenShotable;
import com.qian.zhihuribao.webservice.JsonCallback;
import com.qian.zhihuribao.webservice.WebService;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainFragment extends Fragment implements ScreenShotable, SwipeRefreshLayout.OnRefreshListener {
    private SliderLayout mDemoSlider;
    private Bitmap bitmap;
    private View containerView;
    ArrayList<View> viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
    ExpandableListView expandableListView;
    ExAdapter adapter;
    AppCompatActivity activity;
    private boolean isLoading;
    private LayoutInflater _inflater;
    private String curDate;
    SwipeRefreshLayout mSwipeRefreshLayout;
    JsonCallback latestJsonCallback;
    JsonCallback mBeforeJsonCallback;

    public static MainFragment newInstance(int resId) {
        MainFragment contentFragment = new MainFragment();
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
        latestJsonCallback = new LatestJsonCallback();
        mBeforeJsonCallback = new BeforeJsonCallback();
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
        this.containerView = inflater.inflate(R.layout.fragment_main, container, false);
        initView(containerView);
        onRefresh();
        return containerView;
    }

    private void initView(View rootView) {
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_primary, R.color.title_gray, R.color.search_view_hint_color, R.color.color_primary_dark);


        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_main_head, null);
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);
        mDemoSlider.setAdapter(new SliderAdapter(viewList));
        mDemoSlider.setCustomIndicator((PagerIndicator) view.findViewById(R.id.pagerIndicator));
        mDemoSlider.setCustomAnimation(new ChildAnimationExample(R.id.description_layout));
        mDemoSlider.setPageTransformer(new TabletTransformer());
        mDemoSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mSwipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mSwipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        expandableListView.addHeaderView(view);
        adapter = new ExAdapter(activity);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnScrollListener(new MOnScrollListener());
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }

        });
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
                MainFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public void onResume() {
        super.onResume();
        mDemoSlider.startAutoCycle();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        WebService.getInstance().asyncGet(Config.URL_LATEST, latestJsonCallback);
    }

    class OnItemClickListener implements View.OnClickListener {
        int id;

        public void setId(int id) {
            this.id = id;
        }

        public OnItemClickListener(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(activity, NewsDetailActivity2.class);
            i.putExtra("storyId", id);
            startActivity(i);
        }
    }

    private class LatestJsonCallback extends JsonCallback<Latest> {

        public LatestJsonCallback() {
            super(Latest.class);
        }

        @Override
        public void onSuccess(Latest json) {
            mSwipeRefreshLayout.setRefreshing(false);
            List<TopStory> top_stories = json.getTop_stories();
            if (top_stories != null && top_stories.size() > 0) {
                viewList.clear();
                for (TopStory ts : top_stories) {
                    View view = _inflater.inflate(R.layout.sliderlayout, null);
                    TextView tv = (TextView) view.findViewById(R.id.description);
                    tv.setText(ts.getTitle());
                    SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.draweeView);
                    Uri uri = Uri.parse(ts.getImage());
                    draweeView.setImageURI(uri);
                    view.setOnClickListener(new OnItemClickListener(ts.getId()));
                    viewList.add(view);
                }
                mDemoSlider.getAdapter().notifyDataSetChanged();

            }
            adapter.getLists().clear();
            adapter.add(json);
        }

        @Override
        public void onError(Request request, IOException e) {
            mSwipeRefreshLayout.setRefreshing(false);

        }

    }

    private class ExAdapter extends BaseExpandableListAdapter {

        private Context _context;

        private List<Before> lists;

        public ExAdapter(Context context) {
            this._context = context;
            this.lists = new ArrayList<Before>();
        }

        public List<Before> getLists() {
            return this.lists;
        }

        public Before getLast() {
            if (lists.size() <= 0) {
                return null;
            }
            return lists.get(lists.size() - 1);
        }

        public void add(Before b) {
            lists.add(b);
            notifyDataSetChanged();
        }

        public void setLists(List<Before> lists) {
            this.lists = lists;
            notifyDataSetChanged();
        }

        @Override
        public int getGroupCount() {
            return lists.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            try {
                return lists.get(groupPosition).getStories().size();
            } catch (Exception e) {

            }
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return lists.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            try {
                return lists.get(groupPosition).getStories().get(childPosition);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView t = (TextView) _inflater.inflate(R.layout.fragment_main_title, null);
            Before b = lists.get(groupPosition);
            t.setText(getTitle(b.getDate()));
            expandableListView.expandGroup(groupPosition);
            return t;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = _inflater.inflate(R.layout.fragment_main_item, null);
            Story s = (Story) getChild(groupPosition, childPosition);
            if (s != null) {
                TextView item_title = (TextView) view.findViewById(R.id.item_title);
                item_title.setText(s.getTitle());
                SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.item_image);
                List<String> imgs = s.getImages();
                if (imgs != null && imgs.size() > 0) {
                    Uri uri = Uri.parse(imgs.get(0));
                    draweeView.setImageURI(uri);
                }

            }
            view.setOnClickListener(new OnItemClickListener(s.getId()));
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
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
                        Before b = adapter.getLast();
                        if (b != null) {
                            getBefore(b.getDate());
                        }
                    }
                    break;
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            final ExpandableListView listView = (ExpandableListView) view;
            /**
             * calculate point (0,0)
             */
            int npos = firstVisibleItem;// 其实就是firstVisibleItem
            if (npos == AdapterView.INVALID_POSITION)// 如果第一个位置值无效
                return;

            long pos = listView.getExpandableListPosition(npos);
            int childPos = ExpandableListView.getPackedPositionChild(pos);// 获取第一行child的id
            int groupPos = ExpandableListView.getPackedPositionGroup(pos);// 获取第一行group的id
            if (groupPos != -1) {// 第一行不是显示child,就是group,此时没必要显示指示器
                Before b = (Before) adapter.getGroup(groupPos);
                activity.getSupportActionBar().setTitle(getTitle(b.getDate()));

            } else {
                activity.getSupportActionBar().setTitle("首页");
            }
            Log.d("---'", "pos:" + pos + "--childPos:" + childPos + "--groupPos:" + groupPos);

        }
    }

    private String getTitle(String dateStr) {
        if (curDate.equals(dateStr)) {
            return "今日热闻";
        } else {
            return DateUtil.getDateCNStr(DateUtil.parseDate(dateStr, "yyyyMMdd"));
        }
    }


    private void getBefore(String data) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        WebService.getInstance().asyncGet(Config.URL_BEFORE + data, mBeforeJsonCallback);
    }

    private class BeforeJsonCallback extends JsonCallback<Before> {

        public BeforeJsonCallback() {
            super(Before.class);
        }

        @Override
        public void onSuccess(Before json) {
            adapter.add(json);
            isLoading = false;
        }

        @Override
        public void onError(Request request, IOException e) {
            isLoading = false;
        }

    }
}

