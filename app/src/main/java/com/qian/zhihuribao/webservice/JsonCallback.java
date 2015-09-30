package com.qian.zhihuribao.webservice;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
        Message msg = new Message();
        msg.what = -1;
        msg.obj = this;
        handler.sendMessage(msg);
    }

    @Override
    final public void onResponse(Response response) throws IOException {
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        String bodyStr = response.body().string();
        Log.d("JsonCallback:bodyStr", bodyStr);
        if (isByClass) {
            tjson = JsonUtils.fromJson(bodyStr, tClass);
        } else {
            tjson = JsonUtils.fromJson(bodyStr, type);
        }
        Message msg = new Message();
        msg.what = 1;
        msg.obj = this;
        handler.sendMessage(msg);
    }

    public abstract void onSuccess(Tjson json);

    public abstract void onError(Request request, IOException e);
}