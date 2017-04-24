package itesm.mx.perritos.product;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import itesm.mx.perritos.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends ListFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OnProductSelectedListener mListenerProductSelected;

    private FloatingActionButton floatingActionButton;

    private static final int RC_ADD_PRODUCT = 1;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProductsDatabaseReference;
    private ChildEventListener mChildEventListenerProducts;

    private ArrayList<Product> products;
    private ArrayAdapter<Product> productAdapter;


    public ProductFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProductsDatabaseReference = mFirebaseDatabase.getReference().child("Products");
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        deattachDatabaseReadListener();
        productAdapter.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Product product = new Product("Nombre del producto", 74, R.mipmap.ic_launcher);
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(), products);
        setListAdapter(productAdapter);

        View view = inflater.inflate(R.layout.fragment_store, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_add);
        floatingActionButton.setOnClickListener(this);

        return view;
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListenerProducts == null) {
            mChildEventListenerProducts = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Product product1 = dataSnapshot.getValue(Product.class);
                    product1.setKey(dataSnapshot.getKey());
                    products.add(product1);
                    productAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Product editedProduct = dataSnapshot.getValue(Product.class);
                    for (int i = 0; i < products.size(); ++i) {
                        if (products.get(i).getKey().equals(editedProduct.getKey())) {
                            products.set(i,editedProduct);
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Product removedProduct = dataSnapshot.getValue(Product.class);
                    products.remove(removedProduct);
                    productAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
        mProductsDatabaseReference.addChildEventListener(mChildEventListenerProducts);
    }

    private void deattachDatabaseReadListener() {
        if (mChildEventListenerProducts != null) {
            mProductsDatabaseReference.removeEventListener(mChildEventListenerProducts);
        }
    }

   @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
       super.onListItemClick(l, v, position, id);
       Product product1 = products.get(position);
       mListenerProductSelected.onProductSelectedListener(product1,false);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AddProductActivity.class);
        startActivityForResult(intent,RC_ADD_PRODUCT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_ADD_PRODUCT) {
                Bundle extras = data.getExtras();
                Product product1 = (Product) extras.get("Product");
                mProductsDatabaseReference.push().setValue(product1);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnProductSelectedListener){
            mListenerProductSelected = (OnProductSelectedListener) context;
        }else{
            throw new RuntimeException(context.toString()
            + " must implement onProductSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerProductSelected = null;
    }

    public interface OnProductSelectedListener{
        void onProductSelectedListener(Product product, boolean isEditing);
    }
}
