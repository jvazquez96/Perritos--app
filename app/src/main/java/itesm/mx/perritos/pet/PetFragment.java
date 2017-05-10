package itesm.mx.perritos.pet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.DeniedByServerException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import itesm.mx.perritos.R;

import static android.app.Activity.RESULT_OK;


public class PetFragment extends ListFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {
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

    private ArrayList<Pet> adminPets;
    private ArrayList<Pet> userPets;
    private ArrayList<Pet> requestedPets;
    private ArrayList<Pet> favoritesPets;
    private ArrayAdapter<Pet> petAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPetsDataBaseReference;
    private ChildEventListener mChildEventListenerPets;

    private static final int REQUEST_CODE_ADD_PET = 1;

    private String editKey;

    private boolean isAdmin;

    private boolean isFilterOn;
    private boolean isFavoritesOn;
    private String user;


    public PetFragment() {
        // Required empty public constructor
        isFilterOn = false;
        isFavoritesOn = false;
    }

    public void updatePet(Pet pet, boolean isDeleted) {
        if (isDeleted) {
            mPetsDataBaseReference.child(editKey).removeValue();
        } else {
            mPetsDataBaseReference.child(editKey).setValue(pet);
        }
    }

    public void setAdmin(boolean isAdmin, Context context, String user) {
        Log.d("DEBUG", "setAdmin");
        this.user = user;
        Log.d(DEBUG_TAG,"PEEEEEROOOOO: " + user);
        this.isAdmin = isAdmin;
        requestedPets = new ArrayList<Pet>();
        favoritesPets = new ArrayList<Pet>();
        if (isAdmin) {
            adminPets = new ArrayList<Pet>();
            petAdapter = new PetAdapter(context,adminPets);
            if (floatingAddButton != null) {
                floatingAddButton.setVisibility(View.VISIBLE);
            }
            if (getView() != null) {
                getListView().setOnItemLongClickListener(this);
            }
        } else{
            userPets = new ArrayList<Pet>();
            petAdapter = new PetAdapter(context,userPets);
            if (floatingAddButton != null) {
                floatingAddButton.setVisibility(View.INVISIBLE);
            }
            if (getView() != null) {
                getListView().setOnItemLongClickListener(null);
            }
        }

        if(isFilterOn) {
            petAdapter = new PetAdapter(context, requestedPets);
            if(isAdmin) {
                if (getView() != null) {
                    getListView().setOnItemLongClickListener(this);
                }
            }
        }

        if(isFavoritesOn) {
            petAdapter = new PetAdapter(context, favoritesPets);
            if(isAdmin) {
                if (getView() != null) {
                    getListView().setOnItemLongClickListener(this);
                }
            }
        }

        setListAdapter(petAdapter);
        setListAdapter(petAdapter);
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Pet pet;
        if(isFilterOn){
            pet = requestedPets.get(position);
        }else if(isFavoritesOn){
            pet = favoritesPets.get(position);
        }else{
            pet = adminPets.get(position);
        }
        mListenerPetSelected.onPetSelectedListener(pet, true);
        editKey = pet.getKey();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestedPets = new ArrayList<Pet>();
        favoritesPets = new ArrayList<Pet>();
        if (isAdmin) {
            adminPets = new ArrayList<Pet>();
            petAdapter = new PetAdapter(getActivity(),adminPets);
            floatingAddButton.setVisibility(View.VISIBLE);
            getListView().setOnItemLongClickListener(this);
        } else{
            userPets = new ArrayList<Pet>();
            petAdapter = new PetAdapter(getActivity(),userPets);
            floatingAddButton.setVisibility(View.INVISIBLE);
            getListView().setOnItemLongClickListener(null);
        }

        if(isFilterOn) {
            petAdapter = new PetAdapter(getActivity(), requestedPets);
            if(isAdmin)
                getListView().setOnItemLongClickListener(this);
        }

        if(isFavoritesOn) {
            petAdapter = new PetAdapter(getActivity(), favoritesPets);
            if(isAdmin)
                getListView().setOnItemLongClickListener(this);
        }

        setListAdapter(petAdapter);
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        deattachDatabaseReadListener();
        petAdapter.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        floatingAddButton.setVisibility(View.INVISIBLE);
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListenerPets == null) {
            mChildEventListenerPets = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Pet pet = dataSnapshot.getValue(Pet.class);
                    pet.setKey(dataSnapshot.getKey());
                    if (isAdmin) {
                        adminPets.add(pet);

                        if(pet.isUserInRequestList(user))
                            requestedPets.add(pet);

                        if(pet.isUserInList(user))
                            favoritesPets.add(pet);
                    } else {
                        if (pet.getIsVisible()) {

                            userPets.add(pet);

                            if(pet.isUserInRequestList(user))
                                requestedPets.add(pet);

                            if(pet.isUserInList(user))
                                favoritesPets.add(pet);
                        }
                    }

                    petAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Pet editPet = dataSnapshot.getValue(Pet.class);
                    if (isAdmin) {
                        for (int i = 0; i < adminPets.size(); ++i) {
                            if (adminPets.get(i).getKey().equals(editPet.getKey())) {
                                adminPets.set(i, editPet);
                            }
                        }
                    } else {
                        if (editPet.getIsVisible()) {
                            boolean exist = false;
                            for (int i = 0; i < userPets.size(); ++i) {
                                if (userPets.get(i).getKey().equals(editPet.getKey())) {
                                    userPets.set(i, editPet);
                                    exist = true;
                                }
                            }
                            if (!exist) {
                                userPets.add(editPet);
                            }
                        }
                    }
                    petAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Pet removedPet = dataSnapshot.getValue(Pet.class);
                    if (isAdmin) {
                        adminPets.remove(removedPet);
                    } else {
                        userPets.remove(removedPet);
                    }
                    petAdapter.notifyDataSetChanged();
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
        Intent startAddPetActivity = new Intent(getActivity(),AddPetActivity.class);
        startActivityForResult(startAddPetActivity,REQUEST_CODE_ADD_PET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_PET) {
                Bundle extras = data.getExtras();
                Pet pet = (Pet) extras.get("Pet");
                Log.d(DEBUG_TAG,"Pet fav status: " + pet.getFav());
                mPetsDataBaseReference.push().setValue(pet);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        
        floatingAddButton = (FloatingActionButton) view.findViewById(R.id.floating_add);
        floatingAddButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerPetSelected = null;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Pet pet;

        if(isFilterOn){
            pet = requestedPets.get(position);
        }else if (isFavoritesOn){
            pet = favoritesPets.get(position);
        }else if (isAdmin) {
            pet = adminPets.get(position);
        } else  {
            pet = userPets.get(position);
        }
        editKey = pet.getKey();
        mListenerPetSelected.onPetSelectedListener(pet,false);
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
        void onPetSelectedListener(Pet pet, boolean isEditing);
    }

    public void filterPets(String user){
        this.isFilterOn = true;
        this.isFavoritesOn = false;
        requestedPets = new ArrayList<Pet>();

        if(isAdmin){
            for(int i = 0; i < adminPets.size(); ++i){
                if(adminPets.get(i).isUserInRequestList(user))
                    requestedPets.add(adminPets.get(i));
            }
        }else{
            for(int i = 0; i < userPets.size(); ++i){
                if(userPets.get(i).isUserInRequestList(user))
                    requestedPets.add(userPets.get(i));
            }
        }

        Log.d("PET", Integer.toString(requestedPets.size()));
        petAdapter = new PetAdapter(getContext(), requestedPets);
        setListAdapter(petAdapter);
    }

    public void filterFavorites(String user){
        this.isFavoritesOn = true;
        this.isFilterOn = false;
        favoritesPets = new ArrayList<Pet>();

        if(isAdmin){
            for(int i = 0; i < adminPets.size(); ++i){
                if(adminPets.get(i).isUserInList(user))
                    favoritesPets.add(adminPets.get(i));
            }
        }else{
            for(int i = 0; i < userPets.size(); ++i){
                if(userPets.get(i).isUserInList(user))
                    favoritesPets.add(userPets.get(i));
            }
        }
        petAdapter = new PetAdapter(getContext(), favoritesPets);
        setListAdapter(petAdapter);
    }

    public void setIsFilterOnOff(){
        this.isFilterOn = false;
        this.isFavoritesOn = false;
        if(isAdmin)
            petAdapter = new PetAdapter(getContext(), adminPets);
        else
            petAdapter = new PetAdapter(getContext(), userPets);
        setListAdapter(petAdapter);
        requestedPets.clear();
    }
}
