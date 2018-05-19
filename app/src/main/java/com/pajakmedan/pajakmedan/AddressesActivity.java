package com.pajakmedan.pajakmedan;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.pajakmedan.pajakmedan.adapters.AddressesAdapter;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.dialogs.DeleteAddressDialog;
import com.pajakmedan.pajakmedan.dialogs.ManipulateAddressDialog;
import com.pajakmedan.pajakmedan.listeners.OnRecyclerViewItemClickListener;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.requests.CustomerRequest;

import java.util.List;

import butterknife.BindView;

/**
 * Created by milha on 3/21/2018.
 */

public class AddressesActivity extends BaseActivity {

    @BindView(R.id.imageView_addresses_back)
    ImageView imageViewBack;
    @BindView(R.id.recyclerView_address)
    RecyclerView recyclerViewAddress;
    @BindView(R.id.fab_addresses_add)
    FloatingActionButton fabAdd;

    @Override
    int getContentId() {
        return R.layout.activity_addresses;
    }

    @Override
    void insideOnCreate() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManipulateAddressDialog addAddressDialog = new ManipulateAddressDialog(AddressesActivity.this);
                addAddressDialog.show();
            }
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setValues();
    }

    public void setValues() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.getAllAddresses();
        customerRequest.setListener(new ExecuteAsyncTaskListener() {
            @Override
            public void onPostExecute(Object t) {
                final List<Address> addressList = (List<Address>) t;
                AddressesAdapter addressesAdapter = new AddressesAdapter(AddressesActivity.this, addressList);
                addressesAdapter.setClickListener(new OnRecyclerViewItemClickListener() {
                    @Override
                    public void clickItem(View view, int position) {
                        switch (view.getId()) {
                            case R.id.imageView_address_edit: {
                                ManipulateAddressDialog manipulateAddressDialog = new ManipulateAddressDialog(
                                        AddressesActivity.this,
                                        addressList,
                                        position,
                                        recyclerViewAddress
                                );
                                manipulateAddressDialog.show();
                                break;
                            }
                            case R.id.imageView_address_delete: {
                                new DeleteAddressDialog(AddressesActivity.this, addressList.get(position)).show();
                                break;
                            }
                        }
                    }
                });
                recyclerViewAddress.setAdapter(addressesAdapter);
                recyclerViewAddress.setLayoutManager(new LinearLayoutManager(AddressesActivity.this, LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onPreExecute(MyAsyncTask myAsyncTask) {
                myAsyncTaskList.add(myAsyncTask);
            }
        });
    }
}
