package com.pajakmedan.pajakmedan.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.requests.CustomerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 3/17/2018.
 */

public abstract class BaseDialog extends Dialog {
    Activity activity;
    Context context;

    protected List<MyAsyncTask> myAsyncTaskList = new ArrayList<>();

    public BaseDialog(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentId());
        initComponent();
        insideOnCreate();
    }

    public abstract int getContentId();

    public abstract void initComponent();

    public void insideOnCreate() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!myAsyncTaskList.isEmpty()) {
            for (int i = 0; i < myAsyncTaskList.size(); i++) {
                clearAsyncTask(myAsyncTaskList.get(i));
            }
        }
    }

    protected void clearAsyncTask(MyAsyncTask asyncTask) {
        if (asyncTask != null) {
            if (!asyncTask.isCancelled()) {
                asyncTask.cancel(true);
            }
        }
        asyncTask = null;
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
                    Address.saveEmptyMainAddress();
                    return;
                }
                Address.saveMainAddress(responseJson);
            }
        });
        customerRequest.getMainAddress();
    }

    protected String errorMessageWithAttribute(String rule, String attribute) {
        return String.valueOf(rule.replace(":attribute", attribute));
    }

    protected String errorMessageWithAttribute(String rule, String attribute, String digits) {
        rule = rule.replace(":attribute", attribute);
        rule = rule.replace(":digits", digits);
        return rule;
    }
}
