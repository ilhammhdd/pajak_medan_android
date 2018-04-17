package com.pajakmedan.pajakmedan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by milha on 4/2/2018.
 */

abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        initComponent(itemView);
        insideContructor();
    }

    public void insideContructor() {
    }

    abstract void initComponent(View itemView);
}
