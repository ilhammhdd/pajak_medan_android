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
import com.pajakmedan.pajakmedan.models.Customer;

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
        switch (Constants.AUTH_TYPE) {
            case 1: {
                Auth.GoogleSignInApi.signOut(Constants.GOOGLE_API_CLIENT).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });

                deleteAllAndRedirect(context);
            }
            case 2: {
                AccessToken.setCurrentAccessToken(null);
                LoginManager.getInstance().logOut();

                deleteAllAndRedirect(context);
            }
            default: {
                deleteAllAndRedirect(context);
            }
        }
    }

    private void deleteAllAndRedirect(Context context) {
        Constants.AUTH_TYPE = -1;
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
}
