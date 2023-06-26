package com.candra.eksplorindonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ZoomUser extends AppCompatActivity {

    private ImageView ivZoomUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_user);

        Intent z = getIntent();
        String f = z.getStringExtra("zFotoUser");

        ivZoomUser = findViewById(R.id.iv_zoom_user);

        byte [] imageZoomWisata = Base64.decode(f, Base64.DEFAULT);

        Glide.with(ZoomUser.this)
                .asBitmap()
                .load(imageZoomWisata)
                .into(ivZoomUser);
    }
}