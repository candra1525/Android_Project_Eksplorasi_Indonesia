package com.candra.eksplorindonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class DetailTentangIndonesiaActivity extends AppCompatActivity {

    private ImageView ivBackToHome;
    private ImageView ivpeta, ivBenderaIndonesia, ivBahasaIndonesia, ivLambangNegaraIndonesia, ivSemboyanNegaraIndonesia, ivLaguKebangsaanIndonesia, ivDasarFalsafahNegara, ivKonstitusiNegaraIndonesia, ivBentukNegaraIndonesia, ivSistemNegaraIndonesia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tentang_indonesia);

        ivBackToHome = findViewById(R.id.iv_back_to_home);
        ivBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivpeta = findViewById(R.id.iv_peta);
        Glide.with(DetailTentangIndonesiaActivity.this)
                .load(R.drawable.peta_indonesia)
                .into(ivpeta);
        ivBenderaIndonesia = findViewById(R.id.iv_bendera_indonesia);
        Glide.with(DetailTentangIndonesiaActivity.this)
                .load(R.drawable.bendera_indonesia)
                .into(ivBenderaIndonesia);
        ivBahasaIndonesia = findViewById(R.id.iv_bahasa_indonesia);
        Glide.with(DetailTentangIndonesiaActivity.this)
                .load(R.drawable.bahasa_indonesia)
                .into(ivBahasaIndonesia);
        ivLambangNegaraIndonesia = findViewById(R.id.iv_lambang_negara_indonesia);
        Glide.with(DetailTentangIndonesiaActivity.this)
                .load(R.drawable.lambang_negara_indonesia)
                .into(ivLambangNegaraIndonesia);
        ivSemboyanNegaraIndonesia = findViewById(R.id.iv_semboyan_negara_indonesia);
        Glide.with(DetailTentangIndonesiaActivity.this)
                .load(R.drawable.semboyan_negara_indonesia)
                .into(ivSemboyanNegaraIndonesia);
        ivLaguKebangsaanIndonesia = findViewById(R.id.iv_lagu_kebangsaan_indonesia);
        Glide.with(DetailTentangIndonesiaActivity.this)
                .load(R.drawable.lagu_kebangsaan_indonesia)
                .into(ivLaguKebangsaanIndonesia);
        ivDasarFalsafahNegara = findViewById(R.id.iv_dasar_falsafah_negara);
        Glide.with(DetailTentangIndonesiaActivity.this)
                .load(R.drawable.dasar_falsafah_negara)
                .into(ivDasarFalsafahNegara);
        ivKonstitusiNegaraIndonesia = findViewById(R.id.iv_konstitusi_negara_indonesia);
        Glide.with(DetailTentangIndonesiaActivity.this)
                .load(R.drawable.konstitusi_negara_indonesia)
                .into(ivKonstitusiNegaraIndonesia);
        ivBentukNegaraIndonesia = findViewById(R.id.iv_bentuk_negara_indonesia);
        Glide.with(DetailTentangIndonesiaActivity.this)
                .load(R.drawable.bentuk_negara_indonesia)
                .into(ivBentukNegaraIndonesia);
        ivSistemNegaraIndonesia = findViewById(R.id.iv_sistem_negara_indonesia);
        Glide.with(DetailTentangIndonesiaActivity.this)
                .load(R.drawable.sistem_negara_indonesia)
                .into(ivSistemNegaraIndonesia);


    }
}