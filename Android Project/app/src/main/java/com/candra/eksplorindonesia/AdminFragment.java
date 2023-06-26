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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.candra.eksplorindonesia.API.APIRequestData;
import com.candra.eksplorindonesia.API.RetrofitServer;
import com.candra.eksplorindonesia.Adapter.AdapterUser;
import com.candra.eksplorindonesia.Adapter.AdapterWisata;
import com.candra.eksplorindonesia.Model.ModelAllResponse;
import com.candra.eksplorindonesia.Model.ModelScanUser;
import com.candra.eksplorindonesia.Model.ModelUser;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    private EditText etSearchDataRecyclerView;

    private ImageView ivScanUser;

    private RecyclerView rvUser;

    private AdapterUser adUser;

    private RecyclerView.LayoutManager lmUser;

    private List<ModelScanUser> listScanUser = new ArrayList<>();
    private List<ModelUser> listUser = new ArrayList<>();
    private List<ModelUser> filteredList = new ArrayList<>();

    private LottieAnimationView pbUser;

    private SwipeRefreshLayout swipeBawahRefresh;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminFragment() {
        // Required empty public constructor
        swipeBawahRefresh = null;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
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
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pbUser = view.findViewById(R.id.pb_user);
        pbUser.setVisibility(View.VISIBLE);
        ivScanUser = view.findViewById(R.id.iv_scan_user);
        ivScanUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQRUser();
            }
        });

        rvUser = view.findViewById(R.id.rv_user);

        etSearchDataRecyclerView = view.findViewById(R.id.et_search_user);
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

        lmUser = new LinearLayoutManager(getActivity());
        rvUser.setLayoutManager(lmUser);
        retrieveUser();

       swipeBawahRefresh = view.findViewById(R.id.swipe_refresh);
       refresh();

    }

    private void filterData(String keyword)
    {
        filteredList.clear();

        for(ModelUser user : listUser)
        {
            if(user.getFullname().toLowerCase().contains(keyword.toLowerCase()))
            {
                filteredList.add(user);
            }
        }
        adUser.filterList(filteredList);

        if(filteredList.size() == 0)
        {
            // Tampilkan pesan jika tidak ada hasil pencarian
            Toast.makeText(getActivity(), "Nama User tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanQRUser()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Gunakan Volume Up untuk menyalakan flash !");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(StartScan.class);
        launcher.launch(options);
    }

    // Perbaiki disini
    ActivityResultLauncher<ScanOptions> launcher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null)
        {
            String hasil = result.getContents();

            APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);
            Call<ModelAllResponse> prosesRetrieveScan = ard.ardScanUser(hasil);

            prosesRetrieveScan.enqueue(new Callback<ModelAllResponse>() {
                @Override
                public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                    String kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    listScanUser = response.body().getDataScanUser();

                    if(kode.equals("0"))
                    {
                        Toast.makeText(getActivity(), "Data tidak ditemukan !", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intentDetailUser = new Intent(getActivity(), EditRoleUserActivity.class);
                        intentDetailUser.putExtra("uIdUser", listScanUser.get(0).getIdUser());
                        intentDetailUser.putExtra("uFullname", listScanUser.get(0).getFullname());
                        intentDetailUser.putExtra("uEmail", listScanUser.get(0).getEmail());
                        intentDetailUser.putExtra("uRole", listScanUser.get(0).getRole());
                        intentDetailUser.putExtra("uPhone", listScanUser.get(0).getPhone());
                        intentDetailUser.putExtra("uPassword", listScanUser.get(0).getPassword());
                        intentDetailUser.putExtra("uFotoUser", listScanUser.get(0).getFoto());
                        startActivity(intentDetailUser);
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
        retrieveUser();
    }

    private void retrieveUser()
    {
        APIRequestData ard = RetrofitServer.connectionRetrofit().create(APIRequestData.class);

        Call<ModelAllResponse> getRetrieveUser = ard.ardRetrieveDataUser();
        pbUser.setVisibility(View.VISIBLE);
        getRetrieveUser.enqueue(new Callback<ModelAllResponse>() {
            @Override
            public void onResponse(Call<ModelAllResponse> call, Response<ModelAllResponse> response) {
                pbUser.setVisibility(View.GONE);
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listUser = response.body().getDataUser();

                adUser = new AdapterUser(getContext(), listUser);
                rvUser.setAdapter(adUser);
                adUser.notifyDataSetChanged();
                pbUser.setVisibility(View.GONE);

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
                retrieveUser();
                Toast.makeText(getActivity(), "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show();
                swipeBawahRefresh.setRefreshing(false);
            }
        });
    }
}