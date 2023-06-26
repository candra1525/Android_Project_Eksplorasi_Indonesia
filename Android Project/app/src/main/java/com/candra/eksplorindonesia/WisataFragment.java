package com.candra.eksplorindonesia;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.candra.eksplorindonesia.API.APIRequestData;
import com.candra.eksplorindonesia.API.RetrofitServer;
import com.candra.eksplorindonesia.Adapter.AdapterWisata;
import com.candra.eksplorindonesia.Model.ModelAllResponse;
import com.candra.eksplorindonesia.Model.ModelScanWisata;
import com.candra.eksplorindonesia.Model.ModelUser;
import com.candra.eksplorindonesia.Model.ModelWisata;
import com.candra.eksplorindonesia.Utility.ControllerLogin;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WisataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WisataFragment extends Fragment {
    private EditText etSearchDataRecyclerView;
    private ImageView ivScan, ivAddWisata;
    private RecyclerView rvWisata;
    private AdapterWisata adWisata;
    private RecyclerView.LayoutManager lmWisata;
    private List<ModelScanWisata> listScanWisata = new ArrayList<>();
    private LottieAnimationView pbWisata;
    private List<ModelWisata> listWisata = new ArrayList<>();
    private List<ModelWisata> filteredList = new ArrayList<>();
    private SwipeRefreshLayout swipeBawahRefresh;
    private ControllerLogin cLogin = new ControllerLogin(getActivity());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WisataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WisataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WisataFragment newInstance(String param1, String param2) {
        WisataFragment fragment = new WisataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wisata, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pbWisata = view.findViewById(R.id.pb_wisata);
        pbWisata.setVisibility(View.VISIBLE);
        ivScan = view.findViewById(R.id.iv_scan_wisata);
        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQR();
            }
        });
        rvWisata = view.findViewById(R.id.rv_wisata); // Initialize rvWisata here
        ivAddWisata = view.findViewById(R.id.iv_add_wisata);

        if(cLogin.getPreferences(getActivity(), cLogin.keySP_role).equals("user"))
        {
            ivAddWisata.setVisibility(View.GONE);
        }
        ivAddWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddWisataActivity.class));
            }
        });

        etSearchDataRecyclerView = view.findViewById(R.id.et_search_wisata);
        etSearchDataRecyclerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak perlu melakukan implementasi pada metode ini
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Tidak perlu melakukan implementasi pada metode ini
            }
        });

        // Set layout manager untuk rvWisata
        lmWisata = new LinearLayoutManager(getActivity());
        rvWisata.setLayoutManager(lmWisata);
        retrieveWisata();

        swipeBawahRefresh = view.findViewById(R.id.swipe_refresh_wisata);
        refresh();
    }

    // Filter data
    private void filterData(String keyword) {
        filteredList.clear();

        for (ModelWisata wisata : listWisata) {
            if (wisata.getNamaWisata().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(wisata);
            }
        }
        adWisata.filterList(filteredList);

        if (filteredList.size() == 0) {
            // Tampilkan pesan jika tidak ada hasil pencarian
            Toast.makeText(getActivity(), "Nama Wisata tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanQR()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Gunakan Volume Up untuk menyalakan flash !");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(StartScan.class);
        launcher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> launcher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null)
        {
            String hasil = result.getContents();

            APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
            Call<ModelAllResponse> prosesRetrieveScan = ard.ardScanWisata(hasil);

            prosesRetrieveScan.enqueue(new Callback<ModelAllResponse>() {
                @Override
                public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                    String kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    listScanWisata = response.body().getDataScanWisata();

                    if(kode.equals("0"))
                    {
                        Toast.makeText(getActivity(), "Data tidak ditemukan !", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intentDetail = new Intent(getActivity(), DetailWisataActivity.class);
                        intentDetail.putExtra("xIdWisata", listScanWisata.get(0).getIdWisata());
                        intentDetail.putExtra("xNamaWisata", listScanWisata.get(0).getNamaWisata());
                        intentDetail.putExtra("xLokasiWisata", listScanWisata.get(0).getLokasiWisata());
                        intentDetail.putExtra("xMapsWisata", listScanWisata.get(0).getMapsWisata());
                        intentDetail.putExtra("xFotoWisata", listScanWisata.get(0).getFotoWisata());
                        intentDetail.putExtra("xDeskripsiWisata", listScanWisata.get(0).getDeskripsiWisata());
                        startActivity(intentDetail);
                        Toast.makeText(getActivity(), "Data Berhasil ditemukan !", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "Gagal melakukan scanning", Toast.LENGTH_SHORT).show();
                }
            });

        }
    });

    @Override
    public void onResume() {
        super.onResume();
        retrieveWisata();
    }

    public void retrieveWisata() {
        APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);

        Call<ModelAllResponse> getRetrieveWisata = ard.ardRetrieveDataWisata();
        getRetrieveWisata.enqueue(new Callback<ModelAllResponse>() {
            @Override
            public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                pbWisata.setVisibility(View.GONE);
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listWisata = response.body().getDataWisata();

                adWisata = new AdapterWisata(getContext(), listWisata);
                rvWisata.setAdapter(adWisata);
                adWisata.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ModelAllResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal menghubungi server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //  Refresh ketika di Scroll
    private void refresh() {

        swipeBawahRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveWisata();
                Toast.makeText(getActivity(), "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show();
                swipeBawahRefresh.setRefreshing(false);
            }
        });
    }
}