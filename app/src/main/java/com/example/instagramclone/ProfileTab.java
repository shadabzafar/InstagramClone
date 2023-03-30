package com.example.instagramclone;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class ProfileTab extends Fragment {

    private EditText edtProfileName, edtProfileBio, edtProfileProfession,  edtProfileHobbies, edtProfileSports;
    private Button btnProfileUpdate;
    private ProgressBar profileProgressBar;

    public ProfileTab() {

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtProfileName = view.findViewById(R.id.edtProfileName);
        edtProfileBio = view.findViewById(R.id.edtProfileBio);
        edtProfileProfession = view.findViewById(R.id.edtProfileProfession);
        edtProfileHobbies = view.findViewById(R.id.edtProfileHobbies);
        edtProfileSports = view.findViewById(R.id.edtProfileSports);
        btnProfileUpdate = view.findViewById(R.id.btnProfileUpdate);
        profileProgressBar = view.findViewById(R.id.profileProgressBar);

        ParseUser parseUser = ParseUser.getCurrentUser();

        if (parseUser.get("profileName") == null) {
            edtProfileName.setText("");
        } else {
            edtProfileName.setText(parseUser.get("profileName").toString());
        }

        if (parseUser.get("profileBio") == null) {
            edtProfileBio.setText("");
        } else {
            edtProfileBio.setText(parseUser.get("profileBio").toString());
        }

        if (parseUser.get("profileProfession") == null) {
            edtProfileProfession.setText("");
        } else {
            edtProfileProfession.setText(parseUser.get("profileProfession").toString());
        }

        if (parseUser.get("profileHobbies") == null) {
            edtProfileHobbies.setText("");
        } else {
            edtProfileHobbies.setText(parseUser.get("profileHobbies").toString());
        }

        if (parseUser.get("profileSports") == null) {
            edtProfileSports.setText("");
        } else {
            edtProfileSports.setText(parseUser.get("profileSports").toString());
        }

        btnProfileUpdate.setOnClickListener(view1 -> {

            profileProgressBar.setVisibility(View.VISIBLE);

            parseUser.put("profileName", edtProfileName.getText().toString());
            parseUser.put("profileBio", edtProfileBio.getText().toString());
            parseUser.put("profileProfession", edtProfileProfession.getText().toString());
            parseUser.put("profileHobbies", edtProfileHobbies.getText().toString());
            parseUser.put("profileSports", edtProfileSports.getText().toString());

            parseUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        profileProgressBar.setVisibility(View.GONE);
                        FancyToast.makeText(getContext(),"Updated Successfully", FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS, true).show();
                    }
                    else {
                        profileProgressBar.setVisibility(View.GONE);
                        FancyToast.makeText(getContext(), e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                                        true).show();
                    }
                }
            });
        });

        return view;
    }
}