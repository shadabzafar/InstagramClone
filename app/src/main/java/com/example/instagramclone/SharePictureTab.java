package com.example.instagramclone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SharePictureTab extends Fragment implements View.OnClickListener{

    private ImageView shareImageView;
    private EditText edtShareCaption;
    private Button btnShareImage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Bitmap selectedImageBitmap;
    private ProgressBar shareProgressBar;

    public SharePictureTab() {

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_share_picture_tab, container, false);
        shareImageView = view.findViewById(R.id.shareImageView);
        edtShareCaption = view.findViewById(R.id.edtShareCaption);
        btnShareImage = view.findViewById(R.id.btnShareImage);
        shareProgressBar = view.findViewById(R.id.shareProgressBar);

        shareImageView.setOnClickListener(SharePictureTab.this);
        btnShareImage.setOnClickListener(SharePictureTab.this);

                activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if(data != null && data.getData() != null) {
                                Uri selectedImage = data.getData();
                                try {
                                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                                            selectedImage);
                                } catch (IOException e){
                                   e.printStackTrace();
                                }
                                shareImageView.setImageBitmap(selectedImageBitmap);
                            }
                        }
                    }
                });

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shareImageView:
                if(Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }
                else {
                    getChoosenImage();
                }
                break;

            case R.id.btnShareImage:

                if(selectedImageBitmap != null){
                    if(edtShareCaption.getText().toString().equals("")){
                        FancyToast.makeText(getContext(), "You must enter caption for image", FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                true).show();
                    }
                    else {
                        shareProgressBar.setVisibility(View.VISIBLE);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("img.png", bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", edtShareCaption.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    shareProgressBar.setVisibility(View.GONE);
                                    FancyToast.makeText(getContext(), "UPLOADED", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                true).show();
                                }
                                else {
                                    shareProgressBar.setVisibility(View.GONE);
                                    FancyToast.makeText(getContext(), "Something went wrong!", FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                true).show();
                                }
                            }
                        });
                    }
                }
                else {
                    FancyToast.makeText(getContext(), "You must select an image!", FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                true).show();
                }
                break;
        }
    }

    private void getChoosenImage() {
//        FancyToast.makeText(getContext(), "PERMISSION GRANTED", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
//                true).show();
        Intent intent =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          activityResultLauncher.launch(intent);
    }

}