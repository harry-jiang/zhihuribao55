package com.qian.zhihuribao.bean;

public class StoryExtra {
private int vote_status;

private int popularity;

private boolean favorite;

private int long_comments;

private int comments;

private int short_comments;

    public void setVote_status(int vote_status) {
        this.vote_status = vote_status;
    }

    public int getVote_status() {
        return this.vote_status;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getPopularity() {
        return this.popularity;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public int getLong_comments() {
        return this.long_comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getComments() {
        return this.comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    public int getShort_comments() {
        return this.short_comments;
    }

}