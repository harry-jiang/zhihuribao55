package com.qian.zhihuribao;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.qian.zhihuribao.utils.CacheUtil;
import com.qian.zhihuribao.webservice.WebService;

/**
 * Created by jqian on 2015/10/12.
 */
public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        CacheUtil.open(this);
        WebService.init(this);
    }

    @Override
    public void onTerminate() {
        Fresco.shutDown();
        CacheUtil.close();
        super.onTerminate();

    }
}
