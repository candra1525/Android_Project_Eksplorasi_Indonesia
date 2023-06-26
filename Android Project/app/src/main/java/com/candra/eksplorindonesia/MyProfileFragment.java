package com.candra.eksplorindonesia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.candra.eksplorindonesia.API.APIRequestData;
import com.candra.eksplorindonesia.API.RetrofitServer;
import com.candra.eksplorindonesia.Model.ModelAllResponse;
import com.candra.eksplorindonesia.Utility.ControllerLogin;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {

    private ImageView ivEditProfile, ivQR;

    private CircleImageView ivFotoUser;

    private TextView tvIdUser, tvNamaUser, tvEmailUser, tvRoleUser, tvNomorTeleponUser;
    private Button btnLogOut;

    private ControllerLogin cLogin = new ControllerLogin(getActivity());


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ivEditProfile = view.findViewById(R.id.iv_edit_profile);
        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });
        ivFotoUser = view.findViewById(R.id.iv_foto_user);
        String base64Image = cLogin.getPreferences(getActivity(), cLogin.keySP_foto);
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);

        Glide.with(getActivity())
                .asBitmap()
                .load(imageBytes)
                .into(ivFotoUser);

        ivFotoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(getActivity(), ZoomUser.class);
                z.putExtra("zFotoUser", base64Image);
                startActivity(z);
            }
        });

        tvIdUser = view.findViewById(R.id.tv_id_user);
        tvNamaUser = view.findViewById(R.id.tv_nama_user);
        tvEmailUser = view.findViewById(R.id.tv_email_user);
        tvRoleUser = view.findViewById(R.id.tv_role_user);
        tvNomorTeleponUser = view.findViewById(R.id.tv_nomor_telepon_user);

        tvIdUser.setText(cLogin.getPreferences(getActivity(), String.valueOf(cLogin.keySP_id)));
        tvNamaUser.setText(cLogin.getPreferences(getActivity(), cLogin.keySP_fullname));
        tvEmailUser.setText(cLogin.getPreferences(getActivity(), cLogin.keySP_email));
        tvRoleUser.setText(cLogin.getPreferences(getActivity(), cLogin.keySP_role));
        tvNomorTeleponUser.setText(cLogin.getPreferences(getActivity(), cLogin.keySP_phone));

        btnLogOut = view.findViewById(R.id.btn_logout);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        ivQR = view.findViewById(R.id.iv_qr);
        GenerateQRCode.generateCode(cLogin.getPreferences(getActivity(), cLogin.keySP_fullname), ivQR);
    }

    private void Logout()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Perhatian !");
        dialog.setMessage("Apakah anda yakin ingin Logout ?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cLogin.setPreferences(getActivity(), cLogin.keySP_email, null);
                startActivity(new Intent(getActivity(), SplashScreen.class));
                getActivity().finish();
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