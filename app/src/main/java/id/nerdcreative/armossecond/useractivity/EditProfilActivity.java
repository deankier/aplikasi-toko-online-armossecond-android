package id.nerdcreative.armossecond.useractivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import id.nerdcreative.armossecond.DataUser;
import id.nerdcreative.armossecond.Produk;
import id.nerdcreative.armossecond.R;
import id.nerdcreative.armossecond.adminactivity.UpdateProdukActivity;

public class EditProfilActivity extends AppCompatActivity {

    ImageView btnBack, ivProfil;
    TextView btnSimpan;
    EditText etNama, etUsername, etEmail, etPhone;
    TextInputEditText etAlamat;
    CardView btnChooseFoto, progresLay;
    String getNama, getUsername, getEmail, getPhone, getAlamat, getFoto;
    String userID;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    Uri imageUri;
    Boolean gantiFoto = false;
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        // Inisialisasi
        progresLay = findViewById(R.id.progres_layout);
        btnBack = findViewById(R.id.btn_back);
        ivProfil = findViewById(R.id.iv_profil);
        btnSimpan = findViewById(R.id.btn_simpan);
        etNama = findViewById(R.id.et_nama_profil);
        etUsername = findViewById(R.id.et_username_profil);
        etPhone = findViewById(R.id.et_phone_profil);
        etEmail = findViewById(R.id.et_email_profil);
        etAlamat = findViewById(R.id.et_alamat_profil);
        btnChooseFoto = findViewById(R.id.btn_choose_foto);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("DataUser");

        GetSetDataUser(userID);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnChooseFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progresLay.setVisibility(View.VISIBLE);
                updateProfil();
            }
        });

    }
    private void GetSetDataUser(String ID){
        databaseReference.child(ID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        //Get Data
                        DataSnapshot dataSnapshot = task.getResult();
                        getNama = String.valueOf(dataSnapshot.child("dataNama").getValue());
                        getUsername = String.valueOf(dataSnapshot.child("dataUsername").getValue());
                        getEmail = String.valueOf(dataSnapshot.child("dataEmail").getValue());
                        getPhone = String.valueOf(dataSnapshot.child("dataPhone").getValue());
                        getAlamat = String.valueOf(dataSnapshot.child("dataAlamat").getValue());
                        getFoto = String.valueOf(dataSnapshot.child("dataFoto").getValue());
                        //Set data
                        etNama.setText(getNama);
                        etUsername.setText(getUsername);
                        etEmail.setText(getEmail);
                        etPhone.setText(getPhone);
                        etAlamat.setText(getAlamat);
                        if (getFoto.isBlank()){
                            // ekspresi jika foto masih kosong
                        }else {
                            Glide.with(EditProfilActivity.this).load(getFoto.trim()).into(ivProfil);

                        }
                    }else {
                        // Lakukan jika data tidak ketemu
                    }
                }
            }
        });
    }
    private void getImage() {
        Intent imageIntentGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntentGalery, 2);
    }
    private void updateProfil() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReferencere = FirebaseStorage.getInstance().getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference = database.getReference("Users").child("DataUser");

        if (gantiFoto==true){
            StorageReference fotoStorage = storage.getReferenceFromUrl(getFoto.trim());
            fotoStorage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    ivProfil.setDrawingCacheEnabled(true);
                    ivProfil.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) ivProfil.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byte[] bytes = stream.toByteArray();
                    String namaFile = "IMG"+userID+".jpg";
                    final String pathImage = "ProfilUser/"+namaFile;
                    UploadTask uploadTask = storageReferencere.child(pathImage).putBytes(bytes);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    getReference.child(userID).setValue(new DataUser(etNama.getText().toString(), etUsername.getText().toString()
                                                    , etEmail.getText().toString(), etPhone.getText().toString(), etAlamat.getText().toString(), uri.toString().trim()))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            new AlertDialog.Builder(EditProfilActivity.this)
                                                    .setMessage("Profil berhasil diupdate")
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            etNama.setText("");
                                                            etUsername.setText("");
                                                            etEmail.setText("");
                                                            etPhone.setText("");
                                                            etAlamat.setText("");
                                                            ivProfil.setImageResource(R.drawable.ic_image);
                                                            progresLay.setVisibility(View.INVISIBLE);
                                                            onBackPressed();
                                                            finish();
                                                        }
                                                    }).show();
                                            gantiFoto = false;
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progresLay.setVisibility(View.INVISIBLE);
                            Toast.makeText(EditProfilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progres = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progresLay.setVisibility(View.INVISIBLE);
                    Toast.makeText(EditProfilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            getReference.child(userID).setValue(new DataUser(etNama.getText().toString(), etUsername.getText().toString()
                            , etEmail.getText().toString(), etPhone.getText().toString(), etAlamat.getText().toString(), getFoto))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    new AlertDialog.Builder(EditProfilActivity.this)
                            .setMessage("Profil berhasil diupdate")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    etNama.setText("");
                                    etUsername.setText("");
                                    etEmail.setText("");
                                    etPhone.setText("");
                                    etAlamat.setText("");
                                    ivProfil.setImageResource(R.drawable.ic_image);
                                    progresLay.setVisibility(View.INVISIBLE);
                                    onBackPressed();
                                    finish();
                                }
                            }).show();

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                if (resultCode==RESULT_OK){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivProfil.setImageBitmap(bitmap);
                }
                break;
            case REQUEST_CODE_GALLERY:
                if (resultCode==RESULT_OK){
                    imageUri = data.getData();
                    ivProfil.setImageURI(imageUri);
                    gantiFoto = true;
                }
                break;
        }
    }


}