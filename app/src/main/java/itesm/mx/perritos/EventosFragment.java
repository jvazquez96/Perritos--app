package itesm.mx.perritos;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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



public class EventosFragment  extends ListFragment implements View.OnClickListener {
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

    private OnEventoSelectedListener mListener;

    public EventosFragment() {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Log.d("DEBUG_TAG","CLICK");

        // Inflate layout for the popup
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.addeventpopupwindow,null);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<Evento> eventos = new ArrayList<>();
        Evento evento = new Evento("Oliver","M",R.mipmap.ic_launcher);
        eventos.add(evento);
        EventoAdapter eventoAdapter = new EventoAdapter(getActivity(), eventos);
        setListAdapter(eventoAdapter);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventos, container, false);
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
        Evento evento = new Evento("Oliver","M",R.mipmap.ic_launcher);
        mListener.onEventoSelectedListener(evento);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  OnEventoSelectedListener) {
            mListener = (OnEventoSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEventoSelectedListener");
        }
    }

    /**
     * This interface must be implemented by activities that contains this fragment to allow
     * interaction in this fragment to be communicated to the activity
     */
    public interface OnEventoSelectedListener {
        void onEventoSelectedListener(Evento evento);
    }
}
