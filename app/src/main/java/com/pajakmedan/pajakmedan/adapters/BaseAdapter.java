package com.pajakmedan.pajakmedan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.pajakmedan.pajakmedan.listeners.AdapterItemClickListener;
import com.pajakmedan.pajakmedan.models.Checkout;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by milha on 4/2/2018.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    AdapterItemClickListener listener;

    Locale locale;
    NumberFormat numberFormat;

    BaseAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.locale = new Locale("id", "ID");
        this.numberFormat = NumberFormat.getInstance(locale);
    }

    public void setAdapterItemClickListener(AdapterItemClickListener listener) {
        this.listener = listener;
    }
}
