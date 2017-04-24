package itesm.mx.perritos.product;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import itesm.mx.perritos.R;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageView imgPicture;
    private TextView textName;
    private TextView textPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgPicture = (ImageView) findViewById(R.id.image_product);
        textName = (TextView) findViewById(R.id.text_name);
        textPrice = (TextView) findViewById(R.id.text_price);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Product product = (Product) bundle.getSerializable("Product");
            Glide.with(imgPicture.getContext()).load(product.getPhotoUrl()).into(imgPicture);
            textName.setText(product.getsName());
            textPrice.setText(String.valueOf(product.getdPrice()));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Producto");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
