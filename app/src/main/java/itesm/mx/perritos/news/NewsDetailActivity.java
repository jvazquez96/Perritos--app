/*
        Copyright (C) 2017  Jorge Armando Vazquez Ortiz, Valentin Alexandro Trujillo, Santiago Sandoval
        Treviño and Gerardo Suarez Martinez
        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>
*/
package itesm.mx.perritos.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import itesm.mx.perritos.R;

public class NewsDetailActivity extends AppCompatActivity {

    private Toolbar tlToolbar;


    private ImageView ivNews;
    private TextView tvTitle;
    private TextView tvDescription;

    private News news;

    private String userEmail;

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
            news = (News) bundle.getSerializable("News");
            userEmail = bundle.getString("User");
            Glide.with(ivNews.getContext()).load(news.getPhotoUrl()).into(ivNews);
            tvTitle.setText(news.getTitle());
            tvDescription.setText(news.getDescription());
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId()) {
            case R.id.action_favorite_border:
                if(news.isUserInList(userEmail)){
                    //Quitar de favoritos
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    news.removeUserFromList(userEmail);
                    news.setFavorite(false);
                }else{
                    //Agregar a favoritos
                    item.setIcon(R.drawable.heart);
                    news.addLikedUser(userEmail);
                    news.setFavorite(true);
                    Toast.makeText(getApplicationContext(), "Agregado a Favoritos", Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("News",news);
                intent.putExtra("Delete",false);
                setResult(RESULT_OK,intent);
                finish();
                return true;
            case R.id.action_confirm:
                Intent intent2 = new Intent();
                intent2.putExtra("News",news);
                intent2.putExtra("Delete",false);
                setResult(RESULT_OK,intent2);
                finish();
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("News",news);
            intent.putExtra("Delete",false);
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        if (news.isUserInList(userEmail)) {
            menu.findItem(R.id.action_favorite_border).setIcon(R.drawable.heart);
        } else {
            menu.findItem(R.id.action_favorite_border).setIcon(R.drawable.ic_favorite_border_white_24dp);
        }
        return true;
    }
}
