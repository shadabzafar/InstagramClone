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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtLoginUsername, edtLoginPassword;
    private Button btnLogin2;
    private ProgressBar my_progress_bar2;
    private ConstraintLayout constraintLayout3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin2 = findViewById(R.id.btnLogin2);
        my_progress_bar2 = findViewById(R.id.my_progress_bar2);
        constraintLayout3 = findViewById(R.id.constraintLayout3);

        btnLogin2.setOnClickListener(LoginActivity.this);
        constraintLayout3.setOnClickListener(LoginActivity.this);

        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnLogin2);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnLogin2:

                if(edtLoginUsername.getText().toString().equals("") || edtLoginPassword.getText().toString().equals("")) {

                    FancyToast.makeText(LoginActivity.this, "Fields can't be empty!", FancyToast.LENGTH_SHORT,
                            FancyToast.INFO, true).show();
                }
                else {
                    my_progress_bar2.setVisibility(View.VISIBLE);
                    ParseUser.logInInBackground(edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString()
                            , new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null) {
                                        my_progress_bar2.setVisibility(View.GONE);

                                        FancyToast.makeText(LoginActivity.this, user.get("username")
                                                        + " logged in successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true)
                                                .show();

                                        Intent intent = new Intent(LoginActivity.this, SocialMediaActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        my_progress_bar2.setVisibility(View.GONE);

                                        FancyToast.makeText(LoginActivity.this, e.getMessage(), FancyToast.LENGTH_LONG,
                                                FancyToast.ERROR, true).show();
                                    }
                                }
                            });
                }
                break;

            case R.id.constraintLayout3:
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