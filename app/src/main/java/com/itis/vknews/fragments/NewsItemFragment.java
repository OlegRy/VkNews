package com.itis.vknews.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itis.vknews.R;
import com.itis.vknews.async.DownloadImageTask;
import com.itis.vknews.async.DownloadSomeImagesTask;
import com.itis.vknews.model.Attachment;
import com.itis.vknews.model.AudioAttachment;
import com.itis.vknews.model.Item;
import com.itis.vknews.model.Photo;
import com.itis.vknews.model.PhotoAttachment;
import com.itis.vknews.model.Post;
import com.itis.vknews.model.VideoAttachment;
import com.itis.vknews.services.AudioService;
import com.itis.vknews.utils.Constants;
import com.wnafee.vector.MorphButton;

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
    private GridView gv_images;
    private LinearLayout ll_audio;
    private LinearLayout ll_videos;
    private LinearLayout ll_title_photo;
    private LinearLayout ll_title_video;
    private LinearLayout ll_title_audio;
    private TextView tv_reposts_count;
    private TextView tv_likes_count;
    private MorphButton btn_play_pause;


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
        gv_images = (GridView) view.findViewById(R.id.gv_images);
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
            new DownloadSomeImagesTask(getActivity(), gv_images, bigImages).execute(imageArray);
        }
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

                } else if (attachment instanceof VideoAttachment) {
                    ll_title_video.setVisibility(View.VISIBLE);
                    fillImageView(((VideoAttachment) attachment).getPhoto());
                } else {
                    ll_title_audio.setVisibility(View.VISIBLE);
                    View playerItemView = inflatePlayerItem((AudioAttachment) attachment);
                    ll_title_audio.addView(playerItemView);
                }
            }
            if (smallImages.size() > 0) {
                String[] imageArray = new String[smallImages.size()];
                imageArray = smallImages.toArray(imageArray);
                new DownloadSomeImagesTask(getActivity(), gv_images, bigImages).execute(imageArray);
            }
        }
    }

    private void fillImageView(final String url) {
        ImageView image = new ImageView(getActivity());
        int imageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, getActivity()
                .getResources().getDisplayMetrics());
        image.setLayoutParams(new LinearLayout.LayoutParams(imageSize, imageSize));

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

    private View inflatePlayerItem(AudioAttachment attachment) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View playerView = inflater.inflate(R.layout.player_item, null);

        final Button btn_play_pause;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            btn_play_pause = (MorphButton) playerView.findViewById(R.id.btn_play_pause);
        else
            btn_play_pause = (Button) playerView.findViewById(R.id.btn_play_pause);

        TextView tv_song_title = (TextView) playerView.findViewById(R.id.tv_song_title);
        TextView tv_song_author = (TextView) playerView.findViewById(R.id.tv_song_author);
        TextView tv_duration = (TextView) playerView.findViewById(R.id.tv_duration);

        tv_song_title.setText(attachment.getTitle());
        tv_song_author.setText(attachment.getArtist());

        String songLength = getSongLength(attachment.getDuration());
        tv_duration.setText(songLength);
        final int[] clickCount = {0};
        final String songUrl = attachment.getUrl();
        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_audio.invalidate();
                Intent audioIntent = new Intent(getActivity(), AudioService.class);
                getActivity().stopService(audioIntent);
                if (clickCount[0] % 2 == 0) {
                    audioIntent.setAction(Constants.AUDIO_SERVICE_ACTION_PLAY);
                    if (btn_play_pause instanceof Button)
                        btn_play_pause.setBackgroundResource(com.wnafee.vector.R.drawable.ic_play_to_pause);
                }
                else {
                    audioIntent.setAction(Constants.AUDIO_SERVICE_ACTION_PAUSE);
                    if (btn_play_pause instanceof Button)
                        btn_play_pause.setBackgroundResource(com.wnafee.vector.R.drawable.ic_pause_to_play);
                }
                clickCount[0]++;

                audioIntent.putExtra(Constants.AUDIO_SERVICE_DATA_SOURCE, songUrl);
                getActivity().startService(audioIntent);
            }
        });
        return playerView;
    }

    private String getSongLength(int duration) {
        return duration / 60 + ":" + (duration % 60 < 9 ? "0" : "") + duration % 60;
    }


}
