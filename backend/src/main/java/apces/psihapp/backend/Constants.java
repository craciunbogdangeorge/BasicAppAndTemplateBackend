package apces.psihapp.backend;

import com.google.api.server.spi.Constant;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 * Contains the client IDs and scopes for allowed clients consuming the psih API.
 */
public class Constants {
    public static final String WEB_CLIENT_ID = "replace this with your web client ID";
    public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;
    public static final String ANDROID_CLIENT_ID = "replace this with your Android client ID";
    public static final String IOS_CLIENT_ID = "replace this with your iOS client ID";
    public static final String EMAIL_SCOPE = Constant.API_EMAIL_SCOPE;
    public static final String API_EXPLORER_CLIENT_ID = Constant.API_EXPLORER_CLIENT_ID;

    public static final String MEMCACHE_ANNOUNCEMENTS_KEY = "RECENT_ANNOUNCEMENTS";
}
