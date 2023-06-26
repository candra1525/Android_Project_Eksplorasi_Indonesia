package com.candra.eksplorindonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ZoomWisata extends AppCompatActivity {

    private ImageView ivZoomWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_wisata);

        Intent z = getIntent();
        String f = z.getStringExtra("zFotoWisata");

        ivZoomWisata = findViewById(R.id.iv_zoom_wisata);

        byte [] imageZoomWisata = Base64.decode(f, Base64.DEFAULT);

        Glide.with(ZoomWisata.this)
                .asBitmap()
                .load(imageZoomWisata)
                .into(ivZoomWisata);


    }
}