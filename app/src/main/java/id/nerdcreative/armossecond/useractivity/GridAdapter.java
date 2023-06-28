package id.nerdcreative.armossecond.useractivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import id.nerdcreative.armossecond.DataUser;
import id.nerdcreative.armossecond.Produk;
import id.nerdcreative.armossecond.R;
import id.nerdcreative.armossecond.adminactivity.EditProduk;
import kotlinx.coroutines.channels.ProduceKt;

public class GridAdapter extends BaseAdapter {

    private ArrayList<Produk> produks;
    Context context;
    LayoutInflater layoutInflater;
    ImageView ivFotoProduk;
    TextView tvJudulProduk, tvHarga;
    CardView gridProdukView;

    public GridAdapter(ArrayList<Produk> produks, Context context) {
        this.produks = produks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return produks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.grid_pruduk, null);
        }
        ivFotoProduk = convertView.findViewById(R.id.foto_container_grid);
        tvJudulProduk = convertView.findViewById(R.id.tv_judul_grid);
        tvHarga = convertView.findViewById(R.id.tv_harga_grid);
        gridProdukView = convertView.findViewById(R.id.grid_produk);

        Glide.with(context).load(produks.get(position).getFoto()).into(ivFotoProduk);
        ivFotoProduk.setPadding(0,0,0,0);

        tvJudulProduk.setText(produks.get(position).getJudul());
        tvHarga.setText("Rp"+produks.get(position).getHarga());

        gridProdukView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewProdukActivity.class);
                intent.putExtra("id", produks.get(position).getIdProduk());
                intent.putExtra("judul", produks.get(position).getJudul());
                intent.putExtra("harga", produks.get(position).getHarga());
                intent.putExtra("kategori", produks.get(position).getKategori());
                intent.putExtra("deskripsi", produks.get(position).getDeskripsi());
                intent.putExtra("stok", produks.get(position).getStok());
                intent.putExtra("foto", produks.get(position).getFoto());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
