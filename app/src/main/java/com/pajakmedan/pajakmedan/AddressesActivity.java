package com.pajakmedan.pajakmedan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.adapters.AddressesAdapter;
import com.pajakmedan.pajakmedan.asynctasks.GetAllAddresses;
import com.pajakmedan.pajakmedan.asynctasks.GetMainAddress;
import com.pajakmedan.pajakmedan.dialogs.DeleteAddressDialog;
import com.pajakmedan.pajakmedan.dialogs.ManipulateAddressDialog;
import com.pajakmedan.pajakmedan.listeners.OnRecyclerViewItemClickListener;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

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
        try {
            getAllAddresses();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void setValues(final List<Address> addressList) {
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

    private void getAllAddresses() throws JSONException {
        Customer customer = Hawk.get(Constants.CUSTOMER_KEY);
        GetAllAddresses getAllAddresses = new GetAllAddresses();

        getAllAddresses.execute(new JSONObject()
                .put("data", new JSONObject()
                        .put("url", Constants.DOMAIN + "api/get-all-addresses")
                        .put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY))
                        .put("customer_id", customer.customerId)
                )
        );

        getAllAddresses.setOnRequestListener(new OnRequestListener() {
            @Override
            public <T> void onRequest(T responseGeneric, String key) throws JSONException {
                Hawk.delete(Constants.ALL_ADDRESS_KEY);
                setValues((List<Address>) responseGeneric);
            }
        });
    }
}
