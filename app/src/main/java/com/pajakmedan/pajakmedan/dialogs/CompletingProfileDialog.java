package com.pajakmedan.pajakmedan.dialogs;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.pajakmedan.pajakmedan.Constants;
import com.pajakmedan.pajakmedan.R;
import com.pajakmedan.pajakmedan.asynctasks.MyAsyncTask;
import com.pajakmedan.pajakmedan.listeners.ExecuteAsyncTaskListener;
import com.pajakmedan.pajakmedan.models.Profile;
import com.pajakmedan.pajakmedan.requests.CustomerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

/**
 * Created by milha on 3/20/2018.
 */

public class CompletingProfileDialog extends BaseDialog {

    TextView textViewForName;
    EditText editTextFullName;
    TextView textViewForPhone;
    EditText editTextPhone;
    //    TextView textViewForEmail;
//    EditText editTextEmail;
    Button buttonDone;

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
//        textViewForEmail = findViewById(R.id.textView_completingProfile_forEmail);
        editTextFullName = findViewById(R.id.editText_completingProfile_fullName);
        editTextPhone = findViewById(R.id.editText_completingProfile_phoneNumber);
//        editTextEmail = findViewById(R.id.editText_completingProfile_email);
        buttonDone = findViewById(R.id.button_completingProfile_done);
    }

    @Override
    public void insideOnCreate() {
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
                        Log.d("LOGGING", "EDIT PROFILE RESPONSE => " + t.toString());
                        JSONObject responseJson = (JSONObject) t;
                        try {
                            if (responseJson.getBoolean("success")) {
                                Toasty.success(CompletingProfileDialog.super.activity, "Berhasil edit profile", Toast.LENGTH_SHORT, true).show();
                                dismiss();
                            } else {
                                for (int i = 0; i < responseJson.getJSONArray("message").length(); i++) {
                                    if (responseJson.getJSONArray("message").getString(i).equals(errorMessageWithAttribute(Constants.ERROR_MESSAGE_UNIQUE, "phone number"))) {
                                        Toasty.error(CompletingProfileDialog.super.activity, "Nomor telepon sudah terpakai", Toast.LENGTH_SHORT, true).show();
                                    } else if (responseJson.getJSONArray("message").getString(i).equals(errorMessageWithAttribute(Constants.ERROR_MESSAGE_DIGITS, "phone number", "10"))) {
                                        Toasty.error(CompletingProfileDialog.super.activity, "Jumlah digit nomor telepon tidak valid", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                                dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                try {
                    customerRequest.postEditProfile(new JSONObject()
                            .put("full_name", editTextFullName.getText().toString().equals("") ? null : editTextFullName.getText().toString())
                            .put("phone_number", editTextPhone.getText().toString().equals("") ? null : editTextPhone.getText()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Profile.updateProfile(editTextFullName.getText().toString(), editTextPhone.getText().toString());
//                PostEditProfile postEditProfile = new PostEditProfile(String.valueOf(Hawk.get(Constants.USER_API_TOKEN_KEY)));
//                try {
//                    postEditProfile.execute(new JSONObject()
//                            .put("full_name", editTextFullName.getText().toString().equals("") ? null : editTextFullName.getText().toString())
//                            .put("phone_number", editTextPhone.getText().toString().equals("") ? null : editTextPhone.getText())
//                    );
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                postEditProfile.setOnRequestListener(new OnRequestListener() {
//                    @Override
//                    public <T> void onRequest(T responseGeneric, String key) throws JSONException {
//                        JSONObject responseJson = (JSONObject) responseGeneric;
//                        if (responseJson.getBoolean("success")) {
//                            Toasty.success(CompletingProfileDialog.super.activity, "Berhasil edit profile", Toast.LENGTH_SHORT, true).show();
//                            dismiss();
//                        } else {
//                            for (int i = 0; i < responseJson.getJSONArray("message").length(); i++) {
//                                if (responseJson.getJSONArray("message").getString(i).equals(errorMessageWithAttribute(Constants.ERROR_MESSAGE_UNIQUE, "phone number"))) {
//                                    Toasty.error(CompletingProfileDialog.super.activity, "Nomor telepon sudah terpakai", Toast.LENGTH_SHORT, true).show();
//                                } else if (responseJson.getJSONArray("message").getString(i).equals(errorMessageWithAttribute(Constants.ERROR_MESSAGE_DIGITS, "phone number", "10"))) {
//                                    Toasty.error(CompletingProfileDialog.super.activity, "Jumlah digit nomor telepon tidak valid", Toast.LENGTH_SHORT, true).show();
//                                }
//                            }
//                            dismiss();
//                        }
//                    }
//                });
            }
        });
    }

    private void setValues() {
        Profile profile = Hawk.get(Constants.PROFILE_KEY);
        if (!profile.fullName.equals("")) {
            editTextFullName.setText(profile.fullName);
//            editTextFullName.setFocusable(false);
//            editTextFullName.setClickable(false);
        }

        if (!profile.phoneNumber.equals("")) {
            editTextPhone.setText(profile.phoneNumber);
//            editTextPhone.setFocusable(false);
//            editTextPhone.setClickable(false);
        }

//        if (!profile.email.equals("")) {
//            editTextEmail.setText(profile.email);
//            editTextEmail.setFocusable(false);
//            editTextEmail.setClickable(false);
//        }
    }
}