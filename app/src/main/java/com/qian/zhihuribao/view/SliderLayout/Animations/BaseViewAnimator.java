//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qian.zhihuribao.view.SliderLayout.Animations;

import android.view.View;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.view.ViewHelper;

public abstract class BaseViewAnimator {
    private final int Default_Duration = 1000;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private long mDuration = 1000L;

    public BaseViewAnimator() {
    }

    protected abstract void prepare(View var1);

    public void animate(View target) {
        this.reset(target);
        this.prepare(target);
        this.start();
    }

    public void reset(View target) {
        ViewHelper.setAlpha(target, 1.0F);
        ViewHelper.setScaleX(target, 1.0F);
        ViewHelper.setScaleY(target, 1.0F);
        ViewHelper.setTranslationX(target, 0.0F);
        ViewHelper.setTranslationY(target, 0.0F);
        ViewHelper.setRotation(target, 0.0F);
        ViewHelper.setRotationY(target, 0.0F);
        ViewHelper.setRotationX(target, 0.0F);
        ViewHelper.setPivotX(target, (float) target.getMeasuredWidth() / 2.0F);
        ViewHelper.setPivotY(target, (float) target.getMeasuredHeight() / 2.0F);
    }

    public void start() {
        this.mAnimatorSet.setDuration(this.mDuration);
        this.mAnimatorSet.start();
    }

    public BaseViewAnimator setDuration(long duration) {
        this.mDuration = duration;
        return this;
    }

    public BaseViewAnimator setStartDelay(long delay) {
        this.getAnimatorAgent().setStartDelay(delay);
        return this;
    }

    public long getStartDelay() {
        return this.mAnimatorSet.getStartDelay();
    }

    public BaseViewAnimator addAnimatorListener(AnimatorListener l) {
        this.mAnimatorSet.addListener(l);
        return this;
    }

    public void removeAnimatorListener(AnimatorListener l) {
        this.mAnimatorSet.removeListener(l);
    }

    public void removeAllListener() {
        this.mAnimatorSet.removeAllListeners();
    }

    public BaseViewAnimator setInterpolator(Interpolator interpolator) {
        this.mAnimatorSet.setInterpolator(interpolator);
        return this;
    }

    public long getDuration() {
        return this.mAnimatorSet.getDuration();
    }

    public AnimatorSet getAnimatorAgent() {
        return this.mAnimatorSet;
    }
}
