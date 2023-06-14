package id.nerdcreative.armossecond.adminactivity;

import static android.text.TextUtils.isEmpty;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import id.nerdcreative.armossecond.LoginActivity;
import id.nerdcreative.armossecond.Produk;
import id.nerdcreative.armossecond.R;
import id.nerdcreative.armossecond.adminfragment.HomeFragment;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.MyViewHolder> {

    Context context;
    ArrayList<Produk> produks;

    public ProdukAdapter(Context context, ArrayList<Produk> produks) {
        this.context = context;
        this.produks = produks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.produk_item, parent, false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Produk produk = produks.get(position);
        holder.idProduk.setText(produk.getIdProduk());
        holder.judulProduk.setText(produk.getJudul());
        holder.kategoriProduk.setText(produk.getKategori());
        holder.hargaProduk.setText("Rp"+produk.getHarga());
        String foto = produks.get(position).getFoto();
        if (isEmpty(foto)) {
            holder.fotoProduk.setImageResource(R.drawable.ic_image);
            holder.fotoProduk.setPadding(20,20,20,20);
        } else {
            Glide.with(holder.itemView.getContext()).load(foto.trim()).into(holder.fotoProduk);
            holder.fotoProduk.setPadding(0,0,0,0);
        }

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Yakin hapus produk yang dipilih ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Produk")
                                        .child(produks.get(position).getIdProduk());
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReferenceFromUrl(produks.get(position).getFoto());
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Produk ID : "+produks.get(position).getIdProduk() + " Deleted", Toast.LENGTH_SHORT).show();
                                        reference.removeValue();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        holder.listProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProduk.class);
                intent.putExtra("id", produk.getIdProduk());
                intent.putExtra("judul", produk.getJudul());
                intent.putExtra("harga", produk.getHarga());
                intent.putExtra("kategori", produk.getKategori());
                intent.putExtra("deskripsi", produk.getDeskripsi());
                intent.putExtra("stok", produk.getStok());
                intent.putExtra("foto", produk.getFoto());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return produks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView idProduk, judulProduk, kategoriProduk, hargaProduk;
        ImageView fotoProduk, deleteItem;
        ConstraintLayout listProduk;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            idProduk = itemView.findViewById(R.id.tv_id_produk);
            judulProduk = itemView.findViewById(R.id.tv_judul_pruduk);
            kategoriProduk = itemView.findViewById(R.id.tv_kategori_produk);
            hargaProduk = itemView.findViewById(R.id.tv_harga_produk);
            fotoProduk = itemView.findViewById(R.id.foto_container_list);
            listProduk = itemView.findViewById(R.id.list_produk);
            deleteItem = itemView.findViewById(R.id.delete_item);
        }
    }
}
