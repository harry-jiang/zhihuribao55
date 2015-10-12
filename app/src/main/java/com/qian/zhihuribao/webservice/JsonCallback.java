package com.qian.zhihuribao.webservice;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.qian.zhihuribao.utils.CacheUtil;
import com.qian.zhihuribao.utils.JsonUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;

public abstract class JsonCallback<Tjson> implements Callback {
    private Class<Tjson> tClass;
    private Type type;
    boolean isByClass;

    public JsonCallback(Class<Tjson> tClass) {
        isByClass = true;
        this.tClass = tClass;
    }

    public JsonCallback(Type type) {
        isByClass = false;
        this.type = type;
    }


    public Request request;
    public IOException e;
    public Tjson tjson;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JsonCallback cb;
            switch (msg.what) {
                case -1:
                    cb = (JsonCallback) msg.obj;
                    cb.onError(cb.request, cb.e);
                    break;
                case 1:
                    cb = (JsonCallback) msg.obj;
                    cb.onSuccess(cb.tjson);
                    break;
            }
        }
    };

    @Override
    final public void onFailure(Request request, IOException e) {
        this.request = request;
        this.e = e;
        sendMessage(-1);
    }

    @Override
    final public void onResponse(Response response) throws IOException {
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        String bodyStr = response.body().string();
        Log.d("JsonCallback:bodyStr", bodyStr);
        CacheUtil.save(response.request().urlString(), bodyStr);
        onResponse(bodyStr);

    }

    final public void onResponse(String response) {
        if (isByClass) {
            tjson = JsonUtils.fromJson(response, tClass);
        } else {
            tjson = JsonUtils.fromJson(response, type);
        }
        sendMessage(1);
    }

    private void sendMessage(int what) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = this;
        handler.sendMessage(msg);
    }

    public abstract void onSuccess(Tjson json);

    public abstract void onError(Request request, IOException e);
}