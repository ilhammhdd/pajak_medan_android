package com.pajakmedan.pajakmedan.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.models.Payment;

import java.util.List;

/**
 * Created by milha on 3/3/2018.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    static ClickListener clickListener;
    public List<Payment> paymentList;
    public Context context;
    public LayoutInflater layoutInflater;

    public PaymentAdapter(Context context, List<Payment> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public PaymentAdapter.PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.payment, parent, false);
        return new PaymentAdapter.PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentAdapter.PaymentViewHolder holder, int position) {
        Payment payment = paymentList.get(position);
        holder.textViewName.setText(payment.paymentName);
        Glide.with(context).load(payment.paymentImageUrl).into(holder.imageViewImage);
        holder.layoutPayment.getLayoutParams().height = Constants.DEVICE_HEIGHT / 9;
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ConstraintLayout layoutPayment;
        ImageView imageViewImage;
        TextView textViewName;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            layoutPayment = itemView.findViewById(R.id.layout_payment);
            imageViewImage = itemView.findViewById(R.id.imageView_payment_image);
            textViewName = itemView.findViewById(R.id.textView_payment_name);
        }

        @Override
        public void onClick(View view) {
            PaymentAdapter.clickListener.clickItem(view, paymentList.get(getAdapterPosition()));
        }
    }

    public interface ClickListener {
        void clickItem(View view, Payment payment);
    }

    public void setClickListener(ClickListener clickListener) {
        PaymentAdapter.clickListener = clickListener;
    }
}
