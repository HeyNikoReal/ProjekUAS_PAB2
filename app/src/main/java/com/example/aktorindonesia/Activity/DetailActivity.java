package com.example.aktorindonesia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aktorindonesia.R;
import com.example.aktorindonesia.Sound.BackSound;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivfoto;
    private TextView tvnama, tvTempatLahir, tvTanggalLahir, tvTahunAktif, tvPekerjaan, tvPenghargaan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivfoto = findViewById(R.id.iv_foto);
        tvnama = findViewById(R.id.tv_nama);
        tvPekerjaan = findViewById(R.id.tv_pekerjaan);
        tvPenghargaan = findViewById(R.id.tv_penghargaan);
        tvTahunAktif = findViewById(R.id.tv_tahun_aktif);
        tvTempatLahir = findViewById(R.id.tv_tempat_lahir);
        tvTanggalLahir = findViewById(R.id.tv_tanggal_lahir);

        Intent ambil = getIntent();
        String nama = ambil.getStringExtra("xNama");
        String tempatLahir = ambil.getStringExtra("xTempatLahir");
        String tanggalLahir = ambil.getStringExtra("xTanggalLahir");
        String tahunAktif = ambil.getStringExtra("xTahunAktif");
        String pekerjaan = ambil.getStringExtra("xPekerjaan");
        String penghargaan = ambil.getStringExtra("xPenghargaan");
        String foto = ambil.getStringExtra("xFoto");

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        setTitle(nama);
        tvnama.setText(nama);
        tvTahunAktif.setText(tahunAktif);
        tvPekerjaan.setText(pekerjaan);
        tvPenghargaan.setText(penghargaan);
        tvTempatLahir.setText(tempatLahir);
        try {
            tvTanggalLahir.setText(outputFormat.format(inputFormat.parse(tanggalLahir)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(foto).into(ivfoto);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tampilan, menu);
        int nightmode = AppCompatDelegate.getDefaultNightMode();
        if (nightmode == AppCompatDelegate.MODE_NIGHT_YES) {
            menu.findItem(R.id.menu_night).setTitle("Light Mode");
            menu.findItem(R.id.menu_night).setIcon(R.drawable.ic_light);
        } else {
            menu.findItem(R.id.menu_night).setTitle("Night Mode");
            menu.findItem(R.id.menu_night).setIcon(R.drawable.ic_dark);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_night:
                int nightmode = AppCompatDelegate.getDefaultNightMode();
                if (nightmode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate();
                break;

            case R.id.menu_call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: +6281271590161"));
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackSound.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BackSound.pause();
    }
}