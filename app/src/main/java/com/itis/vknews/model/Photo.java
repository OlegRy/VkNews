package com.itis.vknews.model;

import java.util.List;

public class Photo extends Item {

    private List<String> mSmallPhotos;
    private List<String> mBigPhotos;

    public Photo() {
        setType(Type.PHOTO);
    }

    public List<String> getBigPhotos() {
        return mBigPhotos;
    }

    public void setBigPhotos(List<String> bigPhotos) {
        mBigPhotos = bigPhotos;
    }

    public List<String> getSmallPhotos() {
        return mSmallPhotos;
    }

    public void setSmallPhotos(List<String> smallPhotos) {
        mSmallPhotos = smallPhotos;
    }

}
