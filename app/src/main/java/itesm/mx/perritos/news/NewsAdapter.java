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
import itesm.mx.perritos.Utils.CurrentUser;

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
        ImageView ivLike = (ImageView) convertView.findViewById(R.id.petLikeBtn);
        ImageView ivVisible = (ImageView) convertView.findViewById(R.id.newsVisibleItem);

        Glide.with(ivCover.getContext()).load(news1.getPhotoUrl()).into(ivCover);
        tvTitle.setText(news1.getTitle());
        tvDescription.setText(news1.getDescription());

        //Visible news
        final ImageView ivVisibleNews = (ImageView) convertView.findViewById(R.id.newsVisibleItem);
        if(news1.getIsVisible() == true){
            ivVisible.setVisibility(View.INVISIBLE);
        }else{
            ivVisible.setVisibility(View.VISIBLE);
        }


        if(news1.getIsVisible()){
            ivVisible.setVisibility(View.INVISIBLE);
        }else{
            ivVisible.setVisibility(View.VISIBLE);
        }

        if (news1.isUserInList(CurrentUser.getmInstance().getUserEmail())) {
            ivLike.setVisibility(View.VISIBLE);
        } else {
            ivLike.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }
}
