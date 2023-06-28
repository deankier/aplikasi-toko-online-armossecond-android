package id.nerdcreative.armossecond.useractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import id.nerdcreative.armossecond.R;
import id.nerdcreative.armossecond.adminactivity.EditProduk;

public class ViewProdukActivity extends AppCompatActivity {

    ImageView ivFotoProduk, btnBack, btnChat;
    TextView tvHarga, tvJudul, tvDeskripsi, tvKategori, tvStok;
    String getId, getJudul, getStok, getHarga, getKategori, getDeskripsi, getFoto;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_produk);

        ivFotoProduk = findViewById(R.id.foto_view_produk);
        tvHarga = findViewById(R.id.tv_harga_view_produk);
        tvJudul = findViewById(R.id.tv_judul_view_produk);
        tvDeskripsi = findViewById(R.id.tv_deskripsi_view_produk);
        tvKategori = findViewById(R.id.tv_kategori_view_produk);
        tvStok = findViewById(R.id.tv_stok_view_produk);
        btnBack = findViewById(R.id.btn_back);
        btnChat = findViewById(R.id.btn_chat);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProdukActivity.this, UserChatActivity.class));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GetSetProduk();
    }

    private void GetSetProduk() {
        getId = getIntent().getStringExtra("id");
        getJudul = getIntent().getStringExtra("judul");
        getHarga = getIntent().getStringExtra("harga");
        getStok = getIntent().getStringExtra("stok");
        getKategori = getIntent().getStringExtra("kategori");
        getDeskripsi = getIntent().getStringExtra("deskripsi");
        getFoto = getIntent().getStringExtra("foto");

        Glide.with(ViewProdukActivity.this).load(getFoto).into(ivFotoProduk);
        ivFotoProduk.setPadding(0,0,0,0);
        tvJudul.setText(getJudul);
        tvHarga.setText("Rp"+getHarga);
        tvStok.setText("Stok "+getStok);
        tvKategori.setText(getKategori);
        tvDeskripsi.setText(getDeskripsi);
    }
}