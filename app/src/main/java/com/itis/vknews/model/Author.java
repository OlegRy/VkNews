package com.itis.vknews.model;

import java.io.Serializable;

public class Author implements Serializable {

    private int mId;
    private String mName;
    private String mAvatar;

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
