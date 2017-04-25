package itesm.mx.perritos.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import itesm.mx.perritos.R;

/**
 * Created by jorgevazquez on 3/20/17.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private ArrayList<News> news;


    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context,0,news);
        this.news = news;
    }

    public ArrayList<News> getNews() {
        return this.news;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news1 = news.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_info,parent,false);
        }

        ImageView ivCover = (ImageView) convertView.findViewById(R.id.image_news);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.text_title);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.text_description);

        Glide.with(ivCover.getContext()).load(news1.getPhotoUrl()).into(ivCover);
        tvTitle.setText(news1.getTitle());
        tvDescription.setText(news1.getDescription());

        return convertView;
    }
}
