package com.itis.vknews.utils;

import com.vk.sdk.VKScope;

public class Constants {

    public static final String APP_ID = "4935099";
    public static final String[] SCOPES = { VKScope.WALL, VKScope.FRIENDS };


    public static final String SERVICE_INTENT_BROADCAST = "com.itis.vknews.services.RequestService";
    public static final String INTENT_LIST = "mItems";
    public static final String BUNDLE_ITEM = "item";

    public static final String METHOD_NAME = "newsfeed.get";
    public static final String FILTER_NAMES = "post,photo";
    public static final String FILTERS = "filters";


    public static final int NEWS_COUNT = 10;
    public static final String START_FROM = "start_from";

    // JSON
    public static final String JSON_RESPONSE = "response";
    public static final String JSON_ITEMS = "items";
    public static final String JSON_TYPE = "type";


    public static final String JSON_POST = "post";
    public static final String JSON_TYPE_PHOTO = "photo";
    public static final String JSON_GROUPS = "groups";
    public static final String JSON_PROFILES = "profiles";
    public static final String JSON_ID = "post_id";
    public static final String JSON_PHOTO = "photo_50";
    public static final String JSON_NAME = "name";
    public static final String JSON_FIRST_NAME = "first_name";
    public static final String JSON_LAST_NAME = "last_name";
    public static final String JSON_DATE = "date";
    public static final String JSON_LIKES = "likes";
    public static final String JSON_COUNT = "count";
    public static final String JSON_REPOSTS = "reposts";
    public static final String JSON_TEXT = "text";
    public static final String JSON_ATTACHMENTS = "attachments";
    public static final String JSON_PHOTO_ATTACHMENT = "photo";
    public static final String JSON_POSTED_PHOTO_ATTACHMENT = "posted_photo";
    public static final String JSON_AUDIO_ATTACHMENT = "audio";
    public static final String JSON_VIDEO_ATTACHMENT = "video";
    public static final String JSON_ATTACHMENT_ID = "id";
    public static final String JSON_SMALL_PHOTO = "photo_604";
    public static final String JSON_BIG_PHOTO = "photo_604";
    public static final String JSON_ARTIST = "artist";
    public static final String JSON_TITLE = "title";
    public static final String JSON_DURATION = "duration";
    public static final String JSON_URL = "url";
    public static final String JSON_VIDEO_URL = "player";
    public static final String JSON_NEXT_FROM = "next_from";
    public static final String JSON_PHOTOS = "photos";
    public static final String JSON_AUTHOR_ID = "id";
    public static final String JSON_SOURCE_ID = "source_id";
}
