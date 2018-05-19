package com.pajakmedan.pajakmedan.listeners;

import android.view.View;

/**
 * Created by milha on 4/2/2018.
 */

public abstract class AdapterItemClickListener {
    public abstract <T> void clickItem(View view, int position, T object);
}
