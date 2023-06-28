package id.nerdcreative.armossecond.authactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.nerdcreative.armossecond.DataUser;
import id.nerdcreative.armossecond.R;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText etNama, etUsername, etEmail, etPhone, etPassword;
    private LinearLayout progresLay;
    private Button btnLogin, btnSignUp;
    private ImageView btnBack;
    String Nama,Username,Password,Email,Phone;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progresLay = findViewById(R.id.progres_layout);

        etNama = findViewById(R.id.et_name_signup);
        etPassword = findViewById(R.id.et_password_signup);
        etUsername = findViewById(R.id.et_username_signup);
        etPhone = findViewById(R.id.et_phone_signup);
        etEmail = findViewById(R.id.et_email_signup);

        btnBack = findViewById(R.id.btn_back_signup);
        btnLogin = findViewById(R.id.btn_login_signup);
        btnSignUp = findViewById(R.id.btn_signup);

        mAuth = FirebaseAuth.getInstance();

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Tidak perlu melakukan tindakan sebelum perubahan teks
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Menghapus angka 0 di awal teks (jika ada)
                String inputText = charSequence.toString();
                if (inputText.startsWith("0")) {
                    inputText = inputText.substring(1).trim();
                    etPhone.setText(inputText);
                    etPhone.setSelection(inputText.length());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // Tidak perlu melakukan tindakan setelah perubahan teks
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nama = etNama.getText().toString();
                Username = etUsername.getText().toString().toLowerCase();
                Password = etPassword.getText().toString();
                Email = etEmail.getText().toString();
                Phone = etPhone.getText().toString();

                progresLay.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(Nama)){
                    etNama.setError("Tidak boleh kosong");
                    etNama.requestFocus();
                    progresLay.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(Username)){
                    etUsername.setError("Tidak boleh kosong");
                    etUsername.requestFocus();
                    progresLay.setVisibility(View.GONE);
                }else if (TextUtils.isEmpty(Email)){
                    etEmail.setError("Tidak boleh kosong");
                    etEmail.requestFocus();
                    progresLay.setVisibility(View.GONE);
                }else if (TextUtils.isEmpty(Phone)){
                    etPhone.setError("Tidak boleh kosong");
                    etPhone.requestFocus();
                    progresLay.setVisibility(View.GONE);
                }else if (TextUtils.isEmpty(Password)){
                    etPassword.setError("Tidak boleh kosong");
                    etPassword.requestFocus();
                    progresLay.setVisibility(View.GONE);
                } else if (Password.length()<6) {
                    etPassword.setError("Password terlalu pendek");
                    etPassword.requestFocus();
                    progresLay.setVisibility(View.GONE);
                } else if (!isValidEmail(Email)) {
                    etEmail.setError("Masukan email yang valid");
                    etEmail.requestFocus();
                    progresLay.setVisibility(View.GONE);
                }
                else {
                    getUsers(Username);
                }
            }
        });
    }

    private void CreateAcount() {
        Nama = etNama.getText().toString();
        Username = etUsername.getText().toString().toLowerCase();
        Password = etPassword.getText().toString();
        Email = etEmail.getText().toString();
        Phone = "+62"+etPhone.getText().toString();
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                DataUser Users = new DataUser(Nama, Username, Email, Phone, "", "");
                FirebaseDatabase.getInstance().getReference("Users").child("UserAuth").child(Username).setValue(Users)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progresLay.setVisibility(View.GONE);

                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                new AlertDialog.Builder(SignUpActivity.this)
                                                        .setTitle("Link verifikasi email terkirim")
                                                        .setMessage("Periksa email anda !\nJuga periksa folder SPAM")
                                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // Continue with delete operation
                                                                onBackPressed();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progresLay.setVisibility(View.GONE);
                                            Toast.makeText(SignUpActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progresLay.setVisibility(View.GONE);
                                Toast.makeText(SignUpActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
    private boolean isValidEmail(String email) {
        // Ekspresi reguler untuk memeriksa format email
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        return email.matches(emailPattern);
    }
    private void getUsers(String username){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child("UserAuth");
        databaseReference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        etUsername.setError("Username sudah ada");
                        etUsername.requestFocus();
                        progresLay.setVisibility(View.GONE);
                    }else {
                        CreateAcount();
                    }
                }
            }
        });
    }
}