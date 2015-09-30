package com.qian.zhihuribao.bean;

import java.util.List;

/**
 * 首页 - 今日热文
 */
public class Latest extends Before {

    private List<TopStory> top_stories;

    public List<TopStory> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStory> top_stories) {
        this.top_stories = top_stories;
    }
}