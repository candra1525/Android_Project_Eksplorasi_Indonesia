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
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1;
    private EditText etNamaLengkap, etEmail, etNomorTelepon, etPassword, etKonfirmasiPassword;

    private ImageView ivFoto;

    private Button btnLogin;

    private TextView tvLogin;

    String namaLengkap, email, notelp, password, konfirmasiPassword;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNamaLengkap = findViewById(R.id.et_nama_lengkap_register);
        etEmail = findViewById(R.id.et_email_register);
        etNomorTelepon = findViewById(R.id.et_nomor_telepon_register);
        etPassword = findViewById(R.id.et_password_register);
        etKonfirmasiPassword = findViewById(R.id.et_konfirmasi_password_register);
        ivFoto = findViewById(R.id.iv_foto_register);
        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        tvLogin = findViewById(R.id.tv_redirect_login);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namaLengkap = etNamaLengkap.getText().toString();
                email = etEmail.getText().toString();
                notelp = etNomorTelepon.getText().toString();
                password = etPassword.getText().toString();
                konfirmasiPassword = etKonfirmasiPassword.getText().toString();

                if (namaLengkap.trim().equals("")) {
                    etNamaLengkap.setError("Nama lengkap tidak boleh kosong!");
                    etNamaLengkap.requestFocus();
                } else if (email.trim().equals("")) {
                    etEmail.setError("Email tidak boleh kosong!");
                    etEmail.requestFocus();
                } else if (notelp.trim().equals("")) {
                    etNomorTelepon.setError("Nomor telepon tidak boleh kosong!");
                    etNomorTelepon.requestFocus();
                } else if (password.trim().equals("")) {
                    etPassword.setError("Password tidak boleh kosong!");
                    etPassword.requestFocus();
                } else if (konfirmasiPassword.trim().equals("")) {
                    etKonfirmasiPassword.setError("Konfirmasi password tidak boleh kosong!");
                    etKonfirmasiPassword.requestFocus();
                } else {
                    if(password.length() <= 5)
                    {
                        Toast.makeText(RegisterActivity.this, "Password tidak boleh kurang dari 5 digit !", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Register();
                    }
                }
            }
        });
    }

    private void Register() {
        if (password.equals(konfirmasiPassword)) {
            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

                    APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
                    Call<ModelAllResponse> prosesRegister = ard.ardCreateDataUser(
                            RequestBody.create(MediaType.parse("text/plain"), namaLengkap),
                            RequestBody.create(MediaType.parse("text/plain"), email),
                            RequestBody.create(MediaType.parse("text/plain"), "user"),
                            RequestBody.create(MediaType.parse("text/plain"), notelp),
                            MultipartBody.Part.createFormData("foto", "image.jpg", requestBody),
                            RequestBody.create(MediaType.parse("text/plain"), password)
                    );
                    prosesRegister.enqueue(new Callback<ModelAllResponse>() {
                        @Override
                        public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                            if (response.isSuccessful()) {
                                String kode = response.body().getKode();
                                String pesan = response.body().getPesan();
                                Toast.makeText(RegisterActivity.this, "Proses Register berhasil!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Proses registrasi gagal!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, "Proses registrasi gagal!" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Pilih gambar terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        } else {
            etKonfirmasiPassword.setError("Konfirmasi password tidak sama dengan password!");
            etKonfirmasiPassword.requestFocus();
            etKonfirmasiPassword.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                ivFoto.setImageURI(selectedImageUri);
            }
        }
    }
}
