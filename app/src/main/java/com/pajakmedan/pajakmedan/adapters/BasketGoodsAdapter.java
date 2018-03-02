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
import com.pajakmedan.pajakmedan.models.BasketGoods;
import com.pajakmedan.pajakmedan.models.Goods;

import java.util.List;

/**
 * Created by milha on 3/1/2018.
 */

public class BasketGoodsAdapter extends RecyclerView.Adapter<BasketGoodsAdapter.BasketGoodsViewHolder> {

    static ClickListener clickListener;
    private LayoutInflater inflater;
    public Context context;
    public List<BasketGoods> basketGoods;

    public BasketGoodsAdapter(Context context, List<BasketGoods> basketGoods) {
        this.inflater = LayoutInflater.from(context);
        this.basketGoods = basketGoods;
        this.context = context;
    }

    @Override
    public BasketGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.basket_goods, parent, false);
        return new BasketGoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BasketGoodsAdapter.BasketGoodsViewHolder holder, int position) {
        Goods goods = this.basketGoods.get(position).goods;
        Glide.with(context).load(goods.goodsImageUrl).into(holder.imageViewImage);
        holder.textViewName.setText(goods.goodsName);
        holder.textViewPricee.setText(String.valueOf(goods.goodsPrice));
        String totalAndUnit = this.basketGoods.get(position).goodsQuantity + " " + goods.goodsUnit;
        holder.textViewTotalAndUnit.setText(totalAndUnit);
    }

    @Override
    public int getItemCount() {
        return basketGoods.size();
    }

    public class BasketGoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ConstraintLayout layoutBasketGoods;
        ImageView imageViewImage;
        TextView textViewName;
        TextView textViewPricee;
        TextView textViewTotalAndUnit;

        public BasketGoodsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            layoutBasketGoods = itemView.findViewById(R.id.layout_basketGoods);
            imageViewImage = itemView.findViewById(R.id.imageView_basketGoods_image);
            textViewName = itemView.findViewById(R.id.textView_basketGoods_name);
            textViewPricee = itemView.findViewById(R.id.textView_basketGoods_totalPrice);
            textViewTotalAndUnit = itemView.findViewById(R.id.textView_basketGoods_totalAndUnit);
            layoutBasketGoods.getLayoutParams().height = Constants.DEVICE_HEIGHT / 5;
        }

        @Override
        public void onClick(View view) {
            BasketGoodsAdapter.clickListener.clickItem(view, getAdapterPosition());
        }
    }

    public interface ClickListener {
        void clickItem(View view, int position);
    }

    public void setClickListener(ClickListener clickListener) {
        BasketGoodsAdapter.clickListener = clickListener;
    }
}
