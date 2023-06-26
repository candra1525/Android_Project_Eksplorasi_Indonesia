package com.candra.eksplorindonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.candra.eksplorindonesia.Utility.ControllerLogin;

public class SplashScreen extends AppCompatActivity {

    private ImageView backgroundSplash;

    private ControllerLogin cLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        backgroundSplash = findViewById(R.id.iv_background);

        Glide.with(SplashScreen.this)
                .load(R.drawable.splash)
                .into(backgroundSplash);

        cLogin = new ControllerLogin(SplashScreen.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String role = cLogin.getPreferences(SplashScreen.this, cLogin.keySP_role);
                if(role != null && !role.isEmpty()) {
                    if (role.equals("admin")) {
                        startActivity(new Intent(SplashScreen.this, MainActivityAdmin.class));
                    } else {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                }
            }
        }, 5000);


    }
}