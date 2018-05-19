package com.pajakmedan.pajakmedan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.listeners.OnRecyclerViewItemClickListener;
import com.pajakmedan.pajakmedan.listeners.SetRecyclerViewItemClickListener;
import com.pajakmedan.pajakmedan.models.Address;

import java.util.List;

/**
 * Created by milha on 3/21/2018.
 */

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressViewHolder> implements SetRecyclerViewItemClickListener {
    private Context context;
    private List<Address> addressList;
    private LayoutInflater layoutInflater;
    private OnRecyclerViewItemClickListener clickListener;

    public AddressesAdapter(Context context, List<Address> addressList) {
        this.context = context;
        this.addressList = addressList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        if (addressList.get(position).main) {
            holder.textViewMain.setVisibility(View.VISIBLE);
        } else {
            holder.textViewMain.setVisibility(View.GONE);
        }
        holder.textViewName.setText(addressList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    @Override
    public void setClickListener(OnRecyclerViewItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView textViewMain;
        TextView textViewName;
        ImageView imageViewEdit;
        ImageView imageViewDelete;

        AddressViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            initComponent();
            setOnClickListeners();
        }

        private void initComponent() {
            textViewMain = view.findViewById(R.id.textView_address_main);
            textViewName = view.findViewById(R.id.editText_address_name);
            imageViewEdit = view.findViewById(R.id.imageView_address_edit);
            imageViewDelete = view.findViewById(R.id.imageView_address_delete);
        }

        private void setOnClickListeners() {
            imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.clickItem(view, getAdapterPosition());
                }
            });

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.clickItem(view, getAdapterPosition());
                }
            });
        }
    }
}
