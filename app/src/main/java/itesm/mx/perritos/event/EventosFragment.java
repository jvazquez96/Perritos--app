package itesm.mx.perritos.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import itesm.mx.perritos.R;
import itesm.mx.perritos.news.News;

import static android.app.Activity.RESULT_OK;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import itesm.mx.perritos.R;


public class EventosFragment extends ListFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnEventSelectedListener mListenerEventSelected;


    private FloatingActionButton floatingActionButton;

    private ArrayList<Evento> EventoList;
    private ArrayAdapter<Evento> eventsAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEventsDatabaseReference;
    private ChildEventListener mChildEventListenerEventos;

    public EventosFragment() {
        // Required empty public constructor
    }

    /**
     *
     *
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventosFragment newInstance(String param1, String param2) {
        EventosFragment fragment = new EventosFragment();
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
        mEventsDatabaseReference = mFirebaseDatabase.getReference().child("Event");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        EventoList = new ArrayList<>();
        eventsAdapter = new EventoAdapter(getActivity(),EventoList);
        setListAdapter(eventsAdapter);

        View view  = inflater.inflate(R.layout.fragment_eventos, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_add);
        floatingActionButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Evento event = new Evento("Titulo","Descripcion",R.mipmap.ic_launcher);
        mListenerEventSelected.onEventSelectedListener(event);
    }

    @Override
    public void onClick(View v) {
        Intent startAddEventActivity = new Intent(getActivity(),AddEventActivity.class);
        startActivity(startAddEventActivity);
    }


        @Override
        public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventSelectedListener) {
            mListenerEventSelected = (OnEventSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onEventSelectedListener");
        }
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListenerEventos  == null) {
            mChildEventListenerEventos = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Evento Event1 = dataSnapshot.getValue(Evento.class);
                    if (Event1 != null) {
                        EventoList.add(Event1);
                    }
                    eventsAdapter.notifyDataSetChanged();
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
        mEventsDatabaseReference.addChildEventListener(mChildEventListenerEventos);
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerEventSelected = null;
    }



    public interface OnEventSelectedListener {
        void onEventSelectedListener(Evento event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Evento evento1 = (Evento) extras.get("Event");
                mEventsDatabaseReference.push().setValue(evento1);
        }
    }
}
