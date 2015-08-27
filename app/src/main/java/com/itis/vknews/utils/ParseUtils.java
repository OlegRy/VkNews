package com.itis.vknews.utils;

import com.itis.vknews.model.Attachment;
import com.itis.vknews.model.AudioAttachment;
import com.itis.vknews.model.Author;
import com.itis.vknews.model.Item;
import com.itis.vknews.model.Photo;
import com.itis.vknews.model.PhotoAttachment;
import com.itis.vknews.model.Post;
import com.itis.vknews.model.VideoAttachment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseUtils {

    private static String mNextFrom;

    public static List<Item> parse(JSONObject result) throws JSONException {
        List<Item> items = new ArrayList<>();
        JSONObject responseJson = result.getJSONObject(Constants.JSON_RESPONSE);
        mNextFrom = responseJson.getString(Constants.JSON_NEXT_FROM);
        JSONArray itemsJson = responseJson.getJSONArray(Constants.JSON_ITEMS);
        for (int i = 0; i < itemsJson.length(); i++) {
            Item item = null;
            boolean isPost = false;
            JSONObject itemJson = itemsJson.getJSONObject(i);
            String type = itemJson.getString(Constants.JSON_TYPE);
            if (type.equals(Constants.JSON_POST)) {
                item = new Post();
                isPost = true;
            } else {
                item = new Photo();
            }
            item.setId(itemJson.getInt(Constants.JSON_ID));
            item.setSourceId(itemJson.getInt(Constants.JSON_SOURCE_ID));
            item.setAuthor(getAuthor(responseJson, item.getSourceId()));
            item.setDate(itemJson.getLong(Constants.JSON_DATE));
            if (!itemJson.getString(Constants.JSON_TYPE).equals(Constants.JSON_TYPE_PHOTO)) {
                item.setLikes(itemJson.getJSONObject(Constants.JSON_LIKES).getInt(Constants.JSON_COUNT));
                item.setReposts(itemJson.getJSONObject(Constants.JSON_REPOSTS).getInt(Constants.JSON_COUNT));
            }
            if (isPost) {
                items.add(parsePost(item, itemJson));
            } else {
                items.add(parsePhoto(item, itemJson));
            }
        }
        return items;
    }

    private static Author getAuthor(JSONObject responseJson, int id) throws JSONException {
        Author author = new Author();
        JSONArray authorsJson = null;
        JSONArray groupsJson = responseJson.getJSONArray(Constants.JSON_GROUPS);
        JSONArray profilesJson = responseJson.getJSONArray(Constants.JSON_PROFILES);
        if (groupsJson != null && profilesJson != null) {
            authorsJson = concatJsonArrays(groupsJson, profilesJson);
        }
        if (authorsJson != null) {
            JSONObject authorJson = authorsJson.getJSONObject(getPositionById(authorsJson, id));
            author.setId(authorJson.getInt(Constants.JSON_AUTHOR_ID));
            author.setAvatar(authorJson.getString(Constants.JSON_PHOTO));
            try {
                author.setName(authorJson.getString(Constants.JSON_NAME));
            } catch (JSONException e) {
                author.setName(authorJson.getString(Constants.JSON_FIRST_NAME) + " " +
                        authorJson.getString(Constants.JSON_LAST_NAME));
            }
        }
        return author;
    }

    private static JSONArray concatJsonArrays(JSONArray groups, JSONArray profiles) throws JSONException {
        JSONArray result = new JSONArray();
        for (int i = 0; i < groups.length(); i++) {
            result.put(groups.get(i));
        }
        for (int i = 0; i < profiles.length(); i++) {
            result.put(profiles.get(i));
        }

        return result;
    }

    private static int getPositionById(JSONArray authors, int id) throws JSONException {
        int position = 0;
        boolean found = false;
        for (int i = 0; i < authors.length() && !found; i++) {
            JSONObject author = authors.getJSONObject(i);
            if (Math.abs(id) == Math.abs(author.getInt(Constants.JSON_AUTHOR_ID))) {
                position = i;
                found = true;
            }
        }
        return position;
    }

    private static Post parsePost(Item item, JSONObject json) throws JSONException {
        item.setType(Item.Type.POST);
        Post post = (Post) item;
        post.setText(json.getString(Constants.JSON_TEXT));
        try {
            post.setAttachments(getAttachments(json.getJSONArray(Constants.JSON_ATTACHMENTS)));
        } catch (JSONException e) {
        }

        return post;
    }

    private static Photo parsePhoto(Item item, JSONObject json) throws JSONException {
        item.setType(Item.Type.PHOTO);
        Photo photo = (Photo) item;
        List<String> smallPhotos = new ArrayList<>();
        List<String> bigPhotos = new ArrayList<>();
        JSONArray photoItems = json.getJSONObject(Constants.JSON_PHOTOS).getJSONArray(Constants.JSON_ITEMS);
        for (int i = 0; i < photoItems.length(); i++) {
            JSONObject photoItem = photoItems.getJSONObject(i);
            smallPhotos.add(photoItem.getString(Constants.JSON_SMALL_PHOTO));
            bigPhotos.add(photoItem.getString(Constants.JSON_BIG_PHOTO));
        }
        photo.setSmallPhotos(smallPhotos);
        photo.setBigPhotos(bigPhotos);
        return photo;
    }

    private static List<Attachment> getAttachments(JSONArray jsonArray) throws JSONException {
        List<Attachment> attachments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonAttachment = jsonArray.getJSONObject(i);
            String type = jsonAttachment.getString(Constants.JSON_TYPE);
            if (type.equals(Constants.JSON_PHOTO_ATTACHMENT) || type.equals(Constants.JSON_POSTED_PHOTO_ATTACHMENT)) {
                PhotoAttachment photo = parsePhotoAttachment(new PhotoAttachment(),
                        jsonAttachment.getJSONObject(Constants.JSON_PHOTO_ATTACHMENT));
                attachments.add(photo);
            } else if (type.equals(Constants.JSON_AUDIO_ATTACHMENT)) {
                AudioAttachment audio = parseAudioAttachment(new AudioAttachment(),
                        jsonAttachment.getJSONObject(Constants.JSON_AUDIO_ATTACHMENT));
                attachments.add(audio);
            } else if (type.equals(Constants.JSON_VIDEO_ATTACHMENT)) {
                VideoAttachment video = parseVideoAttachment(new VideoAttachment(),
                        jsonAttachment.getJSONObject(Constants.JSON_VIDEO_ATTACHMENT));
                attachments.add(video);
            }
        }
        return attachments;
    }

    private static VideoAttachment parseVideoAttachment(Attachment attachment, JSONObject json) throws JSONException {
        attachment.setType(Attachment.Type.VIDEO);
        attachment.setId(json.getInt(Constants.JSON_ATTACHMENT_ID));
        VideoAttachment video = (VideoAttachment) attachment;
        video.setTitle(json.getString(Constants.JSON_TITLE));
        video.setDuration(json.getInt(Constants.JSON_DURATION));
        video.setPhoto(json.getString(Constants.JSON_SMALL_PHOTO));
        try {
            video.setUrl(json.getString(Constants.JSON_VIDEO_URL));
        } catch (JSONException e) {
        }
        return video;
    }

    private static AudioAttachment parseAudioAttachment(Attachment attachment, JSONObject json) throws JSONException {
        attachment.setType(Attachment.Type.AUDIO);
        attachment.setId(json.getInt(Constants.JSON_ATTACHMENT_ID));
        AudioAttachment audio = (AudioAttachment) attachment;
        audio.setArtist(json.getString(Constants.JSON_ARTIST));
        audio.setTitle(json.getString(Constants.JSON_TITLE));
        audio.setDuration(json.getInt(Constants.JSON_DURATION));
        audio.setUrl(json.getString(Constants.JSON_URL));
        return audio;
    }

    private static PhotoAttachment parsePhotoAttachment(Attachment attachment, JSONObject json) throws JSONException {
        attachment.setType(Attachment.Type.PHOTO);
        attachment.setId(json.getInt(Constants.JSON_ATTACHMENT_ID));
        PhotoAttachment photo = (PhotoAttachment) attachment;
        photo.setSmallPhoto(json.getString(Constants.JSON_SMALL_PHOTO));
        photo.setBigPhoto(json.getString(Constants.JSON_BIG_PHOTO));
        return photo;
    }

    public static String getNextFrom() {
        return mNextFrom;
    }
}
