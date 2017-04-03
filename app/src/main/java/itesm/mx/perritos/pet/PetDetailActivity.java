package itesm.mx.perritos.pet;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import itesm.mx.perritos.R;


public class PetDetailActivity extends AppCompatActivity {

    private ImageView ivPet;
    private TextView tvName;
    private TextView tvDescription;
    private TextView tvDate;

    private Toolbar tlToolbar;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);
        ivPet = (ImageView) findViewById(R.id.image_pet);
        tvName = (TextView) findViewById(R.id.text_name);
        tvDescription = (TextView) findViewById(R.id.text_description);
        tvDate = (TextView) findViewById(R.id.text_date);
        tvDescription = (TextView) findViewById(R.id.text_description);
        Bundle bundle = getIntent().getExtras();
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");

        if (bundle != null) {
            Pet pet = (Pet) bundle.getSerializable("Pet");
//            Log.d("DEBUG_TAG","Pet image: " + pet.getIdImage());
//            ivPet.setImageResource(pet.getIdImage());
            Log.d("DEBUG_TAG","Receiving pet");
            Glide.with(ivPet.getContext()).load(pet.getPhotoUrl()).into(ivPet);;
            Log.d("DEBUG_TAG","Photo url: " + pet.getPhotoUrl());
            tvName.setText(pet.getName());
            tvDescription.setText(pet.getDescription());
            tvDate.setText(simpleDateFormat.format(calendar.getTime()));
        }

        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mascota");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        return true;
    }
}
