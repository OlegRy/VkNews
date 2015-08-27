package com.itis.vknews.model;

import java.util.List;

public class Post extends Item {

    private String mText;
    private List<Attachment> mAttachments;

    public Post() {
        setType(Type.POST);
    }

    public List<Attachment> getAttachments() {
        return mAttachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        mAttachments = attachments;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

}
