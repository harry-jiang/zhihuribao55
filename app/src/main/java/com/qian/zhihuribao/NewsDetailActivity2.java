package com.qian.zhihuribao;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class NewsDetailActivity2 extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(this));
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private int[] colors = {R.color.color_0, R.color.color_1, R.color.color_2, R.color.color_3,
                R.color.color_4, R.color.color_5, R.color.color_6, R.color.color_7,
                R.color.color_8, R.color.color_9,};

        private Context mContext;

        public RecyclerViewAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mTextView.setBackgroundColor(mContext.getResources().getColor(colors[position % (colors.length)]));
            holder.mTextView.setText(position + "");

        }

        @Override
        public int getItemCount() {
            return colors.length * 4;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView mTextView;

            public ViewHolder(TextView view) {
                super(view);
                mTextView = view;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_detail, menu);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }


}
