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
package itesm.mx.perritos.event;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import itesm.mx.perritos.R;
import itesm.mx.perritos.Utils.CurrentUser;

import static android.R.attr.resource;


public class EventoAdapter extends ArrayAdapter<Evento> {

    private ArrayList<Evento> eventos;


    public EventoAdapter(Context context, ArrayList<Evento> eventos) {
        super(context,0,eventos);
        this.eventos = eventos;
    }

    public ArrayList<Evento> getEvento() {
        return this.eventos;
    }

    public String Borrar_Año(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length()-5);
        }
        return str;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Evento eventos1 = eventos.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.evento_info,parent,false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.text_title);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.text_description);
        TextView tvDireccion = (TextView) convertView.findViewById(R.id.direccion);
        TextView tvTiempoFal = (TextView) convertView.findViewById(R.id.tiempo_faltante);
        ImageView ivEventFav = (ImageView) convertView.findViewById(R.id.EventFavBtn);


        tvTitle.setText(eventos1.getTitle());
        tvDescription.setText(eventos1.getDescription());
        tvDireccion.setText(eventos1.getLugar());
        String FechaCorta = Borrar_Año(eventos1.getStartDate());
        tvTiempoFal.setText(FechaCorta);

        ImageView ivVisible = (ImageView) convertView.findViewById(R.id.eventVisibleItem);

        //VISIBLE
        if(eventos1.getLugarVisible()){
            ivVisible.setVisibility(View.INVISIBLE);
        }else{
            ivVisible.setVisibility(View.VISIBLE);
        }


        //Fav image para eventos  POR IMPLEMENTAR
        if(eventos1.isUserInList(CurrentUser.getmInstance().getUserEmail())) {
            ivEventFav.setVisibility(View.VISIBLE);
        }else {
            ivEventFav.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }
}
