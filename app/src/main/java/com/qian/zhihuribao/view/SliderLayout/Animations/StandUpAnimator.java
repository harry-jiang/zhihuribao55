//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qian.zhihuribao.view.SliderLayout.Animations;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class StandUpAnimator extends BaseViewAnimator {
    public StandUpAnimator() {
    }

    protected void prepare(View target) {
        float x = (float) ((target.getWidth() - target.getPaddingLeft() - target.getPaddingRight()) / 2 + target.getPaddingLeft());
        float y = (float) (target.getHeight() - target.getPaddingBottom());
        this.getAnimatorAgent().playTogether(new Animator[]{ObjectAnimator.ofFloat(target, "pivotX", new float[]{x, x, x, x, x}), ObjectAnimator.ofFloat(target, "pivotY", new float[]{y, y, y, y, y}), ObjectAnimator.ofFloat(target, "rotationX", new float[]{55.0F, -30.0F, 15.0F, -15.0F, 0.0F})});
    }
}
