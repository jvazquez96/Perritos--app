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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, PetFragment.OnPetSelectedListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tlTabLayout;
    private ViewPager vpViewPager;
    private Toolbar tbToolbar;

    private static final String DEBUG_TAG = "DEBUG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);

        vpViewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewPager(vpViewPager);

        tlTabLayout = (TabLayout) findViewById(R.id.tabs);
        tlTabLayout.setupWithViewPager(vpViewPager);
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
        adapter.addFragment(new PetFragment(),"Mascotas");
        adapter.addFragment(new EventosFragment(),"Eventos");
        adapter.addFragment(new NoticiasFragment(), "Noticias");
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
