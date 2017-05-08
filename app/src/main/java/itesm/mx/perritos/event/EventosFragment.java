package itesm.mx.perritos.event;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

import itesm.mx.perritos.R;
import itesm.mx.perritos.news.News;
import itesm.mx.perritos.news.NewsAdapter;
import itesm.mx.perritos.pet.Pet;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class EventosFragment extends ListFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_ADD_EVENT = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnEventSelectedListener mListenerEventSelected;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEventsDataBaseReference;
    private ChildEventListener mChildEventListenerEventos;

    private FloatingActionButton floatingActionButton;
    private ArrayList<Evento> userEvents;
    private ArrayList<Evento> adminEvents;
    private ArrayList<Evento> favoritesEvents;
    private ArrayAdapter<Evento> eventsAdapter;
    private boolean isAdmin = true;
    private boolean isFavoritesOn;
    private  String editKey;
    private String userEmail;
    public EventosFragment() {
        // Required empty public constructor
        isFavoritesOn = false;
    }

    public void updateEvent(Evento evento, boolean isDeleted) {
        if (isDeleted) {
            mEventsDataBaseReference.child(editKey).removeValue();
        } else {
            mEventsDataBaseReference.child(editKey).setValue(evento);
        }
    }

    public void setAdmin(boolean isAdmin, Context context, String user) {
        this.isAdmin = isAdmin;
        this.userEmail = user;
        /*if (isAdmin) {
            adminEvents = new ArrayList<Evento>();
            eventsAdapter = new EventoAdapter(context,adminEvents);
            if (floatingActionButton != null) {
                floatingActionButton.setVisibility(View.VISIBLE);
            }
            if (getView() != null) {
                getListView().setOnItemLongClickListener(this);
            }
        } else {
            userEvents = new ArrayList<Evento>();
            eventsAdapter = new EventoAdapter(context,userEvents);
            if (floatingActionButton != null) {
                floatingActionButton.setVisibility(View.INVISIBLE);
            }
            if (getView() != null) {
                getListView().setOnItemLongClickListener(null);
            }
        }
        setListAdapter(eventsAdapter);*/
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEventsDataBaseReference = mFirebaseDatabase.getReference().child("Events");
    }

    @Override
    public void onResume() {
        super.onResume();
        favoritesEvents = new ArrayList<Evento>();
        if (isAdmin) {
            adminEvents = new ArrayList<Evento>();
            eventsAdapter = new EventoAdapter(getActivity(),adminEvents);
            floatingActionButton.setVisibility(View.VISIBLE);
            getListView().setOnItemLongClickListener(this);
        } else {
            userEvents = new ArrayList<Evento>();
            eventsAdapter = new EventoAdapter(getActivity(),userEvents);
            floatingActionButton.setVisibility(View.INVISIBLE);
            getListView().setOnItemLongClickListener(null);
        }

        if(isFavoritesOn){
            eventsAdapter = new EventoAdapter(getActivity(), favoritesEvents);
            getListView().setOnItemLongClickListener(this);
        }
        setListAdapter(eventsAdapter);
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        deattachDatabaseReadListener();
        eventsAdapter.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        floatingActionButton.setVisibility(View.INVISIBLE);
    }




    private void attachDatabaseReadListener() {
        if (mChildEventListenerEventos  == null) {
            mChildEventListenerEventos = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Evento evento = dataSnapshot.getValue(Evento.class);
                    evento.setKey(dataSnapshot.getKey());
                    if (isAdmin) {
                        adminEvents.add(evento);
                        if(evento.isUserInList(userEmail))
                            favoritesEvents.add(evento);
                    } else {
                        if (evento.getLugarVisible()) {
                            userEvents.add(evento);
                            if(evento.isUserInList(userEmail))
                                favoritesEvents.add(evento);
                        }
                    }
                    eventsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d("DEBUG_TAG","Child Changed");
                    Evento editEvent = dataSnapshot.getValue(Evento.class);
                    if (isAdmin) {
                        for (int i = 0; i < adminEvents.size(); ++i) {
                            if (adminEvents.get(i).getKey().equals(editEvent.getKey())) {
                                adminEvents.set(i, editEvent);
                            }
                        }
                    } else {
                        if (editEvent.getLugarVisible()) {
                            boolean exist = false;
                            for (int i = 0; i < userEvents.size(); ++i) {
                                if (userEvents.get(i).getKey().equals(editEvent.getKey())) {
                                    userEvents.set(i, editEvent);
                                    exist = true;
                                }
                            }
                            if (!exist) {
                                userEvents.add(editEvent);
                            }
                        }
                    }
                    eventsAdapter.notifyDataSetChanged();
                }


                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Evento removedEvent = dataSnapshot.getValue(Evento.class);
                    if (isAdmin) {
                        adminEvents.remove(removedEvent);
                    } else {
                        userEvents.remove(removedEvent);
                    }
                    eventsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
        mEventsDataBaseReference.addChildEventListener(mChildEventListenerEventos);
    }


    private void deattachDatabaseReadListener() {
        if (mChildEventListenerEventos != null) {
            mEventsDataBaseReference.removeEventListener(mChildEventListenerEventos);
        }
    }


    public void onClick(View v) {
        Intent startAddEventActivity = new Intent(getActivity(),AddEventActivity.class);
        startActivityForResult(startAddEventActivity,REQUEST_CODE_ADD_EVENT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_EVENT) {
                Bundle extras = data.getExtras();
                Evento evento = (Evento) extras.get("Event");
                mEventsDataBaseReference.push().setValue(evento);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_eventos, container, false);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_add);
        floatingActionButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(this);
    }



    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Evento evento;
        if(isFavoritesOn){
            evento = favoritesEvents.get(position);
        }else {
            evento = adminEvents.get(position);
        }
        mListenerEventSelected.onEventSelectedListener(evento , true);
        editKey = evento.getKey();
        return true;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListenerEventSelected = null;
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Evento evento;

        if(isFavoritesOn){
            evento = favoritesEvents.get(position);
        }else if (isAdmin) {
            evento = adminEvents.get(position);
        } else {
            evento = userEvents.get(position);
        }
        editKey = evento.getKey();
        mListenerEventSelected.onEventSelectedListener(evento,false);
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

    public interface OnEventSelectedListener {
        void onEventSelectedListener(Evento event, boolean isEditing);
    }

    public void filterFavorites(String user){
        this.isFavoritesOn = true;
        favoritesEvents = new ArrayList<Evento>();

        if(isAdmin){
            for(int i = 0; i < adminEvents.size(); ++i){
                if(adminEvents.get(i).isUserInList(user))
                    favoritesEvents.add(adminEvents.get(i));
            }
        }else{
            for(int i = 0; i < userEvents.size(); ++i){
                if(userEvents.get(i).isUserInList(user))
                    favoritesEvents.add(userEvents.get(i));
            }
        }
        eventsAdapter = new EventoAdapter(getContext(), favoritesEvents);
        setListAdapter(eventsAdapter);
    }

    public void setFavoritesOff(){
        this.isFavoritesOn = false;
        if(isAdmin)
            eventsAdapter = new EventoAdapter(getContext(), adminEvents);
        else
            eventsAdapter = new EventoAdapter(getContext(), userEvents);
        setListAdapter(eventsAdapter);
        favoritesEvents.clear();
    }

}