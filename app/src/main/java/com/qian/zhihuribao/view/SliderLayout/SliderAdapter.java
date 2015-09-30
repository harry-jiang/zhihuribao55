package com.qian.zhihuribao.view.SliderLayout;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * A PagerAdapter that wraps around another PagerAdapter to handle paging wrap-around.
 * Thanks to: https://github.com/antonyt/InfiniteViewPager
 */
public class SliderAdapter extends PagerAdapter {

    List<? extends View> list;

    public SliderAdapter(List<? extends View> list) {
        this.list = list;
    }

    @Override
    final public int getCount() {
        return Integer.MAX_VALUE;
    }

    /**
     * @return the {@link #getCount()} result of the wrapped adapter
     */
    public int getViewCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    final public Object instantiateItem(ViewGroup container, int position) {
        if (getViewCount() == 0) {
            return null;
        }
        int virtualPosition = position % getViewCount();
        return instantiateView(container, virtualPosition);
    }

    public Object instantiateView(ViewGroup container, int position) {
        container.addView(list.get(position), 0);//添加页卡
        return list.get(position);
    }

    public int getCurrentItem(int position) {
        if (getViewCount() == 0) {
            return 0;
        }
        return position % getViewCount();
    }

    @Override
    final public void destroyItem(ViewGroup container, int position, Object object) {
        if (getViewCount() == 0) {
            return;
        }

        int virtualPosition = position % getViewCount();
        destroyView(container, virtualPosition, object);
    }

    public void destroyView(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));//删除页卡;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;//官方提示这样写
    }
}