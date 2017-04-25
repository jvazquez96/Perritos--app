package itesm.mx.perritos.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import itesm.mx.perritos.R;

public class NewsDetailActivity extends AppCompatActivity {

    private Toolbar tlToolbar;


    private ImageView ivNews;
    private TextView tvTitle;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Noticia");
        ivNews = (ImageView) findViewById(R.id.image_cover);
        tvTitle = (TextView) findViewById(R.id.text_title);
        tvDescription = (TextView) findViewById(R.id.text_description);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            News news = (News) bundle.getSerializable("News");
            Glide.with(ivNews.getContext()).load(news.getPhotoUrl()).into(ivNews);
            tvTitle.setText(news.getTitle());
            tvDescription.setText(news.getDescription());
        }

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
}
