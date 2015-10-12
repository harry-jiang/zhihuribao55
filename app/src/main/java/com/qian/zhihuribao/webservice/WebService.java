package com.qian.zhihuribao.webservice;

import android.content.Context;
import android.text.TextUtils;

import com.qian.zhihuribao.utils.CacheUtil;
import com.qian.zhihuribao.utils.NetWorkUtil;
import com.qian.zhihuribao.utils.OkHttpUtil;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * Created by Administrator on 2009/8/20.
 */
public class WebService {
    private static WebService webService;
    Context ctx;

    private WebService(Context ctx) {
        this.ctx = ctx;
    }

    public static void init(Context ctx) {
        webService = new WebService(ctx);
    }

    public static WebService getInstance() {
        return webService;
    }

    public void asyncGet(String url, JsonCallback callback) {
        Request request = new Request.Builder().url(url).build();
        if (NetWorkUtil.isNetWorkConnected(ctx)) {
            OkHttpUtil.enqueue(request, callback);
        } else {
            String str = CacheUtil.get(url);
            if (TextUtils.isEmpty(str)) {
                callback.onFailure(request, new IOException("no NetWork "));
            } else {
                callback.onResponse(str);
            }
        }
    }


}
