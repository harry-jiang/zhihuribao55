package com.qian.zhihuribao.view.SliderLayout.Animations;

import android.util.Log;
import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

public class ChildAnimationExample implements BaseAnimationInterface {

    private final static String TAG = "ChildAnimationExample";
    int id;

    public ChildAnimationExample(int id) {
        this.id = id;
    }

    @Override
    public void onPrepareCurrentItemLeaveScreen(View current) {
        View descriptionLayout = current.findViewById(id);
        if (descriptionLayout != null) {
            descriptionLayout.setVisibility(View.INVISIBLE);
        }
        Log.e(TAG, "onPrepareCurrentItemLeaveScreen called");
    }

    @Override
    public void onPrepareNextItemShowInScreen(View next) {
        View descriptionLayout = next.findViewById(id);
        if (descriptionLayout != null) {
            descriptionLayout.setVisibility(View.INVISIBLE);
        }
        Log.e(TAG, "onPrepareNextItemShowInScreen called");
    }

    @Override
    public void onCurrentItemDisappear(View view) {
        Log.e(TAG, "onCurrentItemDisappear called");
    }

    @Override
    public void onNextItemAppear(View view) {

        View descriptionLayout = view.findViewById(id);
        if (descriptionLayout != null) {
            descriptionLayout.setVisibility(View.VISIBLE);
            new StandUpAnimator().animate(descriptionLayout);
        }
        Log.e(TAG, "onCurrentItemDisappear called");
    }
}
