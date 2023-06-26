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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.candra.eksplorindonesia.API.APIRequestData;
import com.candra.eksplorindonesia.API.RetrofitServer;
import com.candra.eksplorindonesia.Adapter.AdapterKuliner;
import com.candra.eksplorindonesia.Adapter.AdapterWisata;
import com.candra.eksplorindonesia.Model.ModelAllResponse;
import com.candra.eksplorindonesia.Model.ModelKuliner;
import com.candra.eksplorindonesia.Model.ModelScanKuliner;
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
 * Use the {@link KulinerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KulinerFragment extends Fragment {

    //    Deklarasi
    private EditText etSearchDataRecyclerView;
    private ImageView ivScan, ivAddKuliner;
    private RecyclerView rvKuliner;
    private AdapterKuliner adKuliner;
    private RecyclerView.LayoutManager lmKuliner;
    private List<ModelScanKuliner> listScanKuliner = new ArrayList<>();

    private LottieAnimationView pbKuliner;
    private List<ModelKuliner> listKuliner = new ArrayList<>();

    private List <ModelKuliner> filteredList = new ArrayList<>();
    private SwipeRefreshLayout swipeBawahRefresh;

    private ControllerLogin cLogin = new ControllerLogin(getActivity());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KulinerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KulinerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KulinerFragment newInstance(String param1, String param2) {
        KulinerFragment fragment = new KulinerFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kuliner, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pbKuliner = view.findViewById(R.id.pb_kuliner);
        pbKuliner.setVisibility(View.VISIBLE);

        // Code
        ivScan = view.findViewById(R.id.iv_scan_kuliner);
        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQR();
            }
        });

        rvKuliner = view.findViewById(R.id.rv_kuliner);
        ivAddKuliner = view.findViewById(R.id.iv_add_kuliner);
        if(cLogin.getPreferences(getActivity(), cLogin.keySP_role).equals("user"))
        {
            ivAddKuliner.setVisibility(View.GONE);
        }
        ivAddKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddKulinerActivity.class));
            }
        });

        etSearchDataRecyclerView = view.findViewById(R.id.et_search_kuliner);
        etSearchDataRecyclerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
            }
        });

        lmKuliner = new LinearLayoutManager(getActivity());
        rvKuliner.setLayoutManager(lmKuliner);
        retrieveKuliner();

        swipeBawahRefresh = view.findViewById(R.id.swipe_refresh_kuliner);
        refresh();
    }

    private void filterData(String keyword) {
        filteredList.clear();
        for (ModelKuliner kuliner : listKuliner) {
            if (kuliner.getNamaKuliner().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(kuliner);
            }
        }
        adKuliner.filterList(filteredList);
        if (filteredList.size() == 0) {
            Toast.makeText(getActivity(), "Nama Kuliner tidak ditemukan!", Toast.LENGTH_SHORT).show();
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
            String hasilScan = result.getContents();
            APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
            Call<ModelAllResponse> prosesRetrieveScanKuliner = ard.ardScanKuliner(hasilScan);

            prosesRetrieveScanKuliner.enqueue(new Callback<ModelAllResponse>() {
                @Override
                public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                    if (response.isSuccessful() && response.body() != null)
                    {
                        String kode = response.body().getKode();
                        String pesan = response.body().getPesan();

                        listScanKuliner = response.body().getDataScanKuliner();
                        if(kode.equals("0"))
                        {
                            Toast.makeText(getActivity(), "Data tidak ditemukan !", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intentDetailKuliner = new Intent(getActivity(), DetailKulinerActivity.class);
                            intentDetailKuliner.putExtra("xIdKuliner", listScanKuliner.get(0).getIdKuliner());
                            intentDetailKuliner.putExtra("xNamaKuliner", listScanKuliner.get(0).getNamaKuliner());
                            intentDetailKuliner.putExtra("xAsalKuliner", listScanKuliner.get(0).getAsalKuliner());
                            intentDetailKuliner.putExtra("xDeskripsiKuliner", listScanKuliner.get(0).getDeskripsiKuliner());
                            intentDetailKuliner.putExtra("xFotoKuliner", listScanKuliner.get(0).getFotoKuliner());
                            startActivity(intentDetailKuliner);
                            Toast.makeText(getActivity(), "Data Berhasil ditemukan !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error Body is NULL", Toast.LENGTH_SHORT).show();
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
        retrieveKuliner();
    }

    private void retrieveKuliner()
    {
        APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);

        pbKuliner.setVisibility(View.VISIBLE);
        Call<ModelAllResponse> getRetrieveKuliner = ard.ardRetrieveDataKuliner();
        getRetrieveKuliner.enqueue(new Callback<ModelAllResponse>() {
            @Override
            public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listKuliner = response.body().getDataKuliner();

                adKuliner = new AdapterKuliner(getContext(), listKuliner);
                rvKuliner.setAdapter(adKuliner);
                adKuliner.notifyDataSetChanged();
                pbKuliner.setVisibility(View.GONE);
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
                retrieveKuliner();
                Toast.makeText(getActivity(), "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show();
                swipeBawahRefresh.setRefreshing(false);
            }
        });
    }
}