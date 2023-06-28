package id.nerdcreative.armossecond.userfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import id.nerdcreative.armossecond.Produk;
import id.nerdcreative.armossecond.R;
import id.nerdcreative.armossecond.adminfragment.ProdukFragment;
import id.nerdcreative.armossecond.useractivity.GridAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHomeFragment newInstance(String param1, String param2) {
        UserHomeFragment fragment = new UserHomeFragment();
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

    private View v;
    GridView gridView;
    TextView tvJudul, tvHarga;
    String idProduk;
    ArrayList<Produk> produks;
    GridAdapter gridAdapter;
    DatabaseReference dbReference;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_home, container, false);

        gridView = v.findViewById(R.id.grid_produk_view);
        dbReference = FirebaseDatabase.getInstance().getReference("Produk");
        tvHarga = v.findViewById(R.id.tv_harga_grid);
        tvJudul = v.findViewById(R.id.tv_judul_grid);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_user_main);
        progressBar = v.findViewById(R.id.progress_produk);
        progressBar.setVisibility(View.VISIBLE);

        produks = new ArrayList<>();
        gridAdapter = new GridAdapter(produks, getActivity());
        gridView.setAdapter(gridAdapter);

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Produk produk = dataSnapshot.getValue(Produk.class);
                    produks.add(produk);
                }
                gridAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_user_frame, new UserHomeFragment()).commit();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }
}