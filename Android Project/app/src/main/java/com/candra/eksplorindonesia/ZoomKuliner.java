package com.candra.eksplorindonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ZoomKuliner extends AppCompatActivity {

    private ImageView ivZoomKuliner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_kuliner);

        Intent z = getIntent();
        String fotoKuliner = z.getStringExtra("zFotoKuliner");

        ivZoomKuliner = findViewById(R.id.iv_zoom_kuliner);

        byte [] imageZoomKuliner = Base64.decode(fotoKuliner, Base64.DEFAULT);

        Glide.with(ZoomKuliner.this)
                .asBitmap()
                .load(imageZoomKuliner)
                .into(ivZoomKuliner);
    }
}