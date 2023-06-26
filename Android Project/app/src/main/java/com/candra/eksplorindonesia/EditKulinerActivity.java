package com.candra.eksplorindonesia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.candra.eksplorindonesia.Model.ModelAllResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditKulinerActivity extends AppCompatActivity
{
    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageView ivBackToDetailKuliner, ivEditFotoKuliner;
    private EditText etNamaKuliner, etAsalKuliner, etDeskripsiKuliner;
    private TextView tvIdEditKuliner;
    private String tempIdKuliner, tempNamaKuliner, tempAsalKuliner, tempDeskripsiKuliner, tempFotoKuliner;
    private Button btnEditKuliner;
    private Uri selectedImageUri;
    private Boolean gantiFotoKuliner = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kuliner);

        Intent intent = getIntent();
        tempIdKuliner = intent.getStringExtra("varIdKuliner");
        tempNamaKuliner = intent.getStringExtra("varNamaKuliner");
        tempAsalKuliner = intent.getStringExtra("varAsalKuliner");
        tempFotoKuliner = intent.getStringExtra("varFotoKuliner");
        tempDeskripsiKuliner = intent.getStringExtra("varDeskripsiKuliner");

        etNamaKuliner = findViewById(R.id.et_nama_kuliner);
        etAsalKuliner = findViewById(R.id.et_asal_kuliner);
        etDeskripsiKuliner = findViewById(R.id.et_deskripsi_kuliner);
        tvIdEditKuliner = findViewById(R.id.tv_id_editkuliner);

        tvIdEditKuliner.setText(tempIdKuliner);
        etNamaKuliner.setText(tempNamaKuliner);
        etAsalKuliner.setText(tempAsalKuliner);
        etDeskripsiKuliner.setText(tempDeskripsiKuliner);

        ivEditFotoKuliner = findViewById(R.id.iv_edit_foto_kuliner);

        byte [] imageKuliner = Base64.decode(tempFotoKuliner, Base64.DEFAULT);

        Glide.with(EditKulinerActivity.this)
                .asBitmap()
                .load(imageKuliner)
                .into(ivEditFotoKuliner);

        ivEditFotoKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, REQUEST_IMAGE_PICK);
                gantiFotoKuliner = true;
            }
        });

        ivBackToDetailKuliner = findViewById(R.id.iv_back_to_detail_kuliner);
        ivBackToDetailKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent u = new Intent(EditKulinerActivity.this, DetailKulinerActivity.class);
                u.putExtra("xIdKuliner", tempIdKuliner);
                u.putExtra("xNamaKuliner", tempNamaKuliner);
                u.putExtra("xAsalKuliner", tempAsalKuliner);
                u.putExtra("xDeskripsiKuliner", tempDeskripsiKuliner);
                u.putExtra("xFotoKuliner", tempFotoKuliner);
                startActivity(u);
                finish();
            }
        });

        btnEditKuliner = findViewById(R.id.btn_edit_kuliner);
        btnEditKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = etNamaKuliner.getText().toString();
                String asal = etAsalKuliner.getText().toString();
                String deskripsi = etDeskripsiKuliner.getText().toString();
                if(nama.trim().isEmpty())
                {
                    etNamaKuliner.setError("Nama kuliner tidak boleh kosong !");
                    etNamaKuliner.requestFocus();
                }
                else if(asal.trim().isEmpty())
                {
                    etAsalKuliner.setError("Asal kuliner tidak boleh kosong !");
                    etAsalKuliner.requestFocus();
                }
                else if(deskripsi.trim().isEmpty())
                {
                    etDeskripsiKuliner.setError("Deskripsi Kuliner tidak boleh kosong !");
                    etDeskripsiKuliner.requestFocus();
                }
                else
                {
                    editKuliner();
                }
            }
        });

    }

    private void editKuliner()
    {
        String idKuliner = tempIdKuliner;
        APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
        if(gantiFotoKuliner == true)
        {
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                byte [] imageBytes = baos.toByteArray();

                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                Call<ModelAllResponse> editKuliner = ard.ardUpdateDataKuliner(
                        RequestBody.create(MediaType.parse("text/plain"), idKuliner),
                        RequestBody.create(MediaType.parse("text/plain"), etNamaKuliner.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), etAsalKuliner.getText().toString()),
                        MultipartBody.Part.createFormData("foto_kuliner", "image.jpg", requestBody),
                        RequestBody.create(MediaType.parse("text/plain"), etDeskripsiKuliner.getText().toString())
                );

                editKuliner.enqueue(new Callback<ModelAllResponse>() {
                    @Override
                    public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                        String kode = response.body().getKode();
                        String pesan = response.body().getPesan();

                        Toast.makeText(EditKulinerActivity.this, "Data Kuliner Berhasil di Update !", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(EditKulinerActivity.this, DetailKulinerActivity.class);
                        i.putExtra("xIdKuliner", tempIdKuliner);
                        i.putExtra("xNamaKuliner", etNamaKuliner.getText().toString());
                        i.putExtra("xAsalKuliner", etAsalKuliner.getText().toString());
                        i.putExtra("xDeskripsiKuliner", etDeskripsiKuliner.getText().toString());
                        i.putExtra("xFotoKuliner", Base64.encodeToString(imageBytes, Base64.DEFAULT));
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                        Toast.makeText(EditKulinerActivity.this, "Proses Update data Kuliner Gagal !", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(EditKulinerActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Call<ModelAllResponse> updateDataKulinerTanpaFoto = ard.ardUpdateDataKulinerTanpaFoto(
                    RequestBody.create(MediaType.parse("text/plain"), idKuliner),
                    RequestBody.create(MediaType.parse("text/plain"), etNamaKuliner.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), etAsalKuliner.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), etDeskripsiKuliner.getText().toString())
            );

            updateDataKulinerTanpaFoto.enqueue(new Callback<ModelAllResponse>() {
                @Override
                public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                    String kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(EditKulinerActivity.this, "Data kuliner berhasil di Update !", Toast.LENGTH_SHORT).show();

                    Intent ii = new Intent(EditKulinerActivity.this, DetailKulinerActivity.class);
                    ii.putExtra("xIdKuliner", tempIdKuliner);
                    ii.putExtra("xNamaKuliner", etNamaKuliner.getText().toString());
                    ii.putExtra("xAsalKuliner", etAsalKuliner.getText().toString());
                    ii.putExtra("xFotoKuliner", tempFotoKuliner);
                    ii.putExtra("xDeskripsiKuliner", etDeskripsiKuliner.getText().toString());
                    startActivity(ii);
                    finish();
                }

                @Override
                public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                    Toast.makeText(EditKulinerActivity.this, "Proses Update data Kuliner Gagal 2 !" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                ivEditFotoKuliner.setImageURI(selectedImageUri);
            }
        }
    }
}