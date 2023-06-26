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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.candra.eksplorindonesia.DetailWisataActivity;
import com.candra.eksplorindonesia.Model.ModelWisata;
import com.candra.eksplorindonesia.R;
import com.candra.eksplorindonesia.ShareData;

import java.util.List;

public class AdapterWisata extends RecyclerView.Adapter<AdapterWisata.VHWisata> {

    private Context context;
    private List<ModelWisata> listWisata;

    private ShareData sd;

    private ModelWisata mw;
    public AdapterWisata(Context context, List<ModelWisata> listWisata) {
        this.context = context;
        this.listWisata = listWisata;
    }

    @NonNull
    @Override
    public AdapterWisata.VHWisata onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_wisata, parent, false);
        return new VHWisata(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VHWisata holder, @SuppressLint("RecyclerView") int position) {
        mw = listWisata.get(position);

        holder.tvIdWisata.setText(mw.getIdWisata());
        holder.tvNamaWisata.setText(mw.getNamaWisata());
        holder.tvLokasiWisata.setText(mw.getLokasiWisata());
        holder.tvMapsWisata.setText(mw.getMapsWisata());
        holder.tvDeskripsiWisata.setText(mw.getDeskripsiWisata());

        String imageWisata = mw.getFotoWisata();
        byte[] imageBytes = Base64.decode(imageWisata, Base64.DEFAULT);

        Glide.with(context)
                .asBitmap()
                .load(imageBytes)
                .into(holder.ivFotoWisata);

        holder.btnDetailWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelWisata clickedWisata = listWisata.get(position);
                Intent intent = new Intent(context, DetailWisataActivity.class);
                intent.putExtra("xIdWisata", clickedWisata.getIdWisata());
                intent.putExtra("xNamaWisata", clickedWisata.getNamaWisata());
                intent.putExtra("xLokasiWisata", clickedWisata.getLokasiWisata());
                intent.putExtra("xMapsWisata", clickedWisata.getMapsWisata());
                intent.putExtra("xDeskripsiWisata", clickedWisata.getDeskripsiWisata());
                intent.putExtra("xFotoWisata", clickedWisata.getFotoWisata());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listWisata==null) return 0;
        return listWisata.size();
    }


    public class VHWisata extends RecyclerView.ViewHolder {

        TextView tvIdWisata, tvNamaWisata, tvLokasiWisata, tvMapsWisata, tvDeskripsiWisata;
        ImageView ivFotoWisata;
        Button btnDetailWisata;

        public VHWisata(@NonNull View itemView) {
            super(itemView);

            tvIdWisata = itemView.findViewById(R.id.tv_id_wisata);
            tvNamaWisata = itemView.findViewById(R.id.tv_nama_wisata);
            tvLokasiWisata = itemView.findViewById(R.id.tv_lokasi_wisata);
            tvMapsWisata = itemView.findViewById(R.id.tv_maps_wisata);
            tvDeskripsiWisata = itemView.findViewById(R.id.tv_deskripsi_wisata);
            ivFotoWisata = itemView.findViewById(R.id.iv_foto_wisata);
            btnDetailWisata = itemView.findViewById(R.id.btn_detail_wisata);


        }
    }

    // Filter data
    public void filterList(List<ModelWisata> filteredList) {
        listWisata = filteredList;
        notifyDataSetChanged();
    }
}
