package itesm.mx.perritos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoticiasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoticiasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticiasFragment extends ListFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnNewsSelectedListener mListenerNewsSelected;

    private FloatingActionButton floatingAddButon;

    public NoticiasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoticiasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoticiasFragment newInstance(String param1, String param2) {
        NoticiasFragment fragment = new NoticiasFragment();
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

        ArrayList<News> news = new ArrayList<>();
        News news1 = new News("Titulo","Descripcion",R.mipmap.ic_launcher);
        news.add(news1);
        NewsAdapter newsAdapter = new NewsAdapter(getActivity(),news);
        setListAdapter(newsAdapter);

        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        floatingAddButon = (FloatingActionButton) view.findViewById(R.id.floating_add);
        floatingAddButon.setOnClickListener(this);

        return view;

    }


    @Override
    public void onClick(View v) {
        Intent startAddNewsActivity = new Intent(getActivity(),AddNewsActivity.class);
        startActivity(startAddNewsActivity);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        News news = new News("Titulo de noticia","Descripcion",R.mipmap.ic_launcher);
        mListenerNewsSelected.onNewsSelectedListener(news);
    }

        @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewsSelectedListener) {
            mListenerNewsSelected = (OnNewsSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onNewsSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerNewsSelected = null;
    }



    public interface OnNewsSelectedListener {
        void onNewsSelectedListener(News news);
    }
}
