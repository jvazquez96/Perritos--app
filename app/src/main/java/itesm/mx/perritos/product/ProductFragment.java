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
import android.widget.AdapterView;
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
public class ProductFragment extends ListFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {
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

    private ArrayList<Product> adminProducts;
    private ArrayList<Product> userProducts;
    private ArrayAdapter<Product> productAdapter;

    private String editKey;

    private boolean isAdmin;

    public ProductFragment() {
        // Required empty public constructor
    }

    public void updateProduct(Product product, boolean isDeleted) {
        if (isDeleted) {
            mProductsDatabaseReference.child(editKey).removeValue();
        } else {
            mProductsDatabaseReference.child(editKey).setValue(product);
        }
    }

    public void setAdmin(boolean isAdmin, Context context) {
        this.isAdmin = isAdmin;
        if (isAdmin) {
            adminProducts = new ArrayList<>();
            productAdapter  = new ProductAdapter(context,adminProducts);
            if (floatingActionButton != null) {
                floatingActionButton.setVisibility(View.VISIBLE);
            }
            if (getView() != null) {
                getListView().setOnItemLongClickListener(this);
            }
        } else {
            userProducts = new ArrayList<>();
            productAdapter = new ProductAdapter(context,userProducts);
            if (floatingActionButton != null) {
                floatingActionButton.setVisibility(View.INVISIBLE);
            }
            if (getView() != null) {
                getListView().setOnItemLongClickListener(null);
            }
        }
        setListAdapter(productAdapter);
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
        if (isAdmin) {
            adminProducts = new ArrayList<Product>();
            productAdapter  = new ProductAdapter(getActivity(),adminProducts);
            floatingActionButton.setVisibility(View.VISIBLE);
            getListView().setOnItemLongClickListener(this);
        } else {
            userProducts = new ArrayList<Product>();
            productAdapter = new ProductAdapter(getActivity(),userProducts);
            floatingActionButton.setVisibility(View.INVISIBLE);
            getListView().setOnItemLongClickListener(null);
        }
        setListAdapter(productAdapter);
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        deattachDatabaseReadListener();
        productAdapter.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        floatingActionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_add);
        floatingActionButton.setOnClickListener(this);

        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Product product = adminProducts.get(position);
        mListenerProductSelected.onProductSelectedListener(product, true);
        editKey = product.getKey();
        return true;
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListenerProducts == null) {
            mChildEventListenerProducts = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Product product1 = dataSnapshot.getValue(Product.class);
                    product1.setKey(dataSnapshot.getKey());
                    if (isAdmin) {
                        adminProducts.add(product1);
                    } else {
                        if (product1.getIsVisible()) {
                            userProducts.add(product1);
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Product editedProduct = dataSnapshot.getValue(Product.class);
                    if (isAdmin) {
                        for (int i = 0; i < adminProducts.size(); ++i) {
                            if (adminProducts.get(i).getKey().equals(editedProduct.getKey())) {
                                adminProducts.set(i, editedProduct);
                            }
                        }
                    } else {
                        if (editedProduct.getIsVisible()) {
                            boolean exist = false;
                            for (int i = 0; i < userProducts.size(); ++i) {
                                if (userProducts.get(i).getKey().equals(editedProduct.getKey())) {
                                    userProducts.set(i, editedProduct);
                                    exist = true;
                                }
                            }
                            if (!exist) {
                                userProducts.add(editedProduct);
                            }
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Product removedProduct = dataSnapshot.getValue(Product.class);
                    if (isAdmin) {
                        adminProducts.remove(removedProduct);
                    } else  {
                        userProducts.remove(removedProduct);
                    }
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
       Product product1;
       if (isAdmin) {
           product1 = adminProducts.get(position);
       } else {
           product1 = userProducts.get(position);
       }
       editKey = product1.getKey();
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
