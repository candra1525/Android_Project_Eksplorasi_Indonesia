package com.candra.eksplorindonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.candra.eksplorindonesia.API.APIRequestData;
import com.candra.eksplorindonesia.API.RetrofitServer;
import com.candra.eksplorindonesia.Model.ModelAllResponse;
import com.candra.eksplorindonesia.Utility.ControllerLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailKulinerActivity extends AppCompatActivity
{

    private String yIdKuliner, yNamaKuliner, yAsalKuliner, yDeskripsiKuliner, yFotoKuliner;

    private TextView tvIdKuliner, tvNamaKuliner, tvAsalKuliner, tvDeskripsiKuliner;
    private Button btnEditKuliner, btnDeleteKuliner;
    private ImageView ivDetailFotoKuliner, ivBackToKuliner;

    private ControllerLogin cLogin = new ControllerLogin(DetailKulinerActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kuliner);

        Intent i = getIntent();
        yIdKuliner = i.getStringExtra("xIdKuliner");
        yNamaKuliner = i.getStringExtra("xNamaKuliner");
        yAsalKuliner = i.getStringExtra("xAsalKuliner");
        yDeskripsiKuliner = i.getStringExtra("xDeskripsiKuliner");
        yFotoKuliner = i.getStringExtra("xFotoKuliner");

        tvIdKuliner = findViewById(R.id.tv_id_kuliner_detail);
        tvNamaKuliner = findViewById(R.id.tv_nama_kuliner_detail);
        tvAsalKuliner = findViewById(R.id.tv_asal_kuliner_detail);
        tvDeskripsiKuliner = findViewById(R.id.tv_deskripsi_kuliner_detail);
        ivDetailFotoKuliner = findViewById(R.id.iv_kuliner);

        ivDetailFotoKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(DetailKulinerActivity.this, ZoomKuliner.class);
                z.putExtra("zFotoKuliner", yFotoKuliner);
                startActivity(z);
            }
        });

        tvIdKuliner.setText(yIdKuliner);
        tvNamaKuliner.setText(yNamaKuliner);
        tvAsalKuliner.setText(yAsalKuliner);
        tvDeskripsiKuliner.setText(yDeskripsiKuliner);

        byte [] imageBytes = Base64.decode(yFotoKuliner, Base64.DEFAULT);

        Glide.with(DetailKulinerActivity.this)
                .asBitmap()
                .load(imageBytes)
                .into(ivDetailFotoKuliner);



        btnEditKuliner = findViewById(R.id.btn_edit_kuliner);
        btnDeleteKuliner = findViewById(R.id.btn_delete_kuliner);
        if(cLogin.getPreferences(DetailKulinerActivity.this, cLogin.keySP_role).equals("user"))
        {
            btnEditKuliner.setVisibility(View.GONE);
            btnDeleteKuliner.setVisibility(View.GONE);
        }
        btnEditKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect ke Activity lain
                Intent intent = new Intent(DetailKulinerActivity.this, EditKulinerActivity.class);
                intent.putExtra("varIdKuliner", yIdKuliner);
                intent.putExtra("varNamaKuliner", yNamaKuliner);
                intent.putExtra("varAsalKuliner", yAsalKuliner);
                intent.putExtra("varFotoKuliner", yFotoKuliner);
                intent.putExtra("varDeskripsiKuliner", yDeskripsiKuliner);
                startActivity(intent);
                finish();
            }
        });



        btnDeleteKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailKulinerActivity.this);
                dialog.setTitle("Perhatian !");
                dialog.setMessage("Apakah anda yakin ingin menghapus data " + yNamaKuliner + " !");
                dialog.setCancelable(true);
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteKuliner(yIdKuliner);
                        onBackPressed();
                    }
                });
                dialog.show();
            }
        });

        ivBackToKuliner = findViewById(R.id.iv_back_to_kuliner);
        ivBackToKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void deleteKuliner(String id)
    {
        APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
        Call<ModelAllResponse> proses = ard.ardDeleteDataKuliner(id);
        proses.enqueue(new Callback<ModelAllResponse>() {
            @Override
            public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();
                Toast.makeText(DetailKulinerActivity.this, "Berhasil menghapus Data !", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                Toast.makeText(DetailKulinerActivity.this, "Gagal Menghapus Data ! ", Toast.LENGTH_SHORT).show();

            }
        });
    }
}