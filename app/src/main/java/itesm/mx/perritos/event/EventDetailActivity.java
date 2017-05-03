package itesm.mx.perritos.event;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import itesm.mx.perritos.R;
import itesm.mx.perritos.pet.Pet;
import itesm.mx.perritos.pet.PetDetailActivity;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar tlToolbar;
    private Button btnIr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Evento");

        btnIr = (Button) findViewById(R.id.button_ir);
        btnIr.setOnClickListener(this);
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

}
