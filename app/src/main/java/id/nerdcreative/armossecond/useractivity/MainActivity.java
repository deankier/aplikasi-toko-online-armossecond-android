package id.nerdcreative.armossecond.useractivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.nerdcreative.armossecond.DataUser;
import id.nerdcreative.armossecond.R;
import id.nerdcreative.armossecond.userfragment.UserHomeFragment;
import id.nerdcreative.armossecond.userfragment.UserProfilFragment;
import id.nerdcreative.armossecond.userfragment.UserTransaksiFragment;

public class MainActivity extends AppCompatActivity {

    String namaExt, usernameExt, emailExt, phoneExt, userID;
    LinearLayout btnFragHome, btnFragTransaksi, btnFragProfil;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFragHome = findViewById(R.id.btn_frag_home);
        btnFragTransaksi = findViewById(R.id.btn_frag_transaksi);
        btnFragProfil = findViewById(R.id.btn_frag_profil);

        mAuth = FirebaseAuth.getInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_user_frame, new UserHomeFragment()).commit();
        btnFragHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_user_frame, new UserHomeFragment()).commit();
            }
        });
        btnFragTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_user_frame, new UserTransaksiFragment()).commit();
            }
        });
        btnFragProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_user_frame, new UserProfilFragment()).commit();
            }
        });

    }

    private void checkUser(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("DataUser");
        FirebaseUser user = mAuth.getCurrentUser();
        Intent intent = getIntent();
        namaExt = intent.getStringExtra("nama");
        usernameExt = intent.getStringExtra("username");
        emailExt = intent.getStringExtra("email");
        phoneExt = intent.getStringExtra("phone");
        userID = user.getUid();
        DataUser Users = new DataUser(namaExt, usernameExt, emailExt, phoneExt, "", "");
        databaseReference.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
//                        Toast.makeText(MainActivity.this, "Sudah Ada", Toast.LENGTH_SHORT).show();
                    }else {
                        FirebaseDatabase.getInstance().getReference("Users").child("DataUser")
                                .child(userID).setValue(Users);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }
}