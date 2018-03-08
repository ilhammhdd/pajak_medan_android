package com.pajakmedan.pajakmedan.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.models.Goods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milha on 2/19/2018.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder> {

    static ClickListener listener;
    public List<Goods> goodsList = new ArrayList<>();
    public Context context;
    public LayoutInflater layoutInflater;

    public GoodsAdapter(Context context, List<Goods> goodsList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.goodsList = goodsList;
        this.context = context;
    }

    @Override
    public GoodsAdapter.GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.goods, parent, false);
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoodsAdapter.GoodsViewHolder holder, int position) {
        holder.layout_goodsItem.getLayoutParams().height = Constants.DEVICE_HEIGHT / 3;
        Goods goods = goodsList.get(position);
        Log.d("TOOOL", "" + goods.goodsAvalibility);
        Glide.with(context).load(goods.goodsImageUrl).into(holder.imageView_goodsImage);
        if (goods.goodsAvalibility) {
            holder.textViewAvailibility.setText(R.string.tersedia);
            holder.textViewAvailibility.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.textViewAvailibility.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.textViewAvailibility.setText(R.string.tidak_tersedia);
            holder.textViewAvailibility.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
            holder.textViewAvailibility.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        holder.textView_goodsName.setText(goods.goodsName);
        holder.textView_goodsUnit.setText(goods.goodsUnit);
        holder.textView_goodsPrice.setText(String.valueOf(goods.goodsPrice));
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public class GoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView_goodsImage;
        TextView textViewAvailibility;
        TextView textView_goodsName;
        TextView textView_goodsUnit;
        TextView textView_goodsPrice;
        ConstraintLayout layout_goodsItem;

        GoodsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView_goodsImage = itemView.findViewById(R.id.imageView_goods_image);
            textViewAvailibility = itemView.findViewById(R.id.textView_goods_availibility);
            textView_goodsName = itemView.findViewById(R.id.textView_goods_name);
            textView_goodsUnit = itemView.findViewById(R.id.textView_goods_unit);
            textView_goodsPrice = itemView.findViewById(R.id.textView_goods_price);
            layout_goodsItem = itemView.findViewById(R.id.layout_goods_item);
        }

        @Override
        public void onClick(View view) {
            GoodsAdapter.listener.clickItem(view, getAdapterPosition(), goodsList.get(getAdapterPosition()));
        }
    }

    public interface ClickListener {
        void clickItem(View view, int position, Goods goods);
    }

    public void setClickListener(ClickListener listener) {
        GoodsAdapter.listener = listener;
    }
}