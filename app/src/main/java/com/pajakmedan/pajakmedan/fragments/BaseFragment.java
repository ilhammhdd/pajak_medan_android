package com.pajakmedan.pajakmedan.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.asynctasks.GetMainAddress;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Address;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by milha on 3/28/2018.
 */

public abstract class BaseFragment extends Fragment {

    protected Locale locale;
    protected NumberFormat numberFormat;
    protected Context context;

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
}
