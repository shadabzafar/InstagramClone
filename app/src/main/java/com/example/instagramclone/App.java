package com.example.instagramclone;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("sAOuSU7rohNclxN94z2EHoxndDQLdx4hq67GA0yK")
                // if defined
                .clientKey("fC91oJ93Ztws1dlSOSt5bsMwC0pAtJ9UwaCmYnpL")
                .server("https://parseapi.back4app.com/")
                .build());
    }
}

