package com.candra.eksplorindonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AboutActivity extends AppCompatActivity
{

    private ImageView ivBackToHome;

    private ImageView ivPakFarisi, ivCandra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ivBackToHome = findViewById(R.id.iv_back_to_home);
        ivBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivPakFarisi = findViewById(R.id.ivPakFarisi);
        ivCandra = findViewById(R.id.ivCandra);
        Glide.with(this)
                .load(R.drawable.pak_farisi)
                .into(ivPakFarisi);

        Glide.with(this)
                .load(R.drawable.candra)
                .into(ivCandra);
    }
}