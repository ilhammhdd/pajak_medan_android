package com.pajakmedan.pajakmedan.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentId());
        initComponent();
    }

    public abstract int getContentId();

    public abstract void initComponent();

    public <T> JSONObject getResponseData(T responseAll) {
        try {
            JSONObject response = (JSONObject) responseAll;
            return response.getJSONObject(Constants.RESPONSE_DATA_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
