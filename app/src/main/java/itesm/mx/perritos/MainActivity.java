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

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            R.drawable.ic_newspaper_black_24dp,
            R.drawable.ic_store_black_24dp};
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

    private PetFragment petFragment;
    private EventosFragment eventosFragment;
    private NewsFragment newsFragment;
    private ProductFragment productFragment;

    private Pet editablePet;
    private News editableNews;
    private Product editableProduct;

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
        tlTabLayout.getTabAt(1).setIcon(ICON[1]);
        tlTabLayout.getTabAt(2).setIcon(ICON[2]);
        tlTabLayout.getTabAt(3).setIcon(ICON[3]);
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
                    if (user.getEmail().equals("jorgevzqz6@gmail.com") ||
                            user.getEmail().equals("Alexandro4v@gmail.com")) {
                        petFragment.setAdmin(true,getApplicationContext());
                        productFragment.setAdmin(true,getApplicationContext());
                    } else {
                        petFragment.setAdmin(false,getApplicationContext());
                        productFragment.setAdmin(false,getApplicationContext());
                    }
                } else {
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
            startActivityForResult(petDetailIntent, RC_EDIT_PET_FAV);
        }
    }

    @Override
    public void onEventSelectedListener(Evento event) {
        Bundle bundle = new Bundle();
        Intent eventDetailIntent = new Intent(this,EventDetailActivity.class);
        startActivity(eventDetailIntent);
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
            startActivity(productDetailIntent);
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
            startActivity(newsDetailIntent);
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
