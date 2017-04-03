package itesm.mx.perritos.pet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import itesm.mx.perritos.R;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PetFragment extends ListFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DEBUG_TAG = "DEBUG_TAG";

    private FloatingActionButton floatingAddButton;
    private CoordinatorLayout coordinatorLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnPetSelectedListener mListenerPetSelected;

    private ArrayList<Pet> pets;
    private ArrayAdapter<Pet> petAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPetsDataBaseReference;
    private ChildEventListener mChildEventListenerPets;


    private static final int REQUEST_CODE_ADD_PET = 1;


    public PetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPetsDataBaseReference = mFirebaseDatabase.getReference().child("Pets");
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
        Log.d("DEBUG_TAG","onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Debug_tag","onPause");
        deattachDatabaseReadListener();
        petAdapter.clear();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListenerPets == null) {
            Log.d(DEBUG_TAG,"Listener is null");
            mChildEventListenerPets = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Pet pet = dataSnapshot.getValue(Pet.class);
                    pets.add(pet);
                    Log.d("DEBUG_TAG","NEW PET ADDED!!!!!");
                    petAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
        mPetsDataBaseReference.addChildEventListener(mChildEventListenerPets);
    }

    private void deattachDatabaseReadListener() {
        if (mChildEventListenerPets != null) {
            mPetsDataBaseReference.removeEventListener(mChildEventListenerPets);
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("DEBUG_TAG","CLICK");
        Intent startAddPetActivity = new Intent(getActivity(),AddPetActivity.class);
        startActivityForResult(startAddPetActivity,REQUEST_CODE_ADD_PET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_PET) {
                Log.d("DEBUG_TAG","Pet added");
                Bundle extras = data.getExtras();
                Pet pet = (Pet) extras.get("Pet");
                mPetsDataBaseReference.push().setValue(pet);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pets = new ArrayList<>();
        petAdapter = new PetAdapter(getActivity(), pets);
        setListAdapter(petAdapter);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        
        floatingAddButton = (FloatingActionButton) view.findViewById(R.id.floating_add);
        floatingAddButton.setOnClickListener(this);

        Log.d("DEBUG_TAG","onCreateView");

        if (mPetsDataBaseReference  ==  null) {
            Log.d("DEBUG_TAG","THIS THING IS NULL");
        }

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListenerPetSelected = null;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("POSITION: ", String.valueOf(position));
        Pet pet = pets.get(position);
        mListenerPetSelected.onPetSelectedListener(pet);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  OnPetSelectedListener) {
            mListenerPetSelected = (OnPetSelectedListener) context;

        } else {
            throw new RuntimeException(context.toString() + " must implement OnPetSelectedListener && OnPetAddedListener");
        }
    }

    /**
     * This interface must be implemented by activities that contains this fragment to allow
     * interaction in this fragment to be communicated to the activity
     */
    public interface OnPetSelectedListener {
        void onPetSelectedListener(Pet pet);
    }
}
