package id.nerdcreative.armossecond.adminactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import id.nerdcreative.armossecond.R;
import id.nerdcreative.armossecond.adminfragment.ChatFragment;
import id.nerdcreative.armossecond.adminfragment.HomeFragment;
import id.nerdcreative.armossecond.adminfragment.PesananFragment;
import id.nerdcreative.armossecond.adminfragment.ProdukFragment;

public class MainAdminActivity extends AppCompatActivity {

    LinearLayout btnMenuHome, btnMenuProduk, btnMenuPesanan, btnMenuChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_admin_frame, new HomeFragment()).commit();

        btnMenuHome = findViewById(R.id.btn_home_menu);
        btnMenuProduk = findViewById(R.id.btn_produk_menu);
        btnMenuPesanan = findViewById(R.id.btn_pesanan_menu);
        btnMenuChat = findViewById(R.id.btn_chat_menu);

        btnMenuHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_admin_frame, new HomeFragment()).commit();
            }
        });

        btnMenuProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_admin_frame, new ProdukFragment()).commit();
            }
        });

        btnMenuPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_admin_frame, new PesananFragment()).commit();
            }
        });

        btnMenuChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_admin_frame, new ChatFragment()).commit();
            }
        });



    }

}