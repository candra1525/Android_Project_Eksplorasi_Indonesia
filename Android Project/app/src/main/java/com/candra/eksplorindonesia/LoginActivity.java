package com.candra.eksplorindonesia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.candra.eksplorindonesia.API.APIRequestData;
import com.candra.eksplorindonesia.API.RetrofitServer;
import com.candra.eksplorindonesia.Model.ModelAllResponse;
import com.candra.eksplorindonesia.Model.ModelUser;
import com.candra.eksplorindonesia.Utility.ControllerLogin;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{

    private EditText etEmailLogin, etPasswordLogin;

    private Button btnLogin;

    private TextView tvRedirectRegister;

    private String email, password;


    private List<ModelUser> listUser = new ArrayList<>();

    private ControllerLogin cLogin = new ControllerLogin(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailLogin = findViewById(R.id.et_email_login);
        etPasswordLogin = findViewById(R.id.et_password_login);
        tvRedirectRegister = findViewById(R.id.tv_redirect_register);

        tvRedirectRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmailLogin.getText().toString();
                password = etPasswordLogin.getText().toString();

                if(email.trim().isEmpty())
                {
                    etEmailLogin.setError("Email Tidak Boleh Kosong !");
                    etEmailLogin.requestFocus();
                }
                else if(password.trim().isEmpty())
                {
                    etPasswordLogin.setError("Password Tidak Boleh Kosong !");
                    etPasswordLogin.requestFocus();
                }
                else
                {
                    Login();
                }
            }
        });
    }

    private void Login()
    {
        APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);

        Call<ModelAllResponse> prosesLogin = ard.ardLoginUser(email, password);

        prosesLogin.enqueue(new Callback<ModelAllResponse>() {
            @Override
            public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();

                listUser = response.body().getDataUser();


                if(kode.equals("0"))
                {
                    Toast.makeText(LoginActivity.this, "Login Gagal !, Email atau Password Salah !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    cLogin.setPreferences(LoginActivity.this,cLogin.keySP_id, listUser.get(0).getIdUser());
                    cLogin.setPreferences(LoginActivity.this,cLogin.keySP_fullname, listUser.get(0).getFullname());
                    cLogin.setPreferences(LoginActivity.this,cLogin.keySP_email, listUser.get(0).getEmail());
                    cLogin.setPreferences(LoginActivity.this,cLogin.keySP_role, listUser.get(0).getRole());
                    cLogin.setPreferences(LoginActivity.this,cLogin.keySP_phone, listUser.get(0).getPhone());
                    cLogin.setPreferences(LoginActivity.this,cLogin.keySP_password, listUser.get(0).getPassword());
                    cLogin.setPreferences(LoginActivity.this,cLogin.keySP_foto, listUser.get(0).getFoto());

                    String role = cLogin.getPreferences(LoginActivity.this, cLogin.keySP_role);
                    if(role.equals("admin"))
                    {
                        startActivity(new Intent(LoginActivity.this, MainActivityAdmin.class));
                    }
                    else
                    {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    Toast.makeText(LoginActivity.this, "Login Berhasil !", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error ! " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}