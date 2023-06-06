package com.example.aktorindonesia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aktorindonesia.API.APIRequestData;
import com.example.aktorindonesia.API.RetroServer;
import com.example.aktorindonesia.Adapter.AdapterAktor;
import com.example.aktorindonesia.Model.ModelAktor;
import com.example.aktorindonesia.Model.ModelResponse;
import com.example.aktorindonesia.Sound.BackSound;
import com.example.aktorindonesia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvAktor;
    private FloatingActionButton fabTambah;
    private RecyclerView.Adapter adAktor;
    private ProgressBar pbAktor;
    private RecyclerView.LayoutManager LMAktor;
    private List<ModelAktor> listAktor = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackSound.start(this);
        rvAktor = findViewById(R.id.rv_aktor);
        fabTambah = findViewById(R.id.fab_tambah_data);
        pbAktor = findViewById(R.id.pb_proses);

        LMAktor = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAktor.setLayoutManager(LMAktor);

        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TambahActivity.class));
            }
        });
    }


    public void retrieveAktor() {
        pbAktor.setVisibility(View.VISIBLE);
        APIRequestData ARD = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ModelResponse> proses = ARD.ardRetrieve();

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String kode = String.valueOf(response.body().getKode());
                String pesan = response.body().getPesan();
                listAktor = response.body().getData();

                adAktor = new AdapterAktor(MainActivity.this, listAktor);
                rvAktor.setAdapter(adAktor);
                adAktor.notifyDataSetChanged();

                pbAktor.setVisibility(View.GONE);
            }


            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT);
                pbAktor.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackSound.resume();
        retrieveAktor();
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
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Konfirmasi");
        dialog.setMessage("Keluar dari Aplikasi ?");

        dialog.setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        dialog.setNegativeButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finish();
            }

        });
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BackSound.pause();
    }

}