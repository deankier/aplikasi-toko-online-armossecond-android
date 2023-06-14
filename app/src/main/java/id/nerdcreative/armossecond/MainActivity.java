package id.nerdcreative.armossecond;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String namaExt, usernameExt, emailExt, phoneExt, userID;
    Button logout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
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
        DataUser Users = new DataUser(namaExt, usernameExt, emailExt, phoneExt);
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