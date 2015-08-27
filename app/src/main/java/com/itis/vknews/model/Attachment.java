package com.itis.vknews.model;

import java.io.Serializable;

public class Attachment implements Serializable {

    public enum Type {
        PHOTO, VIDEO, AUDIO
    }

    private int mId;
    private Type mType;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }

}

