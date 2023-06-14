package id.nerdcreative.armossecond.adminfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import id.nerdcreative.armossecond.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PesananFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PesananFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View fragmentView;
    CardView btnBelumBayar, btnKonfirmasi, btnSelesai;
    LinearLayout bgBelumBayar, bgKonfirmasi, bgSelesai;

    public PesananFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PesananFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PesananFragment newInstance(String param1, String param2) {
        PesananFragment fragment = new PesananFragment();
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

    LinearLayout underBelumByr, underKonfirm, underSelesai;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_pesanan, container, false);

        btnBelumBayar = fragmentView.findViewById(R.id.btn_belumbayar_pesanan);
        btnKonfirmasi = fragmentView.findViewById(R.id.btn_konfirmasi_pesanan);
        btnSelesai = fragmentView.findViewById(R.id.btn_selesai_pesanan);
        bgBelumBayar = fragmentView.findViewById(R.id.bg_belumbayar);
        bgKonfirmasi = fragmentView.findViewById(R.id.bg_konfirmasi);
        bgSelesai = fragmentView.findViewById(R.id.bg_selesai);

        underBelumByr = fragmentView.findViewById(R.id.belumbayar_under);
        underKonfirm = fragmentView.findViewById(R.id.konfirmasi_under);
        underSelesai = fragmentView.findViewById(R.id.selesai_under);

        underBelumByr.setVisibility(View.VISIBLE);
        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                underKonfirm.setVisibility(View.VISIBLE);
                underBelumByr.setVisibility(View.INVISIBLE);
                underSelesai.setVisibility(View.INVISIBLE);
            }
        });
        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                underSelesai.setVisibility(View.VISIBLE);
                underBelumByr.setVisibility(View.INVISIBLE);
                underKonfirm.setVisibility(View.INVISIBLE);
            }
        });
        btnBelumBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                underBelumByr.setVisibility(View.VISIBLE);
                underKonfirm.setVisibility(View.INVISIBLE);
                underSelesai.setVisibility(View.INVISIBLE);
            }
        });



        return fragmentView;
    }
}