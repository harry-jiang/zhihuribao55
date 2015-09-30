package com.qian.zhihuribao.bean;

import java.util.List;

public class Theme {
    private int limit;

    private List<Object> subscribed;

    private List<Other> others;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Object> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(List<Object> subscribed) {
        this.subscribed = subscribed;
    }

    public List<Other> getOthers() {
        return others;
    }

    public void setOthers(List<Other> others) {
        this.others = others;
    }
}