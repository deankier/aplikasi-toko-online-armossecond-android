package id.nerdcreative.armossecond.adminfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import id.nerdcreative.armossecond.LoginActivity;
import id.nerdcreative.armossecond.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View fragmentView;
    ImageView btnShowMenunMainAdmin;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Produk");
    String totalProduk;
    TextView tvTotalProduk;
    CardView toProduk, toStokHabis, toProdukTerjual, toBelumBayar, toKonfirmasi, toChat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        tvTotalProduk = fragmentView.findViewById(R.id.total_produk);

        toProduk = fragmentView.findViewById(R.id.view_total_produk);
        toStokHabis = fragmentView.findViewById(R.id.view_stok);
        toProdukTerjual = fragmentView.findViewById(R.id.view_terjual);
        toBelumBayar = fragmentView.findViewById(R.id.view_belum_bayar);
        toKonfirmasi = fragmentView.findViewById(R.id.view_perlu_komfirmasi);
        toChat = fragmentView.findViewById(R.id.view_chat);

        toProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment produkFragment = new ProdukFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_admin_frame, produkFragment).commit();
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalProduk = String.valueOf(snapshot.getChildrenCount());
                tvTotalProduk.setText(totalProduk);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })  ;

        btnShowMenunMainAdmin = fragmentView.findViewById(R.id.show_menu_main_admin);
        btnShowMenunMainAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                // Inflate layout untuk popup menu
                popupMenu.getMenuInflater().inflate(R.menu.popup_main_admin, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Tangani klik pada item menu
                        switch (item.getItemId()) {
                            case R.id.mn_popup_logout:
                                FirebaseAuth.getInstance().signOut();
                                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                // Set style popup menu
                try {
                    Field fieldPopup = PopupMenu.class.getDeclaredField("mPopup");
                    fieldPopup.setAccessible(true);
                    Object menuPopupHelper = fieldPopup.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                popupMenu.show();

            }
        });

        return  fragmentView;
    }
}