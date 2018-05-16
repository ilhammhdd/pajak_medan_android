package com.pajakmedan.pajakmedan.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.asynctasks.GetMainAddress;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by milha on 3/17/2018.
 */

public abstract class BaseDialog extends Dialog {
    Activity activity;

    public BaseDialog(Context context) {
        super(context);
//        Hawk.init(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentId());
        initComponent();
        insideOnCreate();
    }

    public abstract int getContentId();

    public abstract void initComponent();

    public void insideOnCreate() {
    }

    public <T> JSONObject getResponseData(T responseAll) {
        try {
            JSONObject response = (JSONObject) responseAll;
            return response.getJSONObject(Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getMainAddress() {
        GetMainAddress getMainAddress = new GetMainAddress(String.valueOf(Hawk.get(Constants.USER_API_TOKEN_KEY)));
        getMainAddress.execute();
        getMainAddress.setOnRequestListener(new OnRequestListener() {
            @Override
            public <T> void onRequest(T responseGeneric, String key) throws JSONException {
                if (responseGeneric == null) {
                    Log.d("RESPONSE_MAIN_ADDRESS", "NULL");
                    Address.saveEmptyMainAddress();
                    return;
                }

                Log.d("RESPONSE_MAIN_ADDRESS", responseGeneric.toString());
                Address.saveMainAddress((JSONObject) responseGeneric);
            }
        });
    }

    protected String errorMessageWithAttribute(String rule, String attribute) {
        return String.valueOf(rule.replace(":attribute", attribute));
    }

    protected String errorMessageWithAttribute(String rule, String attribute, String digits) {
        rule  = rule.replace(":attribute", attribute);
        rule = rule.replace(":digits", digits);
        return rule;
    }
}
