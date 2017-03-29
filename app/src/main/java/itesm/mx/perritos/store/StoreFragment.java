package itesm.mx.perritos.store;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

import itesm.mx.perritos.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends ListFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OnProductSelectedListener onProductSelectedListener;
    private FloatingActionButton floatingActionButton;


    public StoreFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<Product> products = new ArrayList<Product>();

        Product product = new Product("Nombre del producto", 74, R.mipmap.ic_launcher);
        products.add(product);
        ProductAdapter productAdapter = new ProductAdapter(getActivity(), products);
        setListAdapter(productAdapter);

        View view = inflater.inflate(R.layout.fragment_store, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_add);
        floatingActionButton.setOnClickListener(this);

        return view;
    }

   @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        /*Product product = new Product("Nombre del producto", 74, R.mipmap.ic_launcher);
        onProductSelectedListener.onProductSelectedListener(product);*/
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AddProductActivity.class);
        startActivity(intent);
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnProductSelectedListener){
            onProductSelectedListener = (OnProductSelectedListener) context;
        }else{
            throw new RuntimeException(context.toString()
            + " must implement onProductSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onProductSelectedListener = null;
    }*/

    public interface OnProductSelectedListener{
        void onProductSelectedListener(Product product);
    }
}
