//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qian.zhihuribao.view.sidemenu.util;

import android.view.animation.Interpolator;

public abstract class SupportAnimator {
    public SupportAnimator() {
    }

    public abstract boolean isNativeAnimator();

    public abstract Object get();

    public abstract void start();

    public abstract void setDuration(int var1);

    public abstract void setInterpolator(Interpolator var1);

    public abstract void addListener(SupportAnimator.AnimatorListener var1);

    public abstract boolean isRunning();

    public interface AnimatorListener {
        void onAnimationStart();

        void onAnimationEnd();

        void onAnimationCancel();

        void onAnimationRepeat();
    }
}
