package com.pajakmedan.pajakmedan;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class Constants {
    public static final String DOMAIN = "http://192.168.0.109/";
    public static GoogleApiClient GOOGLE_API_CLIENT;
    public static int DEVICE_WIDTH = BaseActivity.getDeviceWidth();
    public static int DEVICE_HEIGHT = BaseActivity.getDeviceHeight();
    public static String AUTH_TYPE_KEY = "8ee5b71356cba0ce69d7c9c47d6ff835";
    public static final String INSTALLATION = "INSTALLATION";
    public static String CURRENT_CATEGORY_KEY = "23458e5bf21c734dcbf0b582c2efd2a0";
    public static String CURRENT_GOODS_KEY = "d381a1f743f82d64096bc2cc96f4bd77";
    public static String CURRENT_PAYMENT_KEY = "452bbb672005ff2c11ab55991278767b";
    public static String CURRENT_BASKET_GOODS_KEY = "76bdf448e5b375637f1f907b479b250a";
    public static String USER_API_TOKEN_KEY = "c9434c6b0fddf378dcf9f24e5085c6f2";
    public static String USER_KEY = "16a223f175124cb73fdee1cf0ac2a3e8";
    public static String PROFILE_KEY = "bb321de2d6143822560de05097096813";
    public static String BASKET_KEY = "82b5601763808faae81f5a026933f61d";
    public static String CUSTOMER_KEY = "a43f4136b976b973887a9989a67853cb";
    public static String PROFILE_PHOTO = "d10b57e55f53f2b715f0c63ed8d60ac7";
    public static String MAIN_ADDRESS_KEY = "08780c1c73fa2f351c8a8d56c152fe37";
    public static String ALL_ADDRESS = "f15590053e02b0f77cd3bb0bb794983c";
    public static String RESPONSE_DATA_KEY = "response_data";
    public static int GOODS_QUANTITY = 0;
    public static boolean PROFILE_COMPLETE = false;

    public static <T> Object getResponseDataGeneric(T responseGeneric, String key) throws JSONException {
        JSONObject responseAll = (JSONObject) responseGeneric;
        return responseAll.get(key);
    }
}