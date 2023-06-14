package id.nerdcreative.armossecond.adminactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.nerdcreative.armossecond.LoginActivity;
import id.nerdcreative.armossecond.R;

public class EditProduk extends AppCompatActivity {

    LinearLayout btnTambahDiskon, btnEdit;
    ImageView btnBack;
    ImageView containerFoto;
    String idProduk, judulProduk, hargaProduk, kategoriProduk, deskripsiProduk, fotoProduk, stokProduk;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvHarga, tvJudul, tvStok, tvDeskripsi, tvKategori;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_produk);

        getProduk();

        tvHarga = findViewById(R.id.tv_harga_detail);
        tvJudul = findViewById(R.id.tv_judul_detail);
        tvStok = findViewById(R.id.tv_stok_detail);
        tvKategori = findViewById(R.id.tv_kategori_detail);
        tvDeskripsi = findViewById(R.id.tv_deskripsi_detail);

        btnBack = findViewById(R.id.btn_back);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_edit);

        btnTambahDiskon = findViewById(R.id.btn_tambah_diskon);
        btnEdit = findViewById(R.id.btn_edit_detail);

        btnTambahDiskon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditProduk.this)
                        .setTitle("Pemberitahuan")
                        .setMessage("Maaf untuk sementara ini belum dapat menambahkan diskon.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProduk.this, UpdateProdukActivity.class);
                intent.putExtra("id", idProduk);
                intent.putExtra("harga", hargaProduk);
                intent.putExtra("judul", judulProduk);
                intent.putExtra("stok", stokProduk);
                intent.putExtra("kategori", kategoriProduk);
                intent.putExtra("deskripsi", deskripsiProduk);
                intent.putExtra("foto", fotoProduk);
                startActivity(intent);
            }
        });

        containerFoto = findViewById(R.id.foto_produk_detail);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProduk();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void getProduk(){
        idProduk = getIntent().getStringExtra("id");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Produk");
        databaseReference.child(idProduk).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().exists()){
                    DataSnapshot dataSnapshot = task.getResult();
                    judulProduk = String.valueOf(dataSnapshot.child("judul").getValue());
                    hargaProduk = String.valueOf(dataSnapshot.child("harga").getValue());
                    kategoriProduk = String.valueOf(dataSnapshot.child("kategori").getValue());
                    deskripsiProduk = String.valueOf(dataSnapshot.child("deskripsi").getValue());
                    fotoProduk = String.valueOf(dataSnapshot.child("foto").getValue()).trim();
                    stokProduk = String.valueOf(dataSnapshot.child("stok").getValue());

                    Glide.with(EditProduk.this).load(fotoProduk).into(containerFoto);
                    containerFoto.setPadding(0,0,0,0);
                    tvHarga.setText("Rp"+hargaProduk);
                    tvJudul.setText(judulProduk);
                    tvStok.setText("Stok "+stokProduk);
                    tvKategori.setText(kategoriProduk);
                    tvDeskripsi.setText(deskripsiProduk);
                }

            }
        });
    }
    private void getDataPost(){
        Glide.with(EditProduk.this).load(fotoProduk).into(containerFoto);
        containerFoto.setPadding(0,0,0,0);
        tvHarga.setText("Rp"+hargaProduk);
        tvJudul.setText(judulProduk);
        tvStok.setText("Stok "+stokProduk);
        tvKategori.setText(kategoriProduk);
        tvDeskripsi.setText(deskripsiProduk);
    }
}