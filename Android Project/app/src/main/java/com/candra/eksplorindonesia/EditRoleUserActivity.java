package com.candra.eksplorindonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.candra.eksplorindonesia.API.APIRequestData;
import com.candra.eksplorindonesia.API.RetrofitServer;
import com.candra.eksplorindonesia.Model.ModelAllResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRoleUserActivity extends AppCompatActivity
{

    private ImageView ivBackToListUser, ivEditFotoUser;

    private TextView tvIdEditUser;
    private EditText etNamaUser, etEmailUser, etNomorTeleponUser;
    private Spinner spRole;
    private Button btnSimpanPerubahanUser;

    private String idUser, fullname, email, role, phone, password, fotoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_role_user);

        Intent dataUser = getIntent();
        idUser = dataUser.getStringExtra("uIdUser");
        fullname = dataUser.getStringExtra("uFullname");
        email = dataUser.getStringExtra("uEmail");
        role = dataUser.getStringExtra("uRole");
        phone = dataUser.getStringExtra("uPhone");
        password = dataUser.getStringExtra("uPassword");
        fotoUser = dataUser.getStringExtra("uFotoUser");


        ivBackToListUser = findViewById(R.id.iv_back_to_list_user);
        ivBackToListUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvIdEditUser = findViewById(R.id.tv_id_edit_user);
        ivEditFotoUser = findViewById(R.id.iv_edit_foto_user);
        etNamaUser = findViewById(R.id.et_nama_user);
        etEmailUser = findViewById(R.id.et_email_user);
        spRole = findViewById(R.id.sp_role);
        etNomorTeleponUser = findViewById(R.id.et_nomor_telepon_user);

        etNamaUser.setText(fullname);
        etEmailUser.setText(email);
        etNomorTeleponUser.setText(phone);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.role_user, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapter);

        int position = adapter.getPosition(role);
        spRole.setSelection(position);

        ivEditFotoUser = findViewById(R.id.iv_edit_foto_user);
        byte [] imageUser = Base64.decode(fotoUser, Base64.DEFAULT);

        Glide.with(EditRoleUserActivity.this)
                .asBitmap()
                .load(imageUser)
                .into(ivEditFotoUser);

        btnSimpanPerubahanUser = findViewById(R.id.btn_simpan_perubahan_user);
        btnSimpanPerubahanUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanPerubahan();
            }
        });
    }

    private void simpanPerubahan()
    {
        String idUser1 = idUser;
        String namaUser1 = fullname;
        String email1 = email;
        String role1 = spRole.getSelectedItem().toString();
        String phone1 = phone;
        String password1 = password;

        APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
        Call<ModelAllResponse> updateDataUserTanpaFoto = ard.ardUpdateDataUserTanpaFoto(
                RequestBody.create(MediaType.parse("text/plain"), idUser1),
                RequestBody.create(MediaType.parse("text/plain"), namaUser1),
                RequestBody.create(MediaType.parse("text/plain"), email1),
                RequestBody.create(MediaType.parse("text/plain"), role1),
                RequestBody.create(MediaType.parse("text/plain"), phone1),
                RequestBody.create(MediaType.parse("text/plain"), password1)
        );

        updateDataUserTanpaFoto.enqueue(new Callback<ModelAllResponse>() {
            @Override
            public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(EditRoleUserActivity.this, "Role User berhasil di ubah !", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                Toast.makeText(EditRoleUserActivity.this, "Proses update role User Gagal !", Toast.LENGTH_SHORT).show();
            }
        });

    }
}