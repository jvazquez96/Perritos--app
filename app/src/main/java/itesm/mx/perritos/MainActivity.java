package itesm.mx.perritos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import itesm.mx.perritos.User.User;
import itesm.mx.perritos.Utils.CurrentUser;
import itesm.mx.perritos.event.AddEventActivity;
import itesm.mx.perritos.event.EventDetailActivity;
import itesm.mx.perritos.event.Evento;
import itesm.mx.perritos.event.EventosFragment;
import itesm.mx.perritos.news.AddNewsActivity;
import itesm.mx.perritos.news.News;
import itesm.mx.perritos.news.NewsDetailActivity;
import itesm.mx.perritos.news.NewsFragment;
import itesm.mx.perritos.pet.AddPetActivity;
import itesm.mx.perritos.pet.Pet;
import itesm.mx.perritos.pet.PetDetailActivity;
import itesm.mx.perritos.pet.PetFragment;
import itesm.mx.perritos.product.AddProductActivity;
import itesm.mx.perritos.product.Product;
import itesm.mx.perritos.product.ProductDetailActivity;
import itesm.mx.perritos.product.ProductFragment;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
                                                                PetFragment.OnPetSelectedListener,
                                                                View.OnClickListener,
                                                                NavigationView.OnNavigationItemSelectedListener,
                                                                EventosFragment.OnEventSelectedListener,
                                                                ProductFragment.OnProductSelectedListener,
                                                                NewsFragment.OnNewsSelectedListener{

    private TabLayout tlTabLayout;
    private Toolbar tbToolbar;

    private final int [] ICON ={ R.drawable.ic_pets_black_24dp,
            R.drawable.ic_event_black_24dp,
            R.drawable.ic_web_black_24dp,
            R.drawable.ic_store_black_24dp,
            R.drawable.ic_pets_black_24dp_2,
            R.drawable.ic_event_black_24dp_2,
            R.drawable.ic_web_black_24dp_2,
            R.drawable.ic_store_black_24dp_2};
    
    private ViewPager vpViewPager;
    // Firebase Objects
    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final String DEBUG_TAG = "DEBUG_TAG";
    private static final int RC_SIGN_IN = 1;
    private static final int RC_EDIT_PET =2;
    private static final int RC_EDIT_NEWS = 3;
    private static final int RC_EDIT_PRODUCT = 4;
    private static final int RC_EDIT_PET_FAV = 5;
    private static final int RC_EDIT_PRODUCT_FAV = 6;
    private static final int RC_EDIT_NEWS_FAV = 7;
    private static final int RC_EDIT_EVENT = 8;
    private static final int RC_EDIT_EVENT_FAV = 9;


    private PetFragment petFragment;
    private EventosFragment eventosFragment;
    private NewsFragment newsFragment;
    private ProductFragment productFragment;

    private Pet editablePet;
    private Evento editableEvent;
    private News editableNews;
    private Product editableProduct;
    private int actualTab = 0;


    private User mainUser;

    private TextView textUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);

        vpViewPager = (ViewPager) findViewById(R.id.viewpager);
        vpViewPager.setOffscreenPageLimit(4);
        setUpViewPager(vpViewPager);

        tlTabLayout = (TabLayout) findViewById(R.id.tabs);
        tlTabLayout.setupWithViewPager(vpViewPager);
        tlTabLayout.getTabAt(0).setIcon(ICON[0]);
        tlTabLayout.getTabAt(1).setIcon(ICON[5]);
        tlTabLayout.getTabAt(2).setIcon(ICON[6]);
        tlTabLayout.getTabAt(3).setIcon(ICON[7]);
        tlTabLayout.addOnTabSelectedListener(this);

        // Custom toolbar
        tbToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tbToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Cultura perrona");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tbToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        textUserName = (TextView) header.findViewById(R.id.textView);
        navigationView.setNavigationItemSelectedListener(this);

        editablePet = null;

        // Initialize Firebase Componentes
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user signed in
                    // if user is admin
                    if (user.getEmail().equals("jorgevzqz6@gmail.com") ||
                            user.getEmail().equals("alexandro4v@gmail.com") ||
                            user.getEmail().equals("prueba@prueba.com")) {
                        textUserName.setText(user.getEmail());
                        petFragment.setAdmin(true,getApplicationContext(), CurrentUser.getmInstance().getUserEmail());
                        productFragment.setAdmin(true,getApplicationContext());
                        newsFragment.setAdmin(true,getApplicationContext());
                    } else {
                        petFragment.setAdmin(false,getApplicationContext(), CurrentUser.getmInstance().getUserEmail());
                        productFragment.setAdmin(false,getApplicationContext());
                        newsFragment.setAdmin(false,getApplicationContext());
                    }
                    prepareUser(user.getEmail());
                } else {
                    cleanUser();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                                    .setTheme(R.style.LoginTheme)
                                    .build(),
                                    RC_SIGN_IN);
                }
            }
        };
    }

    private void prepareUser(String userEmail) {
        CurrentUser.getmInstance().setUserEmail(userEmail);
    }

    private void cleanUser() {
        CurrentUser.getmInstance().setUserEmail(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_sign_out) {
            AuthUI.getInstance().signOut(this);
        }else if(id == R.id.nav_requests){
            petFragment.filterPets(CurrentUser.getmInstance().getUserEmail());
        }else if(id == R.id.nav_start){
            petFragment.setIsFilterOnOff();
        }else if(id == R.id.nav_favorites){
            petFragment.filterFavorites(CurrentUser.getmInstance().getUserEmail());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                AuthUI.getInstance().signOut(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tlTabLayout.getTabAt(actualTab).setIcon(ICON[actualTab + 4]);
        actualTab = tab.getPosition();
        vpViewPager.setCurrentItem(tab.getPosition());
        tlTabLayout.getTabAt(actualTab).setIcon(ICON[actualTab]);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        petFragment = new PetFragment();
        eventosFragment = new EventosFragment();
        newsFragment = new NewsFragment();
        productFragment = new ProductFragment();
        adapter.addFragment(petFragment);
        adapter.addFragment(eventosFragment);
        adapter.addFragment(newsFragment);
        adapter.addFragment(productFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_EDIT_PET) {
                Bundle bundle = data.getExtras();
                Boolean isDeleted = false;
                if (bundle != null) {
                    editablePet = (Pet) bundle.getSerializable("Pet");
                    isDeleted = bundle.getBoolean("Delete");
                }
                petFragment.updatePet(editablePet,isDeleted);
            } else if (requestCode == RC_EDIT_NEWS) {
                Bundle bundle = data.getExtras();
                boolean isDeleted = false;
                if (bundle != null) {
                    editableNews = (News) bundle.getSerializable("News");
                    isDeleted = bundle.getBoolean("Delete");
                }
                newsFragment.updateNews(editableNews,isDeleted);
            } else if (requestCode == RC_EDIT_PRODUCT) {
                Bundle bundle = data.getExtras();
                boolean isDeleted = false;
                if (bundle != null) {
                    editableProduct = (Product) bundle.getSerializable("Product");
                    isDeleted = bundle.getBoolean("Delete");
                }
                productFragment.updateProduct(editableProduct,isDeleted);
            } else if (requestCode == RC_SIGN_IN) {
                if (resultCode == RESULT_CANCELED) {
                    finish();
                }
            }else if (requestCode == RC_EDIT_PET_FAV){
                Bundle bundle = data.getExtras();
                boolean isDeleted = false;
                if (bundle != null) {
                    editablePet = (Pet) bundle.getSerializable("Pet");
                    isDeleted = bundle.getBoolean("Delete");
                }
                petFragment.updatePet(editablePet,isDeleted);
            } else if (requestCode == RC_EDIT_PRODUCT_FAV) {
                Bundle bundle = data.getExtras();
                boolean isDeleted = false;
                if (bundle != null) {
                    editableProduct = (Product) bundle.getSerializable("Product");
                    isDeleted = bundle.getBoolean("Delete");
                }
                productFragment.updateProduct(editableProduct,isDeleted);
            } else if (requestCode == RC_EDIT_NEWS_FAV) {
                Bundle bundle = data.getExtras();
                boolean isDeleted = false;
                if (bundle != null) {
                    editableNews = (News) bundle.getSerializable("News");
                    isDeleted = bundle.getBoolean("Delete");
                }
                newsFragment.updateNews(editableNews,isDeleted);
            }
            else if (requestCode == RC_EDIT_EVENT_FAV) {
                Bundle bundle = data.getExtras();
                boolean isDeleted = false;
                if (bundle != null) {
                    editableEvent = (Evento) bundle.getSerializable("Event");
                    isDeleted = bundle.getBoolean("Delete");
                }
                eventosFragment.updateEvent(editableEvent,isDeleted);
            }
            else if (requestCode == RC_EDIT_EVENT) {
                Bundle bundle = data.getExtras();
                boolean isDeleted = false;
                if (bundle != null) {
                    editableEvent = (Evento) bundle.getSerializable("Event");
                    isDeleted = bundle.getBoolean("Delete");
                }
                eventosFragment.updateEvent(editableEvent,isDeleted);
            }
        }
    }

    @Override
    public void onPetSelectedListener(Pet pet, boolean isEditing) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Pet",pet);
        if (isEditing) {
            // Editing pet
            Intent petEditIntent= new Intent(this, AddPetActivity.class);
            bundle.putBoolean("isEditing",true);
            petEditIntent.putExtras(bundle);
            editablePet = pet;
            startActivityForResult(petEditIntent,RC_EDIT_PET);
        } else {
            Intent petDetailIntent = new Intent(this, PetDetailActivity.class);
            petDetailIntent.putExtras(bundle);
            petDetailIntent.putExtra("User", CurrentUser.getmInstance().getUserEmail());
            startActivityForResult(petDetailIntent, RC_EDIT_PET_FAV);
        }
    }

    @Override
    public void onProductSelectedListener(Product product, boolean isEditing) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Product",product);
        if (isEditing) {
            Intent productEditIntent = new Intent(this, AddProductActivity.class);
            bundle.putBoolean("isEditing",true);
            productEditIntent.putExtras(bundle);
            editableProduct = product;
            startActivityForResult(productEditIntent,RC_EDIT_PRODUCT);
        } else {
            Intent productDetailIntent = new Intent(this, ProductDetailActivity.class);
            productDetailIntent.putExtras(bundle);
            productDetailIntent.putExtra("User",CurrentUser.getmInstance().getUserEmail());
            startActivityForResult(productDetailIntent,RC_EDIT_PRODUCT_FAV);
        }
    }

    @Override
    public void onNewsSelectedListener(News news,boolean isEditing) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("News",news);
        if (isEditing) {
            Intent newsEditIntent = new Intent(this, AddNewsActivity.class);
            bundle.putBoolean("isEditing",true);
            newsEditIntent.putExtras(bundle);
            editableNews = news;
            startActivityForResult(newsEditIntent,RC_EDIT_NEWS);
        } else {
            Intent newsDetailIntent = new Intent(this,NewsDetailActivity.class);
            newsDetailIntent.putExtras(bundle);
            newsDetailIntent.putExtra("User",CurrentUser.getmInstance().getUserEmail());
            startActivityForResult(newsDetailIntent,RC_EDIT_NEWS_FAV);
        }
    }

    @Override
    public void onEventSelectedListener(Evento event, boolean isEditing) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Event",event);
        if (isEditing) {
            // Editing event
            Intent eventEditIntent= new Intent(this, AddEventActivity.class);
            bundle.putBoolean("isEditing",true);
            eventEditIntent.putExtras(bundle);
            editableEvent = event;
            startActivityForResult(eventEditIntent,RC_EDIT_EVENT);
        } else {
            Intent eventDetailIntent = new Intent(this, EventDetailActivity.class);
            eventDetailIntent.putExtras(bundle);
            eventDetailIntent.putExtra("User",CurrentUser.getmInstance().getUserEmail());
            startActivityForResult(eventDetailIntent, RC_EDIT_EVENT_FAV);
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
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
