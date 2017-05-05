package itesm.mx.perritos.event;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import itesm.mx.perritos.R;
import itesm.mx.perritos.pet.Pet;
import itesm.mx.perritos.pet.PetDetailActivity;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar tlToolbar;
    private TextView textStartDate;
    private TextView textEndDate;
    private TextView tvTituloEvento;
    private TextView tvDescripcionEvento;
    private TextView Direccion;
    private Evento MyEvent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Evento");

        textStartDate = (TextView) findViewById(R.id.text_FechaInicial);
        textEndDate = (TextView) findViewById(R.id.text_FechaFinal);
        tvTituloEvento = (TextView) findViewById(R.id.event_name);
        tvDescripcionEvento = (TextView) findViewById(R.id.text_description);
        Direccion = (TextView) findViewById(R.id.text_Lugar);
        bundle = getIntent().getExtras();
        if(bundle != null){
            MyEvent = (Evento) bundle.getSerializable("Event");
            textStartDate.setText("Empieza " + MyEvent.getStartDate() + " a las " + MyEvent.getHoraInicio());
            textEndDate.setText("Termina " + MyEvent.getEndDate() + " a las " + MyEvent.getHoraFinal());
            tvTituloEvento.setText(MyEvent.getTitle());
            tvDescripcionEvento.setText(MyEvent.getDescription());
            Direccion.setText(MyEvent.getLugar());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        String directionStr = "ITESM Monterrrey";
        //String directionStr = direccionEt.getText().toString();
        directionStr= directionStr.replace(' ','+');
        Uri uri = Uri.parse("geo:0,0?q="+ directionStr);

        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        if(intent.resolveActivity(getPackageManager()) != null ){
            startActivity(intent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_favorite_border:
                if (item.getIcon().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp).getConstantState()
                )) {
                    item.setIcon(R.drawable.heart);
                    Toast.makeText(getApplicationContext(), "Agregado a Favoritos", Toast.LENGTH_SHORT).show();
                    MyEvent.setIsFav(true);
                } else {
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    MyEvent.setIsFav(false);
                }
                break;
        }
        return true;
    }
}
