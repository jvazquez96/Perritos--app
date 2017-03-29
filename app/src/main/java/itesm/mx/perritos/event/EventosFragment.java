package itesm.mx.perritos.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ArrayList<Evento> eventos = new ArrayList<>();
        Evento eventos1 = new Evento("Titulo","Descripcion", R.mipmap.ic_launcher);
        eventos.add(eventos1);
        EventoAdapter eventosAdapter = new EventoAdapter(getActivity(),eventos);
        setListAdapter(eventosAdapter);

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerEventSelected = null;
    }



    public interface OnEventSelectedListener {
        void onEventSelectedListener(Evento event);
    }
}