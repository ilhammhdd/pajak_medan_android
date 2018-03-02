package com.pajakmedan.pajakmedan.models;

/**
 * Created by milha on 2/19/2018.
 */

public class Goods {

    public int goodsId;
    public String goodsImageUrl;
    public String goodsName;
    public int goodsPrice;
    public boolean goodsAvalibility;
    public String goodsUnit;
    public int minOrder;
    public String condition;

    public Goods(int goodsId, String goodsImageUrl, String goodsName, int goodsPrice, String goodsUnit, boolean goodsAvalibility, int minOrder, String condition) {
        this.goodsId = goodsId;
        this.goodsImageUrl = goodsImageUrl;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsUnit = goodsUnit;
        this.goodsAvalibility = goodsAvalibility;
        this.minOrder = minOrder;
        this.condition = condition;
    }
}
