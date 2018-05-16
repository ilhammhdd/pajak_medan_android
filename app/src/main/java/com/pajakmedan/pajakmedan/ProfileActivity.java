package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.content.res.Resources;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.asynctasks.GetMainAddress;
import com.pajakmedan.pajakmedan.dialogs.CompletingProfileDialog;
import com.pajakmedan.pajakmedan.listeners.OnRequestListener;
import com.pajakmedan.pajakmedan.models.Address;
import com.pajakmedan.pajakmedan.models.Customer;
import com.pajakmedan.pajakmedan.models.Profile;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by milha on 3/16/2018.
 */

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.textView_profile_fullName)
    TextView textViewFullName;
    @BindView(R.id.textView_profile_phoneNumber)
    TextView textViewPhone;
    @BindView(R.id.textView_profile_email)
    TextView textViewEmail;
    @BindView(R.id.textView_profile_address)
    TextView textViewAddress;
    @BindView(R.id.button_profile_address)
    Button buttonManipulateAddress;
    @BindView(R.id.button_profile_profile)
    Button buttonManipulateProfile;

    @Override
    int getContentId() {
        return R.layout.activity_profile;
    }

    @Override
    void insideOnCreate() {
        textViewAddress.setMovementMethod(new ScrollingMovementMethod());
        setProfileValues();
        setAddressValues();

        buttonManipulateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompletingProfileDialog profileDialog = new CompletingProfileDialog(ProfileActivity.this);
                profileDialog.show();
            }
        });

        buttonManipulateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, AddressesActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAddressValues();
    }

    private void setProfileValues() {
        Profile profile = Hawk.get(Constants.PROFILE_KEY);
        textViewFullName.setText(profile.fullName);
        textViewEmail.setText(profile.email);
        if (!profile.phoneNumber.equals("")) {
            Constants.PROFILE_COMPLETE = true;
            textViewPhone.setText(profile.phoneNumber);
            textViewPhone.setTextColor(getResources().getColor(R.color.colorBlack));
        } else {
            Constants.PROFILE_COMPLETE = false;
            buttonManipulateProfile.setText(R.string.lengkapi_profile);
            textViewPhone.setText(R.string.tidak_ada_alamat_utama);
            textViewPhone.setTextColor(getResources().getColor(R.color.colorRedAlert));
        }
    }

    @OnClick(R.id.imageView_profile_back)
    void backPressed() {
        finish();
    }

    private void setAddressValues() {
        Address address = Hawk.get(Constants.MAIN_ADDRESS_KEY);

        if (!address.name.equals("")) {
            buttonManipulateAddress.setText(R.string.edit_alamat);
            textViewAddress.setText(address.name);
            textViewAddress.setTextColor(getResources().getColor(R.color.colorBlack));
        } else {
            buttonManipulateAddress.setText(R.string.tambah_alamat);
            textViewAddress.setText(R.string.tidak_ada_alamat_utama);
            textViewAddress.setTextColor(getResources().getColor(R.color.colorRedAlert));
        }
    }
}
