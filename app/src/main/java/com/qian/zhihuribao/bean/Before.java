package com.qian.zhihuribao.bean;

import java.util.List;

/**
 * 首页 - 今日热文
 */
public class Before {
    private String date;

    private List<Story> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

}