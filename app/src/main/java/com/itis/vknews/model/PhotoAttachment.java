package com.itis.vknews.model;

public class PhotoAttachment extends Attachment {

    private String mSmallPhoto;
    private String mBigPhoto;

    public PhotoAttachment() {
        setType(Type.PHOTO);
    }

    public String getBigPhoto() {
        return mBigPhoto;
    }

    public void setBigPhoto(String bigPhoto) {
        mBigPhoto = bigPhoto;
    }

    public String getSmallPhoto() {
        return mSmallPhoto;
    }

    public void setSmallPhoto(String smallPhoto) {
        mSmallPhoto = smallPhoto;
    }

}
