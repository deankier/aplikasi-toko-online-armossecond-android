package id.nerdcreative.armossecond.userfragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.nerdcreative.armossecond.authactivity.LoginActivity;
import id.nerdcreative.armossecond.R;
import id.nerdcreative.armossecond.useractivity.EditProfilActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfilFragment newInstance(String param1, String param2) {
        UserProfilFragment fragment = new UserProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View v;
    CardView btnLogout, btnProfil;
    ImageView ivProfil;
    TextView tvNama, tvEmail;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_user_profil, container, false);

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = v.findViewById(R.id.adViewProfil);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        ivProfil = v.findViewById(R.id.iv_user_profil);
        tvNama = v.findViewById(R.id.tv_nama_profil);
        tvEmail = v.findViewById(R.id.tv_email_profil);
        userID = mUser.getUid();

        GetSetDataUser(userID);

        btnProfil = v.findViewById(R.id.btn_profil);
        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfilActivity.class));
            }
        });

        btnLogout = v.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Yakin ingin loogout?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();

            }
        });

        return v;
    }

    private void GetSetDataUser(String ID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("DataUser");;
        databaseReference.child(ID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        //Get Data
                        DataSnapshot dataSnapshot = task.getResult();
                        String getNama = String.valueOf(dataSnapshot.child("dataNama").getValue());
                        String getEmail = String.valueOf(dataSnapshot.child("dataEmail").getValue());
                        String getFoto = String.valueOf(dataSnapshot.child("dataFoto").getValue());
                        //Set data
                        tvNama.setText(getNama);
                        tvEmail.setText(getEmail);
                        if (getFoto.isBlank()){
                            // ekspresi jika foto masih kosong
                        }else {
                            Glide.with(UserProfilFragment.this).load(getFoto.trim()).into(ivProfil);

                        }
                    }else {
                        // Lakukan jika data tidak ketemu
                    }
                }
            }
        });
    }

}