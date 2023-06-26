package com.candra.eksplorindonesia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.candra.eksplorindonesia.API.APIRequestData;
import com.candra.eksplorindonesia.API.RetrofitServer;
import com.candra.eksplorindonesia.Model.ModelAllResponse;
import com.candra.eksplorindonesia.Utility.ControllerLogin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageView ivBackToMyProfile, ivDeleteProfile, ivEditFotoProfile;
    private EditText etNamaLengkap, etEmail, etNomorTelepon, etPassword;
    private Button btnSimpanPerubahan, btnUbahFoto;

    private ControllerLogin cLogin;
    private Uri selectedImageUri;

    String namaLengkap, email, nomorTelepon, password;
    boolean klikubah = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        cLogin = new ControllerLogin(EditProfileActivity.this);

        ivBackToMyProfile = findViewById(R.id.iv_back_to_my_profile);
        ivBackToMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivEditFotoProfile = findViewById(R.id.iv_edit_foto_profile);
        String base64Image = cLogin.getPreferences(EditProfileActivity.this, cLogin.keySP_foto);
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);

        Glide.with(EditProfileActivity.this)
                .asBitmap()
                .load(imageBytes)
                .into(ivEditFotoProfile);

        ivDeleteProfile = findViewById(R.id.iv_delete_profile);
        ivDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idUser = cLogin.getPreferences(EditProfileActivity.this, String.valueOf(cLogin.keySP_id));
                DeleteAccount(idUser);
            }
        });

        btnUbahFoto = findViewById(R.id.btn_ubah_foto);
        btnUbahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
                klikubah = true;
            }
        });

        etNamaLengkap = findViewById(R.id.et_edit_nama_lengkap);
        etEmail = findViewById(R.id.et_edit_email);
        etNomorTelepon = findViewById(R.id.et_edit_nomor_telepon);
        etPassword = findViewById(R.id.et_edit_password);

        etNamaLengkap.setText(cLogin.getPreferences(EditProfileActivity.this, cLogin.keySP_fullname));
        etEmail.setText(cLogin.getPreferences(EditProfileActivity.this, cLogin.keySP_email));
        etNomorTelepon.setText(cLogin.getPreferences(EditProfileActivity.this, cLogin.keySP_phone));
        etPassword.setText(cLogin.getPreferences( EditProfileActivity.this, cLogin.keySP_password));

        btnSimpanPerubahan = findViewById(R.id.btn_simpan_perubahan);
        btnSimpanPerubahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                namaLengkap = etNamaLengkap.getText().toString();
                email = etEmail.getText().toString();
                nomorTelepon = etNomorTelepon.getText().toString();
                password = etPassword.getText().toString();
                if(namaLengkap.trim().isEmpty())
                {
                    etNamaLengkap.setError("Nama Lengkap tidak boleh kosong !");
                    etNamaLengkap.requestFocus();
                }
                else if(email.trim().isEmpty())
                {
                    etEmail.setError("Email tidak boleh kosong !");
                    etEmail.requestFocus();
                }
                else if(nomorTelepon.trim().isEmpty())
                {
                    etNomorTelepon.setError("Nomor Telepon tidak boleh kosong !");
                    etNomorTelepon.requestFocus();
                }
                else if(password.trim().isEmpty())
                {
                    etPassword.setError("Password tidak boleh kosong");
                    etPassword.requestFocus();
                }
                else
                {
                    if(password.length() <= 5)
                    {
                        Toast.makeText(EditProfileActivity.this, "Password tidak boleh kurang dari 5 digit !", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(EditProfileActivity.this);
                        dialog.setTitle("Perhatian !");
                        dialog.setCancelable(true);
                        dialog.setMessage("Jika anda ingin menyimpan perubahan data, anda harus login kembali !");
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SimpanPerubahanUser();
                            }
                        });
                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });
    }

    private void SimpanPerubahanUser()
    {
        if(klikubah == true)
        {
            try {
                Call<ModelAllResponse> updateUser;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                byte [] imageBytes = baos.toByteArray();
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);

//                Toast.makeText(this, "base : " + imageBytes, Toast.LENGTH_SHORT).show();

                updateUser = ard.ardUpdateDataUser(
                        RequestBody.create(MediaType.parse("text/plain"), cLogin.getPreferences(EditProfileActivity.this, cLogin.keySP_id)),
                        RequestBody.create(MediaType.parse("text/plain"), namaLengkap),
                        RequestBody.create(MediaType.parse("text/plain"), email),
                        RequestBody.create(MediaType.parse("text/plain"), cLogin.getPreferences(EditProfileActivity.this , cLogin.keySP_role)),
                        RequestBody.create(MediaType.parse("text/plain"), nomorTelepon),
                        MultipartBody.Part.createFormData("foto", "image.jpg", requestBody),
                        RequestBody.create(MediaType.parse("text/plain"), password)
                );
                updateUser.enqueue(new Callback<ModelAllResponse>() {
                    @Override
                    public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                        if(response.isSuccessful())
                        {
                            String kode = response.body().getKode();
                            String pesan  = response.body().getPesan();

                            Toast.makeText(EditProfileActivity.this, "Data User berhasil di Update ! Silahkan Login Ulang !", Toast.LENGTH_SHORT).show();
                            cLogin.setPreferences(EditProfileActivity.this, cLogin.keySP_email, null);
                            startActivity(new Intent(EditProfileActivity.this, SplashScreen.class));
                        }
                        else {
                            Toast.makeText(EditProfileActivity.this, "Proses update data user gagal!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                        Toast.makeText(EditProfileActivity.this, "Proses update data user gagal! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(EditProfileActivity.this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Call<ModelAllResponse> updateUserTanpaFoto;
            APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
            updateUserTanpaFoto = ard.ardUpdateDataUserTanpaFoto(
                    RequestBody.create(MediaType.parse("text/plain"), cLogin.getPreferences(EditProfileActivity.this, cLogin.keySP_id)),
                    RequestBody.create(MediaType.parse("text/plain"), namaLengkap),
                    RequestBody.create(MediaType.parse("text/plain"), email),
                    RequestBody.create(MediaType.parse("text/plain"), cLogin.getPreferences(EditProfileActivity.this , cLogin.keySP_role)),
                    RequestBody.create(MediaType.parse("text/plain"), nomorTelepon),
                    RequestBody.create(MediaType.parse("text/plain"), password)
            );

            updateUserTanpaFoto.enqueue(new Callback<ModelAllResponse>() {
                @Override
                public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                    if(response.isSuccessful())
                    {
                        String kode = response.body().getKode();
                        String pesan  = response.body().getPesan();

                        Toast.makeText(EditProfileActivity.this, "Data User berhasil di Update ! Silahkan Login Ulang !", Toast.LENGTH_SHORT).show();
                        cLogin.setPreferences(EditProfileActivity.this, cLogin.keySP_email, null);
                        startActivity(new Intent(EditProfileActivity.this, SplashScreen.class));
                    }
                    else {
                        Toast.makeText(EditProfileActivity.this, "Proses update data user gagal!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Proses update data user gagal! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void DeleteAccount(String idUser) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditProfileActivity.this);
        dialog.setTitle("Peringatan !");
        dialog.setMessage("Apakah anda yakin ingin menghapus Account ?");
        dialog.setCancelable(true);
        dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);

                Call<ModelAllResponse> prosesDeleteAccount = ard.ardDeleteDataUser(idUser);

                prosesDeleteAccount.enqueue(new Callback<ModelAllResponse>() {
                    @Override
                    public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                        if (response.isSuccessful()) {
                            String kode = response.body().getKode();
                            String pesan = response.body().getPesan();
                            Toast.makeText(EditProfileActivity.this, "Account berhasil dihapus !", Toast.LENGTH_SHORT).show();
                            cLogin.setPreferences(EditProfileActivity.this, cLogin.keySP_id, null);
                            cLogin.setPreferences(EditProfileActivity.this, cLogin.keySP_email, null);
                            cLogin.setPreferences(EditProfileActivity.this, cLogin.keySP_password, null);
                            cLogin.setPreferences(EditProfileActivity.this, cLogin.keySP_role, null);
                            cLogin.setPreferences(EditProfileActivity.this, cLogin.keySP_phone, null);
                            cLogin.setPreferences(EditProfileActivity.this, cLogin.keySP_fullname, null);
                            cLogin.setPreferences(EditProfileActivity.this, cLogin.keySP_foto, null);
                            startActivity(new Intent(EditProfileActivity.this, SplashScreen.class));
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Gagal menghapus account!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                        Toast.makeText(EditProfileActivity.this, "Account Gagal dihapus ! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                ivEditFotoProfile.setImageURI(selectedImageUri);
            }
        }
    }
}
