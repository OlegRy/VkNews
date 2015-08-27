package com.itis.vknews.model;

public class VideoAttachment extends Attachment {

    private String mTitle;
    private int mDuration;
    private String mPhoto;
    private String mUrl;

    public VideoAttachment() {
        setType(Type.VIDEO);
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
