package com.pajakmedan.pajakmedan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.models.Basket;
import com.pajakmedan.pajakmedan.requests.BasketRequest;
import com.pajakmedan.pajakmedan.requests.CustomerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by milha on 2/19/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    static DisplayMetrics displaymetrics = new DisplayMetrics();

    Locale locale;
    NumberFormat numberFormat;

    public List<MyAsyncTask> myAsyncTaskList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        ButterKnife.bind(this);
        Hawk.init(getApplicationContext()).build();

        this.locale = new Locale("id", "ID");
        this.numberFormat = NumberFormat.getInstance(locale);

        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        insideOnCreate();
    }

    abstract int getContentId();

    abstract void insideOnCreate();

    @Override
    protected void onStop() {
        super.onStop();
        if (!myAsyncTaskList.isEmpty()) {
            for (int i = 0; i < myAsyncTaskList.size(); i++) {
                clearAsyncTask(myAsyncTaskList.get(i));
            }
        }
    }

    public void clearAsyncTask(MyAsyncTask asyncTask) {
        if (asyncTask != null) {
            if (!asyncTask.isCancelled()) {
                asyncTask.cancel(true);
            }
        }
        asyncTask = null;
    }

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

    public void openBasket(Context context) {
        startActivity(new Intent(context, BasketActivity.class));
    }

    public void getMainAddress() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setListener(new ExecuteAsyncTaskListener() {
            @Override
            public void onPreExecute(MyAsyncTask myAsyncTask) {
                myAsyncTaskList.add(myAsyncTask);
            }

            @Override
            public void onPostExecute(Object t) {
                JSONObject responseJson = (JSONObject) t;
                if (responseJson == null) {
                    Log.d("LOGGING_MAIN_ADDRESS", "RESPONSE JSON => NULL");
                    Address.saveEmptyMainAddress();
                    return;
                }
                Log.d("LOGGING_MAIN_ADDRESS", "RESPONSE JSON => " + responseJson.toString());

                Address.saveMainAddress(responseJson);
            }
        });
        customerRequest.getMainAddress();
    }

    void getBasket() {
        BasketRequest basketRequest = new BasketRequest();
        basketRequest.setListener(new ExecuteAsyncTaskListener() {
            @Override
            public void onPreExecute(MyAsyncTask myAsyncTask) {
                myAsyncTaskList.add(myAsyncTask);
            }

            @Override
            public void onPostExecute(Object t) {
                JSONObject response = (JSONObject) t;
                Log.d("LOGGING", "GET BASKET RESPONSE => " + response.toString());
                try {
                    JSONObject responseData = response.getJSONObject("response_data");
                    if (responseData.has("basket")) {
                        JSONObject basket = responseData.getJSONObject("basket");
                        Basket.saveBasket(basket, basket.getString("description") != null);
                        return;
                    }
                    Basket.saveEmptyBasket();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        basketRequest.getBasket();
    }

    protected String errorMessageWithAttribute(String rule, String attribute) {
        return String.valueOf(rule.replace(":attribute", attribute));
    }
}
