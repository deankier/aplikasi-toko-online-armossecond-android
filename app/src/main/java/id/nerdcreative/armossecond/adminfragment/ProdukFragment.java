package id.nerdcreative.armossecond.adminfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import id.nerdcreative.armossecond.Produk;
import id.nerdcreative.armossecond.R;
import id.nerdcreative.armossecond.adminactivity.ProdukAdapter;
import id.nerdcreative.armossecond.adminactivity.TambahProdukActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProdukFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProdukFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View fragmentView;

    CardView btnTambahProduk;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ProdukAdapter produkAdapter;
    ArrayList<Produk> produks;
    ProgressBar progressBar;
    public ProdukFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProdukFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProdukFragment newInstance(String param1, String param2) {
        ProdukFragment fragment = new ProdukFragment();
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

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_produk, container, false);
        progressBar = fragmentView.findViewById(R.id.progress_produk);
        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout = fragmentView.findViewById(R.id.swipe_refresh_produk);

        btnTambahProduk = fragmentView.findViewById(R.id.tambah_produk);
        btnTambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), TambahProdukActivity.class));
            }
        });

        recyclerView = fragmentView.findViewById(R.id.produk_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Produk");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        produks = new ArrayList<>();
        produkAdapter = new ProdukAdapter(getActivity(), produks);
        recyclerView.setAdapter(produkAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Produk produk = dataSnapshot.getValue(Produk.class);
                    produks.add(produk);
                }
                produkAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_admin_frame, new ProdukFragment()).commit();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return fragmentView;
    }


}