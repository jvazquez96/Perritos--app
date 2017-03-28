package itesm.mx.perritos;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.os.Bundle;


public class PetDetailActivity extends AppCompatActivity {

    private ImageView ivPet;

    private Toolbar tlToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);
        ivPet = (ImageView) findViewById(R.id.image_pet);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Pet pet = (Pet) bundle.getSerializable("Pet");
            Log.d("DEBUG_TAG","Pet image: " + pet.getIdImage());
//            ivPet.setImageResource(pet.getIdImage());
        }

        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        return true;
    }
}
