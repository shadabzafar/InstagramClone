package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPost extends AppCompatActivity {

    private ProgressBar userPostProgressBar;
    private LinearLayout linearLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_post);

        userPostProgressBar = findViewById(R.id.userPostProgressBar);
        linearLayout = findViewById(R.id.linearLayout);

        Intent receivedIntentObject = getIntent();
        String receivedUsername = receivedIntentObject.getStringExtra("username");
        FancyToast.makeText(UsersPost.this, receivedUsername, FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true)
                .show();

        setTitle(receivedUsername + "'s Posts");

        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("Photo");
        parseQuery.whereEqualTo("username", receivedUsername);
        parseQuery.orderByDescending("createdAt");

        userPostProgressBar.setVisibility(View.VISIBLE);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null && objects.size() > 0){
                    for (ParseObject post: objects){
                        TextView imageDes = new TextView(UsersPost.this);
                        imageDes.setText(post.get("image_des") + "");
                        ParseFile postPicture = (ParseFile) post.get("picture");

                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data != null && e == null){
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView postImageView = new ImageView(UsersPost.this);
                                    LinearLayout.LayoutParams imageView_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageView_params.setMargins(5, 5, 5 ,5);
                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams des_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    des_params.setMargins(5, 5, 5 , 5);
                                    imageDes.setLayoutParams(des_params);
                                    imageDes.setGravity(Gravity.CENTER);
                                    imageDes.setTextSize(20f);

                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(imageDes);

                                }
                                else {
                                    FancyToast.makeText(UsersPost.this, receivedUsername + " doesn't have any post",
                                            FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
                                    finish();
                                }
                            }
                        });
                    }
                }
                userPostProgressBar.setVisibility(View.GONE);
            }
        });

    }
}