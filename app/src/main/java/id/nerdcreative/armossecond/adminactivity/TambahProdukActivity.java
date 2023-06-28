package id.nerdcreative.armossecond.adminactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import id.nerdcreative.armossecond.Produk;
import id.nerdcreative.armossecond.R;

public class TambahProdukActivity extends AppCompatActivity {

    String[] arrayKategori = new String[]{"Topi" ,"Baju", "Jaket", "Tas", "Celana", "Sepatu"};
    AutoCompleteTextView dwnKategori;
    TextInputEditText etJudul, etHarga, etStok, etDeskripsi;
    String judul, harga, stok, deskripsi, kategori, foto;
    Button btnTambahkanProduk, btnTambahFoto;
    ImageView ImageContainer, btnBack;
    Uri imageUri;
    LinearLayout progresLay;
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        progresLay = findViewById(R.id.progres_layout);
        ImageContainer = findViewById(R.id.foto_container);

        dwnKategori = findViewById(R.id.dwn_kategori);
        ArrayAdapter<String> adapter_kategori = new ArrayAdapter<>(
                this, R.layout.drop_down_item_kategori,arrayKategori);
        dwnKategori.setAdapter(adapter_kategori);

        etJudul = findViewById(R.id.et_judul_produk);
        etHarga = findViewById(R.id.et_harga_produk);
        etStok = findViewById(R.id.et_stok_produk);
        etDeskripsi = findViewById(R.id.et_deskripsi_produk);

        btnBack = findViewById(R.id.btn_back_produk);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnTambahFoto = findViewById(R.id.btn_tambah_foto);
        btnTambahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        btnTambahkanProduk = findViewById(R.id.btn_tambahkan_produk);
        btnTambahkanProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judul = etJudul.getText().toString();
                harga = etHarga.getText().toString();
                stok = etStok.getText().toString();
                deskripsi = etDeskripsi.getText().toString();
                kategori = dwnKategori.getText().toString();
                foto = ImageContainer.toString().trim();


                progresLay.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(judul)){
                    etJudul.setError("Tidak boleh kosong");
                    etJudul.requestFocus();
                    progresLay.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(harga)){
                    etHarga.setError("Tidak boleh kosong");
                    etHarga.requestFocus();
                    progresLay.setVisibility(View.GONE);
                }else if (TextUtils.isEmpty(stok)){
                    etStok.setError("Tidak boleh kosong");
                    etStok.requestFocus();
                    progresLay.setVisibility(View.GONE);
                }else if (TextUtils.isEmpty(deskripsi)){
                    etDeskripsi.setError("Tidak boleh kosong");
                    etDeskripsi.requestFocus();
                    progresLay.setVisibility(View.GONE);
                }else if (TextUtils.isEmpty(kategori)){
                    dwnKategori.setError("Tidak boleh kosong");
                    dwnKategori.requestFocus();
                    progresLay.setVisibility(View.GONE);
                }else if (imageUri == null){
                    Toast.makeText(TambahProdukActivity.this, "Masukan foto produk", Toast.LENGTH_SHORT).show();
                    progresLay.setVisibility(View.GONE);
                }else{
                    addProduk();
                }
            }
        });


    }

    private void addProduk() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference = database.getReference();
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        ImageContainer.setDrawingCacheEnabled(true);
        ImageContainer.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) ImageContainer.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bytes = stream.toByteArray();

        String idProduk = generateRandomId();
        String namaFile = "IMG"+idProduk+".jpg";
        final String pathImage = "FotoProduk/"+namaFile;
        UploadTask uploadTask = reference.child(pathImage).putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        getReference.child("Produk").child(idProduk).setValue(new Produk(
                                judul,harga,stok,kategori, deskripsi , uri.toString().trim(), idProduk)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                new AlertDialog.Builder(TambahProdukActivity.this)
                                        .setMessage("Produk berhasil ditambahkan")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Continue with delete operation
                                                progresLay.setVisibility(View.GONE);
                                                etJudul.setText("");
                                                etHarga.setText("");
                                                etStok.setText("");
                                                dwnKategori.setText("");
                                                etDeskripsi.setText("");
                                                ImageContainer.setImageResource(R.drawable.ic_image);
                                                ImageContainer.setPadding(20,20,20,20);
                                                progresLay.setVisibility(View.GONE);
//                                                startActivity(new Intent(TambahProdukActivity.this, MainAdminActivity.class));
                                                onBackPressed();
                                                finish();
                                            }
                                        }).show();

                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progresLay.setVisibility(View.GONE);
                Toast.makeText(TambahProdukActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progresLay.setVisibility(View.VISIBLE);
                double progres = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
            }
        });
    }

    private void getImage() {
        Intent imageIntentGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntentGalery, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                if (resultCode==RESULT_OK){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ImageContainer.setImageBitmap(bitmap);
                }
                break;
            case REQUEST_CODE_GALLERY:
                if (resultCode==RESULT_OK){
                    imageUri = data.getData();
                    ImageContainer.setImageURI(imageUri);
                    ImageContainer.setPadding(0,0,0,0);
                }
                break;
        }
    }

    public static String generateRandomId() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int ID_LENGTH = 10;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}