package com.pajakmedan.pajakmedan.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.requests.CustomerRequest;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by milha on 3/28/2018.
 */

public abstract class BaseFragment extends Fragment {

    protected Locale locale;
    protected NumberFormat numberFormat;
    protected Context context;

    protected List<MyAsyncTask> myAsyncTaskList = new ArrayList<>();

    public BaseFragment(){
        this.locale = new Locale("id", "ID");
        this.numberFormat = NumberFormat.getInstance(locale);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    abstract int getLayoutId();

    @Override
    public void onStop() {
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
}
