package com.candra.eksplorindonesia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.candra.eksplorindonesia.Utility.ControllerLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    private ImageView ivToAbout;
    private TextView tvNamaUserHome;

    private Button btnLihatDetailTentangIndonesia;

    private LinearLayout llPindahFragmentWisata, llPindahFragmentKuliner;

    private ControllerLogin cLogin;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cLogin = new ControllerLogin(getActivity());

        ivToAbout = view.findViewById(R.id.iv_to_about);
        ivToAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });

        btnLihatDetailTentangIndonesia = view.findViewById(R.id.btn_lihat_detail_tentang_indonesia);
        btnLihatDetailTentangIndonesia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DetailTentangIndonesiaActivity.class));
            }
        });

        tvNamaUserHome = view.findViewById(R.id.tv_nama_user_home);
        tvNamaUserHome.setText(cLogin.getPreferences(getActivity(), cLogin.keySP_fullname));

        llPindahFragmentWisata = view.findViewById(R.id.ll_pindah_fragment_wisata);
        llPindahFragmentWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomNavigationView nav = getActivity().findViewById(R.id.bottomNav);
                nav.getMenu().findItem(R.id.wisata).setChecked(true);
                navigateToFragment(new WisataFragment());
            }
        });

        llPindahFragmentKuliner = view.findViewById(R.id.ll_pindah_fragment_kuliner);
        llPindahFragmentKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomNavigationView nav = getActivity().findViewById(R.id.bottomNav);
                nav.getMenu().findItem(R.id.kuliner).setChecked(true);
                navigateToFragment(new KulinerFragment());
            }
        });
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
