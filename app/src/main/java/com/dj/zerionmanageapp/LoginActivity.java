package com.dj.zerionmanageapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();

                if (TextUtils.isEmpty(usernameString) && TextUtils.isEmpty(passwordString)) {
                    username.setError(getString(R.string.empty_field));
                    password.setError(getString(R.string.empty_field));
                } else if (TextUtils.isEmpty(usernameString)) {
                    username.setError(getString(R.string.empty_field));
                } else if (TextUtils.isEmpty(passwordString)) {
                    password.setError(getString(R.string.empty_field));
                } else if (usernameString.equals("hello") && passwordString.equals("world")){
                    new generateToken().execute();
                } else {
                    username.setError(getString(R.string.invalid_creds));
                    password.setError(getString(R.string.invalid_creds));
                }
            }
        });
    }

    private class generateToken extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        String access_token = null;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this, null, "Checking Credentials...");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String clientKey = "2ac3be342280be456e76a25f4f9134c50ab500e6";
            String clientSecret = "eebd044a24be68f57b3ec91db674d6a51481a7fb";
            String URL = "https://app.iformbuilder.com/exzact/api/oauth/token";
            access_token = AccessToken.getToken(clientKey, clientSecret, URL);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            progressDialog.dismiss();
            Intent intent = new Intent(LoginActivity.this, ListActivity.class);
            intent.putExtra("access_token", access_token);
            startActivity(intent);
            finish();
        }
    }
}
