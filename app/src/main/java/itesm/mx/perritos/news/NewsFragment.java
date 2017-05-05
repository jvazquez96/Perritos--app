package itesm.mx.perritos.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
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
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends ListFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final int REQUEST_CODE_ADD_NEWS = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnNewsSelectedListener mListenerNewsSelected;

    private FloatingActionButton floatingAddButon;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mNewsDatabaseReference;
    private ChildEventListener mChildEventListenerNews;

    private ArrayList<News> userNews;
    private ArrayList<News> adminNews;
    private ArrayAdapter<News> newsAdapter;

    private String editKey;

    private boolean isAdmin;


    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateNews(News news1, boolean isDeleted) { // flata
        if (isDeleted) {
            mNewsDatabaseReference.child(editKey).removeValue();
        } else {
            mNewsDatabaseReference.child(editKey).setValue(news1);
        }
    }

    public void setAdmin(boolean isAdmin, Context context) { // falta
        this.isAdmin = isAdmin;
        if (isAdmin) {
            adminNews = new ArrayList<>();
            newsAdapter = new NewsAdapter(context,adminNews);
            if (floatingAddButon != null) {
                floatingAddButon.setVisibility(View.VISIBLE);
            }
            if (getView() != null) {
                getListView().setOnItemLongClickListener(this);
            }
        } else {
            userNews = new ArrayList<>();
            newsAdapter = new NewsAdapter(context,userNews);
            if (floatingAddButon != null) {
                floatingAddButon.setVisibility(View.INVISIBLE);
            }
            if (getView() != null) {
                getListView().setOnItemLongClickListener(null);
            }
        }
        setListAdapter(newsAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mNewsDatabaseReference = mFirebaseDatabase.getReference().child("News");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) { // falta
        News news1 = adminNews.get(position);
        mListenerNewsSelected.onNewsSelectedListener(news1, true);
        editKey = news1.getKey();
        return true;
    }

    @Override
    public void onResume() { // falta
        super.onResume();
        if (isAdmin) {
            adminNews = new ArrayList<>();
            newsAdapter = new NewsAdapter(getActivity(),adminNews);
            floatingAddButon.setVisibility(View.VISIBLE);
            getListView().setOnItemLongClickListener(this);
        } else {
            userNews = new ArrayList<>();
            newsAdapter = new NewsAdapter(getActivity(),userNews);
            floatingAddButon.setVisibility(View.INVISIBLE);
            getListView().setOnItemLongClickListener(null);
        }
        setListAdapter(newsAdapter);
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        deattachDatabaseReadListener();
        newsAdapter.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        floatingAddButon.setVisibility(View.INVISIBLE);
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListenerNews  == null) {
            mChildEventListenerNews = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    News news1 = dataSnapshot.getValue(News.class);
                    news1.setKey(dataSnapshot.getKey());
                    if (isAdmin) {
                        adminNews.add(news1);
                    } else {
                        if (news1.getIsVisible()) {
                            userNews.add(news1);
                        }
                    }
                    newsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d("DEBUG_TAG","Child Changed");
                    News editNews = dataSnapshot.getValue(News.class);
                    if (isAdmin) {
                        for (int i = 0; i < adminNews.size(); ++i) {
                            if (adminNews.get(i).getKey().equals(editNews.getKey())) {
                                adminNews.set(i, editNews);
                            }
                        }
                    } else {
                        if (editNews.getIsVisible()) {
                            boolean exist = false;
                            for (int i = 0; i < userNews.size(); ++i) {
                                if (userNews.get(i).getKey().equals(editNews.getKey())) {
                                    userNews.set(i, editNews);
                                    exist = true;
                                }
                            }
                            if (!exist) {
                                userNews.add(editNews);
                            }
                        }
                    }
                    newsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    News removedNews = dataSnapshot.getValue(News.class);
                    if (isAdmin) {
                        adminNews.remove(removedNews);
                    } else {
                        userNews.remove(removedNews);
                    }
                    newsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
        mNewsDatabaseReference.addChildEventListener(mChildEventListenerNews);
    }

    private void deattachDatabaseReadListener() {
        if (mChildEventListenerNews != null) {
            mNewsDatabaseReference.removeEventListener(mChildEventListenerNews);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        floatingAddButon = (FloatingActionButton) view.findViewById(R.id.floating_add);
        floatingAddButon.setOnClickListener(this);
        return view;

    }


    @Override
    public void onClick(View v) {
        Intent startAddNewsActivity = new Intent(getActivity(),AddNewsActivity.class);
        startActivityForResult(startAddNewsActivity,REQUEST_CODE_ADD_NEWS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_NEWS) {
                Bundle extras = data.getExtras();
                News news1 = (News) extras.get("News");
                mNewsDatabaseReference.push().setValue(news1);
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        News news1;
        if (isAdmin) {
            news1 = adminNews.get(position);
        } else {
            news1 = userNews.get(position);
        }
        mListenerNewsSelected.onNewsSelectedListener(news1,false);
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
        void onNewsSelectedListener(News news, boolean isEditing);
    }
}
