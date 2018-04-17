package com.pajakmedan.pajakmedan.listeners;

import android.view.View;

import com.pajakmedan.pajakmedan.models.Payment;

/**
 * Created by milha on 4/2/2018.
 */

public abstract class AdapterItemClickListener {
    public abstract <T> void clickItem(View view, int position, T object);

    public <T> void clickShowMore(View view, int position, T object) {

    }
}
