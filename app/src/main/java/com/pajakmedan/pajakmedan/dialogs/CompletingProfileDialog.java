package com.pajakmedan.pajakmedan.dialogs;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.models.Profile;

import butterknife.BindView;

/**
 * Created by milha on 3/20/2018.
 */

public class CompletingProfileDialog extends BaseDialog {

    TextView textViewForName;
    EditText editTextFullName;
    TextView textViewForPhone;
    EditText editTextPhone;
    TextView textViewForEmail;
    EditText editTextEmail;

    public CompletingProfileDialog(Activity context) {
        super(context);
        super.activity = context;
        setValues();
    }

    @Override
    public int getContentId() {
        return R.layout.dialog_completing_profile;
    }

    @Override
    public void initComponent() {
        textViewForName = findViewById(R.id.textView_completingProfile_forName);
        textViewForPhone = findViewById(R.id.textView_completingProfile_forPhone);
        textViewForEmail = findViewById(R.id.textView_completingProfile_forEmail);
        editTextFullName = findViewById(R.id.editText_completingProfile_fullName);
        editTextPhone = findViewById(R.id.editText_completingProfile_phoneNumber);
        editTextEmail = findViewById(R.id.editText_completingProfile_email);
    }

    private void setValues() {
        /*textViewForName.setVisibility(View.GONE);
        textViewForPhone.setVisibility(View.GONE);
        textViewForEmail.setVisibility(View.GONE);
        editTextEmail.setVisibility(View.GONE);
        editTextFullName.setVisibility(View.GONE);
        editTextPhone.setVisibility(View.GONE);*/

        Profile profile = Hawk.get(Constants.PROFILE_KEY);
        if (!profile.fullName.equals("")) {
            editTextFullName.setText(profile.fullName);
            editTextFullName.setFocusable(false);
            editTextFullName.setClickable(false);
        }
        if (!profile.phoneNumber.equals("")) {
            editTextPhone.setText(profile.phoneNumber);
            editTextPhone.setFocusable(false);
            editTextPhone.setClickable(false);
        }
        if (!profile.email.equals("")) {
            editTextEmail.setText(profile.email);
            editTextEmail.setFocusable(false);
            editTextEmail.setClickable(false);
        }
    }
}