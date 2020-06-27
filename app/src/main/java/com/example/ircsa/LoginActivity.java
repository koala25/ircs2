package com.example.ircsa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    public EditText email;
    public EditText password;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private static final String TAG = "FaceLog";
    private TextView txtName;
    private ImageView txtImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Declare Variables here :
        email = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        txtName = findViewById(R.id.setup_name);
        txtImage = findViewById(R.id.setup_image);

        mCallbackManager = CallbackManager.Factory.create();
        /*LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
            */

        };




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Pass the activity result back to the Facebook SDK
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void Onclick_submit(View view) {
        String email_string = email.getText().toString();
        String password_string = password.getText().toString();
        Log.i("Mail", email.getText().toString());

        if (!TextUtils.isEmpty(email_string) && !TextUtils.isEmpty(password_string)) {
            mAuth.signInWithEmailAndPassword(email_string, password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Intent mainintent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainintent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error Signing in!!", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

    }

    public void onclick_Create_newAccount(View v) {
        Intent create_new_account_Intent = new Intent(LoginActivity.this, create_new_account.class);
        startActivity(create_new_account_Intent);
        finish();
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser current_user = mAuth.getCurrentUser();

        if (current_user != null) {
            Intent mainintent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainintent);
            finish();


        } else {
            Log.i("Output", "Not Logged in");
        }
    }

    private void updateUI() {

        Toast.makeText(LoginActivity.this, "You're are LoggedIn", Toast.LENGTH_LONG);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent mainintent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainintent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

   /* AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if(currentAccessToken==null){
                txtName.setText("");
                txtImage.setImageResource(0);

                Toast.makeText(LoginActivity.this,"You are logged Out",Toast.LENGTH_LONG).show();
            }else{
                loaduserProfile(currentAccessToken);
            }

        }
    };*/

    /*private void loaduserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com"+id+"/picture?type=normal";

                    Log.i("asdfg","zxcvb");
                    txtName.setText(first_name+" "+last_name);
                    Glide.with(LoginActivity.this).load(image_url).into(txtImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name, last_name, email, id");
        request.setParameters(parameters);
        request.executeAsync();
    }*/
    public void onClickGoogle_Signup(View view) {
        Log.i("Signup", "Google Signup");
        Toast.makeText(LoginActivity.this, "Google Signup will not work as of now. Wait for Update", Toast.LENGTH_LONG).show();
    }
}



