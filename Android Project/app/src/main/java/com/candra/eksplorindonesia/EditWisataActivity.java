package com.candra.eksplorindonesia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class EditWisataActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView ivBackToDetailWisata, ivEditFotoWisata;
    private EditText etNamaWisata, etLokasiWisata, etMapsWisata, etDeskripsiWisata;

    private TextView tvIdEditWisata;
    private String tempIdWisata, tempNamaWisata, tempLokasiWisata, tempMapsWisata, tempDeskripsiWisata, tempFotoWisata;
    private Button btnEditWisata;
    private Uri selectedImageUri;
    private boolean gantiFotoWisata = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wisata);

        Intent intent = getIntent();
        tempIdWisata = intent.getStringExtra("varIdWisata");
        tempNamaWisata = intent.getStringExtra("varNamaWisata");
        tempLokasiWisata = intent.getStringExtra("varLokasiWisata");
        tempMapsWisata = intent.getStringExtra("varMapsWisata");
        tempFotoWisata = intent.getStringExtra("varFotoWisata");
        tempDeskripsiWisata = intent.getStringExtra("varDeskripsiWisata");

        etNamaWisata = findViewById(R.id.et_nama_wisata);
        etLokasiWisata = findViewById(R.id.et_lokasi_wisata);
        etMapsWisata = findViewById(R.id.et_maps_wisata);
        etDeskripsiWisata = findViewById(R.id.et_deskripsi_wisata);
        tvIdEditWisata = findViewById(R.id.tv_id_editwisata);

        tvIdEditWisata.setText(tempIdWisata);
        etNamaWisata.setText(tempNamaWisata);
        etLokasiWisata.setText(tempLokasiWisata);
        etMapsWisata.setText(tempMapsWisata);
        etDeskripsiWisata.setText(tempDeskripsiWisata);

        ivEditFotoWisata = findViewById(R.id.iv_edit_foto_wisata);
        byte [] imageWisata = Base64.decode(tempFotoWisata, Base64.DEFAULT);

        Glide.with(EditWisataActivity.this)
                .asBitmap()
                .load(imageWisata)
                .into(ivEditFotoWisata);

        ivEditFotoWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
                gantiFotoWisata = true;
            }
        });

        ivBackToDetailWisata = findViewById(R.id.iv_back_to_detail_wisata);
        ivBackToDetailWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(EditWisataActivity.this, DetailWisataActivity.class);
                x.putExtra("xIdWisata", tempIdWisata);
                x.putExtra("xNamaWisata", tempNamaWisata);
                x.putExtra("xLokasiWisata", tempLokasiWisata);
                x.putExtra("xMapsWisata", tempMapsWisata);
                x.putExtra("xFotoWisata", tempFotoWisata);
                x.putExtra("xDeskripsiWisata", tempDeskripsiWisata);
                startActivity(x);
                finish();
            }
        });

        btnEditWisata = findViewById(R.id.btn_edit_wisata);
        btnEditWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama, lokasi, maps, deskripsi;
                nama = etNamaWisata.getText().toString();
                lokasi = etLokasiWisata.getText().toString();
                maps = etMapsWisata.getText().toString();
                deskripsi = etDeskripsiWisata.getText().toString();
                if(nama.trim().equals(""))
                {
                    etNamaWisata.setError("Nama Wisata tidak boleh kosong !");
                    etNamaWisata.requestFocus();
                }
                else if(lokasi.trim().equals(""))
                {
                    etLokasiWisata.setError("Lokasi Wisata Tidak boleh Kosong");
                    etLokasiWisata.requestFocus();
                }
                else if(maps.trim().equals(""))
                {
                    etMapsWisata.setError("Maps Wisata Tidak boleh Kosong !");
                    etMapsWisata.requestFocus();
                }
                else if(deskripsi.trim().equals(""))
                {
                    etDeskripsiWisata.setError("Deskripsi Wisata Tidak boleh Kosong !");
                    etDeskripsiWisata.requestFocus();
                }
                else {
                    editData();
                }
            }
        });
    }

    private void editData()
    {
        String idWisata = tempIdWisata;
        APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
        if(gantiFotoWisata == true)
        {
            try
            {
                Call<ModelAllResponse> edit;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                byte [] imageBytes = baos.toByteArray();

                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

                edit = ard.ardUpdateDataWisata(
                        RequestBody.create(MediaType.parse("text/plain"), idWisata),
                        RequestBody.create(MediaType.parse("text/plain"), etNamaWisata.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), etLokasiWisata.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), etMapsWisata.getText().toString()),
                        MultipartBody.Part.createFormData("foto_wisata", "image.jpg", requestBody),
                        RequestBody.create(MediaType.parse("text/plain"), etDeskripsiWisata.getText().toString())
                );

                edit.enqueue(new Callback<ModelAllResponse>() {
                    @Override
                    public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                        String code = response.body().getKode();
                        String message = response.body().getPesan();

                        Toast.makeText(EditWisataActivity.this, "Data Wisata Berhasil di Update", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(EditWisataActivity.this, DetailWisataActivity.class);
                        i.putExtra("xIdWisata", tempIdWisata);
                        i.putExtra("xNamaWisata", etNamaWisata.getText().toString());
                        i.putExtra("xLokasiWisata", etLokasiWisata.getText().toString());
                        i.putExtra("xMapsWisata", etMapsWisata.getText().toString());
                        i.putExtra("xFotoWisata", Base64.encodeToString(imageBytes, Base64.DEFAULT));
                        i.putExtra("xDeskripsiWisata", etDeskripsiWisata.getText().toString());
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                        Toast.makeText(EditWisataActivity.this, "Proses Update data Wisata Gagal !", Toast.LENGTH_SHORT).show();

                    }
                });
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(EditWisataActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Call <ModelAllResponse> updateDataWisataTanpaFoto = ard.ardUpdateDataWisataTanpaFoto(
                    RequestBody.create(MediaType.parse("text/plain"), idWisata),
                    RequestBody.create(MediaType.parse("text/plain"), etNamaWisata.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), etLokasiWisata.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), etMapsWisata.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), etDeskripsiWisata.getText().toString())
            );

            updateDataWisataTanpaFoto.enqueue(new Callback<ModelAllResponse>() {
                @Override
                public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                    String code = response.body().getKode();
                    String message = response.body().getPesan();

                    Toast.makeText(EditWisataActivity.this, "Data Wisata Berhasil di Update", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(EditWisataActivity.this, DetailWisataActivity.class);
                    i.putExtra("xIdWisata", tempIdWisata);
                    i.putExtra("xNamaWisata", etNamaWisata.getText().toString());
                    i.putExtra("xLokasiWisata", etLokasiWisata.getText().toString());
                    i.putExtra("xMapsWisata", etMapsWisata.getText().toString());
                    i.putExtra("xFotoWisata", tempFotoWisata);
                    i.putExtra("xDeskripsiWisata", etDeskripsiWisata.getText().toString());
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                    Toast.makeText(EditWisataActivity.this, "Proses Update data Wisata Gagal !", Toast.LENGTH_SHORT).show();

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
                ivEditFotoWisata.setImageURI(selectedImageUri);
            }
        }
    }
}