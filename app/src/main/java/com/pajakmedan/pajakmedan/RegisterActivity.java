package com.pajakmedan.pajakmedan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.pajakmedan.pajakmedan.asynctasks.Register;
import com.pajakmedan.pajakmedan.listeners.OnPostListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by milha on 1/6/2018.
 */

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button button_register, button_register_google, button_register_signOutGoogle, button_register_facebook, button_register_signOutFacebook;
    EditText editText_nama, editText_email, editText_noHp, editText_username, editText_password, editText_confirmPassword;
    TextView textView_status, textView_login;

    GoogleSignInOptions gso;
    GoogleApiClient gac;
    CallbackManager callbackManager;

    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    private static final int REQ_CODE = 9001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (!Constants.AUTH_TYPE.equals("")) {
            startActivity(new Intent(getApplicationContext(), CustomerHome.class));
            finish();
        }

        button_register = findViewById(R.id.button_register);
        button_register_google = findViewById(R.id.button_register_google);
        button_register_signOutGoogle = findViewById(R.id.button_register_signOutGoogle);
        button_register_facebook = findViewById(R.id.button_register_facebook);
        button_register_signOutFacebook = findViewById(R.id.button_register_signOutFacebook);
        editText_nama = findViewById(R.id.editText_register_nama);
        editText_email = findViewById(R.id.editText_register_email);
        editText_noHp = findViewById(R.id.editText_register_noHp);
        editText_username = findViewById(R.id.editText_register_username);
        editText_password = findViewById(R.id.editText_register_password);
        editText_confirmPassword = findViewById(R.id.editText_register_confirmPassword);
        textView_status = findViewById(R.id.textView_register_text);
        textView_login = findViewById(R.id.textView_register_loginDisini);

        /*gso*/
        Constants.GOOGLE_SIGN_IN_OPTIONS = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        /*gac*/
