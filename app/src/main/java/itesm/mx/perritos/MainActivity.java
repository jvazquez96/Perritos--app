package itesm.mx.perritos;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, PetFragment.OnPetSelectedListener, View.OnClickListener {

    private TabLayout tlTabLayout;
    private ViewPager vpViewPager;
    private Toolbar tbToolbar;
    private ImageButton imgbtnMenu;
    private final int [] ICON ={
            R.drawable.ic_pets_black_24dp,
            R.drawable.ic_event_black_24dp,
            R.drawable.ic_new_releases_black_24dp,
            R.drawable.ic_store_black_24dp};

    private static final String DEBUG_TAG = "DEBUG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vpViewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewPager(vpViewPager);

        tlTabLayout = (TabLayout) findViewById(R.id.tabs);
        tlTabLayout.setupWithViewPager(vpViewPager);
        tlTabLayout.getTabAt(0).setIcon(ICON[0]);
        tlTabLayout.getTabAt(1).setIcon(ICON[1]);
        tlTabLayout.getTabAt(2).setIcon(ICON[2]);
        tlTabLayout.addOnTabSelectedListener(this);

        // Custom toolbar
        tbToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tbToolbar);

        // Buttons from the toolbar
        imgbtnMenu = (ImageButton) tbToolbar.findViewById(R.id.button_menu);
        imgbtnMenu.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_menu:
                Log.d(DEBUG_TAG,"Menu Button");
                break;
        }


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

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
