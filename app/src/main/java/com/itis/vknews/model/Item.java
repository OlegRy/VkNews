package com.itis.vknews.model;

import java.io.Serializable;

public class Item implements Serializable {

    public enum Type {
        POST, PHOTO
    }

    private int mId;
    private Type mType;
    private int mLikes;
    private int mReposts;
    private Author mAuthor;
    private long mDate;
    private int mSourceId;

    public Author getAuthor() {
        return mAuthor;
    }

    public void setAuthor(Author author) {
        mAuthor = author;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int likes) {
        mLikes = likes;
    }

    public int getReposts() {
        return mReposts;
    }

    public void setReposts(int reposts) {
        mReposts = reposts;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public int getSourceId() {
        return mSourceId;
    }

    public void setSourceId(int sourceId) {
        mSourceId = sourceId;
    }
}
