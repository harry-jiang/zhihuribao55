package com.qian.zhihuribao.view.SliderLayout.transformers;

import android.support.v4.view.ViewPager;
import android.util.Log;

import com.qian.zhihuribao.view.SliderLayout.SliderLayout;

public class AutoTransformer implements ViewPager.OnPageChangeListener {

    SliderLayout viewPager;
    private BaseTransformer[] bt = new BaseTransformer[]{
            new AccordionTransformer(), new BackgroundToForegroundTransformer(), new CubeInTransformer(),
            new DepthPageTransformer(), new DefaultTransformer(), new FadeTransformer(), new FlipHorizontalTransformer(),
            new FlipPageViewTransformer(), new ForegroundToBackgroundTransformer(), new RotateDownTransformer(), new RotateUpTransformer(),
            new StackTransformer(), new TabletTransformer(), new ZoomOutTransformer(), new ZoomInTransformer(), new ZoomOutSlideTransformer()
    };

    public AutoTransformer(SliderLayout viewPager) {
        this.viewPager = viewPager;
        viewPager.setPageTransformer(getTransformer());
    }

    public BaseTransformer getTransformer() {
        int i = (int) (Math.random() * bt.length);
        Log.d("getTransformer::::", "" + i);
        return bt[i];
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewPager.setPageTransformer(getTransformer());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}