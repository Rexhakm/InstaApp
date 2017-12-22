package com.mergimrama.instaapp.UIActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mergimrama.instaapp.AppPreferences;
import com.mergimrama.instaapp.LoginAsyncTask;
import com.mergimrama.instaapp.MainActivity;
import com.mergimrama.instaapp.R;
import com.mergimrama.instaapp.callbacks.LoginCallback;
import com.mergimrama.instaapp.model.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Mergim on 15-Dec-17.
 */

public class LoginActivity extends AppCompatActivity implements LoginCallback {

    EditText editTextUsername;
    EditText editTextPassword;
    Button buttonLogin;
    TextView textViewSignUp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextUsername = (EditText) findViewById(R.id.login_username);
        editTextPassword = (EditText) findViewById(R.id.login_password);
        buttonLogin = (Button) findViewById(R.id.button_login);
        textViewSignUp = (TextView) findViewById(R.id.sign_up_login);

        AppPreferences.init(getApplicationContext());
        if (!AppPreferences.getUserId().equals("")) {

        } else {

        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = editTextUsername.getText().toString();
                String password = getMD5EncryptedString(editTextPassword.getText().toString());

                String url = "http://appsix.net/paintbook/index.php?User=" + username + "&Password=" +
                        password;
                new LoginAsyncTask(LoginActivity.this).execute(url);
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoginResponse(User user, boolean success) {
        if(success) {
            AppPreferences.saveUserId(user.getUserId());
            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userId", user.getUserId());
            startActivity(intent);
            System.out.println("user: " + user.toString());
        } else {
            Toast.makeText(LoginActivity.this, "Couldn't login", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getMD5EncryptedString(String encTarget){
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }
        return md5;
    }
}