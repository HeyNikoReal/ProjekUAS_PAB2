package com.example.aktorindonesia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aktorindonesia.API.APIRequestData;
import com.example.aktorindonesia.API.RetroServer;
import com.example.aktorindonesia.Model.ModelResponse;
import com.example.aktorindonesia.R;
import com.example.aktorindonesia.Sound.BackSound;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {
    private EditText etNama, etTempatLahir, etTanggalLahir, etTahunAktif, etPekerjaan, etFilm, etPenghargaan, etFoto;
    private String nama, tempatLahir, tanggalLahir, tahunAktif, pekerjaan, film, penghargaan, foto;
    private Button btnTambah;
    private DatePickerDialog dpd;
    private String choosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNama = findViewById(R.id.et_nama);
        etTempatLahir = findViewById(R.id.et_tempat_lahir);
        etTanggalLahir = findViewById(R.id.et_tanggal_lahir);
        etTahunAktif = findViewById(R.id.et_tahun_aktif);
        etPekerjaan = findViewById(R.id.et_pekerjaan);
        etFilm = findViewById(R.id.et_film);
        etPenghargaan = findViewById(R.id.et_penghargaan);
        etFoto = findViewById(R.id.et_foto);
        btnTambah = findViewById(R.id.btn_tambah);

        etTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar kalender = Calendar.getInstance();
                dpd = new DatePickerDialog
                        (TambahActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                String tahun = Integer.toString(year);
                                String bulan = Integer.toString(month + 1);
                                String tanggal = Integer.toString(day);
                                choosenDate = tahun + "-" + bulan + "-" + tanggal;
                                etTanggalLahir.setText(choosenDate);
                                etTanggalLahir.setError(null);
                            }
                        }
                                , kalender.get(Calendar.YEAR)
                                , kalender.get(Calendar.MONTH)
                                , kalender.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = etNama.getText().toString();
                tempatLahir = etTempatLahir.getText().toString();
                tanggalLahir = etTanggalLahir.getText().toString();
                tahunAktif = etTahunAktif.getText().toString();
                pekerjaan = etPekerjaan.getText().toString();
                film = etFilm.getText().toString();
                penghargaan = etPenghargaan.getText().toString();
                foto = etFoto.getText().toString();

                if (nama.trim().isEmpty()) {
                    etNama.setError("Nama Tidak Boleh Kosong");
                } else if (tempatLahir.trim().isEmpty()) {
                    etTempatLahir.setError("Tempat Lahir Tidak Boleh Kosong");
                } else if (tanggalLahir.trim().isEmpty()) {
                    Toast.makeText(TambahActivity.this, "Tanggal Lahir Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else if (tahunAktif.trim().isEmpty()) {
                    etTahunAktif.setError("Tahun Aktif Tidak Boleh Kosong");
                } else if (pekerjaan.trim().isEmpty()) {
                    etPekerjaan.setError("Pekerjaan Tidak Boleh Kosong");
                } else if (film.trim().isEmpty()) {
                    etFilm.setError("Film Yang Diperankan Tidak Boleh Kosong");
                } else if (penghargaan.trim().isEmpty()) {
                    etPenghargaan.setError("Penghargaan Tidak Boleh Kosong");
                } else if (foto.trim().isEmpty()) {
                    etFoto.setError("URL Foto Tidak Boleh Kosong");
                } else {
                    tambahAktor();
                }
            }
        });
    }

    private void tambahAktor() {
        APIRequestData ARD = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ModelResponse> proses = ARD.ardCreate(nama, tempatLahir, tanggalLahir, tahunAktif, pekerjaan, film, penghargaan, foto);

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();
                Toast.makeText(TambahActivity.this, "Kode : " + kode + ", Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });
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

}