//        Constants.GOOGLE_API_CLIENT = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, Constants.GOOGLE_SIGN_IN_OPTIONS).build();
        Constants.GOOGLE_API_CLIENT = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, Constants.GOOGLE_SIGN_IN_OPTIONS).build();
        /*callbackManager*/
        Constants.CALLBACK_MANAGER = CallbackManager.Factory.create();

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register register = new Register();

                if (!editText_password.getText().toString().equals(editText_confirmPassword.getText().toString())) {
                    textView_status.setText("Konfirmasi password harus sesuai");
                } else {
                    try {
                        JSONObject data = new JSONObject();
                        data.put("url", Constants.DOMAIN + "api/register");
                        data.put("fullName", editText_nama.getText());
                        data.put("phoneNumber", editText_noHp.getText());
                        data.put("email", editText_email.getText());
                        data.put("username", editText_username.getText());
                        data.put("password", editText_password.getText());

                        JSONObject dataChunk = new JSONObject();
                        dataChunk.put("data", data);

                        register.execute(dataChunk);

                        register.setOnPostListener(new OnPostListener() {
                            @Override
                            public void onPost(JSONObject response) throws JSONException {
                                textView_status.setText(response.getString("message"));
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        textView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
                finish();
            }
        });

        button_register_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });

        button_register_signOutGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {
                signOutGoogle();
            }
        });

        button_register_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInFacebook();
            }
        });

        button_register_signOutFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutFacebook();
            }
        });

        LoginManager.getInstance().registerCallback(/*callbackManager*/ Constants.CALLBACK_MANAGER,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("===============Sign in Facebook Success===============");
                        facebookSignInOnSuccess(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("===============Sign in Facebook Canceled===============");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        System.out.println("===============Sign in Facebook Error===============");
                    }
                });


    }


    @Override
    protected void onStart() {
        super.onStart();

        Constants.GOOGLE_API_CLIENT.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(Constants.GOOGLE_API_CLIENT);
        startActivityForResult(signInIntent, REQ_CODE);
    }

    private void signOutGoogle() {
        Auth.GoogleSignInApi.signOut(Constants.GOOGLE_API_CLIENT).setResultCallback(new ResultCallback<Status>() {

            @Override
            public void onResult(@NonNull Status status) {
                System.out.println("===============Sign Out Google Success===============");
            }
        });
    }

    private void signInFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile", "user_friends"));
    }

    private void signOutFacebook() {
        System.out.println("==================Sign Out Facebook Success==================");
        LoginManager.getInstance().logOut();
    }

    private void facebookSignInOnSuccess(LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();

        if (accessToken != null) {
            System.out.println("==================Token : " + accessToken.getToken() + "==================");
            System.out.println("==================Application Id : " + accessToken.getApplicationId() + "==================");
            System.out.println("==================User Id : " + accessToken.getUserId() + "==================");
            System.out.println("==================Permissions : " + accessToken.getPermissions() + "==================");
        } else {
            /*accessTokenTracker*/
            Constants.ACCESS_TOKEN_TRACKER = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    if (currentAccessToken != null) {
                        System.out.println("==================Token : " + currentAccessToken.getToken() + "==================");
                        System.out.println("==================Application Id : " + currentAccessToken.getApplicationId() + "==================");
                        System.out.println("==================User Id : " + currentAccessToken.getUserId() + "==================");
                        System.out.println("==================Permissions : " + currentAccessToken.getPermissions() + "==================");
                    }
                }
            };

            /*accessTokenTracker*/
            Constants.ACCESS_TOKEN_TRACKER.startTracking();
        }

        Profile profile = Profile.getCurrentProfile();

        if (profile != null) {
            System.out.println("==================First Name : " + profile.getFirstName() + "==================");
            System.out.println("==================Last Name : " + profile.getLastName() + "==================");
            System.out.println("==================Name : " + profile.getName() + "==================");
            System.out.println("==================Id : " + profile.getId() + "==================");
            System.out.println("==================Link URI : " + profile.getLinkUri() + "==================");
            System.out.println("==================Profile Picture URI : " + profile.getProfilePictureUri(300, 300) + "==================");

            Constants.AUTH_TYPE = "facebook";

            Intent customerHomeIntent = new Intent(getApplicationContext(), CustomerHome.class);
            startActivity(customerHomeIntent);
        } else {
            /*profileTracker*/
            Constants.PROFILE_TRACKER = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    if (currentProfile != null) {
                        System.out.println("==================First Name : " + currentProfile.getFirstName() + "==================");
                        System.out.println("==================Last Name : " + currentProfile.getLastName() + "==================");
                        System.out.println("==================Name : " + currentProfile.getName() + "==================");
                        System.out.println("==================Id : " + currentProfile.getId() + "==================");
                        System.out.println("==================Link URI : " + currentProfile.getLinkUri() + "==================");
                        System.out.println("==================Profile Picture URI : " + currentProfile.getProfilePictureUri(300, 300) + "==================");

                        Constants.AUTH_TYPE = "facebook";

                        startActivity(new Intent(getApplicationContext(), CustomerHome.class));
                        finish();
                    }
                }
            };

            /*profileTracker*/
            Constants.PROFILE_TRACKER.startTracking();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*callbackManager*/
        Constants.CALLBACK_MANAGER.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            System.out.println("==================Sign In Google Success==================");
            GoogleSignInAccount account = result.getSignInAccount();

            String displayName, email, id, idToken, serverAuthCode;
            Uri photoUrl;
            if (account != null) {
                displayName = account.getDisplayName();
                email = account.getEmail();
                id = account.getId();
                idToken = account.getIdToken();
                serverAuthCode = account.getServerAuthCode();
                photoUrl = account.getPhotoUrl();

                System.out.println("==================Display Name : " + displayName + "==================");
                System.out.println("==================Email : " + email + "==================");
                System.out.println("==================Id: " + id + "==================");
                System.out.println("==================Id Token : " + idToken + "==================");
                System.out.println("==================Server Auth Code : " + serverAuthCode + "==================");
                System.out.println("==================Photo Url : " + photoUrl + "==================");

                Constants.AUTH_TYPE = "google";

                startActivity(new Intent(getApplicationContext(), CustomerHome.class));
                finish();
            }
        } else {
            System.out.println("==================Sign in Google Failed==================");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
