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

public class AddWisataActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageView ivBackToWisata, ivAddFotoWisata;

    private EditText etAddNamaWisata, etAddLokasiWisata, etAddMapsWisata, etDeskripsiWisata;

    private Button btnTambahWisata;

    String namaWisata, lokasiWisata, mapsWisata, deskripsiWisata;

    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wisata);

        ivBackToWisata = findViewById(R.id.iv_back_to_wisata2);
        ivBackToWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivAddFotoWisata = findViewById(R.id.iv_add_foto_wisata);
        ivAddFotoWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        etAddNamaWisata = findViewById(R.id.et_add_nama_wisata);
        etAddLokasiWisata = findViewById(R.id.et_add_lokasi_wisata);
        etAddMapsWisata = findViewById(R.id.et_add_maps_wisata);
        etDeskripsiWisata = findViewById(R.id.et_add_deskripsi_wisata);

        btnTambahWisata = findViewById(R.id.btn_add_wisata);
        btnTambahWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namaWisata = etAddNamaWisata.getText().toString();
                lokasiWisata = etAddLokasiWisata.getText().toString();
                mapsWisata = etAddMapsWisata.getText().toString();
                deskripsiWisata = etDeskripsiWisata.getText().toString();

                if(namaWisata.trim().equals(""))
                {
                    etAddNamaWisata.setError("Nama Wisata tidak boleh kosong !");
                    etAddNamaWisata.requestFocus();
                }
                else if(mapsWisata.trim().equals(""))
                {
                    etAddMapsWisata.setError("Maps Wisata tidak boleh kosong !");
                    etAddMapsWisata.requestFocus();
                }
                else if(lokasiWisata.trim().equals(""))
                {
                    etAddLokasiWisata.setError("Lokasi Wisata tidak boleh kosong !");
                    etAddLokasiWisata.requestFocus();
                }
                else if(deskripsiWisata.trim().equals(""))
                {
                    etDeskripsiWisata.setError("Deskripsi Wisata tidak boleh kosong !");
                    etDeskripsiWisata.requestFocus();
                }
                else
                {
                    AddWisata();
                }
            }
        });
    }

    private void AddWisata()
    {
        if(selectedImageUri != null)
        {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                byte[] imageBytes = baos.toByteArray();
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
                Call<ModelAllResponse> prosesAdd = ard.ardCreateDataWisata(
                        RequestBody.create(MediaType.parse("text/plain"), namaWisata),
                        RequestBody.create(MediaType.parse("text/plain"), lokasiWisata),
                        RequestBody.create(MediaType.parse("text/plain"), mapsWisata),
                        MultipartBody.Part.createFormData("foto_wisata", "image.jpg", requestBody),
                        RequestBody.create(MediaType.parse("text/plain"), deskripsiWisata)
                );

                prosesAdd.enqueue(new Callback<ModelAllResponse>() {
                    @Override
                    public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                        if(response.isSuccessful())
                        {
                            String kode = response.body().getKode();
                            String pesan  = response.body().getPesan();

                            Toast.makeText(AddWisataActivity.this, "Data Wisata Berhasil disimpan !", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(AddWisataActivity.this, "Proses tambah data wisata gagal!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                        Toast.makeText(AddWisataActivity.this, "Proses tambah data wisata gagal! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(AddWisataActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(AddWisataActivity.this, "Pilih gambar terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                ivAddFotoWisata.setImageURI(selectedImageUri);
            }
        }
    }
}