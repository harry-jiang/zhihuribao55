package com.qian.zhihuribao.webservice;

import com.qian.zhihuribao.utils.OkHttpUtil;
import com.squareup.okhttp.Request;

/**
 * Created by Administrator on 2009/8/20.
 */
public class WebService {
    private static WebService webService = new WebService();

    private WebService() {

    }

    public static WebService getInstance() {
        return webService;
    }

    public void asyncGet(String url, JsonCallback callback) {
        Request request = new Request.Builder().url(url).build();
        OkHttpUtil.enqueue(request, callback);
    }




}
