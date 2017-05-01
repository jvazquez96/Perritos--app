package itesm.mx.perritos.pet;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import itesm.mx.perritos.R;


public class PetDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivPet;
    private TextView tvName;
    private TextView tvDescription;
    private TextView tvDate;
    private Button btnSolicitudAdopcion;
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
        btnSolicitudAdopcion = (Button) findViewById(R.id.enviar_email_btn);
        Bundle bundle = getIntent().getExtras();
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        btnSolicitudAdopcion.setOnClickListener(this);


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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enviar_email_btn:
                enviarMail();
                break;
        }

    }

    public void enviarMail(){
        String TO [] = {"nobody@mail.com"};
        //String CC [] = {"nobody@mail.com"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.EMPTY.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de adopción");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try{
            startActivity(Intent.createChooser(emailIntent,"Solicitud de adopción por correo..."));
            finish();
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(PetDetailActivity.this,
                    "No existe algún cliente de correo electronico en el dispositivo.", Toast.LENGTH_SHORT).show();
        }


    }
}
