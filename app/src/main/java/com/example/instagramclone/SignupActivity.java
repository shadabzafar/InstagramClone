package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSignup, btnLogin;
    private ProgressBar my_progress_bar;
    ConstraintLayout constraintLayout2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        setTitle("SignUp");

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        my_progress_bar = findViewById(R.id.my_progress_bar);
        constraintLayout2 = findViewById(R.id.constraintLayout2);

        btnSignup.setOnClickListener(SignupActivity.this);
        btnLogin.setOnClickListener(SignupActivity.this);
        constraintLayout2.setOnClickListener(SignupActivity.this);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnSignup);
                }
                return false;
            }
        });

        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(SignupActivity.this, SocialMediaActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignup:

                if(edtEmail.getText().toString().equals("") || edtUsername.getText().toString().equals("") ||
                edtPassword.getText().toString().equals(""))
                {
                    FancyToast.makeText(SignupActivity.this, "Fields can't be empty!", FancyToast.LENGTH_SHORT,
                                    FancyToast.INFO, true).show();
                }
                else {
                    ParseUser parseUser = new ParseUser();
                    parseUser.setEmail(edtEmail.getText().toString());
                    parseUser.setUsername(edtUsername.getText().toString());
                    parseUser.setPassword(edtPassword.getText().toString());

                    my_progress_bar.setVisibility(View.VISIBLE);

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                my_progress_bar.setVisibility(View.GONE);
                                FancyToast.makeText(SignupActivity.this, parseUser.get("username")
                                                + " signed up successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true)
                                        .show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                my_progress_bar.setVisibility(View.GONE);
                                FancyToast.makeText(SignupActivity.this, e.getMessage(), FancyToast.LENGTH_LONG,
                                        FancyToast.ERROR, true).show();
                            }
                        }
                    });
                }

                break;

            case R.id.btnLogin:
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.constraintLayout2:
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

}