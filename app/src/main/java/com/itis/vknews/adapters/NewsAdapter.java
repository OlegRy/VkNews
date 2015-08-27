package com.itis.vknews.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itis.vknews.R;
import com.itis.vknews.async.DownloadImageTask;
import com.itis.vknews.model.Attachment;
import com.itis.vknews.model.Item;
import com.itis.vknews.model.Photo;
import com.itis.vknews.model.PhotoAttachment;
import com.itis.vknews.model.Post;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<Item> mItems;
    private SimpleDateFormat mSimpleDateFormat;
    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public NewsAdapter(List<Item> items) {
        mItems = items;
        mSimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mItems.get(position);
        holder.tv_author.setText(item.getAuthor().getName());
        holder.tv_likes_count.setText(Integer.toString(item.getLikes()));
        holder.tv_reposts_count.setText(Integer.toString(item.getReposts()));
        holder.tv_date.setText(mSimpleDateFormat.format(new Date(item.getDate() * 1000)));
        new DownloadImageTask(holder.iv_avatar).execute(item.getAuthor().getAvatar());
        if (item instanceof Photo) {
            Photo photo = (Photo) item;
            holder.tv_text.setVisibility(View.GONE);
            if (photo.getSmallPhotos() != null) {
                new DownloadImageTask(holder.iv_sample).execute(photo.getSmallPhotos().get(0));
            }
        } else {
            Post post = (Post) item;
            holder.tv_text.setText(post.getText());
            if (post.getAttachments() != null) {
                List<Attachment> attachments = post.getAttachments();
                boolean isPhoto = false;
                for (int i = 0; i < attachments.size() && !isPhoto; i++) {
                     if (attachments.get(i) instanceof PhotoAttachment) {
                         isPhoto = true;
                         PhotoAttachment photo = (PhotoAttachment) attachments.get(i);
                         new DownloadImageTask(holder.iv_sample).execute(photo.getSmallPhoto());
                     }
                }
                if (!isPhoto) holder.iv_sample.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView iv_avatar;
        private TextView tv_author;
        private TextView tv_date;
        private TextView tv_text;
        private ImageView iv_sample;
        private TextView tv_likes_count;
        private TextView tv_reposts_count;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_avatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            tv_author = (TextView) itemView.findViewById(R.id.tv_author);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            iv_sample = (ImageView) itemView.findViewById(R.id.iv_sample);
            tv_likes_count = (TextView) itemView.findViewById(R.id.tv_likes_count);
            tv_reposts_count = (TextView) itemView.findViewById(R.id.tv_reposts_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }


}
