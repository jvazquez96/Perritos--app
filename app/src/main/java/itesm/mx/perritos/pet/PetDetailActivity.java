package itesm.mx.perritos.pet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import itesm.mx.perritos.R;

import static android.R.id.primary;
import static java.lang.System.load;


public class PetDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivPet;
    private TextView tvName;
    private TextView tvDescription;
    private TextView tvDate;
    private Toolbar tlToolbar;
    private Calendar calendar;
    private ImageView favImage;
    private Bundle bundle;
    private  Button btnSolicitudPet;
    private Pet pet;
    private CollapsingToolbarLayout cool;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPetsDataBaseReference;
    private ChildEventListener mChildEventListenerPets;
    private static final int REQUEST_CODE_ADD_PET = 1;
    private ArrayList<Pet> pets;
    private ArrayAdapter<Pet> petAdapter;
    private static final String DEBUG_TAG = "DEBUG_TAG";
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);
         tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Add the following code to make the up arrow white:
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_backspace_white_24dp);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        cool = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        ivPet = (ImageView) findViewById(R.id.image_pet);
        btnSolicitudPet = (Button) findViewById(R.id.btn_solicitud_pet) ;
        tvName = (TextView) findViewById(R.id.text_name);
        tvDescription = (TextView) findViewById(R.id.text_description);
        tvDate = (TextView) findViewById(R.id.text_date);
        tvDescription = (TextView) findViewById(R.id.text_description);
        bundle = getIntent().getExtras();
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        favImage = (ImageView) findViewById(R.id.petLikeBtn);
        btnSolicitudPet.setOnClickListener(this);

        if (bundle != null) {
            pet = (Pet) bundle.getSerializable("Pet");
            userEmail = bundle.getString("User");
            Glide.with(ivPet.getContext()).load(pet.getPhotoUrl()).into(ivPet);
            tvName.setText(pet.getName());
            tvDescription.setText(pet.getDescription());
            tvDate.setText(simpleDateFormat.format(calendar.getTime()));
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mPetsDataBaseReference = mFirebaseDatabase.getReference().child("Pets");
            cool.setTitle(pet.getName());
            cool.setExpandedTitleColor(Color.WHITE);
            cool.setCollapsedTitleTextColor(Color.WHITE);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite_border:
                if(pet.isUserInList(userEmail)){
                    Log.d("CORAZON", "elimina");
                    //Si ya es favorita se elimina
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    pet.setFav(false);
                    pet.removeUserFromList(userEmail);
                }else{
                    Log.d("CORAZON", "agrega");
                    //Si no era favorita se agrega
                    item.setIcon(R.drawable.heart);
                    pet.setFav(true);
                    pet.addLikedUser(userEmail);
                    Toast.makeText(getApplicationContext(), "Agregado a Favoritos", Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                Intent intent2 = new Intent();
                intent2.putExtra("Pet", pet);
                intent2.putExtra("Delete", false);
                setResult(RESULT_OK, intent2);
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent2 = new Intent();
            intent2.putExtra("Pet", pet);
            intent2.putExtra("Delete", false);
            setResult(RESULT_OK, intent2);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        if (pet.isUserInList(userEmail)) {
            menu.findItem(R.id.action_favorite_border).setIcon(R.drawable.heart);
        } else {
            menu.findItem(R.id.action_favorite_border).setIcon(R.drawable.ic_favorite_border_white_24dp);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
            String TO[] = {"cultura.perrona.10@gmail.com"};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.EMPTY.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de adopción");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hola, mi nombre es "
                    + "(Tu nombre)"
                    + " y me gustaría recibir más información acerca del procedimiento de adopción para el acompañante de vida "
                    + pet.getName()
                    + ", ¡Saludos!");
            try {
                startActivity(Intent.createChooser(emailIntent, "Solicitud de adopción por correo..."));
                pet.addUserRequest(userEmail);
                Intent intent3 = new Intent();
                intent3.putExtra("Pet", pet);
                intent3.putExtra("Delete", false);
                setResult(RESULT_OK, intent3);
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(PetDetailActivity.this,
                        "No existe algún cliente de correo electronico en el dispositivo.", Toast.LENGTH_SHORT).show();
            }
        }
}
