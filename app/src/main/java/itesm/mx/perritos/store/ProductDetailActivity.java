package itesm.mx.perritos.store;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import itesm.mx.perritos.R;
import itesm.mx.perritos.pet.PetDetailActivity;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private Button btnSolicitudProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Producto");

        btnSolicitudProducto = (Button) findViewById(R.id.btn_solicitud_producto);

        btnSolicitudProducto.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_solicitud_producto:
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
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de producto");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try{
            startActivity(Intent.createChooser(emailIntent,"Solicitud de producto por correo..."));
            finish();
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(ProductDetailActivity.this,
                    "No existe algún cliente de correo electronico en el dispositivo.", Toast.LENGTH_SHORT).show();
        }


    }
}
