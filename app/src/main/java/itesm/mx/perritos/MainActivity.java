package itesm.mx.perritos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import itesm.mx.perritos.event.EventDetailActivity;
import itesm.mx.perritos.event.Evento;
import itesm.mx.perritos.event.EventosFragment;
import itesm.mx.perritos.news.News;
import itesm.mx.perritos.news.NewsDetailActivity;
import itesm.mx.perritos.news.NoticiasFragment;
import itesm.mx.perritos.pet.Pet;
import itesm.mx.perritos.pet.PetDetailActivity;
import itesm.mx.perritos.pet.PetFragment;
import itesm.mx.perritos.store.Product;
import itesm.mx.perritos.store.ProductDetailActivity;
import itesm.mx.perritos.store.StoreFragment;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
                                                                PetFragment.OnPetSelectedListener,
                                                                View.OnClickListener,
                                                                PetFragment.OnPetAddedListener,
                                                                NavigationView.OnNavigationItemSelectedListener,
                                                                EventosFragment.OnEventSelectedListener,
                                                                StoreFragment.OnProductSelectedListener,
                                                                NoticiasFragment.OnNewsSelectedListener{


    private TabLayout tlTabLayout;
    private Toolbar tbToolbar;
    private ImageButton imgbtnMenu;

    private final int [] ICON ={ R.drawable.ic_pets_black_24dp,
            R.drawable.ic_event_black_24dp,
            R.drawable.ic_new_releases_black_24dp,
            R.drawable.ic_store_black_24dp};

    private ViewPager vpViewPager;

    // Firebase Objects
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPetsDataBaseReference;
    private ChildEventListener mChildEventListenerPets;

    private static final String DEBUG_TAG = "DEBUG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);

        vpViewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewPager(vpViewPager);

        tlTabLayout = (TabLayout) findViewById(R.id.tabs);
        tlTabLayout.setupWithViewPager(vpViewPager);
        tlTabLayout.getTabAt(0).setIcon(ICON[0]);
        tlTabLayout.getTabAt(1).setIcon(ICON[1]);
        tlTabLayout.getTabAt(2).setIcon(ICON[2]);
        tlTabLayout.getTabAt(3).setIcon(ICON[3]);
        tlTabLayout.addOnTabSelectedListener(this);

        // Custom toolbar
        tbToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tbToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tbToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize Firebase Componentes
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mPetsDataBaseReference = mFirebaseDatabase.getReference().child("Pets");

        mChildEventListenerPets = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Pet pet = dataSnapshot.getValue(Pet.class);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
//
//        switch (v.getId()){
//            case R.id.button_menu:
//                Log.d(DEBUG_TAG,"Menu Button");
//                break;
//        }


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vpViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PetFragment());
        adapter.addFragment(new EventosFragment());
        adapter.addFragment(new NoticiasFragment());
        adapter.addFragment(new StoreFragment());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPetSelectedListener(Pet pet) {
        Log.d(DEBUG_TAG, "onPetSelectedListener");
        Bundle bundle = new Bundle();
        Intent petDetailIntent = new Intent(this, PetDetailActivity.class);
        bundle.putSerializable("Pet",pet);
        petDetailIntent.putExtras(bundle);
        startActivity(petDetailIntent);
    }

    @Override
    public void onPetAdded(Pet pet) {
        mPetsDataBaseReference.push().setValue(pet);
    }

    @Override
    public void onEventSelectedListener(Evento event) {
        Bundle bundle = new Bundle();
        Intent eventDetailIntent = new Intent(this,EventDetailActivity.class);
        startActivity(eventDetailIntent);
    }

    @Override
    public void onProductSelectedListener(Product product) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, ProductDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNewsSelectedListener(News news) {
        Intent newsDetailIntent = new Intent(this,NewsDetailActivity.class);
        startActivity(newsDetailIntent);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Log.d(DEBUG_TAG,"position: " + position);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
