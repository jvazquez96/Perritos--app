package itesm.mx.perritos.product;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import itesm.mx.perritos.R;
import itesm.mx.perritos.pet.Pet;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView imgPicture;
    private TextView textName;
    private TextView textPrice;
    private ImageView favImage;
    private Product product;
    private Button btnSolicitudProduct;
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgPicture = (ImageView) findViewById(R.id.image_product);
        textName = (TextView) findViewById(R.id.text_name);
        textPrice = (TextView) findViewById(R.id.text_price);
        btnSolicitudProduct = (Button) findViewById(R.id.btn_solicitud_producto);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product = (Product) bundle.getSerializable("Product");
            userEmail = bundle.getString("User");
            Glide.with(imgPicture.getContext()).load(product.getPhotoUrl()).into(imgPicture);
            textName.setText(product.getsName());
            textPrice.setText(String.valueOf(product.getdPrice()));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Producto");
        btnSolicitudProduct.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        if (product.isUserInList(userEmail)) {
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
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Disponibilidad de producto");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hola, mi nombre es "
                + "(Tu nombre)"
                + " y me gustaría recibir más información acerca del producto "
                + product.getsName()
                + ", ¡Saludos!");
        try {
            startActivity(Intent.createChooser(emailIntent, "Solicitud de adopción por correo..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ProductDetailActivity.this,
                    "No existe algún cliente de correo electronico en el dispositivo.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite_border:
                if(product.isUserInList(userEmail)){
                    //Quitar de favoritos
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    product.removeUserFromList(userEmail);
                    product.setFav(false);
                }else{
                    //Agregar
                    item.setIcon(R.drawable.heart);
                    product.addLikedUser(userEmail);
                    product.setFav(true);
                    Toast.makeText(getApplicationContext(), "Agregado a Favoritos", Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("Product", product);
                intent.putExtra("Delete", false);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            case R.id.action_confirm:
                Intent intent2 = new Intent();
                intent2.putExtra("Product", product);
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
            Intent intent = new Intent();
            intent.putExtra("Product", product);
            intent.putExtra("Delete", false);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
