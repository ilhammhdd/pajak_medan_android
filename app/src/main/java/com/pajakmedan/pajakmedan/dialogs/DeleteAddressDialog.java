package com.pajakmedan.pajakmedan.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.AddressesActivity;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.asynctasks.PostDeleteAddress;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * Created by milha on 3/22/2018.
 */

public class DeleteAddressDialog extends BaseDialog {

    Button buttonYes;
    Button buttonNo;

    public DeleteAddressDialog(final Activity context, final Address address) {
        super(context);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer customer = Hawk.get(Constants.CUSTOMER_KEY);
                PostDeleteAddress postDeleteAddress = new PostDeleteAddress();
                try {
                    postDeleteAddress.execute(new JSONObject()
                            .put("data", new JSONObject()
                                    .put("url", Constants.DOMAIN + "api/post-delete-address")
                                    .put("api_token", Hawk.get(Constants.USER_API_TOKEN_KEY))
                                    .put("customer_id", customer.customerId)
                                    .put("address_id", address.addressId)
                            )
                    );
                    postDeleteAddress.setOnRequestListener(new OnRequestListener() {
                        @Override
                        public <T> void onRequest(T responseGeneric, String key) throws JSONException {
                            if ((Boolean) responseGeneric) {
                                Toasty.success(context, context.getResources().getString(R.string.berhasil_hapus)).show();
                                dismiss();
                                context.finish();
                                context.startActivity(new Intent(context, AddressesActivity.class));
                                return;
                            }

                            Toasty.error(context, context.getResources().getString(R.string.gagal_hapus)).show();
                            dismiss();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public int getContentId() {
        return R.layout.dialog_delete_address;
    }

    @Override
    public void initComponent() {
        buttonYes = findViewById(R.id.button_deleteAddress_yes);
        buttonNo = findViewById(R.id.button_deleteAddress_no);
    }
}
