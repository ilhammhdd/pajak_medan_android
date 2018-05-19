package com.pajakmedan.pajakmedan.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.pajakmedan.pajakmedan.AddressesActivity;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.requests.CustomerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by milha on 3/21/2018.
 */

public class ManipulateAddressDialog extends BaseDialog {

    EditText editTextName;
    CheckBox checkBoxMain;
    Button buttonDone;

    public ManipulateAddressDialog(final Activity context) {
        super(context);
        super.activity = context;
        editTextName.setMovementMethod(new ScrollingMovementMethod());
        buttonDone.setOnClickListener(new View.OnClickListener() {
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
                            Toasty.success(context, context.getResources().getString(R.string.berhasil_ditambah)).show();
                            dismiss();
                            context.finish();
                            context.startActivity(new Intent(context, AddressesActivity.class));
                            return;
                        }
                        Toasty.error(context, context.getResources().getString(R.string.gagal_ditambah)).show();
                        dismiss();
                        getMainAddress();
                    }
                });
                try {
                    customerRequest.postAddAddress(new JSONObject()
                            .put("name", editTextName.getText().toString())
                            .put("main", checkBoxMain.isChecked() ? 1 : 0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ManipulateAddressDialog(final Activity context, final List<Address> addressList, final int position, final RecyclerView recyclerViewAddress) {
        super(context);
        super.activity = context;
        setValues(addressList.get(position));
        editTextName.setMovementMethod(new ScrollingMovementMethod());
        buttonDone.setOnClickListener(new View.OnClickListener() {
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
                        JSONObject responseJSONObject = (JSONObject) t;
                        Address address;
                        try {
                            address = Address.extractTheAddress(responseJSONObject.getJSONObject(Constants.RESPONSE_DATA_KEY).getJSONObject("edited_address"));
                            if (responseJSONObject.getBoolean("success")) {
                                dismiss();
                                Toasty.success(context, context.getResources().getString(R.string.berhasil_diedit)).show();
                                addressList.set(position, address);
                                context.finish();
                                context.startActivity(new Intent(context, AddressesActivity.class));
                            } else {
                                Log.d("EDIT_ADDRESS_FAILED", responseJSONObject.toString());
                                dismiss();
                                Toasty.error(context, ManipulateAddressDialog.super.activity.getResources().getString(R.string.gagal_diedit)).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getMainAddress();
                    }
                });
                try {
                    customerRequest.postEditAddress(new JSONObject()
                            .put("address_id", addressList.get(position).addressId)
                            .put("name", editTextName.getText().toString())
                            .put("main", checkBoxMain.isChecked() ? 1 : 0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getContentId() {
        return R.layout.dialog_manipulate_address;
    }

    @Override
    public void initComponent() {
        editTextName = findViewById(R.id.editText_addAddress_name);
        checkBoxMain = findViewById(R.id.checkbox_addAddress_main);
        buttonDone = findViewById(R.id.button_addAddress_done);
    }

    private void setValues(Address address) {
        editTextName.setText(address.name);
        checkBoxMain.setChecked(address.main);
    }
}
