package com.pajakmedan.pajakmedan.models;

/**
 * Created by milha on 3/3/2018.
 */

public class Payment {
    public int paymentId;
    public String paymentImageUrl;
    public String paymentName;
    public String paymentDetail;

    public Payment(int paymentId, String paymentImageUrl, String paymentName, String paymentDetail) {
        this.paymentId = paymentId;
        this.paymentImageUrl = paymentImageUrl;
        this.paymentName = paymentName;
        this.paymentDetail = paymentDetail;
    }
}
