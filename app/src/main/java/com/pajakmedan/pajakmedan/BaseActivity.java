package com.pajakmedan.pajakmedan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.asynctasks.GetBasket;
import com.pajakmedan.pajakmedan.asynctasks.GetMainAddress;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.models.Customer;
import com.pajakmedan.pajakmedan.models.Payment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by milha on 2/19/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    static DisplayMetrics displaymetrics = new DisplayMetrics();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        ButterKnife.bind(this);
        Hawk.init(getApplicationContext()).build();

        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        insideOnCreate();
    }

    abstract int getContentId();

    abstract void insideOnCreate();

    public static int getDeviceWidth() {
        return displaymetrics.widthPixels;
    }

    public static int getDeviceHeight() {
        return displaymetrics.heightPixels;
    }

    public void logout(Context context) {
        switch ((int) Hawk.get(Constants.AUTH_TYPE_KEY)) {
            case 1: {
                Auth.GoogleSignInApi.signOut(Constants.GOOGLE_API_CLIENT).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });
            }
            case 2: {
                AccessToken.setCurrentAccessToken(null);
                LoginManager.getInstance().logOut();
            }
        }
        deleteAllAndRedirect(context);
    }

    public void deleteAllAndRedirect(Context context) {
        Hawk.deleteAll();

        startActivity(new Intent(context, RegisterActivity.class));
        finish();
    }

    public void getAllCheckout() {
        try {
            Customer customer = Hawk.get(Constants.CUSTOMER_KEY);
            JSONObject request = new JSONObject()
                    .put("data", new JSONObject()
                            .put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY))
                            .put("customer_id", customer.customerId)
                    );


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openBasket(Context context) {
//        Payment payment = Hawk.get(Constants.CURRENT_PAYMENT_KEY);
//        if (payment != null) {
//            Log.d("LOGGING_CURRENT_PAYMENT", "payment still exists");
//            startActivity(new Intent(context, PaymentIssuedActivity.class));
//            return;
//        }
//
//        Log.d("LOGGING_CURRENT_PAYMENT", "payment doesn't exists");

        startActivity(new Intent(context, BasketActivity.class));
    }

    public void getMainAddress() {
        Customer customer = Hawk.get(Constants.CUSTOMER_KEY);
        GetMainAddress getMainAddress = new GetMainAddress();
        try {
            getMainAddress.execute(new JSONObject()
                    .put("data", new JSONObject()
                            .put("url", Constants.DOMAIN + "api/get-main-address")
                            .put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY))
                            .put("customer_id", customer.customerId)
                    ));
            getMainAddress.setOnRequestListener(new OnRequestListener() {
                @Override
                public <T> void onRequest(T responseGeneric, String key) throws JSONException {
//                    Address.saveAddress((JSONObject) responseGeneric);
                    if (responseGeneric == null) {
                        Log.d("LOGGING_MAIN_ADDRESS", "RESPONSE : NULL");
                        Address.saveEmptyMainAddress();
                        return;
                    }

                    Log.d("LOGGING_MAIN_ADDRESS", "RESPONSE : " + responseGeneric.toString());
                    Address.saveMainAddress((JSONObject) responseGeneric);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getBasket() {
        GetBasket getBasket = new GetBasket(String.valueOf(Hawk.get(Constants.USER_API_TOKEN_KEY)));
        getBasket.execute();

        getBasket.setOnRequestListener(new OnRequestListener() {
            @Override
            public <T> void onRequest(T responseGeneric, String key) throws JSONException {
                JSONObject response = (JSONObject) responseGeneric;
                Log.d("MY_LOGGING_GET_BASKET", response.toString());
                JSONObject responseData = response.getJSONObject("response_data");
                if (responseData.has("basket")) {
                    JSONObject basket = responseData.getJSONObject("basket");
                    Log.d("RESPONSE_BASKET", basket.toString());
                    Basket.saveBasket(basket, basket.getString("description") != null);
                    return;
                }
                Basket.saveEmptyBasket();
            }
        });
    }

    protected String errorMessageWithAttribute(String rule, String attribute) {
        return String.valueOf(rule.replace(":attribute", attribute));
    }
}
