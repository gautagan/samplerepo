package com.example.amplifyandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobile.client.results.SignInState;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoServiceConstants;

import java.util.HashMap;
import java.util.Map;

import static com.amazonaws.mobile.client.UserState.SIGNED_IN;
import static com.amazonaws.mobile.client.UserState.SIGNED_OUT;

public class AuthenticationActivity extends AppCompatActivity {

    private final String TAG = AuthenticationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        signIn();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );




        /*
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i(TAG, userStateDetails.getUserState().toString());
                switch (userStateDetails.getUserState()){
                    case SIGNED_IN:
                        Intent i = new Intent(AuthenticationActivity.this, MainActivity.class);
                        startActivity(i);
                        break;
                    case SIGNED_OUT:
                        showSignIn();
                        break;
                    default:
                        AWSMobileClient.getInstance().signOut();
                        showSignIn();
                        break;
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.toString());
            }
        });
        */


    }

    public void signIn() {
        Map< String,String> hm = new HashMap< String,String>();
        hm.put("key","value");
        Log.v("sample","Hello!");
        AWSMobileClient.getInstance().signIn("asdasdasd", "asdasdasdsd!", hm, new Callback<SignInResult>() {
            @Override
            public void onResult(final SignInResult signInResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("sample","Hello1!");
                        Log.d("APP", "Sign-in callback state: " + signInResult.getSignInState());
                        switch (signInResult.getSignInState()) {
                            case DONE:
                                Log.d(TAG, "Sign-in done.");
                                break;
                            case SMS_MFA:
                                Log.d(TAG, "Please confirm sign-in with SMS.");
                                break;
                            case NEW_PASSWORD_REQUIRED:
                                Log.d(TAG, "Please confirm sign-in with new password.");
                                break;
                            case CUSTOM_CHALLENGE:
                                confirmSignIn();
                                break;
                            default:
                                Log.d(TAG, "Unsupported sign-in confirmation: " + signInResult.getSignInState());
                                break;
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Sign-in error", e);
            }
        });
    }

    public void confirmSignIn() {
        Map<String, String> res = new HashMap<String, String>();
        res.put(CognitoServiceConstants.CHLG_RESP_ANSWER, "<CHALLENGE_RESPONSE>");
        AWSMobileClient.getInstance().confirmSignIn(res, new Callback<SignInResult>() {
            @Override
            public void onResult(final SignInResult signInResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Sign-in callback state: " + signInResult.getSignInState());
                        switch (signInResult.getSignInState()) {
                            case DONE:
                                Log.d(TAG, "Sign-in done.");
                                break;
                            case SMS_MFA:
                                Log.d(TAG, "Please confirm sign-in with SMS.");
                                break;
                            case NEW_PASSWORD_REQUIRED:
                                Log.d(TAG, "Please confirm sign-in with new password.");
                                break;
                            default:
                                Log.d(TAG, "Unsupported sign-in confirmation: " + signInResult.getSignInState());
                                break;
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Confirm Custom auth Sign-in error", e);
            }
        });
    }

    private void showSignIn() {
        try {
            AWSMobileClient.getInstance().showSignIn(this,
                    SignInUIOptions.builder().nextActivity(MainActivity.class).build());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
