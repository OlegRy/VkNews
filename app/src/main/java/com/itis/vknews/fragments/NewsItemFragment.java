package com.itis.vknews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itis.vknews.R;
import com.itis.vknews.async.DownloadImageTask;
import com.itis.vknews.model.Attachment;
import com.itis.vknews.model.Item;
import com.itis.vknews.model.Photo;
import com.itis.vknews.model.PhotoAttachment;
import com.itis.vknews.model.Post;
import com.itis.vknews.model.VideoAttachment;
import com.itis.vknews.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsItemFragment extends Fragment {

    private Item mItem;

    private ImageView iv_avatar;
    private TextView tv_author;
    private TextView tv_date;
    private TextView tv_text;
    private LinearLayout ll_images;
    private TextView tv_reposts_count;
    private TextView tv_likes_count;


    public static NewsItemFragment newInstance(Item item) {
        NewsItemFragment fragment = new NewsItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.BUNDLE_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mItem = (Item) args.getSerializable(Constants.BUNDLE_ITEM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_item, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {

        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        tv_author = (TextView) view.findViewById(R.id.tv_author);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        tv_text = (TextView) view.findViewById(R.id.tv_text);
        ll_images = (LinearLayout) view.findViewById(R.id.ll_images);
        tv_likes_count = (TextView) view.findViewById(R.id.tv_likes_count);
        tv_reposts_count = (TextView) view.findViewById(R.id.tv_reposts_count);

        fillViews();
    }

    private void fillViews() {
        tv_author.setText(mItem.getAuthor().getName());
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        tv_date.setText(format.format(new Date(mItem.getDate() * 1000)));
        if (mItem instanceof Post) {
            tv_text.setText(((Post) mItem).getText());
        }
        tv_likes_count.setText(Integer.toString(mItem.getLikes()));
        tv_reposts_count.setText(Integer.toString(mItem.getReposts()));
        fillImageViews();
    }

    private void fillImageViews() {
        if (mItem instanceof Post) {
            Post post = (Post) mItem;
            fillByAttachments(post);
        } else {
            Photo photo = (Photo) mItem;
            fillByPhotos(photo);
        }
    }

    private void fillByPhotos(Photo photo) {
        for (String url : photo.getBigPhotos()) {
            fillImageView(url, false);
        }
    }

    private void fillByAttachments(Post post) {
        for (Attachment attachment : post.getAttachments()) {
            if (attachment instanceof PhotoAttachment) {
                fillImageView(((PhotoAttachment) attachment).getBigPhoto(), false);
            } else if (attachment instanceof VideoAttachment) {
                fillImageView(((VideoAttachment) attachment).getPhoto(), true);
            } else {

            }
        }
    }

    private void fillImageView(String url, boolean isVideo) {
        ImageView image = new ImageView(getActivity());
        image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        if (isVideo) {

        }
        new DownloadImageTask(image, ll_images).execute(url);
    }
}
