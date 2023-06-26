package com.candra.eksplorindonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.candra.eksplorindonesia.API.APIRequestData;
import com.candra.eksplorindonesia.API.RetrofitServer;
import com.candra.eksplorindonesia.Adapter.AdapterWisata;
import com.candra.eksplorindonesia.Model.ModelAllResponse;
import com.candra.eksplorindonesia.Utility.ControllerLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailWisataActivity extends AppCompatActivity {

    private String yIdWisata, yNamaWisata, yLokasiWisata, yMapsWisata, yDeskripsiWisata, yFotoWisata;

    private TextView tvIdWisata, tvNamaWisata, tvLokasiWisata, tvDeskripsiWisata, tvMapsWisata;

    private Button btnEditWisata, btnDeleteWisata, btnCheckMaps;
    private ImageView ivDetailFotoWisata;

    private ImageView ivBackToWisata;

    private ShareData sd;

    private ControllerLogin cLogin = new ControllerLogin(DetailWisataActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);

        Intent i = getIntent();
        yIdWisata = i.getStringExtra("xIdWisata");
        yNamaWisata = i.getStringExtra("xNamaWisata");
        yLokasiWisata = i.getStringExtra("xLokasiWisata");
        yMapsWisata = i.getStringExtra("xMapsWisata");
        yFotoWisata = i.getStringExtra("xFotoWisata");
        yDeskripsiWisata = i.getStringExtra("xDeskripsiWisata");

        tvIdWisata = findViewById(R.id.tv_id_wisata_detail);
        tvNamaWisata = findViewById(R.id.tv_nama_wisata_detail);
        tvLokasiWisata = findViewById(R.id.tv_lokasi_wisata_detail);
        tvMapsWisata = findViewById(R.id.tv_maps_wisata_detail);
        tvDeskripsiWisata = findViewById(R.id.tv_deskripsi_wisata_detail);
        ivDetailFotoWisata = findViewById(R.id.iv_wisata);

        ivDetailFotoWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(DetailWisataActivity.this, ZoomWisata.class);
                z.putExtra("zFotoWisata", yFotoWisata);
                startActivity(z);
            }
        });

        tvIdWisata.setText(yIdWisata);
        tvNamaWisata.setText(yNamaWisata);
        tvLokasiWisata.setText(yLokasiWisata);
        tvMapsWisata.setText(yMapsWisata);
        tvDeskripsiWisata.setText(yDeskripsiWisata);

        byte [] imageBytes = Base64.decode(yFotoWisata, Base64.DEFAULT);

        Glide.with(DetailWisataActivity.this)
                .asBitmap()
                .load(imageBytes)
                .into(ivDetailFotoWisata);

        btnEditWisata = findViewById(R.id.btn_edit_wisata);
        btnDeleteWisata = findViewById(R.id.btn_delete_wisata);

        if(cLogin.getPreferences(DetailWisataActivity.this, cLogin.keySP_role).equals("user"))
        {
            btnEditWisata.setVisibility(View.GONE);
            btnDeleteWisata.setVisibility(View.GONE);
        }
        btnEditWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect ke Activity lain
                Intent intent = new Intent(DetailWisataActivity.this, EditWisataActivity.class);
                intent.putExtra("varIdWisata", yIdWisata);
                intent.putExtra("varNamaWisata", yNamaWisata);
                intent.putExtra("varLokasiWisata", yLokasiWisata);
                intent.putExtra("varMapsWisata", yMapsWisata);
                intent.putExtra("varFotoWisata", yFotoWisata);
                intent.putExtra("varDeskripsiWisata", yDeskripsiWisata);
                startActivity(intent);
                finish();
            }
        });

        btnCheckMaps = findViewById(R.id.btn_check_maps);
        btnCheckMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri gmmIntentUri = Uri.parse(yMapsWisata);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(DetailWisataActivity.this, "Google Maps Tidak tersedia !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnDeleteWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailWisataActivity.this);
                dialog.setTitle("Perhatian !");
                dialog.setMessage("Apakah anda yakin ingin menghapus data " + yNamaWisata + " !");
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
                        deleteWisata(yIdWisata);
                        onBackPressed();
                    }
                });
                dialog.show();
            }
        });

        ivBackToWisata = findViewById(R.id.iv_back_to_wisata);
        ivBackToWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Back ke sebelumnya
                onBackPressed();
            }
        });
    }

    private void deleteWisata(String id)
    {
        APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
        Call<ModelAllResponse> proses = ard.ardDeleteDataWisata(id);
        proses.enqueue(new Callback<ModelAllResponse>() {
            @Override
            public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();
                Toast.makeText(DetailWisataActivity.this, "Berhasil menghapus Data !", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                Toast.makeText(DetailWisataActivity.this, "Gagal Menghapus Data ! ", Toast.LENGTH_SHORT).show();

            }
        });
    }
}