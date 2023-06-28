package id.nerdcreative.armossecond.authactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.nerdcreative.armossecond.R;
import id.nerdcreative.armossecond.adminactivity.MainAdminActivity;
import id.nerdcreative.armossecond.useractivity.MainActivity;

public class LoginActivity extends AppCompatActivity {

    TextView BtnForgot;
    TextInputEditText etUsername, etPassword;
    String Username, Password;
    String getName, getUsername, getEmail, getPhone;
    LinearLayout ProgresLay;
    Button Login, BtnSignUp;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    String adminToken, userToken;
    FirebaseAuth.AuthStateListener userAuthListener, adminAuthListener;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BtnForgot = findViewById(R.id.btn_forgot_login);
        BtnSignUp = findViewById(R.id.btn_signup_login);
        Login = findViewById(R.id.btn_login);
        ProgresLay = findViewById((R.id.progres_layout));
        etUsername = findViewById(R.id.et_username_login);
        etPassword = findViewById(R.id.et_password_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // untuk menyimpan informasi login
        sharedPreferences = getSharedPreferences("UserAuth", MODE_PRIVATE);
        userAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (!user.isEmailVerified()){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Email belum terverifikasi")
                            .setMessage("Periksa email anda untuk \nverifikasi !")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    ProgresLay.setVisibility(View.GONE);
                                }
                            }).show();
                }else{
                    ProgresLay.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("nama",getName);
                    intent.putExtra("username",getUsername);
                    intent.putExtra("email",getEmail);
                    intent.putExtra("phone",getPhone);
                    startActivity(intent);
                    finish();

                }
            }
        };
        adminAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (!user.isEmailVerified()){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Email belum terverifikasi")
                            .setMessage("Periksa email anda untuk \nverifikasi !")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    ProgresLay.setVisibility(View.GONE);
                                }
                            }).show();
                }else{
                    ProgresLay.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login Sebagai Admin", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainAdminActivity.class));
                    finish();
                }
            }
        };
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username = etUsername.getText().toString().toLowerCase();
                Password = etPassword.getText().toString();

                ProgresLay.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(Username)){
                    etUsername.setError("Tidak boleh kosong");
                    etUsername.requestFocus();
                    ProgresLay.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(Password)) {
                    etPassword.setError("Tidak boleh kosong");
                    etPassword.requestFocus();
                    ProgresLay.setVisibility(View.GONE);
                }
                else {
                    getAdmin(Username);
                }
            }
        });
        BtnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });
        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

    }
    private void getUsers(String username){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child("UserAuth");
        databaseReference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        getName = String.valueOf(dataSnapshot.child("dataNama").getValue());
                        getUsername = String.valueOf(dataSnapshot.child("dataUsername").getValue());
                        getEmail = String.valueOf(dataSnapshot.child("dataEmail").getValue());
                        getPhone = String.valueOf(dataSnapshot.child("dataPhone").getValue());
                        UserLogin();
                    }else {
                        etUsername.setError("Username tidak ada");
                        etUsername.requestFocus();
                        ProgresLay.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    private void UserLogin() {
        mAuth.signInWithEmailAndPassword(getEmail, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("nama",getName);
                    intent.putExtra("username",getUsername);
                    intent.putExtra("email",getEmail);
                    intent.putExtra("phone",getName);
                    // Mendapatkan token login
                    mAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                userToken = task.getResult().getToken();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userToken", userToken);
                                editor.apply();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mAuth.addAuthStateListener(userAuthListener);
                }else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login Gagal")
                            .setMessage("Periksa password anda!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    ProgresLay.setVisibility(View.GONE);
                                    etPassword.requestFocus();
                                }
                            }).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                ProgresLay.setVisibility(View.GONE);
            }
        });
    }
    private void getAdmin(String username){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        databaseReference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        getEmail = String.valueOf(dataSnapshot.child("dataEmail").getValue());
                        AdminLogin();
                    }else {
                        getUsers(Username);
                    }
                }
            }
        });
    }
    private void AdminLogin() {
        mAuth.signInWithEmailAndPassword(getEmail, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // Mendapatkan token login
                    mAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                adminToken = task.getResult().getToken();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("adminToken", adminToken);
                                editor.apply();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mAuth.addAuthStateListener(adminAuthListener);
                }else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login Gagal")
                            .setMessage("Periksa password anda!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    ProgresLay.setVisibility(View.GONE);
                                    etPassword.requestFocus();
                                }
                            }).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                ProgresLay.setVisibility(View.GONE);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            if (sharedPreferences.getString("adminToken", null) != null){
                mAuth.addAuthStateListener(adminAuthListener);
            } else if (sharedPreferences.getString("userToken", null) != null) {
                mAuth.addAuthStateListener(userAuthListener);
            }
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (sharedPreferences.getString("adminToken", null) != null){
            mAuth.removeAuthStateListener(adminAuthListener);
        } else if (sharedPreferences.getString("userToken", null) != null) {
            mAuth.removeAuthStateListener(userAuthListener);
        }
    }

}