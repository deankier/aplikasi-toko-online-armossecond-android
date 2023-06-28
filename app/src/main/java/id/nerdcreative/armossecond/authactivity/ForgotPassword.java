package id.nerdcreative.armossecond.authactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import id.nerdcreative.armossecond.R;

public class ForgotPassword extends AppCompatActivity {

    ImageView btnBack;
    Button btnKirim;
    TextInputEditText etEmail;
    String getEmail;
    LinearLayout progresLay;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Inisialisasi
        btnBack = findViewById(R.id.btn_back_forgot);
        btnKirim = findViewById(R.id.btn_kirim);
        etEmail = findViewById(R.id.et_email_forgot);
        mAuth = FirebaseAuth.getInstance();
        progresLay = findViewById(R.id.progres_layout);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progresLay.setVisibility(View.VISIBLE);
                getEmail = etEmail.getText().toString();
                if (!isValidEmail(getEmail)){
                    etEmail.setError("Masukan email yang valid");
                    etEmail.requestFocus();
                    progresLay.setVisibility(View.INVISIBLE);
                }else {
                    sentResetPassword();
                }
            }
        });

    }

    private boolean isValidEmail(String email) {
        // Ekspresi reguler untuk memeriksa format email
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        return email.matches(emailPattern);
    }

    private void sentResetPassword(){
        String getEmail = etEmail.getText().toString();
        mAuth.sendPasswordResetEmail(getEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progresLay.setVisibility(View.GONE);
                            new AlertDialog.Builder(ForgotPassword.this)
                                    .setTitle("Link reset password terkirim")
                                    .setMessage("Periksa email anda !" +
                                    "\nperiksa juga pada folder spam.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            onBackPressed();
                                        }
                                    })
                                    .show();
                        }else {
                            progresLay.setVisibility(View.GONE);
                            Toast.makeText(ForgotPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}