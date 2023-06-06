package com.example.aktorindonesia.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.aktorindonesia.API.APIRequestData;
import com.example.aktorindonesia.API.RetroServer;
import com.example.aktorindonesia.Activity.DetailActivity;
import com.example.aktorindonesia.Activity.MainActivity;
import com.example.aktorindonesia.Activity.UbahActivity;
import com.example.aktorindonesia.Model.ModelAktor;
import com.example.aktorindonesia.Model.ModelResponse;
import com.example.aktorindonesia.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterAktor extends RecyclerView.Adapter<AdapterAktor.VHAktor> {

    private Context ctx;
    private List<ModelAktor> listAktor;

    public AdapterAktor(Context ctx, List<ModelAktor> listAktor) {
        this.ctx = ctx;
        this.listAktor = listAktor;
    }

    @NonNull
    @Override
    public AdapterAktor.VHAktor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View varView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_aktor, parent, false);
        return new VHAktor(varView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAktor.VHAktor holder, int position) {
        ModelAktor MA = listAktor.get(position);
        holder.tvId.setText(MA.getId());
        holder.tvNama.setText(MA.getNama());
        holder.tvTempatLahir.setText(MA.getTempat_lahir());
        holder.tvTanggalLahir.setText(MA.getTanggal_lahir());
        holder.tvTahunAktif.setText(MA.getTahun_aktif());
        holder.tvPekerjaan.setText(MA.getPekerjaaan());
        holder.tvPenghargaan.setText(MA.getPenghargaan());
        holder.tvFoto.setText(MA.getFoto());
        Glide.with(holder.itemView.getContext()).load(MA.getFoto()).
                apply(new RequestOptions().override(1000, 1000)).
                into(holder.ivFoto);

    }

    @Override
    public int getItemCount() {
        return listAktor.size();
    }

    public class VHAktor extends RecyclerView.ViewHolder {

        TextView tvId, tvNama, tvTempatLahir, tvTanggalLahir, tvTahunAktif, tvPekerjaan, tvPenghargaan, tvFoto;
        ImageView ivFoto;
        Button btnDetail, btnUbah, btnHapus;

        public VHAktor(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvTempatLahir = itemView.findViewById(R.id.tv_tempat_lahir);
            tvTanggalLahir = itemView.findViewById(R.id.tv_tanggal_lahir);
            tvTahunAktif = itemView.findViewById(R.id.tv_tahun_aktif);
            tvPekerjaan = itemView.findViewById(R.id.tv_pekerjaan);
            tvPenghargaan = itemView.findViewById(R.id.tv_penghargaan);
            tvFoto = itemView.findViewById(R.id.tv_foto);
            ivFoto = itemView.findViewById(R.id.iv_foto);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            btnUbah = itemView.findViewById(R.id.btn_ubah);
            btnHapus = itemView.findViewById(R.id.btn_hapus);

            btnHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder pesan = new AlertDialog.Builder(ctx);
                    pesan.setTitle("Perhatian");
                    pesan.setMessage("Apakah Anda yakin ingin menghapus data dari " + tvNama.getText().toString() + " ?");
                    pesan.setCancelable(true);

                    pesan.setNegativeButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hapusAktor(tvId.getText().toString());
                        }
                    });

                    pesan.setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    pesan.show();
                }
            });

            btnUbah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pindah = new Intent(ctx, UbahActivity.class);
                    pindah.putExtra("xId", tvId.getText().toString());
                    pindah.putExtra("xNama", tvNama.getText().toString());
                    pindah.putExtra("xTempatLahir", tvTempatLahir.getText().toString());
                    pindah.putExtra("xTanggalLahir", tvTanggalLahir.getText().toString());
                    pindah.putExtra("xTahunAktif", tvTahunAktif.getText().toString());
                    pindah.putExtra("xPekerjaan", tvPekerjaan.getText().toString());
                    pindah.putExtra("xPenghargaan", tvPenghargaan.getText().toString());
                    pindah.putExtra("xFoto", tvFoto.getText().toString());
                    ctx.startActivity(pindah);
                }
            });

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pindah = new Intent(ctx, DetailActivity.class);
                    pindah.putExtra("xNama", tvNama.getText().toString());
                    pindah.putExtra("xTempatLahir", tvTempatLahir.getText().toString());
                    pindah.putExtra("xTanggalLahir", tvTanggalLahir.getText().toString());
                    pindah.putExtra("xTahunAktif", tvTahunAktif.getText().toString());
                    pindah.putExtra("xPekerjaan", tvPekerjaan.getText().toString());
                    pindah.putExtra("xPenghargaan", tvPenghargaan.getText().toString());
                    pindah.putExtra("xFoto", tvFoto.getText().toString());
                    ctx.startActivity(pindah);
                }
            });
        }
    }

    public void hapusAktor(String id) {
        APIRequestData ARD = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ModelResponse> proses = ARD.ardDelete(id);
        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String pesan = response.body().getPesan();
                String kode = response.body().getKode();
                Toast.makeText(ctx, "Kode = " + kode + ", Pesan :  " + pesan, Toast.LENGTH_SHORT).show();
                ((MainActivity) ctx).retrieveAktor();
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(ctx, "Gagal Menghubungi Server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
