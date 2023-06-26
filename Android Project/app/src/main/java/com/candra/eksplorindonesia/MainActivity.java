package com.candra.eksplorindonesia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.candra.eksplorindonesia.Utility.ControllerLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ControllerLogin cLogin = new ControllerLogin(MainActivity.this);
    public BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (cLogin.isLogin(MainActivity.this, cLogin.keySP_email)) {
            setContentView(R.layout.activity_main);
            nav = findViewById(R.id.bottomNav);

            openFragment(new HomeFragment());

            nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            openFragment(new HomeFragment());
                            return true;

                        case R.id.kuliner:
                            openFragment(new KulinerFragment());
                            return true;

                        case R.id.wisata:
                            openFragment(new WisataFragment());
                            return true;

                        case R.id.profile:
                            openFragment(new MyProfileFragment());
                            return true;
                    }
                    return false;
                }
            });

        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void openFragment(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_container, f);
        ft.commit();
    }
}
