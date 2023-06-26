package com.candra.eksplorindonesia.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.candra.eksplorindonesia.EditRoleUserActivity;
import com.candra.eksplorindonesia.Model.ModelUser;
import com.candra.eksplorindonesia.R;
import com.candra.eksplorindonesia.ShareData;


import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.VHUser>
{

    private Context context;
    private List<ModelUser> listUser;
    private ModelUser mu;

    private String pass, email;

    private ShareData sd = new ShareData();
    public AdapterUser(Context context, List<ModelUser> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public AdapterUser.VHUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
        return new VHUser(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.VHUser holder, @SuppressLint("RecyclerView") int position) {
        mu = listUser.get(position);

        holder.tvIdUser.setText(mu.getIdUser());
        holder.tvNamaUser.setText(mu.getFullname());
        holder.tvEmailUser.setText(mu.getEmail());
        holder.tvRoleUser.setText(mu.getRole());
        holder.tvNomorTeleponUser.setText(mu.getPhone());
        pass = mu.getPassword();

        String imageUser = mu.getFoto();
        byte[] imageBytes = Base64.decode(imageUser, Base64.DEFAULT);

        Glide.with(context)
                .asBitmap()
                .load(imageBytes)
                .into(holder.ivUser);

        holder.btnUbahRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelUser changeRole = listUser.get(position);
                Intent intent = new Intent(context, EditRoleUserActivity.class);
                intent.putExtra("uIdUser", changeRole.getIdUser());
                intent.putExtra("uFullname", changeRole.getFullname());
                intent.putExtra("uEmail", changeRole.getEmail());
                intent.putExtra("uRole", changeRole.getRole());
                intent.putExtra("uPhone", changeRole.getPhone());
                intent.putExtra("uPassword", changeRole.getPassword());
                intent.putExtra("uFotoUser", changeRole.getFoto());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listUser == null) return 0;
        return listUser.size();
    }

    public class VHUser extends RecyclerView.ViewHolder {
        TextView tvIdUser, tvNamaUser, tvEmailUser, tvRoleUser, tvNomorTeleponUser;
        ImageView ivUser;
        Button btnUbahRole;
        public VHUser(@NonNull View itemView) {
            super(itemView);

            tvIdUser = itemView.findViewById(R.id.tv_id_user);
            tvNamaUser = itemView.findViewById(R.id.tv_nama_user);
            tvEmailUser = itemView.findViewById(R.id.tv_email_user);
            tvRoleUser = itemView.findViewById(R.id.tv_role);
            tvNomorTeleponUser = itemView.findViewById(R.id.tv_phone_user);
            ivUser = itemView.findViewById(R.id.iv_foto_usr);
            btnUbahRole = itemView.findViewById(R.id.btn_ubah_role);
        }
    }

    public void filterList(List<ModelUser> filteredList)
    {
        listUser = filteredList;
        notifyDataSetChanged();
    }
}
