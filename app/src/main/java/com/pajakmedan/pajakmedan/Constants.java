package com.pajakmedan.pajakmedan;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.ProfileTracker;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

class Constants {
    static final String DOMAIN = "http://192.168.1.4/";
    static String AUTH_TYPE = "";
    static GoogleSignInOptions GOOGLE_SIGN_IN_OPTIONS;
    static GoogleApiClient GOOGLE_API_CLIENT;
    static CallbackManager CALLBACK_MANAGER;
    static AccessTokenTracker ACCESS_TOKEN_TRACKER;
    static ProfileTracker PROFILE_TRACKER;
    static String USER_API_TOKEN;
}
