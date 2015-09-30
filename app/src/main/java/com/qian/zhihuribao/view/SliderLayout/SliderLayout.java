package com.qian.zhihuribao.view.SliderLayout;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.qian.zhihuribao.view.SliderLayout.Animations.BaseAnimationInterface;
import com.qian.zhihuribao.view.SliderLayout.transformers.BaseTransformer;

import java.lang.reflect.Field;


public class SliderLayout extends ViewPager {

    /**
     * the duration between animation.
     */
    private long mSliderDuration = 4000;

    private PagerAdapter mAdapter;

    private Handler mHandler;

    private boolean isAutoCycle;

    private PagerIndicator mIndicator;

    private PagerIndicator.IndicatorVisibility mIndicatorVisibility = PagerIndicator.IndicatorVisibility.Visible;

    private BaseAnimationInterface mCustomAnimation;

    private BaseTransformer mViewPagerTransformer;

    private Runnable mCycleTask = new Runnable() {
        @Override
        public void run() {
            moveNextPosition();
            mHandler.postDelayed(this, mSliderDuration);
        }
    };

    public SliderLayout(Context context) {
        super(context);
        intiData();
    }

    public SliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        intiData();
    }

    private void intiData() {
        mHandler = new Handler();
        setSliderTransformDuration();
        reStartAutoCycle();
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = adapter;
        super.setAdapter(adapter);
    }

    public void movePrevPosition() {

        if (mAdapter == null)
            throw new IllegalStateException("You did not set a slider adapter");
        int item = getCurrentItem() - 1;
        item = item < 0 ? mAdapter.getCount() - 1 : item;
        setCurrentItem(item, true);
    }

    public void moveNextPosition() {

        if (mAdapter == null)
            throw new IllegalStateException("You did not set a slider adapter");

        setCurrentItem((getCurrentItem() + 1) % mAdapter.getCount(), true);
        Log.d("hhhhhh", "::::" + (getCurrentItem() + 1) % mAdapter.getCount());
    }

    public void startAutoCycle() {
        isAutoCycle = true;
        mHandler.removeCallbacks(mCycleTask);
        mHandler.postDelayed(mCycleTask, mSliderDuration);
    }

    private void reStartAutoCycle() {
        if (isAutoCycle)
            startAutoCycle();
    }

    public void stopAutoCycle() {
        isAutoCycle = false;
        mHandler.removeCallbacks(mCycleTask);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeCallbacks(mCycleTask);
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                reStartAutoCycle();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setCustomAnimation(BaseAnimationInterface animation){
        mCustomAnimation = animation;
        if(mViewPagerTransformer != null){
            mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        }
    }

    public void setSliderTransformDuration() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext());
            mScroller.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPageTransformer(BaseTransformer transformer) {
        mViewPagerTransformer = transformer;
        mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        setPageTransformer(true, transformer);
    }

    public void setCustomIndicator(PagerIndicator indicator){
        if(mIndicator != null){
            mIndicator.destroySelf();
        }
        mIndicator = indicator;
        mIndicator.setIndicatorVisibility(mIndicatorVisibility);
        mIndicator.setViewPager(this);
        mIndicator.redraw();
    }


    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 1100;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int period) {
            this(context, interpolator);
            mDuration = period;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            Log.d("hhhhhh", "mDuration" + mDuration);
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            Log.d("hhhhhh", "mDuration" + mDuration);
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
