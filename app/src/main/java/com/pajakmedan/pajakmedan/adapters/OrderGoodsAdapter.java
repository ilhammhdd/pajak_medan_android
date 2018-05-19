package com.pajakmedan.pajakmedan.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.models.BasketGoods;
import com.pajakmedan.pajakmedan.models.Goods;

import java.util.List;

/**
 * Created by milha on 4/6/2018.
 */

public class OrderGoodsAdapter extends BaseAdapter {

    private List<BasketGoods> basketGoods;

    public OrderGoodsAdapter(Context context, List<BasketGoods> basketGoods) {
        super(context);
        this.basketGoods = basketGoods;
    }

    @Override
    public OrderGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = super.layoutInflater.inflate(R.layout.order_goods, parent, false);
        return new OrderGoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        OrderGoodsViewHolder orderGoodsViewHolder = (OrderGoodsViewHolder) holder;
        Goods goods = this.basketGoods.get(position).goods;

        Glide.with(context).load(goods.goodsImageUrl).into(orderGoodsViewHolder.imageViewOrderGoods);
        orderGoodsViewHolder.textViewName.setText(goods.goodsName);
        orderGoodsViewHolder.textViewTotalPrice.setText(String.valueOf(goods.goodsPrice));
        String totalAndUnit = String.valueOf(this.basketGoods.get(position).goodsQuantity) + " " + String.valueOf(goods.goodsUnit);
        orderGoodsViewHolder.textViewTotalAndUnit.setText(totalAndUnit);
    }

    @Override
    public int getItemCount() {
        return basketGoods.size();
    }

    class OrderGoodsViewHolder extends BaseViewHolder {

        ConstraintLayout constraintLayoutOrderGoods;
        ImageView imageViewOrderGoods;
        TextView textViewName;
        TextView textViewTotalPrice;
        TextView textViewTotalAndUnit;

        OrderGoodsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void initComponent(View itemView) {
            imageViewOrderGoods = itemView.findViewById(R.id.imageView_orderGoods_image);
            textViewName = itemView.findViewById(R.id.textView_orderGoods_name);
            textViewTotalPrice = itemView.findViewById(R.id.textView_orderGoods_totalPrice);
            textViewTotalAndUnit = itemView.findViewById(R.id.textView_orderGoods_totalAndUnit);
            constraintLayoutOrderGoods = itemView.findViewById(R.id.constraintLayout_orderGoods_item);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
