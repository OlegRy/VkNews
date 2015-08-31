package com.itis.vknews.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itis.vknews.FullPhotoActivity;
import com.itis.vknews.R;
import com.itis.vknews.async.DownloadImageTask;
import com.itis.vknews.async.DownloadSomeImagesTask;
import com.itis.vknews.model.Attachment;
import com.itis.vknews.model.Item;
import com.itis.vknews.model.Photo;
import com.itis.vknews.model.PhotoAttachment;
import com.itis.vknews.model.Post;
import com.itis.vknews.model.VideoAttachment;
import com.itis.vknews.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsItemFragment extends Fragment {

    private Item mItem;

    private ImageView iv_avatar;
    private TextView tv_author;
    private TextView tv_date;
    private TextView tv_text;
    private LinearLayout ll_images;
    private LinearLayout ll_audio;
    private LinearLayout ll_videos;
    private LinearLayout ll_title_photo;
    private LinearLayout ll_title_video;
    private LinearLayout ll_title_audio;
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
        ll_audio = (LinearLayout) view.findViewById(R.id.ll_audios);
        ll_videos = (LinearLayout) view.findViewById(R.id.ll_videos);
        ll_title_photo = (LinearLayout) view.findViewById(R.id.ll_title_photo);
        ll_title_video = (LinearLayout) view.findViewById(R.id.ll_title_video);
        ll_title_audio = (LinearLayout) view.findViewById(R.id.ll_title_audio);
        tv_likes_count = (TextView) view.findViewById(R.id.tv_likes_count);
        tv_reposts_count = (TextView) view.findViewById(R.id.tv_reposts_count);

        fillViews();
    }

    private void fillViews() {
        tv_author.setText(mItem.getAuthor().getName());
        new DownloadImageTask(iv_avatar).execute(mItem.getAuthor().getAvatar());
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
        List<String> smallImages = new ArrayList<>();
        List<String> bigImages = new ArrayList<>();

        smallImages.addAll(photo.getSmallPhotos());
        bigImages.addAll(photo.getBigPhotos());

        if (smallImages.size() > 0) {
            String[] imageArray = new String[smallImages.size()];
            imageArray = smallImages.toArray(imageArray);
            new DownloadSomeImagesTask(getActivity(), ll_images, bigImages).execute(imageArray);
        }

        //setListenerToImageView(smallImages);
    }

    private void fillByAttachments(Post post) {
        List<String> smallImages = new ArrayList<>();
        List<String> bigImages = new ArrayList<>();
        if (post.getAttachments() != null) {
            for (Attachment attachment : post.getAttachments()) {
                if (attachment instanceof PhotoAttachment) {
                    smallImages.add(((PhotoAttachment) attachment).getSmallPhoto());
                    bigImages.add(((PhotoAttachment) attachment).getBigPhoto());
                    ll_title_photo.setVisibility(View.VISIBLE);
                    //fillImageView(((PhotoAttachment) attachment).getSmallPhoto(), false);

                } else if (attachment instanceof VideoAttachment) {
                    ll_title_video.setVisibility(View.VISIBLE);
                    fillImageView(((VideoAttachment) attachment).getPhoto(), true);
                } else {
                    ll_title_audio.setVisibility(View.VISIBLE);
                }
            }
            if (smallImages.size() > 0) {
                String[] imageArray = new String[smallImages.size()];
                imageArray = smallImages.toArray(imageArray);
                new DownloadSomeImagesTask(getActivity(), ll_images, bigImages).execute(imageArray);
            }
        }
        //setListenerToImageView(smallImages);
    }

    private void fillImageView(final String url, boolean isVideo) {
        ImageView image = new ImageView(getActivity());
        image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        if (isVideo) {
            new DownloadImageTask(image, ll_videos).execute(url);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (url != null) {
                        Uri webUri = Uri.parse(url);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, webUri);
                        if (browserIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(browserIntent);
                        }
                    }
                }
            });
        }
        else new DownloadImageTask(image, ll_images).execute(url);
    }

    private void setListenerToImageView(final List<String> images) {

        if (images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                ImageView image = (ImageView) ll_images.getChildAt(i);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent photoIntent = new Intent(getActivity(), FullPhotoActivity.class);

                        photoIntent.putStringArrayListExtra(Constants.INTENT_PHOTO_LIST,
                                (ArrayList<String>) images);

                        getActivity().startActivity(photoIntent);
                    }
                });

            }
        }
    }
}
