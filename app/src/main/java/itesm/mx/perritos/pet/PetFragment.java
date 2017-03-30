package itesm.mx.perritos.pet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import itesm.mx.perritos.R;


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

    private FloatingActionButton floatingAddButton;
    private PopupWindow mPopupWindow;
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
        mPetsDataBaseReference.addChildEventListener(mChildEventListenerPets);
    }

    @Override
    public void onClick(View v) {
        Log.d("DEBUG_TAG","CLICK");

        // Inflate layout for the popup
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.addpetpopupwindow,null);
        mPopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Get the current layout
        LinearLayout layout = new LinearLayout(getContext());

        //  Reference the button from the pop up layout.
        ImageButton btn = (ImageButton) customView.findViewById(R.id.button_done);
        // When the button is pressed dismiss the view.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                // Light the background
                Pet pet = new Pet("Oliver","M",1,"Bonito",0,0);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pug);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                   bitmap.recycle();
                byte[] bytes = byteArrayOutputStream.toByteArray();
                String encodedImage = Base64.encodeToString(bytes,Base64.DEFAULT);
                pet.setEncodedImage(encodedImage);
                mPetsDataBaseReference.push().setValue(pet);
                coordinatorLayout.getForeground().setAlpha(0);

            }
        });
        // Position where the pop up is going to be displayed
        mPopupWindow.showAtLocation(layout, Gravity.CENTER_HORIZONTAL,10,10);
        // Dim the background
        coordinatorLayout.getForeground().setAlpha(200);
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
        // Light the background
        coordinatorLayout.getForeground().setAlpha(0);


        floatingAddButton = (FloatingActionButton) view.findViewById(R.id.floating_add);
        floatingAddButton.setOnClickListener(this);
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
