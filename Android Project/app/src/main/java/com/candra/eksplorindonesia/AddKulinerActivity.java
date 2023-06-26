package com.candra.eksplorindonesia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class AddKulinerActivity extends AppCompatActivity
{
    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageView ivBackToKuliner, ivAddFotoKuliner;

    private EditText etAddNamaKuliner, etAddAsalKuliner, etAddDeskripsiKuliner;
    private Button btnTambahKuliner;

    String namaKuliner, asalKuliner, deskripsiKuliner;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kuliner);

        ivBackToKuliner = findViewById(R.id.iv_back_to_kuliner2);
        ivBackToKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivAddFotoKuliner = findViewById(R.id.iv_add_foto_kuliner);
        ivAddFotoKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        etAddNamaKuliner = findViewById(R.id.et_add_nama_kuliner);
        etAddAsalKuliner = findViewById(R.id.et_add_asal_kuliner);
        etAddDeskripsiKuliner = findViewById(R.id.et_add_deskripsi_kuliner);

        btnTambahKuliner = findViewById(R.id.btn_add_kuliner);
        btnTambahKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namaKuliner = etAddNamaKuliner.getText().toString();
                asalKuliner = etAddAsalKuliner.getText().toString();
                deskripsiKuliner = etAddDeskripsiKuliner.getText().toString();

                if(namaKuliner.trim().isEmpty())
                {
                    etAddNamaKuliner.setError("Nama Kuliner tidak boleh kosong !");
                    etAddNamaKuliner.requestFocus();
                }
                else if(asalKuliner.trim().isEmpty())
                {
                    etAddAsalKuliner.setError("Asal kuliner tidak boleh kosong !");
                    etAddAsalKuliner.requestFocus();
                }
                else if(deskripsiKuliner.trim().isEmpty())
                {
                    etAddDeskripsiKuliner.setError("Deskripsi kuliner tidak boleh kosong !");
                    etAddDeskripsiKuliner.requestFocus();
                }
                else
                {
                    AddKuliner();
                }
            }
        });
    }

    private void AddKuliner()
    {
        if(selectedImageUri != null)
        {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                byte [] imageBytes = baos.toByteArray();
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);

                Call<ModelAllResponse> prosesAdd = ard.ardCreateDataKuliner(
                        RequestBody.create(MediaType.parse("text/plain"), namaKuliner),
                        RequestBody.create(MediaType.parse("text/plain"), asalKuliner),
                        MultipartBody.Part.createFormData("foto_kuliner", "image.jpg", requestBody),
                        RequestBody.create(MediaType.parse("text/plain"), deskripsiKuliner)
                );

                prosesAdd.enqueue(new Callback<ModelAllResponse>() {
                    @Override
                    public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                        if(response.isSuccessful())
                        {
                            String kode = response.body().getKode();
                            String pesan = response.body().getPesan();
                            Toast.makeText(AddKulinerActivity.this, "Data Kuliner Berhasil di simpan !", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(AddKulinerActivity.this, "Proses tambah data kuliner gagal !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                        Toast.makeText(AddKulinerActivity.this, "Proses tambah data kuliner gagal! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(AddKulinerActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(AddKulinerActivity.this, "Pilih gambar terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                ivAddFotoKuliner.setImageURI(selectedImageUri);
            }
        }
    }
}