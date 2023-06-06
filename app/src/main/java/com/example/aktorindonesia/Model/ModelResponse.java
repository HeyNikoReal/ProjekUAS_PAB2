package com.example.aktorindonesia.Model;

import java.util.List;

public class ModelResponse {
    public String kode, pesan;
    public List<ModelAktor> data;

    public String getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelAktor> getData() {
        return data;
    }
}
