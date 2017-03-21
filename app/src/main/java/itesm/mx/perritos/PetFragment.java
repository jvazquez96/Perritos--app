package itesm.mx.perritos;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;


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

    private OnPetSelectedListener mListener;

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
        Button btn = (Button) customView.findViewById(R.id.button_done);
        // When the button is pressed dismiss the view.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Light the background
                coordinatorLayout.getForeground().setAlpha(0);
                mPopupWindow.dismiss();
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

        ArrayList<Pet> pets = new ArrayList<>();
        Pet pet = new Pet("Oliver","M",1,"Bonito",0, R.mipmap.ic_launcher);
        pets.add(pet);
        PetAdapter petAdapter = new PetAdapter(getActivity(), pets);
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
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("POSITION: ", String.valueOf(position));
        Pet pet = new Pet("Oliver","M",1,"Bonito",0, R.mipmap.ic_launcher);
        mListener.onPetSelectedListener(pet);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  OnPetSelectedListener) {
            mListener = (OnPetSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPetSelectedListener");
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
