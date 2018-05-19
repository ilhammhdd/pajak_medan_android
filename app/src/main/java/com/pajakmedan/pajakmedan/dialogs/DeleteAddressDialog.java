package com.pajakmedan.pajakmedan.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.pajakmedan.pajakmedan.AddressesActivity;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.requests.CustomerRequest;

import org.json.JSONException;
import org.json.JSONObject;

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
                CustomerRequest customerRequest = new CustomerRequest();
                customerRequest.setListener(new ExecuteAsyncTaskListener() {
                    @Override
                    public void onPreExecute(MyAsyncTask myAsyncTask) {
                        myAsyncTaskList.add(myAsyncTask);
                    }

                    @Override
                    public void onPostExecute(Object t) {
                        if ((Boolean) t) {
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
                try {
                    customerRequest.postDeleteAddress(new JSONObject()
                            .put("address_id", address.addressId));
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
